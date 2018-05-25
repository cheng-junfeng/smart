package com.zxinglib.control;

import android.graphics.Rect;

public interface ICropRect {
    Rect getCropRect(int previewW);
    boolean needCrop();
}
