package com.jmolsmobile.videoplay.view;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jmolsmobile.videocapture.R;
import com.jmolsmobile.videocapture.R2;
import com.jmolsmobile.videocapture.app.activity.VideoBaseCompatActivity;
import com.jmolsmobile.videocapture.utils.LogUtil;
import com.jmolsmobile.videocapture.utils.ToolbarUtil;
import com.jmolsmobile.videoplay.config.VideoStat;

import butterknife.BindView;
import butterknife.OnClick;


public class SurfaceViewActivity extends VideoBaseCompatActivity {

    private static final String TAG = "SurfaceViewActivity";
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.surfaceView)
    SurfaceView surfaceView;

    @BindView(R2.id.start)
    Button start;
    @BindView(R2.id.pause)
    Button pause;

    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private VideoStat status = VideoStat.INIT;
    private int currentPosition = 0;

    @Override
    protected int setContentView() {
        return R.layout.video_surface_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "SurfaceView", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");
    }

    protected void initView() {
        updateButton(VideoStat.INIT);
        mediaPlayer = new MediaPlayer();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new SurfaceViewLis());
    }

    @OnClick({R2.id.start, R2.id.pause})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.start) {
            if (status == VideoStat.PLAY) {
                stop();
            } else {
                replay();//as replay from 0
            }
        } else if (viewId == R.id.pause) {
            if (status == VideoStat.PLAY) {
                pause();
            } else {
                replay();
            }
        }
    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtil.d(TAG, "holder被销毁了");
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
                stop();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtil.d(TAG, "holder被创建了");
            if (currentPosition == 0) {
                play(0);
            }

            if (currentPosition > 0) {
                play(currentPosition);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format,
                                   int width, int height) {
            LogUtil.d(TAG, "holder的大小变化了");
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            updateButton(VideoStat.PAUSE);
        }
    }

    private void replay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.seekTo(currentPosition);
            LogUtil.d(TAG, "replay replay :" + currentPosition);
        }
        updateButton(VideoStat.PLAY);
    }

    private void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
            currentPosition = 0;
            mediaPlayer.pause();
            updateButton(VideoStat.STOP);
        }
    }

    private void play(final int currentPos) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor fd = this.getAssets().openFd("video.mp4");
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
                    fd.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.setDisplay(surfaceHolder);
            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕回调
                    mediaPlayer.start();
                    mediaPlayer.seekTo(currentPos);
                    updateButton(VideoStat.PLAY);
                }
            });
        } catch (Exception e) {
            LogUtil.d(TAG, "Exception:" + e.toString());
            Toast.makeText(this, "播放失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void updateButton(VideoStat newStatus) {
        status = newStatus;
        switch (status) {
            case INIT: {
                start.setText("START");
                start.setEnabled(true);//start
                pause.setText("PAUSE");
                pause.setEnabled(false);
            }
            break;
            case PLAY: {
                start.setText("STOP");
                start.setEnabled(true);    //start
                pause.setText("PAUSE");
                pause.setEnabled(true);          //pause
            }
            break;
            case PAUSE: {
                start.setText("STOP");
                start.setEnabled(false);
                pause.setText("PLAY");
                pause.setEnabled(true);          //pause
            }
            break;
            case STOP: {
                start.setText("START");
                start.setEnabled(true);//start
                pause.setText("PAUSE");
                pause.setEnabled(false);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        //停止
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
