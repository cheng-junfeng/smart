package com.smart.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.base.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;


public class BindService extends Service {
    private final String TAG = "BindService";

    private int mCount = 0;
    private Timer timer;
    /**
     * 创建生成的本地 Binder 对象，实现 AIDL 制定的方法
     */
    public class MyBind extends Binder {
        public int getCount(){
            LogUtil.d(TAG, "getCount:"+mCount);
            return mCount;
        }

        public void stopTimer(){
            if(timer != null){
                timer.cancel();
                timer = null;
            }
        }
    }

    /**
     * 客户端与服务端绑定时的回调，返回 mIBinder 后客户端就可以通过它远程调用服务端的方法，即实现了通讯
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "onCreate:"+mCount+":"+Thread.currentThread());
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mCount++;
                if(mCount > 50){
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy:"+mCount);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
