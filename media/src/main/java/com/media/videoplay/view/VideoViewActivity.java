package com.media.videoplay.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.media.R;
import com.media.R2;
import com.media.app.activity.MediaBaseCompatActivity;
import com.media.utils.ToolbarUtil;

import butterknife.BindView;


public class VideoViewActivity extends MediaBaseCompatActivity {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.videoview)
    VideoView videoview;

    @Override
    protected int setContentView() {
        return R.layout.video_video_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "VideoView", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //本地的视频
          Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);

        //设置视频控制器
        videoview.setMediaController(new MediaController(this));

        //播放完成回调
        videoview.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //设置视频路径
        videoview.setVideoURI(uri);

        //开始播放视频
        videoview.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VideoViewActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}