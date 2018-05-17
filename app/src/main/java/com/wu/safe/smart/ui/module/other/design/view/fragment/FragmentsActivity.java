package com.wu.safe.smart.ui.module.other.design.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.config.Extra;


public class FragmentsActivity extends BaseCompatActivity {
    private final static String TAG = "FragmentsActivity";

    @Override
    protected int setContentView() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "图片工厂", null, new View.OnClickListener() {
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
        int pos = bundle.getInt(Extra.FRAGMENT_POS, 1);
        BaseCompatFragment fragment = BaseFragmentFactory.getInstance().getFragment(pos);
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.commit();
    }
}
