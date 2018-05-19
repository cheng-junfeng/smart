package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wu.safe.base.ui.widget.CountDownView;
import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class CountViewActivity extends BaseCompatActivity {
    private final static String TAG = "CountViewActivity";
    @BindView(R.id.cdv_time)
    CountDownView cdvTime;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_count_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "倒计时", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        cdvTime.setTime(5);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(new CountDownView.OnLoadingFinishListener() {
            @Override
            public void finish() {
                DialogUtils.showToast(mContext, "完成倒计时");
                finish();
            }
        });
    }

    @OnClick(R.id.cdv_time)
    public void onViewClicked() {
        cdvTime.stop();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cdvTime.stop();
    }
}
