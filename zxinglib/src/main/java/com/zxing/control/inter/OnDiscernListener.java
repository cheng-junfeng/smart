package com.zxing.control.inter;


import com.zxing.result.ErrorResult;
import com.zxing.result.SuccessResult;

public interface OnDiscernListener {
    void onSuccess(SuccessResult successResult);
    void onError(ErrorResult errorResult);
}
