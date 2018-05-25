package com.zxinglib.result;

import android.graphics.Bitmap;

import com.zxinglib.control.decode.DecodeType;

public class ErrorResult {
    public DecodeType decodeType;
    public String message;
    public Bitmap previewBitmap;
}
