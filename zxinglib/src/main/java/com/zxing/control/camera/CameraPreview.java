package com.zxing.control.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zxing.control.inter.CameraOpenCallBack;

import java.io.IOException;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraPreview.class.getSimpleName();
    private SurfaceHolder mHolder;
    private Context mContext;

    private boolean hasSurface;

    public boolean isHasSurface() {
        return hasSurface;
    }

    private CameraManager cameraManager;

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public CameraPreview(Context context) {
        super(context);

        init(context,null,-1);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context,attrs,-1);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mContext = context;
        mHolder = getHolder();
//        mHolder.addCallback(this);

        cameraManager = new CameraManager(mContext.getApplicationContext());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        Camera mCamera = cameraManager.getCamera();
        if (mHolder.getSurface() == null || mCamera == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            cameraManager.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            cameraManager.setCameraDisplayOrientation();
            cameraManager.startPreview();
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (cameraFrameCallBack != null){
                cameraFrameCallBack.onPreviewFrame(data,camera);
            }
        }
    };

    private CameraFrameCallBack cameraFrameCallBack;
    /**
     * 开始处理frame数据
     */
    public void startSingleHandFrame(CameraFrameCallBack cameraFrameCallBack){
        this.cameraFrameCallBack = cameraFrameCallBack;

        Camera mCamera = cameraManager.getCamera();
        if (mCamera == null || cameraFrameCallBack == null){
            return;
        }
        mCamera.setOneShotPreviewCallback(previewCallback);
    }

    public void initCamera() {
        SurfaceHolder surfaceHolder = getHolder();
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (cameraOpenCallBack != null){
                cameraOpenCallBack.onSuccess(cameraManager.getCamera());
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            if (cameraOpenCallBack != null){
                cameraOpenCallBack.onException(ioe);
            }
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            if (cameraOpenCallBack != null){
                cameraOpenCallBack.onException(e);
            }
        }
    }

    public void addSurfaceCallBack() {
        removeSurfaceCallBack();
        getHolder().addCallback(this);
    }

    public void removeSurfaceCallBack(){
        getHolder().removeCallback(this);
    }

    public interface CameraFrameCallBack{
        void onPreviewFrame(byte[] data, Camera camera);
    }

    /**
     * 开始预览
     */
    public void startPreview(){
        if (cameraManager != null){
            cameraManager.startPreview();
        }
    }

    public boolean isPreviewing(){
        if (cameraManager == null){
            return false;
        }
        return cameraManager.isPreviewing();
    }

    /**
     * 停止预览
     */
    public void stopPreview(){
        if (cameraManager != null){
            cameraManager.stopPreview();
        }
    }

    private CameraOpenCallBack cameraOpenCallBack;
    public void setCameraOpenCallBack(CameraOpenCallBack cameraOpenCallBack) {
        this.cameraOpenCallBack = cameraOpenCallBack;
    }
}
