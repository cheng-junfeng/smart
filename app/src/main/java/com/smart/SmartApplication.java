package com.smart;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.baidu.BmapAPI;
import com.blankj.utilcode.util.Utils;
import com.hyphenate.EaseUIAPI;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
import com.base.config.GlobalConfig;
import com.base.utils.LogUtil;
import com.push.PushAPI;
import com.smart.db.control.AppDbManager;
import com.smart.ui.module.other.data.control.DemoManager;
import com.user.UserAPI;
import com.user.ui.view.MyAboutActivity;
import com.user.ui.view.MyUpgradeActvity;

import java.util.List;

public class SmartApplication extends MultiDexApplication {
    public static final String TAG = "SmartApplication";
    private Context mContext;
    private static SmartApplication instance;

    public static synchronized SmartApplication getInstance() {
        if (null == instance) {
            instance = new SmartApplication();
        }
        return instance;
    }

    public SmartApplication(){}

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        LogUtil.d(TAG, "onCreate");

        //util
        Utils.init(mContext);
        if(!shouldInit()){
            LogUtil.d(TAG, "return while not main process");
            return;
        }
        LogUtil.d(TAG, "init");

        //db
        AppDbManager.init(mContext);

        //module
        UserAPI.init(mContext);
        PushAPI.init(mContext);
        BmapAPI.getInstance().init(mContext);
        EaseUIAPI.init(mContext);

        //upgrade
        initUpgrade();

        //demo
        DemoManager.init(mContext);//for datas
    }

    private void initUpgrade(){
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = false;
        Beta.initDelay = 1 * 1000;
        Beta.largeIconId = R.mipmap.ic_launcher;
        Beta.smallIconId = R.mipmap.ic_launcher;
        Beta.defaultBannerId = R.mipmap.ic_launcher;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.showInterruptedStrategy = false;
        Beta.canShowUpgradeActs.add(MyAboutActivity.class);
        Beta.upgradeListener = new UpgradeListener() {
            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                LogUtil.d(TAG, "onUpgrade:"+isManual);
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), MyUpgradeActvity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    //Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Bugly.init(getApplicationContext(), "438468c3c2", GlobalConfig.IS_DEBUG);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        LogUtil.d(TAG, "shouldInit myPid:" + myPid);
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
