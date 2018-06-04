package com.smart.ui.module.other.plug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.hint.listener.OnConfirmListener;
import com.hint.utils.DialogUtils;
import com.base.utils.FileUtil;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.utils.AppUtils;

import butterknife.OnClick;


public class PlugActivity extends BaseCompatActivity {
    private static final String HIKON_PKG_NAME = "com.linkstart.hikvisionsdk";
    private static final String HAIAPK_NAME = "hikvisionsdk-release.apk";

    private final static String TAG = "PlugActivity";
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_plug;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "插件", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.cvDisasterLive})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.cvDisasterLive) {
            if (AppUtils.isAppInstalled(this, HIKON_PKG_NAME)) {
                try {
                    PackageInfo packageInfo = this.getPackageManager().getPackageInfo("com.linkstart.hikvisionsdk", 0);
                    LogUtil.d(TAG, "");
                    Intent i = new Intent();
                    ComponentName cn = new ComponentName("com.linkstart.hikvisionsdk",
                            "com.hikvision.sdk.ui.UnitListActivity");
                    i.setComponent(cn);
                    i.putExtra("url", "localhost");
                    i.putExtra("userName", "admin");
                    i.putExtra("password", "123456");
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                DialogUtils.showConfirmDialog(mContext, "尚未安装单位视频插件，立即安装", new OnConfirmListener() {
                    @Override
                    public void onClickPositive() {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + HAIAPK_NAME;
                        if (FileUtil.copyFile(mContext, HAIAPK_NAME, path)) {
                            AppUtils.installUpdate(mContext, path);
                        }
                    }

                    @Override
                    public void onClickNegative() {
                    }
                });
            }
        }
    }
}
