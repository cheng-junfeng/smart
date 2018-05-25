package com.zxinglib.result;

import android.graphics.Bitmap;

import com.google.zxing.Result;
import com.zxinglib.control.decode.DecodeType;

public class SuccessResult {
    public DecodeType decodeType;
    public Result[] rawResults;
    public Result rawResult;
    public Bitmap previewBitmap;

    public Result getResult(){
        return this.rawResult;
    }
}
