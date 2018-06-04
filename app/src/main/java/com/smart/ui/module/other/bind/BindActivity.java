package com.smart.ui.module.other.bind;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.service.BindService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Service 提供了onBind 模式，可以将Activity/ 或service 绑定，在同一进程中
 * 如果service 在其它进程中，此时就会有ClassCastException 的问题，此时就不得不用AIDl
 * ava.lang.ClassCastException：android.os.BinderProxy cannot be cast
 * AIDL: Android Studio 直接可以创建接口，自动生成类，用起来也很方便（只有非基本类型需要序列号，稍微复杂）
 * */

public class BindActivity extends BaseCompatActivity {
    private final static String TAG = "BindActivity";
    @BindView(R.id.aidl_text)
    TextView aidlText;

    private BindService.MyBind mybind;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
            LogUtil.d(TAG, "onServiceConnected");
            mybind= (BindService.MyBind) service;
            aidlText.setText(mybind.getCount() + "");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(TAG, "onServiceDisconnected");
            mybind.stopTimer();
            mybind = null;
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_aidl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate:"+Thread.currentThread());
        ToolbarUtil.setToolbarLeft(toolbar, "BIND", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        createConnect();
    }

    private void createConnect() {
        Intent intent1 = new Intent(getApplicationContext(), BindService.class);
        bindService(intent1, mConnection, BIND_AUTO_CREATE);
    }

    @OnClick({R.id.aidl_get, R.id.aidl_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aidl_get:
                aidlText.setText(mybind.getCount() + "");
                break;
            case R.id.aidl_stop:
                mybind.stopTimer();
                mybind = null;
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        unbindService(mConnection);
    }
}
