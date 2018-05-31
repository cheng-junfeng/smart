package com.video.ui.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Toast;


import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.custom.widget.ScrollGridView;
import com.video.R;
import com.video.R2;
import com.video.app.VideoBaseCompatActivity;
import com.video.capture.configuration.CaptureConfiguration;
import com.video.ui.adapter.VideoInfoGridAdapter;
import com.video.ui.bean.VideoInfo;
import com.video.utils.VideoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class VideoMainActivity extends VideoBaseCompatActivity {
    private final static String TAG = "VideoMainActivity";
    private static final int REQUEST_RECORD_VIDEO = 1;

    @BindView(R2.id.svAddVideo)
    ScrollGridView svAddVideo;

    private VideoInfoGridAdapter videoAdapter;
    ArrayList<VideoInfo> photoInfos;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.video_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "视频", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;
        initView();
    }

    private void initView() {
        videoAdapter = new VideoInfoGridAdapter(this);
        List<String> allString = VideoUtil.getExistVideoFile();
        photoInfos = new ArrayList<VideoInfo>();
        for (String tempStr : allString) {
            VideoInfo image = new VideoInfo();
            image.setImagePath(tempStr);
            image.setImageType(2);
            photoInfos.add(image);
        }

        if (photoInfos != null && photoInfos.size() > 0) {
            videoAdapter.addData(photoInfos);
            photoInfos.clear();
        }
        LogUtil.d(TAG, videoAdapter.getCount() + "");
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
                LogUtil.d(TAG, "position:" + position);
                if (position == (videoAdapter.getCount() - 1)) {
                    startVideoCaptureActivity();
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

    @OnClick({R2.id.tvVideoTag, R2.id.cv_surface, R2.id.cv_texture, R2.id.cv_videoview, R2.id.cv_webview})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tvVideoTag) {
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
                VideoInfo image = new VideoInfo();
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
