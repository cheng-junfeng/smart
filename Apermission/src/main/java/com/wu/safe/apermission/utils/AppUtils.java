package com.wu.safe.apermission.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.smart.base.utils.LogUtil;

import java.io.File;


public class AppUtils {
    private final static String TAG = "AppUtils";
    public static boolean isAppInstalled(Context context, String packagename)
    {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        }catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
            return false;
        }else{
            return true;
        }
    }

    public static void installUpdate(Context context, String path) {
        File file = new File(path);
        LogUtil.d(TAG, "file:" + file.getAbsolutePath());

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.linkstart.flame.provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        if (uri != null) {
            LogUtil.d(TAG, uri.getPath());
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            // 开始安装
            context.startActivity(installIntent);
        } else {
            LogUtil.d(TAG, "uri null");
        }
    }
}
