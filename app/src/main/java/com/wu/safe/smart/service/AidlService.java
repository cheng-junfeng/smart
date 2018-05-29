package com.wu.safe.smart.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.base.utils.LogUtil;
import com.wu.safe.smart.IMyAidlInterface;

import java.util.Timer;
import java.util.TimerTask;


public class AidlService extends Service {
    private final String TAG = "AidlService";

    private int mCount = 0;
    private Timer timer;
    /**
     * 创建生成的本地 Binder 对象，实现 AIDL 制定的方法
     */
    private IBinder mIBinder = new IMyAidlInterface.Stub() {

        @Override
        public int getCount() throws RemoteException {
            LogUtil.d(TAG, "getCount:"+mCount);
            return mCount;
        }

        @Override
        public void stopTimer() throws RemoteException {
            LogUtil.d(TAG, "stopTimer");
            if (timer != null) {
                timer.cancel();
            }
        }
    };

    /**
     * 客户端与服务端绑定时的回调，返回 mIBinder 后客户端就可以通过它远程调用服务端的方法，即实现了通讯
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "onCreate:"+mCount);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mCount++;
                if(mCount > 100){
                    if (timer != null) {
                        timer.cancel();
                    }
                } else {
                    LogUtil.d(TAG, "[Timer]run:"+mCount);
                }
            }
        };
        timer.schedule(task, 0, 1*1000);
    }
}
