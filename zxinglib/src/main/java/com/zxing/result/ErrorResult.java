package com.zxing.result;

import android.graphics.Bitmap;

import com.zxing.control.decode.DecodeType;

public class ErrorResult {
    public DecodeType decodeType;
    public String message;
    public Bitmap previewBitmap;
}
