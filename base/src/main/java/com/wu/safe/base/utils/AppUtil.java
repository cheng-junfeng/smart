package com.wu.safe.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppUtil {
    private static final String RELEASE_META = "APP_RELEASE";
    private static final String PUSH_META = "JPUSH_APPKEY";
    private static final String debug_key =  "e6e55105889bcfacb706cc1f";

    public static boolean getAppRelease(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getBoolean(RELEASE_META, true);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void changeToDebug(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appInfo.metaData.putString(PUSH_META, debug_key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}