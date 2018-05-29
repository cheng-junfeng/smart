package com.wu.safe.smart.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.base.utils.LogUtil;
import com.hint.utils.ToastUtils;
import com.wu.safe.smart.ui.module.guide.view.GuideActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class AlarmService extends IntentService {
    public static final String TAG = "AlarmService";
    private static final String ACTION_CHECK_ALARM = "com.action.CHECK.ALARM";

    private static long lastNext = 0;
    private boolean isRunning = false;
    private Context mContext;

    public AlarmService() {
        super(TAG);
    }

    public static void startAutoCheck(Context context) {
        LogUtil.d(TAG, ":startAutoCheck");
        Intent checkIntent = new Intent(context, AlarmService.class);
        checkIntent.setAction(ACTION_CHECK_ALARM);
        context.startService(checkIntent);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        mContext = this;
        ToastUtils.showToast(mContext, "从服务onStart中弹出提示");
        LogUtil.d(TAG, "onStart: ");

        long alarmTime = System.currentTimeMillis() + 30*60*1000;
        Intent checkIntent = new Intent(ACTION_CHECK_ALARM);
        PendingIntent pi = PendingIntent.getService(mContext, 0, checkIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(pi);

        LogUtil.d(TAG, "set alarm: "+ alarmTime);
        mgr.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        LogUtil.d(TAG, "onHandleIntent: ");
        if (intent != null) {
            String action = intent.getAction();
            LogUtil.d(TAG, "onHandleIntent :" + action);
            if (ACTION_CHECK_ALARM.equals(action)) {
                if (isRunning) {
                    return;
                }
                Observable.interval(1, 30, TimeUnit.MINUTES) //30 min
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                LogUtil.d(TAG, "onSubscribe");
                                isRunning = true;
                            }

                            @Override
                            public void onNext(Long aLong) {
                                long nowNext = System.currentTimeMillis();
                                if ((nowNext - lastNext) < 5 * 1000) { // reject for too many next
                                    return;
                                }
                                lastNext = nowNext;

                                Intent newIntent = new Intent(mContext, GuideActivity.class);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplication().startActivity(newIntent);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.d(TAG, "onError:" + e.toString());
                                isRunning = false;
                            }

                            @Override
                            public void onComplete() {
                                LogUtil.d(TAG, "onComplete:");
                                isRunning = false;
                            }
                        });
            }
        }
    }
}
