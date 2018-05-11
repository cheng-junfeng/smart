package com.wu.safe.base.app.thread;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class MyHandler extends Handler {
    WeakReference<Activity> weakReference ;

    public MyHandler(Activity activity ){
        weakReference = new WeakReference<Activity>( activity) ;
    }

    @Override
    public void handleMessage(Message msg) {
        if ( weakReference.get() != null ){
            super.handleMessage(msg);
        }
    }
}