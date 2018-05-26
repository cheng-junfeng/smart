package com.wu.safe.smart.ui.module.other.design.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import com.jaeger.library.StatusBarUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import butterknife.OnClick;

public class StatusViewActivity extends BaseCompatActivity {
    private final static String TAG = "StatusViewActivity";

    @Override
    protected int setContentView() {
        return R.layout.activity_status_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "状态栏", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.red_view, R.id.primary_view, R.id.traslant_view, R.id.all_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.red_view:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.color_red));
                break;
            case R.id.primary_view:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.colorPrimary));
                break;
            case R.id.traslant_view:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.colorTransparent));
                break;
            case R.id.all_view:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
        }
    }
}
