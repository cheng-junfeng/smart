package com.smart.ui.module.other.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.smart.IMyAidlInterface;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.service.AidlService;

import butterknife.BindView;
import butterknife.OnClick;


public class AidlActivity extends BaseCompatActivity {
    private final static String TAG = "AidlActivity";
    @BindView(R.id.aidl_text)
    TextView aidlText;

    private IMyAidlInterface mAidl;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
            LogUtil.d(TAG, "onServiceConnected");
            mAidl = IMyAidlInterface.Stub.asInterface(service);
            try {
                aidlText.setText(mAidl.getCount() + "");
            } catch (RemoteException e) {
                LogUtil.d(TAG, "onServiceConnected");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(TAG, "onServiceDisconnected");
            try {
                mAidl.stopTimer();
            } catch (RemoteException e) {
                LogUtil.d(TAG, "onServiceConnected");
            }
            mAidl = null;
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_aidl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "AIDL", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        createConnect();
    }

    private void createConnect() {
        Intent intent1 = new Intent(getApplicationContext(), AidlService.class);
        bindService(intent1, mConnection, BIND_AUTO_CREATE);
    }

    @OnClick({R.id.aidl_get, R.id.aidl_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aidl_get:
                try {
                    aidlText.setText(mAidl.getCount() + "");
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "onServiceConnected");
                }
                break;
            case R.id.aidl_stop:
                try {
                    mAidl.stopTimer();
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "onServiceConnected");
                }
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
