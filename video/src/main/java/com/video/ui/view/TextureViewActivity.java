package com.video.ui.view;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.widget.Button;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.video.R;
import com.video.R2;
import com.video.app.VideoBaseCompatActivity;
import com.video.config.VideoStat;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class TextureViewActivity extends VideoBaseCompatActivity implements SurfaceTextureListener,
        OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
        OnVideoSizeChangedListener {
    private static final String TAG = "TextureViewActivity";

    @BindView(R2.id.textureview)
    TextureView textureview;

    @BindView(R2.id.start)
    Button start;
    @BindView(R2.id.pause)
    Button pause;

    private MediaPlayer mediaPlayer;
    private VideoStat status = VideoStat.INIT;
    private int currentPosition = 0;

    @Override
    protected int setContentView() {
        return R.layout.video_texture_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "TextureView", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textureview.setSurfaceTextureListener(this);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {

    }

    @Override
    public void onPrepared(MediaPlayer arg0) {

    }

    @Override
    public void onCompletion(MediaPlayer arg0) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int arg1) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int arg1,
                                          int arg2) {
        Surface s = new Surface(surface);
        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor fd = this.getAssets().openFd("video.mp4");
            mediaPlayer.setDataSource(fd.getFileDescriptor(),
                    fd.getStartOffset(), fd.getLength());
            mediaPlayer.setSurface(s);
            mediaPlayer.prepare();
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        mediaPlayer.stop();
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
                                            int arg2) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {

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
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        //停止
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}