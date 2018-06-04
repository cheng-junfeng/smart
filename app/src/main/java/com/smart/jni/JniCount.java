package com.smart.jni;


public class JniCount {
    static {
        System.loadLibrary("JniCount");
    }

    public static native String getFromNative();
}
