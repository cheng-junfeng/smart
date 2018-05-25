/**
 * Copyright 2014 Jeroen Mols
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.media.videocapture.camera;

import android.annotation.SuppressLint;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.os.Build;
import android.view.SurfaceHolder;

import com.media.utils.LogUtil;
import com.media.videocapture.camera.OpenCameraException.OpenType;

import java.io.IOException;
import java.util.List;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class CameraWrapper {

	private Camera mCamera = null;
	private Parameters mParameters = null;

	public Camera getCamera() {
		return mCamera;
	}

	public void openCamera() throws OpenCameraException {
		mCamera = null;
		try {
			mCamera = openCameraFromSystem();
		} catch (final RuntimeException e) {
			e.printStackTrace();
			throw new OpenCameraException(OpenType.INUSE);
		}

		if (mCamera == null)
			throw new OpenCameraException(OpenType.NOCAMERA);
	}

	public void prepareCameraForRecording() throws PrepareCameraException {
		try {
			storeCameraParametersBeforeUnlocking();
			unlockCameraFromSystem();
		} catch (final RuntimeException e) {
			e.printStackTrace();
			throw new PrepareCameraException();
		}
	}

	public void releaseCamera() {
		if (getCamera() == null)
			return;
		releaseCameraFromSystem();
	}

	public void startPreview(final SurfaceHolder holder) throws IOException {
		// ------------start---------
		mCamera.setDisplayOrientation(90);
		// ------------end-----------
		mCamera.setPreviewDisplay(holder);
		mCamera.startPreview();
	}

	public void stopPreview() throws Exception {
		mCamera.stopPreview();
		mCamera.setPreviewCallback(null);
	}

	public RecordingSize getSupportedRecordingSize(int width, int height) {
		Size recordingSize = getOptimalSize(getSupportedVideoSizes(),
				width, height);
		if (recordingSize == null) {
			LogUtil.e(LogUtil.CAMERA,
					"Failed to find supported recording size - falling back to requested: "
							+ width + "x" + height);
			return new RecordingSize(width, height);
		}
		LogUtil.d(LogUtil.CAMERA, "Recording size: " + recordingSize.width + "x"
				+ recordingSize.height);
		return new RecordingSize(recordingSize.width, recordingSize.height);
	}

	public CamcorderProfile getBaseRecordingProfile() {
		if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P))
			return CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
		else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P))
			return CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
		else
			return CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
	}

	public void configureForPreview(int viewWidth, int viewHeight) {
		final Parameters params = getCameraParametersFromSystem();
		final Size previewSize = getOptimalSize(
				params.getSupportedPreviewSizes(), viewWidth, viewHeight);
		params.setPreviewSize(previewSize.width, previewSize.height);
		params.setPreviewFormat(ImageFormat.NV21);
		// ------------start-----------
		params.set("orientation", "portrait");// 设置竖屏
		// ------------end-----------
		mCamera.setParameters(params);
		LogUtil.d(LogUtil.CAMERA, "Preview size: " + previewSize.width + "x"
				+ previewSize.height);
	}

	public void enableAutoFocus() {
		final Parameters params = getCameraParametersFromSystem();
		params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		mCamera.setParameters(params);
	}

	protected Camera openCameraFromSystem() {
		return Camera.open(CameraInfo.CAMERA_FACING_BACK);
	}

	protected void unlockCameraFromSystem() {
		mCamera.unlock();
	}

	protected void releaseCameraFromSystem() {
		mCamera.release();
	}

	protected Parameters getCameraParametersFromSystem() {
		return mCamera.getParameters();
	}

	protected List<Size> getSupportedVideoSizes() {
		Parameters params = getCameraParametersAfterUnlocking();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return params.getSupportedVideoSizes();
		} else {
			LogUtil.e(LogUtil.CAMERA,
					"Using supportedPreviewSizes iso supportedVideoSizes due to API restriction");
			return params.getSupportedPreviewSizes();
		}
	}

	protected void storeCameraParametersBeforeUnlocking() {
		mParameters = getCameraParametersFromSystem();
	}

	private Parameters getCameraParametersAfterUnlocking() {
		return mParameters;
	}

	/**
	 * Copyright (C) 2013 The Android Open Source Project
	 * <p/>
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may
	 * not use this file except in compliance with the License. You may obtain a
	 * copy of the License at
	 * <p/>
	 * http://www.apache.org/licenses/LICENSE-2.0
	 * <p/>
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	 * License for the specific language governing permissions and limitations
	 * under the License.
	 */
	public Size getOptimalSize(List<Size> sizes, int w, int h) {
		// Use a very small tolerance because we want an exact match.
		final double ASPECT_TOLERANCE = 0.1;
		final double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;

		// Start with max value and refine as we iterate over available preview
		// sizes. This is the
		// minimum difference between view and camera height.
		double minDiff = Double.MAX_VALUE;

		// Target view height
		final int targetHeight = h;

		// Try to find a preview size that matches aspect ratio and the target
		// view size.
		// Iterate over all available sizes and pick the largest size that can
		// fit in the view and
		// still maintain the aspect ratio.
		for (final Size size : sizes) {
			final double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
				continue;
			}
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find preview size that matches the aspect ratio, ignore the
		// requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (final Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

}
