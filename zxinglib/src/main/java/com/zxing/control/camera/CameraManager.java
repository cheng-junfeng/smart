/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.control.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * This object wraps the Camera service object and expects to be the only one talking to it. The
 * implementation encapsulates the steps needed to take preview-sized images, which are used for
 * both preview and decoding.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
@SuppressWarnings("deprecation") // camera APIs
public final class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();

    private static final int MIN_PREVIEW_PIXELS = 480 * 320; // normal screen
    private static final double MAX_ASPECT_DISTORTION = 0.15;

    private final Context mContext;
    private Camera mCamera;
    private int mCameraId;
    private AutoFocusManager autoFocusManager;
    private CameraConfigurationManager configManager;
    private boolean previewing;
    public boolean isPreviewing() {
        return previewing;
    }

    public CameraManager(Context context) {
        this.configManager = new CameraConfigurationManager(context);
        this.mContext = context;
    }


    /**
     * Opens the camera driver and initializes the hardware parameters.
     *
     * @param holder The surface object which the camera will draw preview frames into.
     * @throws IOException Indicates the camera driver failed to open.
     */
    public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        if (mCamera == null) {
            mCamera = openBackCamera();
        }
    }

    public synchronized boolean isOpen() {
        return mCamera != null;
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.setOneShotPreviewCallback(null);
            stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public synchronized void startPreview() {
        if (mCamera != null && !previewing) {
            mCamera.startPreview();
            previewing = true;
            autoFocusManager = new AutoFocusManager(mContext, mCamera);
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public synchronized void stopPreview() {
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (mCamera != null && previewing) {
            mCamera.stopPreview();
//            previewCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    public Camera openBackCamera(){
        if (mCamera != null){
            return mCamera;
        }
        int cameraId = findBackCameraId();
        if (cameraId < 0){
            return null;
        }
        return openCamera(cameraId);
    }

    private Camera openCamera(int cameraId) {
        if (mCamera != null){
            return mCamera;
        }

        boolean hasHardware = checkCameraHardware(mContext);
        if (!hasHardware){
            return null;
        }

        try {
            mCamera = Camera.open(cameraId); // attempt to get a Camera instance

            if (mCamera != null){
                mCameraId = cameraId;
                setCameraParameters();
                setCameraDisplayOrientation();
            }
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return mCamera; // returns null if camera is unavailable
    }

    /**
     * Check if this device has a camera
     */
    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * 设置照相机方向
     */
    public void setCameraDisplayOrientation() {
        if (mCamera == null || mCameraId < 0) {
            return;
        }

        Camera.CameraInfo selectedCameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, selectedCameraInfo);

        int cwRotationFromNaturalToCamera = selectedCameraInfo.orientation;
        Log.i(TAG, "Camera at: " + cwRotationFromNaturalToCamera);

        // Still not 100% sure about this. But acts like we need to flip this:
        if (Camera.CameraInfo.CAMERA_FACING_FRONT == selectedCameraInfo.facing) {
            cwRotationFromNaturalToCamera = (360 - cwRotationFromNaturalToCamera) % 360;
            Log.i(TAG, "Front camera overriden to: " + cwRotationFromNaturalToCamera);
        }

        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        int displayRotation = display.getRotation();
        int cwRotationFromNaturalToDisplay;
        switch (displayRotation) {
            case Surface.ROTATION_0:
                cwRotationFromNaturalToDisplay = 0;
                break;
            case Surface.ROTATION_90:
                cwRotationFromNaturalToDisplay = 90;
                break;
            case Surface.ROTATION_180:
                cwRotationFromNaturalToDisplay = 180;
                break;
            case Surface.ROTATION_270:
                cwRotationFromNaturalToDisplay = 270;
                break;
            default:
                // Have seen this return incorrect values like -90
                if (displayRotation % 90 == 0) {
                    cwRotationFromNaturalToDisplay = (360 + displayRotation) % 360;
                } else {
                    throw new IllegalArgumentException("Bad rotation: " + displayRotation);
                }
        }
        Log.i(TAG, "Display at: " + cwRotationFromNaturalToDisplay);

        int cwRotationFromDisplayToCamera = (360 + cwRotationFromNaturalToCamera - cwRotationFromNaturalToDisplay) % 360;

        mCamera.setDisplayOrientation(cwRotationFromDisplayToCamera);
    }

    private int findFrontCameraId() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    private int findBackCameraId() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    /**
     * 设置照相机参数
     *
     */
    private void setCameraParameters() {
        if (mCamera == null) {
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();

        //获取屏幕分辨率
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point theScreenResolution = new Point();
        display.getSize(theScreenResolution);

        Point cameraPreviewSize = findBestPreviewSizeValue(parameters, theScreenResolution);

        //1、设置预览分辨率
        parameters.setPreviewSize(cameraPreviewSize.x, cameraPreviewSize.y);
        //2、设置对焦模式配置参数
//        parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);//自动对焦模式
        //3、设置场景
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE); //扫描条码场景

        mCamera.setParameters(parameters);
    }

    /**
     * 发现最佳分辨率
     *
     * @param parameters
     * @param screenResolution
     * @return
     */
    private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {
        //在这里获取全部支持预览的分辨率
        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            Log.w(TAG, "Device returned no supported preview sizes; using default");
            Camera.Size defaultSize = parameters.getPreviewSize();
            if (defaultSize == null) {
                throw new IllegalStateException("Parameters contained no preview size!");
            }
            return new Point(defaultSize.width, defaultSize.height);
        }

        // Sort by size, descending 对所有的分辨率进行排序
        List<Camera.Size> supportedPreviewSizes = new ArrayList<>(rawSupportedSizes);
        Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        //这里会把所有支持预览的分辨率打印出来,这里可以方便进行调试
        if (Log.isLoggable(TAG, Log.INFO)) {
            StringBuilder previewSizesString = new StringBuilder();
            for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
                previewSizesString.append(supportedPreviewSize.width).append('x')
                        .append(supportedPreviewSize.height).append(' ');
            }
            Log.i(TAG, "Supported preview sizes: " + previewSizesString);
        }

        // 分辨率比例,这个一个关键点1
        double screenAspectRatio = screenResolution.y / (double) screenResolution.x;
//    double screenAspectRatio = screenResolution.x / (double) screenResolution.y;

        // Remove sizes that are unsuitable
        Iterator<Camera.Size> it = supportedPreviewSizes.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewSize = it.next();
            int realWidth = supportedPreviewSize.width;
            int realHeight = supportedPreviewSize.height;
            if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            boolean isCandidatePortrait = realWidth < realHeight;
            int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
            int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
            double aspectRatio = maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }

            if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
                Point exactPoint = new Point(realWidth, realHeight);
                Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
                return exactPoint;
            }
        }

        // If no exact match, use largest preview size. This was not a great idea on older devices because
        // of the additional computation needed. We're likely to get here on newer Android 4+ devices, where
        // the CPU is much more powerful.
        if (!supportedPreviewSizes.isEmpty()) {
            for (int i = 0; i < supportedPreviewSizes.size(); i++) {
                Camera.Size largestPreview = supportedPreviewSizes.get(i);
                if (largestPreview.width == screenResolution.y && largestPreview.height == screenResolution.x) {
                    Log.i(TAG, "Using same suitable preview size: " + largestPreview);
                    return new Point(largestPreview.width, largestPreview.height);
                }
            }
            Camera.Size largestPreview = supportedPreviewSizes.get(0);
            Point largestSize = new Point(largestPreview.width, largestPreview.height);
            Log.i(TAG, "Using largest suitable preview size: " + largestSize);
            return largestSize;
        }

        // If there is nothing at all suitable, return current preview size
        Camera.Size defaultPreview = parameters.getPreviewSize();
        if (defaultPreview == null) {
            throw new IllegalStateException("Parameters contained no preview size!");
        }
//    Point defaultSize = new Point(defaultPreview.width, defaultPreview.height);
        Point defaultSize = new Point(defaultPreview.height, defaultPreview.width);
//    Log.i(TAG, "defaultPreview " + defaultPreview.width + " " + defaultPreview.height);
        Log.i(TAG, "No suitable preview sizes, using default: " + defaultSize);
        return defaultSize;
    }

    public Camera getCamera() {
        return mCamera;
    }

    /**
     * Convenience method for
     *
     * @param newSetting if {@code true}, light should be turned on if currently off. And vice versa.
     */
    public synchronized void setTorch(boolean newSetting) {
        if (mCamera != null && newSetting != configManager.getTorchState(mCamera)) {
            boolean wasAutoFocusManager = autoFocusManager != null;
            if (wasAutoFocusManager) {
                autoFocusManager.stop();
                autoFocusManager = null;
            }
            configManager.setTorch(mCamera, newSetting);
            if (wasAutoFocusManager) {
                autoFocusManager = new AutoFocusManager(mContext, mCamera);
                autoFocusManager.start();
            }
        }
    }
}
