package com.push.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.base.config.GlobalConfig;
import com.base.utils.LogUtil;
import com.base.utils.ShareUtil;
import com.push.app.control.Push;

public class JpushService extends IntentService {
    public static final String TAG = "JIGUANG-JpushService";

    public static final String ACTION_CHECK_JPUSH = "com.intent.action.CHECK_JPUSH";

    public static final long ALARM_SHORT = 10 * 60 * 1000; //10 min

    public JpushService() {
        super(TAG);
    }

    public static void startJpush(Context context) {
        LogUtil.d(Push.TAG, TAG + ":startAutoCheck");
        Push.resume(context.getApplicationContext());
        Intent checkIntent = new Intent(ACTION_CHECK_JPUSH);
        PendingIntent pi = PendingIntent.getService(context, 0, checkIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(pi);

        LogUtil.d(TAG, "setRepeating: ");
        mgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_SHORT, pi);
    }

    public static void stopJpush(Context context){
        LogUtil.d(TAG, TAG + ":stopJpush");
        Push.stop(context.getApplicationContext());

        Intent checkIntent = new Intent(ACTION_CHECK_JPUSH);
        PendingIntent pi = PendingIntent.getService(context, 0, checkIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(pi);

        Intent intent = new Intent(context, JpushService.class);
        context.stopService(intent);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        LogUtil.d(TAG, "onStart :" + startId);
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            LogUtil.d(TAG, "onHandleIntent :" + action);
            if (ACTION_CHECK_JPUSH.equals(action)) {
                //username 不为空时，自动恢复
                if (!TextUtils.isEmpty(ShareUtil.getString(GlobalConfig.MY_USERNAME))) {
                    Push.resume(this);
                    LogUtil.d(TAG, "onHandleIntent :recovery for token");
                }else{
                    LogUtil.d(TAG, "onHandleIntent :no recovery for no token");
                }
            }
        }
    }
}
