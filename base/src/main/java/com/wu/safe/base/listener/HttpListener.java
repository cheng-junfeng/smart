package com.wu.safe.base.listener;

public interface HttpListener {
    void onSuccess(Object e);

    void onFail(String e);
}
