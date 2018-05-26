package com.base.app.event;

import android.support.annotation.NonNull;

import com.base.utils.LogUtil;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxBusHelper {

    public final static String TAG = "RxBusHelper";
    private static HashMap<String, CompositeDisposable> mSubscriptionMap;

    /**
     * 发布消息
     *
     * @param o
     */
    public static void post(Object o) {
        RxBus.getInstance().post(o);
    }

    public static <T> void doOnMainThread(Object o, Class<T> aClass, final OnEventListener<T> listener) {
        Disposable d = RxBus.getInstance().register(aClass).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<T>() {
            @Override
            public void accept(@NonNull T t) throws Exception {
                listener.onEvent(t);
            }
        });
        addSubscription(o, d);
    }

    /**
     * 保存订阅后的disposable
     *
     * @param o
     * @param disposable
     */
    private static void addSubscription(Object o, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = o.getClass().getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubscriptionMap.put(key, disposables);
        }
    }

    /**
     * 取消订阅
     *
     * @param o
     */
    public static void unSubscribe(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }
        String key = o.getClass().getName();
        LogUtil.d(TAG, "unSubscribe:" + key);
        if (!mSubscriptionMap.containsKey(key)) {
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }
        mSubscriptionMap.remove(key);
    }

    /**
     * 取消所有的订阅，后面都接收不到了，这个用在退出程序的时候调用
     */
    public static void unregisterAll() {
        RxBus.getInstance().unregisterAll();
    }

    public interface OnEventListener<T> {
        void onEvent(T t);
    }
}
