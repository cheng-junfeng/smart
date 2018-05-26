package com.base.net.control;

import com.google.gson.annotations.SerializedName;

public class HttpResult<T> {

    // this will receive message or status, msg as message field
    @SerializedName(value = "message", alternate = {"msg"})
    private String message;

    public String getMessage() {
        return message;
    }

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    //用来模仿Data
    @SerializedName(value = "data", alternate = {"result"})
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
