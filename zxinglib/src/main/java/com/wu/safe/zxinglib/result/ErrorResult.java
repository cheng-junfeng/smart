package com.wu.safe.zxinglib.result;

import android.graphics.Bitmap;

import com.wu.safe.zxinglib.control.decode.DecodeType;

public class ErrorResult {
    public DecodeType decodeType;
    public String message;
    public Bitmap previewBitmap;
}
