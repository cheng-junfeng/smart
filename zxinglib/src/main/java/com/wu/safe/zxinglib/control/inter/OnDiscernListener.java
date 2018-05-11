package com.wu.safe.zxinglib.control.inter;


import com.wu.safe.zxinglib.result.ErrorResult;
import com.wu.safe.zxinglib.result.SuccessResult;

public interface OnDiscernListener {
    void onSuccess(SuccessResult successResult);
    void onError(ErrorResult errorResult);
}
