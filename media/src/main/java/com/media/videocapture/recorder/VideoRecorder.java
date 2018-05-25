/**
 * Copyright 2014 Jeroen Mols
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.media.videocapture.recorder;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.media.utils.LogUtil;
import com.media.utils.VideoFile;
import com.media.videocapture.camera.CameraWrapper;
import com.media.videocapture.camera.OpenCameraException;
import com.media.videocapture.camera.PrepareCameraException;
import com.media.videocapture.camera.RecordingSize;
import com.media.videocapture.configuration.CaptureConfiguration;
import com.media.videocapture.preview.CapturePreview;
import com.media.videocapture.preview.CapturePreviewInterface;

import java.io.IOException;

public class VideoRecorder implements OnInfoListener, CapturePreviewInterface {

    private       CameraWrapper  mCameraWrapper;
    private final Surface        mPreviewSurface;
    private       CapturePreview mVideoCapturePreview;

    private final CaptureConfiguration mCaptureConfiguration;
    private final VideoFile            mVideoFile;

    private MediaRecorder mRecorder;
    private boolean mRecording = false;
    private final VideoRecorderInterface mRecorderInterface;

    public VideoRecorder(VideoRecorderInterface recorderInterface, CaptureConfiguration captureConfiguration, VideoFile videoFile,
                         CameraWrapper cameraWrapper, SurfaceHolder previewHolder) {
        mCaptureConfiguration = captureConfiguration;
        mRecorderInterface = recorderInterface;
        mVideoFile = videoFile;
        mCameraWrapper = cameraWrapper;
        mPreviewSurface = previewHolder.getSurface();

        initializeCameraAndPreview(previewHolder);
    }

    protected void initializeCameraAndPreview(SurfaceHolder previewHolder) {
        try {
            mCameraWrapper.openCamera();
        } catch (final OpenCameraException e) {
            e.printStackTrace();
            mRecorderInterface.onRecordingFailed(e.getMessage());
            return;
        }

        mVideoCapturePreview = new CapturePreview(this, mCameraWrapper, previewHolder);
    }

    public void toggleRecording() {
        if (isRecording()) {
            stopRecording(null);
        } else {
            startRecording();
        }
    }

    protected void startRecording() {
        mRecording = false;

        if (!initRecorder()) return;
        if (!prepareRecorder()) return;
        if (!startRecorder()) return;

        mRecording = true;
        mRecorderInterface.onRecordingStarted();
        LogUtil.d(LogUtil.RECORDER, "Successfully started recording - outputfile: " + mVideoFile.getFullPath());
    }

    public void stopRecording(String message) {
        if (!isRecording()) return;

        try {
            getMediaRecorder().stop();
            mRecorderInterface.onRecordingSuccess();
            LogUtil.d(LogUtil.RECORDER, "Successfully stopped recording - outputfile: " + mVideoFile.getFullPath());
        } catch (final RuntimeException e) {
            LogUtil.d(LogUtil.RECORDER, "Failed to stop recording");
        }

        mRecording = false;
        mRecorderInterface.onRecordingStopped(message);
    }

    private boolean initRecorder() {
        try {
            mCameraWrapper.prepareCameraForRecording();
        } catch (final PrepareCameraException e) {
            e.printStackTrace();
            mRecorderInterface.onRecordingFailed("初始化视频失败");
            LogUtil.e(LogUtil.RECORDER, "Failed to initialize recorder - " + e.toString());
            return false;
        }

        mRecorder = new MediaRecorder();
        configureMediaRecorder(getMediaRecorder(), mCameraWrapper.getCamera());

        LogUtil.d(LogUtil.RECORDER, "MediaRecorder successfully initialized");
        return true;
    }

    @SuppressWarnings("deprecation")
    protected void configureMediaRecorder(final MediaRecorder recorder, android.hardware.Camera camera) throws IllegalStateException, IllegalArgumentException {
        recorder.setCamera(camera);
        recorder.setAudioSource(mCaptureConfiguration.getAudioSource());
        recorder.setVideoSource(mCaptureConfiguration.getVideoSource());

        CamcorderProfile baseProfile = mCameraWrapper.getBaseRecordingProfile();
        baseProfile.fileFormat = mCaptureConfiguration.getOutputFormat();

        RecordingSize size = mCameraWrapper.getSupportedRecordingSize(mCaptureConfiguration.getVideoWidth(), mCaptureConfiguration.getVideoHeight());
        baseProfile.videoFrameWidth = size.width;
        baseProfile.videoFrameHeight = size.height;
        baseProfile.videoBitRate = mCaptureConfiguration.getVideoBitrate();

        baseProfile.audioCodec = mCaptureConfiguration.getAudioEncoder();
        baseProfile.videoCodec = mCaptureConfiguration.getVideoEncoder();

        recorder.setProfile(baseProfile);
        recorder.setMaxDuration(mCaptureConfiguration.getMaxCaptureDuration());
        recorder.setOutputFile(mVideoFile.getFullPath());
        //------------start---------
        recorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        //------------end---------
        try {
            recorder.setMaxFileSize(mCaptureConfiguration.getMaxCaptureFileSize());
        } catch (IllegalArgumentException e) {
            LogUtil.e(LogUtil.RECORDER, "Failed to set max filesize - illegal argument: " + mCaptureConfiguration.getMaxCaptureFileSize());
        } catch (RuntimeException e2) {
            LogUtil.e(LogUtil.RECORDER, "Failed to set max filesize - runtime exception");
        }
        recorder.setOnInfoListener(this);
    }

    private boolean prepareRecorder() {
        try {
            getMediaRecorder().prepare();
            LogUtil.d(LogUtil.RECORDER, "MediaRecorder successfully prepared");
            return true;
        } catch (final IllegalStateException e) {
            e.printStackTrace();
            LogUtil.e(LogUtil.RECORDER, "MediaRecorder preparation failed - " + e.toString());
            return false;
        } catch (final IOException e) {
            e.printStackTrace();
            LogUtil.e(LogUtil.RECORDER, "MediaRecorder preparation failed - " + e.toString());
            return false;
        }
    }

    private boolean startRecorder() {
        try {
            getMediaRecorder().start();
            LogUtil.d(LogUtil.RECORDER, "MediaRecorder successfully started");
            return true;
        } catch (final IllegalStateException e) {
            e.printStackTrace();
            LogUtil.e(LogUtil.RECORDER, "MediaRecorder start failed - " + e.toString());
            return false;
        } catch (final RuntimeException e2) {
            e2.printStackTrace();
            LogUtil.e(LogUtil.RECORDER, "MediaRecorder start failed - " + e2.toString());
            mRecorderInterface.onRecordingFailed("Unable to record video with given settings");
            return false;
        }
    }

    protected boolean isRecording() {
        return mRecording;
    }

    protected MediaRecorder getMediaRecorder() {
        return mRecorder;
    }

    private void releaseRecorderResources() {
        MediaRecorder recorder = getMediaRecorder();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    public void releaseAllResources() {
        if (mVideoCapturePreview != null) {
            mVideoCapturePreview.releasePreviewResources();
        }
        if (mCameraWrapper != null) {
            mCameraWrapper.releaseCamera();
            mCameraWrapper = null;
        }
        releaseRecorderResources();
        LogUtil.d(LogUtil.RECORDER, "Released all resources");
    }

    @Override
    public void onCapturePreviewFailed() {
        mRecorderInterface.onRecordingFailed("无法显示相机预览");
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        switch (what) {
            case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
                // NOP
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                LogUtil.d(LogUtil.RECORDER, "MediaRecorder max duration reached");
                stopRecording("录制停止，录制时间已到上限");
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                LogUtil.d(LogUtil.RECORDER, "MediaRecorder max filesize reached");
                stopRecording("录制停止，录制文件大小已到上限");
                break;
            default:
                break;
        }
    }

}