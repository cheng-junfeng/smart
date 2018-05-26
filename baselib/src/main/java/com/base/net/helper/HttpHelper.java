package com.base.net.helper;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.base.utils.LogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class HttpHelper {
    private final static String TAG = "HttpHelper";

    public static Observable getObservable(Observable apiObservable) {
        return  apiObservable
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.d(TAG, "0 doOnDispose...");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable getObservable(Observable apiObservable, LifecycleProvider lifecycle) {
        if (lifecycle != null) {
            //随生命周期自动管理.eg:onCreate(start)->onStop(end)
            return  apiObservable
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            LogUtil.d(TAG, "1 doOnDispose...");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle.bindToLifecycle());//需要在这个位置添加
        } else {
            return  apiObservable
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            LogUtil.d(TAG, "2 doOnDispose...");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static Observable getObservable(Observable apiObservable, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event) {
        if (lifecycle != null) {
            //手动管理移除监听生命周期.eg:ActivityEvent.STOP
            return  apiObservable
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            LogUtil.d(TAG, "3 doOnDispose...");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle.bindUntilEvent(event));//需要在这个位置添加
        } else {
            return  apiObservable
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            LogUtil.d(TAG, "4 doOnDispose...");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static Observable getObservable(Observable apiObservable, LifecycleProvider<FragmentEvent> lifecycle, FragmentEvent event) {
        if (lifecycle != null) {
            //手动管理移除监听生命周期.eg:FragmentEvent.STOP
            return  apiObservable
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            LogUtil.d(TAG, "5 doOnDispose...");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle.bindUntilEvent(event));//需要在这个位置添加
        } else {
            return  apiObservable
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            LogUtil.d(TAG, "6 doOnDispose...");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }
}
