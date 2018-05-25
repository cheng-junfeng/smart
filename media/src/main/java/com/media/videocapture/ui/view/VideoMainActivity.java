package com.media.videocapture.ui.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.media.R;
import com.media.R2;
import com.media.app.activity.MediaBaseCompatActivity;
import com.media.videocapture.configuration.CaptureConfiguration;
import com.media.videocapture.ui.adapter.AudioAdapter;
import com.media.videocapture.ui.adapter.VideoInfoGridAdapter;
import com.media.videocapture.ui.bean.AudioInfoBean;
import com.media.videocapture.ui.bean.ImageInfo;
import com.media.videocapture.ui.widget.ScrollGridView;
import com.media.utils.AudioRecoderUtils;
import com.media.utils.LogUtil;
import com.media.utils.PopupWindowFactory;
import com.media.utils.TimeUtils;
import com.media.utils.ToolbarUtil;
import com.media.utils.VideoUtil;
import com.media.videoplay.view.SurfaceViewActivity;
import com.media.videoplay.view.TextureViewActivity;
import com.media.videoplay.view.VideoViewActivity;
import com.media.videoplay.view.WebViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class VideoMainActivity extends MediaBaseCompatActivity {
    private final static String TAG = "VideoAddActivity";
    private static final int REQUEST_RECORD_VIDEO = 1;
    private static final int MAX_VIDEO_COUNT = 3;

    @BindView(R2.id.svAddVideo)
    ScrollGridView svAddVideo;
    @BindView(R2.id.rvAudioList)
    RecyclerView rvAudioList;
    @BindView(R2.id.ibAudioRecorder)
    ImageButton ibAudioRecorder;
    @BindView(R2.id.rlAddInfoLayout)
    RelativeLayout rlAddInfoLayout;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private VideoInfoGridAdapter videoAdapter;
    private AudioAdapter audioAdapter;

    ArrayList<ImageInfo> photoInfos;
    ArrayList<AudioInfoBean> audioInfos;

    private int CANCLE_LENGHT = -150;     // 上滑取消录制距离
    int startY = 0;                       // 首次按住录制控件时获取的Y轴坐标

    // PopupWindow中的控件
    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;
    private TextView tTextView;
    // 录音控制类
    private AudioRecoderUtils mAudioRecoderUtils;
    // 音频播放
    public MediaPlayer mediaPlayer = new MediaPlayer();

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.video_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "Video", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAudioList.setLayoutManager(layoutManager);
        audioAdapter = new AudioAdapter(this, mediaPlayer);
        if (audioInfos != null && audioInfos.size() > 0) {
            audioAdapter.addData(audioInfos);
            audioInfos.clear();
        }
        rvAudioList.setAdapter(audioAdapter);

        initVideoGridView();

        initPopupWindow();
        initAudioRecorder();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtil.d("MediaPlayer", what + ":" + extra);
                return false;
            }
        });
        ibAudioRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startY = (int) event.getY();
                        // 手机息屏后,会释放MediaPlayer资源,所以需要判断为空
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }
                        }
                        // 控制不能超过三个录音
                        if (audioAdapter.getItemCount() < 3) {
                            mPop.showAtLocation(rlAddInfoLayout, Gravity.CENTER, 0, 0);
                        } else {
                            Toast.makeText(mContext, "音频文件不能超过3个", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        int endY = (int) event.getY();
                        if (startY < 0) {
                            return true;
                        }
                        if (endY - startY < CANCLE_LENGHT) {
                            mAudioRecoderUtils.cancelRecord();          // 取消录音（不保存录音文件）
                            mPop.dismiss();
                        } else {
                            if (audioAdapter.getItemCount() < 4) {
                                mAudioRecoderUtils.stopRecord();        // 结束录音（保存录音文件）
                                mPop.dismiss();
                            }
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        int moveY = (int) event.getY();
                        if (moveY < 0) {
                            return true;
                        }
                        if (moveY - startY < CANCLE_LENGHT) {
                            tTextView.setText("松开手指,取消录音");
                        } else {
                            tTextView.setText("手指上滑,取消录音");
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        mAudioRecoderUtils.cancelRecord();      // 取消录音（不保存录音文件）
                        mPop.dismiss();
                        break;
                }
                return true;
            }
        });
    }

    private void initPopupWindow() {
        // PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(this, view);

        // PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        tTextView = (TextView) view.findViewById(R.id.tv_recording_title);
    }

    private void initVideoGridView() {
        videoAdapter = new VideoInfoGridAdapter(this);
        if (photoInfos != null && photoInfos.size() > 0) {
            videoAdapter.addData(photoInfos);
            photoInfos.clear();
        }
        LogUtil.d("Video", videoAdapter.getCount() + "");
        svAddVideo.setAdapter(videoAdapter);
        svAddVideo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = svAddVideo.getWidth();
                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                videoAdapter.setItemSize(columnWidth);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    svAddVideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    svAddVideo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        //设置GridView点击事件
        svAddVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == (videoAdapter.getCount() - 1)) {
                    if (videoAdapter.getCount() == MAX_VIDEO_COUNT + 1) {
                        Toast.makeText(mContext, "最多上传3个视频", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Uri uri = Uri.parse(videoAdapter.getItem(position).getImagePath());
                    // 调用系统自带的播放器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/*");
                    startActivity(intent);
                }
            }
        });
    }


    private void initAudioRecorder() {
        mAudioRecoderUtils = new AudioRecoderUtils();

        // 录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            // 录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            // 录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath, long audioTime) {
                // showMessage("录音保存在：" + filePath);
                if (filePath.equals("") && audioTime < 1000) {
                    Toast.makeText(mContext, "录音时间过短", Toast.LENGTH_SHORT).show();
                } else {
                    mTextView.setText(TimeUtils.long2String(0));
                    AudioInfoBean bean = new AudioInfoBean();
                    bean.setAdudioPath(filePath);
                    bean.setAudioTime(String.valueOf(audioTime / 1000));
                    audioAdapter.addData(bean);
                }

            }
        });
    }

    @OnClick({R2.id.ibAudioRecorder, R2.id.tvVideoTag, R2.id.cv_surface, R2.id.cv_texture, R2.id.cv_videoview, R2.id.cv_webview})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ibAudioRecorder) {

        } else if (viewId == R.id.tvVideoTag) {
            startVideoCaptureActivity();
        } else if (viewId == R.id.cv_surface) {
            Intent intent = new Intent(this, SurfaceViewActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.cv_texture) {
            Intent intent = new Intent(this, TextureViewActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.cv_videoview) {
            Intent intent = new Intent(this, VideoViewActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.cv_webview) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 拍摄视频页面
     */
    private void startVideoCaptureActivity() {
        String filename = VideoUtil.createFileName();
        LogUtil.d(TAG, "filename:" + filename);
        if (!TextUtils.isEmpty(filename)) {
            CaptureConfiguration config = VideoUtil.createCaptureConfiguration();
            Intent intent = new Intent(this, VideoCaptureActivity.class);
            intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
            intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
            startActivityForResult(intent, REQUEST_RECORD_VIDEO);
        } else {
            LogUtil.d(TAG, "文件夹路径为空");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_VIDEO) {
            if (resultCode == Activity.RESULT_OK) {
                String fileName = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
                LogUtil.d(TAG, "文件夹路径为空");
                ImageInfo image = new ImageInfo();
                image.setImagePath(fileName);
                image.setImageType(2);
                videoAdapter.addData(image);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mContext, "取消视频拍摄", Toast.LENGTH_SHORT).show();
            } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
                Toast.makeText(mContext, "拍摄出现错误", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
