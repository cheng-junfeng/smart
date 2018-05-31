package com.video.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.base.utils.LogUtil;
import com.video.R;
import com.video.capture.camera.CameraWrapper;
import com.video.capture.configuration.CaptureConfiguration;
import com.video.capture.recorder.RecordingButtonInterface;
import com.video.capture.recorder.VideoRecorder;
import com.video.capture.recorder.VideoRecorderInterface;
import com.video.utils.VideoFile;

public class VideoCaptureActivity extends Activity implements RecordingButtonInterface, VideoRecorderInterface {

	public static final String			TAG				= "VideoCaptureActivity";
	public static final int			RESULT_ERROR				= 753245;

	public static final String		EXTRA_OUTPUT_FILENAME		= "com.video.extraoutputfilename";
	public static final String		EXTRA_CAPTURE_CONFIGURATION	= "com.video.extracaptureconfiguration";
	public static final String		EXTRA_ERROR_MESSAGE			= "com.video.extraerrormessage";

	private static final String		SAVED_RECORDED_BOOLEAN		= "com.video.savedrecordedboolean";
	protected static final String	SAVED_OUTPUT_FILENAME		= "com.video.savedoutputfilename";

	private boolean					mVideoRecorded				= false;
	VideoFile mVideoFile					= null;
	private CaptureConfiguration	mCaptureConfiguration;

	private VideoCaptureView		mVideoCaptureView;
	private VideoRecorder			mVideoRecorder;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_capture);

		initializeCaptureConfiguration(savedInstanceState);

		mVideoCaptureView = (VideoCaptureView) findViewById(R.id.videocapture_videocaptureview_vcv);
		if (mVideoCaptureView == null) return; // Wrong orientation

		initializeRecordingUI();
	}

	private void initializeCaptureConfiguration(final Bundle savedInstanceState) {
		mCaptureConfiguration = generateCaptureConfiguration();
		mVideoRecorded = generateVideoRecorded(savedInstanceState);
		mVideoFile = generateOutputFile(savedInstanceState);
	}

	private void initializeRecordingUI() {
		mVideoRecorder = new VideoRecorder(this, mCaptureConfiguration, mVideoFile, new CameraWrapper(),
				mVideoCaptureView.getPreviewSurfaceHolder());
        mVideoCaptureView.setRecordingButtonInterface(this);

		if (mVideoRecorded) {
			mVideoCaptureView.updateUIRecordingFinished(getVideoThumbnail());
		} else {
			mVideoCaptureView.updateUINotRecording();
		}
	}

	@Override
	protected void onPause() {
		if (mVideoRecorder != null) {
			mVideoRecorder.stopRecording(null);
		}
		releaseAllResources();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		finishCancelled();
	}

	@Override
	public void onRecordButtonClicked() {
		mVideoRecorder.toggleRecording();
	}

	@Override
	public void onAcceptButtonClicked() {
		finishCompleted();
	}

	@Override
	public void onDeclineButtonClicked() {
		finishCancelled();
	}

	@Override
	public void onRecordingStarted() {
		mVideoCaptureView.updateUIRecordingOngoing();
	}

	@Override
	public void onRecordingStopped(String message) {
		if (message != null) {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}

		mVideoCaptureView.updateUIRecordingFinished(getVideoThumbnail());
		releaseAllResources();
	}

	@Override
	public void onRecordingSuccess() {
		mVideoRecorded = true;
	}

	@Override
	public void onRecordingFailed(String message) {
		finishError(message);
	}

	private void finishCompleted() {
		final Intent result = new Intent();
		result.putExtra(EXTRA_OUTPUT_FILENAME, mVideoFile.getFullPath());
		this.setResult(RESULT_OK, result);
		finish();
	}

	private void finishCancelled() {
		this.setResult(RESULT_CANCELED);
		finish();
	}

	private void finishError(final String message) {
		Toast.makeText(getApplicationContext(), "不能录制视频: " + message, Toast.LENGTH_LONG).show();

		final Intent result = new Intent();
		result.putExtra(EXTRA_ERROR_MESSAGE, message);
		this.setResult(RESULT_ERROR, result);
		finish();
	}

	private void releaseAllResources() {
		if (mVideoRecorder != null) {
			mVideoRecorder.releaseAllResources();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean(SAVED_RECORDED_BOOLEAN, mVideoRecorded);
		savedInstanceState.putString(SAVED_OUTPUT_FILENAME, mVideoFile.getFullPath());
		super.onSaveInstanceState(savedInstanceState);
	}

	protected CaptureConfiguration generateCaptureConfiguration() {
		CaptureConfiguration returnConfiguration = this.getIntent().getParcelableExtra(EXTRA_CAPTURE_CONFIGURATION);
		if (returnConfiguration == null) {
			returnConfiguration = new CaptureConfiguration();
			LogUtil.d(TAG, "No captureconfiguration passed - using default configuration");
		}
		return returnConfiguration;
	}

	private boolean generateVideoRecorded(final Bundle savedInstanceState) {
		if (savedInstanceState == null) return false;
		return savedInstanceState.getBoolean(SAVED_RECORDED_BOOLEAN, false);
	}

	protected VideoFile generateOutputFile(Bundle savedInstanceState) {
		VideoFile returnFile = null;
		if (savedInstanceState != null) {
			returnFile = new VideoFile(savedInstanceState.getString(SAVED_OUTPUT_FILENAME));
		} else {
			returnFile = new VideoFile(this.getIntent().getStringExtra(EXTRA_OUTPUT_FILENAME));
		}
		return returnFile;
	}

	public Bitmap getVideoThumbnail() {
		final Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(mVideoFile.getFullPath(),
				Thumbnails.FULL_SCREEN_KIND);
		if (thumbnail == null) {
			LogUtil.d(TAG, "Failed to generate video preview");
		}
		return thumbnail;
	}
}
