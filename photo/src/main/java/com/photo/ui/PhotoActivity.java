package com.photo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.base.utils.ToolbarUtil;
import com.photo.R;
import com.photo.app.PhotoBaseCompatActivity;
import com.photo.app.PhotoBaseCompatFragment;
import com.photo.config.PhotoConfig;
import com.photo.ui.fragment.BaseFragmentFactory;


public class PhotoActivity extends PhotoBaseCompatActivity {
    private final static String TAG = "PhotoActivity";

    private String urlStr;

    @Override
    protected int setContentView() {
        return R.layout.activity_pic_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
		urlStr = (bundle == null) ? null : bundle.getString(PhotoConfig.PHOTO_URL);
        String titleStr = (bundle == null) ? PhotoConfig.DEFAULT_TITLE : bundle.getString(PhotoConfig.PHOTO_TITLE, PhotoConfig.DEFAULT_TITLE);

        ToolbarUtil.setToolbarLeft(toolbar, titleStr, null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = getIntent().getExtras();
        int pos = bundle.getInt(PhotoConfig.FRAGMENT_POS, 1);
        PhotoBaseCompatFragment fragment = BaseFragmentFactory.getInstance().getFragment(pos);
        fragment.setPath(urlStr);
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.commit();
    }
}
