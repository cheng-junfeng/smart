package com.zxinglib.control.inter;


import com.zxinglib.result.ErrorResult;
import com.zxinglib.result.SuccessResult;

public interface OnDiscernListener {
    void onSuccess(SuccessResult successResult);
    void onError(ErrorResult errorResult);
}
