package com.audio.ui;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.audio.R;
import com.audio.R2;
import com.audio.app.AudioBaseCompatActivity;
import com.audio.utils.AudioRecoderUtils;
import com.audio.utils.PopupWindowFactory;
import com.audio.utils.TimeUtils;
import com.audio.ui.adapter.AudioAdapter;
import com.audio.ui.bean.AudioInfoBean;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AudioMainActivity extends AudioBaseCompatActivity {
    private final static String TAG = "AudioMainActivity";

    @BindView(R2.id.rvAudioList)
    RecyclerView rvAudioList;
    @BindView(R2.id.ibAudioRecorder)
    ImageButton ibAudioRecorder;
    @BindView(R2.id.rlAddInfoLayout)
    RelativeLayout rlAddInfoLayout;

    private AudioAdapter audioAdapter;
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
        return R.layout.audio_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "音频", null, new View.OnClickListener() {
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
        List<String> allString = AudioRecoderUtils.getExistAudioFile();
        audioInfos = new ArrayList<AudioInfoBean>();
        for (String tempStr : allString) {
            AudioInfoBean bean = new AudioInfoBean();
            bean.setAdudioPath(tempStr);
            bean.setAudioTime(String.valueOf(3));
            audioInfos.add(bean);
        }
        if (audioInfos != null && audioInfos.size() > 0) {
            audioAdapter.addData(audioInfos);
            audioInfos.clear();
        }
        rvAudioList.setAdapter(audioAdapter);

        initPopupWindow();
        initAudioRecorder();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtil.d(TAG, what + ":" + extra);
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
                            mAudioRecoderUtils.startRecord();
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
}
