package com.wu.safe.apermission.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.wu.safe.apermission.R;
import com.wu.safe.apermission.R2;
import com.wu.safe.apermission.app.acitvity.PermissBaseCompatActivity;
import com.wu.safe.apermission.utils.AppUtils;
import com.wu.safe.base.app.listener.OnSelectClickListener;
import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.FileUtil;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ToolbarUtil;

import butterknife.OnClick;


public class PlugActivity extends PermissBaseCompatActivity {
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

    @OnClick({R2.id.cvDisasterLive})
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
                DialogUtils.showSelectDialog(mContext, "尚未安装单位视频插件，立即安装", new OnSelectClickListener() {
                    @Override
                    public void onClickPositive() {
                        DialogUtils.dismissDialog();
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + HAIAPK_NAME;
                        if (FileUtil.copyFile(mContext, HAIAPK_NAME, path)) {
                            AppUtils.installUpdate(mContext, path);
                        }
                    }

                    @Override
                    public void onClickNegative() {
                        DialogUtils.dismissDialog();
                    }
                });
            }
        }
    }
}
