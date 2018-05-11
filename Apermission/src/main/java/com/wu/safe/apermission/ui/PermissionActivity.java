package com.wu.safe.apermission.ui;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.wu.safe.apermission.R;
import com.wu.safe.apermission.app.acitvity.PermissBaseCompatActivity;
import com.wu.safe.apermission.utils.PermissionsCheckerUtils;
import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ToolbarUtil;


public class PermissionActivity extends PermissBaseCompatActivity {

    private final static String TAG = "PermissionActivity";

    public static final String[] mPermission=
            {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_SETTINGS};
    private PermissionsCheckerUtils checkerUtils;
    private static final int REQUEST_CODE_STORAGE = 100;
    private Context mContext;
    private boolean checkPermission = false;

    @Override
    protected int setContentView() {
        return R.layout.activity_permiss;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "权限", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        checkerUtils = new PermissionsCheckerUtils(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission && checkerUtils.lacksPermissions(mPermission)) {
                checkPermission = true;
                ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_STORAGE);
                LogUtil.d(TAG, "permission onResume 1");
            } else {
                DialogUtils.showToast(mContext, "已有权限");
            }
        } else {
            DialogUtils.showToast(mContext, "SDK <23");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_STORAGE:{
                if (requestCode == REQUEST_CODE_STORAGE && checkerUtils.lacksPermissions(mPermission)) {
                    LogUtil.d(TAG, "delay");
                    DialogUtils.showToast(mContext, "权限申请失败");
                } else {
                    LogUtil.d(TAG, "grant");
                    DialogUtils.showToast(mContext, "权限申请成功");
                }
            }break;
            default:break;
        }
    }
}
