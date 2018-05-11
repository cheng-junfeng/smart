package com.wu.safe.base.config;


import com.wu.safe.base.BuildConfig;

public interface GlobalConfig {
    boolean IS_DEBUG = BuildConfig.RELEASE;
    boolean LOG_OPEN = true;

    //intent
    String MAIN_INTENT = "android.intent.smart.MAIN";
    //sharePre
    String MY_USERNAME = "my.username";
}
