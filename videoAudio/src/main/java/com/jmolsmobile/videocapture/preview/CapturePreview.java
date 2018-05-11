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

package com.jmolsmobile.videocapture.preview;

import android.view.SurfaceHolder;

import com.jmolsmobile.videocapture.utils.LogUtil;
import com.jmolsmobile.videocapture.camera.CameraWrapper;

import java.io.IOException;

public class CapturePreview implements SurfaceHolder.Callback {

	private boolean							mPreviewRunning	= false;
	private final CapturePreviewInterface	mInterface;
	public final CameraWrapper				mCameraWrapper;

	public CapturePreview(CapturePreviewInterface capturePreviewInterface, CameraWrapper cameraWrapper,
			SurfaceHolder holder) {
		mInterface = capturePreviewInterface;
		mCameraWrapper = cameraWrapper;

		initalizeSurfaceHolder(holder);
	}

	@SuppressWarnings("deprecation")
	private void initalizeSurfaceHolder(final SurfaceHolder surfaceHolder) {
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Necessary for older API's
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		// NOP
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
		if (mPreviewRunning) {
			try {
				mCameraWrapper.stopPreview();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		try {
			mCameraWrapper.configureForPreview(width, height);
			LogUtil.d(LogUtil.PREVIEW, "Configured camera for preview in surface of " + width + " by " + height);
		} catch (final RuntimeException e) {
			e.printStackTrace();
			LogUtil.d(LogUtil.PREVIEW, "Failed to show preview - invalid parameters set to camera preview");
			mInterface.onCapturePreviewFailed();
			return;
		}

		try {
			mCameraWrapper.enableAutoFocus();
		} catch (final RuntimeException e) {
			e.printStackTrace();
			LogUtil.d(LogUtil.PREVIEW, "AutoFocus not available for preview");
		}

		try {
			mCameraWrapper.startPreview(holder);
			setPreviewRunning(true);
		} catch (final IOException e) {
			e.printStackTrace();
			LogUtil.d(LogUtil.PREVIEW, "Failed to show preview - unable to connect camera to preview (IOException)");
			mInterface.onCapturePreviewFailed();
		} catch (final RuntimeException e) {
			e.printStackTrace();
			LogUtil.d(LogUtil.PREVIEW, "Failed to show preview - unable to start camera preview (RuntimeException)");
			mInterface.onCapturePreviewFailed();
		}
	}

	@Override
	public void surfaceDestroyed(final SurfaceHolder holder) {
		// NOP
	}

	public void releasePreviewResources() {
		if (mPreviewRunning) {
			try {
				mCameraWrapper.stopPreview();
				setPreviewRunning(false);
			} catch (final Exception e) {
				e.printStackTrace();
				LogUtil.e(LogUtil.PREVIEW, "Failed to clean up preview resources");
			}
		}
	}

	protected void setPreviewRunning(boolean running) {
		mPreviewRunning = running;
	}

}