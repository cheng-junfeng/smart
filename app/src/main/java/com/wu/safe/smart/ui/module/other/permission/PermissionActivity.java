package com.wu.safe.smart.ui.module.other.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.hint.utils.ToastUtils;
import com.permission.PermissionFail;
import com.permission.PermissionHelper;
import com.permission.PermissionSucceed;
import com.permission.PermissionUtils;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import butterknife.OnClick;


public class PermissionActivity extends BaseCompatActivity {

    private final static String TAG = "PermissionActivity";
    // 打电话权限申请的请求码
    private static final int CALL_PHONE_REQUEST_CODE = 0x0011;

    private Context mContext;

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
        PermissionHelper.with(this).requestCode(CALL_PHONE_REQUEST_CODE)
                .requestPermission(Manifest.permission.CALL_PHONE).request();
    }

    @PermissionSucceed(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhone() {
        LogUtil.d(TAG, "PermissionSucceed");
        if(PermissionUtils.lacksPermission(mContext, Manifest.permission.CALL_PHONE)){
            ToastUtils.showToast(mContext, "缺乏拨号权限");
        }else{
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:18802734547");
            intent.setData(data);
            startActivity(intent);
        }
    }

    @PermissionFail(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhoneFail() {
        LogUtil.d(TAG, "PermissionFail");
        ToastUtils.showToast(mContext, "您拒绝了拨打电话");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this, CALL_PHONE_REQUEST_CODE, permissions);
    }

    @OnClick(R.id.ll_permiss)
    public void onViewClicked() {
        PermissionHelper.with(this).requestCode(CALL_PHONE_REQUEST_CODE)
                .requestPermission(Manifest.permission.CALL_PHONE).request();
    }
}
