package com.zxing.control.inter;

import android.hardware.Camera;

/**
 * 相机开启回调
 */
public interface CameraOpenCallBack {
    void onSuccess(Camera camera);
    void onException(Exception exception);
}
