package com.photo.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.base.utils.ToolbarUtil;
import com.bumptech.glide.Glide;
import com.photo.R;
import com.photo.R2;
import com.photo.app.PhotoBaseCompatActivity;
import com.photo.config.PhotoConfig;
import com.photo.ui.widget.PhotoView;

import java.io.File;

import butterknife.BindView;


public class PhotoViewActivity extends PhotoBaseCompatActivity {
    private static final String TAG = "PhotoViewActivity";
    @BindView(R2.id.image)
    PhotoView image;

    private int default_res = R.drawable.photo_default_image;
    String urlStr;
    String titleStr;

    @Override
    protected int setContentView() {
        return R.layout.photo_activity_show_big_image;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        urlStr = "/storage/emulated/0/1/timg-13.jpg";
//		urlStr = (bundle == null) ? null : bundle.getString(PhotoConfig.PHOTO_URL);
        titleStr = (bundle == null) ? PhotoConfig.DEFAULT_TITLE : bundle.getString(PhotoConfig.PHOTO_NAME, PhotoConfig.DEFAULT_TITLE);
        ToolbarUtil.setToolbarLeft(toolbar, titleStr, null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
    }

    private void initView() {
        File file = new File(urlStr);
        //show the image if it exist in local path
        if (file != null && file.exists()) {
            Glide.with(this)
                    .load(file)
                    .error(default_res)
                    .into(image);
        } else {
            image.setImageResource(default_res);
        }
    }
}
