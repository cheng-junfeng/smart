package com.wu.safe.smart.ui.module.other.thread;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * CountDownTime 在主线程完成回调，结束不方便
 * TimerTask 在一个子线程中完成回掉，相对靠谱
 * Rx 子线程执行，主线程回调，略微延迟
 * ThreadPool 线程池，多个线程，子线程中返回
 * Thread 线程 单个线程 相对靠谱
 * HandlerThread   handler线程，单个线程，子线程中返回
 *
 * */

public class ThreadActivity extends BaseCompatActivity {
    private final static String TAG = "ThreadActivity";

    private int mCount = 0;

    @Override
    protected int setContentView() {
        return R.layout.activity_thread_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "线程", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        boolean isMain = (Looper.getMainLooper() == Looper.myLooper());
        LogUtil.d(TAG, "MainThread:" + Thread.currentThread().getName());
    }

    private Handler handler1;
    private void holdHandler() {
        mCount = 0;
        LogUtil.d(TAG, "[handler]holdHandler:");
        HandlerThread handlerThread = new HandlerThread("handler");
        handlerThread.start();
        handler1 = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // 处理从子线程发送过来的消息
                mCount++;
                if(mCount <= 3){
                    LogUtil.d(TAG, "[handler]Handler:" + Thread.currentThread().getName());
                    handler1.sendMessageDelayed(new Message(), 1000);
                }
            }
        };
        handler1.sendMessage(new Message());
    }

    Handler tempHandler;
    private void holdThread() {
        LogUtil.d(TAG, "[Thread]holdThread:");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++){
                    LogUtil.d(TAG, "[Thread]run:"+Thread.currentThread());
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){}
                }
            }
        };
        tempHandler = new Handler();
        tempHandler.post(runnable);
    }

    ExecutorService executorService;
    private void holdThreadPool() {
        LogUtil.d(TAG, "[ThreadPool]holdThreadPool:");
        executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 3; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    LogUtil.d(TAG, "[ThreadPool]ExecutorService" + Thread.currentThread().getName());
                }
            };
            executorService.execute(syncRunnable);
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){}
        }
    }

    private void holdRx() {
        LogUtil.d(TAG, "[Rx]holdRx:");
        final int count = 3;
        Observable.interval(0, 1, TimeUnit.SECONDS) //30 min
                .take((int) (count)) //设置总共发送的次数
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "[Rx]onSubscribe");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtil.d(TAG, "[Rx]onNext:"+ Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG, "[Rx]onError:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "[Rx]onComplete:");
                    }
                });
    }

    private Timer timer;
    private void holdTimer() {
        mCount = 0;
        LogUtil.d(TAG, "[Timer]holdTimer:");
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mCount++;
                if(mCount > 3){
                    if (timer != null) {
                        timer.cancel();
                    }
                } else {
                    LogUtil.d(TAG, "[Timer]onComplete:");
                }
            }
        };
        timer.schedule(task, 0, 1*1000);
    }

    private CountDownTimer countDownTimer;
    private void holdCount() {
        mCount = 0;
        LogUtil.d(TAG, "[Count]holdCount:");
        countDownTimer = new CountDownTimer(4 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                LogUtil.d(TAG, "[Count]onTick:" + millisUntilFinished);
                mCount++;
                if(mCount > 4){
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                LogUtil.d(TAG, "[Count]onFinish:");
            }
        };
        countDownTimer.start();
    }

    @OnClick({R.id.handler_view, R.id.thread_view, R.id.thread_pool_view, R.id.rx_view, R.id.timer_view, R.id.count_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.handler_view:
                holdHandler();
                break;
            case R.id.thread_view:
                holdThread();
                break;
            case R.id.thread_pool_view:
                holdThreadPool();
                break;
            case R.id.rx_view:
                holdRx();
                break;
            case R.id.timer_view:
                holdTimer();
                break;
            case R.id.count_view:
                holdCount();
                break;
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        if(handler1 != null){
            handler1.removeCallbacks(null);
            handler1 = null;
        }

        if(tempHandler != null){
            tempHandler.removeCallbacks(null);
            tempHandler = null;
        }

        if(executorService != null){
            executorService.shutdown();
        }

        if(timer != null){
            timer.cancel();
            timer = null;
        }

        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
