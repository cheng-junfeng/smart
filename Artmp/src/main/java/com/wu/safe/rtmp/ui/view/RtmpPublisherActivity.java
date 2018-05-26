package com.wu.safe.rtmp.ui.view;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.base.utils.LogUtil;
import com.wu.safe.rtmp.R;
import com.wu.safe.rtmp.app.activity.RtmpCompatActivity;


public class RtmpPublisherActivity extends RtmpCompatActivity implements CompoundButton.OnCheckedChangeListener, ITXLivePushListener {

    public static final String TAG = "RtmpPublisherActivity";

    private String url="rtmp://www.chengjunfeng.cn:10000/hls/test";

    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXCloudVideoView mCaptureView;

    private boolean mVideoPublish;
    private boolean cameraFront = false;

    private int mBeautyStyle = TXLiveConstants.BEAUTY_STYLE_SMOOTH;
    private int mBeautyLevel = 5;
    private int mWhiteningLevel = 3;
    private int mRuddyLevel = 2;

    private CheckBox cbCamera;

    @Override
    protected int setContentView() {
        return R.layout.activity_live_publisher;
    }

    @Override
    protected boolean setToolbar(){
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView() {
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePusher.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);
        mLivePusher.setConfig(mLivePushConfig);

        mCaptureView = (TXCloudVideoView) findViewById(R.id.video_view);
        cbCamera = (CheckBox) findViewById(R.id.cbCamera);
        cbCamera.setOnCheckedChangeListener(this);

        mVideoPublish = startLive(url);
    }

    private boolean startLive(String url) {
        if (TextUtils.isEmpty(url) || (!url.trim().toLowerCase().startsWith("rtmp://"))) {
            Toast.makeText(getApplicationContext(), "推流地址不合法，目前支持rtmp推流!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int customModeType = 0;

        onActivityRotation();
        mLivePushConfig.setCustomModeType(customModeType);
        mLivePusher.setPushListener(this);
        Bitmap bitmap = decodeResource(getResources(), R.drawable.default_error);
        mLivePushConfig.setPauseImg(300, 5);
        mLivePushConfig.setPauseImg(bitmap);
        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
        mLivePushConfig.enableNearestIP(getIntent().getBooleanExtra("ENABLE_NEARESTIP", false));

        mLivePushConfig.setFrontCamera(cameraFront);
        mLivePushConfig.setBeautyFilter(mBeautyLevel, mWhiteningLevel, mRuddyLevel);
        mLivePusher.setConfig(mLivePushConfig);
        mLivePusher.startCameraPreview(mCaptureView);

        //开启直播
        mLivePusher.startPusher(url.trim());
        return true;
    }

    protected void onActivityRotation() {
        // 自动旋转打开，Activity随手机方向旋转之后，需要改变推流方向
        int mobileRotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        //boolean screenCaptureLandscape = false;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                // screenCaptureLandscape = true;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                //screenCaptureLandscape = true;
                break;
            default:
                break;
        }
        mLivePusher.setRenderRotation(0); //因为activity也旋转了，本地渲染相对正方向的角度为0。
        mLivePushConfig.setHomeOrientation(pushRotation);
        if (mLivePusher.isPushing()) {
            mLivePusher.setConfig(mLivePushConfig);
            mLivePusher.stopCameraPreview(false);
            mLivePusher.startCameraPreview(mCaptureView);
        }
    }

    private void stopPublishRtmp() {
        mLivePusher.stopBGM();
        mLivePusher.stopCameraPreview(true);
        mLivePusher.stopScreenCapture();
        mLivePusher.setPushListener(null);
        mLivePusher.stopPusher();
        mCaptureView.setVisibility(View.GONE);

        if (mLivePushConfig != null) {
            mLivePushConfig.setPauseImg(null);
        }
    }

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCaptureView != null) {
            mCaptureView.onResume();
        }

        if (mVideoPublish && mLivePusher != null) {
            mLivePusher.resumePusher();
            mLivePusher.resumeBGM();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCaptureView != null) {
            mCaptureView.onPause();
        }

        if (mVideoPublish && mLivePusher != null) {
            mLivePusher.pausePusher();
            mLivePusher.pauseBGM();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPublishRtmp();
        if (mCaptureView != null) {
            mCaptureView.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        onActivityRotation();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int viewId = compoundButton.getId();
        if(viewId == R.id.cbCamera){
            cameraFront = isChecked;
            if (mLivePusher.isPushing()) {
                mLivePusher.switchCamera();
            }
            mLivePushConfig.setFrontCamera(cameraFront);
        }
    }

    @Override
    public void onPushEvent(int i, Bundle bundle) {
        LogUtil.d(TAG, "liveState"+i + "");
        LogUtil.d(TAG, "onPushEvent:"+bundle.toString());
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        LogUtil.d(TAG, "onNetStatus:" + bundle.toString());
    }
}
