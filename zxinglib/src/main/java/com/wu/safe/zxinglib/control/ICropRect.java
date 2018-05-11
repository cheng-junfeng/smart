package com.wu.safe.zxinglib.control;

import android.graphics.Rect;

public interface ICropRect {
    Rect getCropRect(int previewW);
    boolean needCrop();
}
