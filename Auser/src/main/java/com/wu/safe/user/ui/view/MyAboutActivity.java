package com.wu.safe.user.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.smart.base.utils.DialogUtils;
import com.smart.base.utils.LogUtil;
import com.smart.base.utils.ShareUtil;
import com.smart.base.utils.ToolbarUtil;
import com.wu.safe.user.BuildConfig;
import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseCompatActivity;
import com.wu.safe.user.config.UserSharePre;

import butterknife.BindView;
import butterknife.OnClick;


public class MyAboutActivity extends UserBaseCompatActivity {

    private final static String TAG = "MyAboutActivity";
    @BindView(R2.id.tv_version)
    TextView tvVersion;
    @BindView(R2.id.iv_new)
    TextView ivNew;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "关于我们", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        tvVersion.setText("当前版本：" + BuildConfig.VERSION_NAME);
        int currentCode = BuildConfig.VERSION_CODE;
        int upgradeCode = ShareUtil.getInt(UserSharePre.MY_UPGRADE_CODE, 0);
        LogUtil.d(TAG, "onResume:"+currentCode+" "+upgradeCode);
        if(upgradeCode > currentCode){
            ivNew.setVisibility(View.VISIBLE);
        }else{
            ivNew.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.ly_checkVersion)
    public void onViewClicked() {
        DialogUtils.showProgressMsgDialog(mContext, "正在检测更新");
        Beta.checkUpgrade();
        tvVersion.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtils.dismissProgressDialog();
            }
        }, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        DialogUtils.dismissProgressDialog();
        LogUtil.d(TAG, "onPause:");
    }
}
