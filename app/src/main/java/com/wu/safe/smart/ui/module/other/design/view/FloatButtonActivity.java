package com.wu.safe.smart.ui.module.other.design.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.hint.utils.ToastUtils;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class FloatButtonActivity extends BaseCompatActivity {
    private final static String TAG = "FloatButtonActivity";

    @BindView(R.id.float_button)
    FloatingActionMenu floatButton;

    @Override
    protected int setContentView() {
        return R.layout.activity_float_button;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "悬浮按钮", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.float_A, R.id.float_B})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.float_A:
                floatButton.toggle(true);
                ToastUtils.showToast(this, "A");
                break;
            case R.id.float_B:
                floatButton.toggle(true);
                ToastUtils.showToast(this, "B");
                break;
        }
    }
}
