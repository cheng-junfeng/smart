package com.smart.jni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class JniActivity extends BaseCompatActivity {
    private final static String TAG = "JniActivity";

    @BindView(R.id.aidl_text)
    TextView aidlText;
    @BindView(R.id.aidl_stop)
    Button aidlStop;

    @Override
    protected int setContentView() {
        return R.layout.activity_aidl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate:" + Thread.currentThread());
        ToolbarUtil.setToolbarLeft(toolbar, "Jni", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        aidlStop.setVisibility(View.GONE);
    }


    @OnClick({R.id.aidl_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aidl_get:
                aidlText.setText(JniCount.getFromNative());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
    }
}
