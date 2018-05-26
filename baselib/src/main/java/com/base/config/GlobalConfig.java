package com.base.config;

import com.base.BuildConfig;

public interface GlobalConfig {
    boolean IS_DEBUG = BuildConfig.RELEASE;

    //intent
    String MAIN_INTENT = "android.intent.smart.MAIN";
    //sharePre
    String MY_USERNAME = "my.username";
}