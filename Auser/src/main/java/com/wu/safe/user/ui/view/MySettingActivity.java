package com.wu.safe.user.ui.view;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SDCardUtils;
import com.hintlib.utils.DialogUtils;
import com.smart.base.app.thread.WeakHandler;
import com.smart.base.utils.CacheUtil;
import com.smart.base.utils.LogUtil;
import com.smart.base.utils.ToolbarUtil;
import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MySettingActivity extends UserBaseCompatActivity {
    private final static String TAG = "MySettingActivity";

    @BindView(R2.id.tv_set_cache_size)
    TextView tvSetCacheSize;

    private Context mContext;
    private WeakHandler handler;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        handler = new WeakHandler(this);

        ToolbarUtil.setToolbarLeft(toolbar, "设置中心", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initCacheSize();
    }

    public void initCacheSize() {
        //目前只是展示内部缓存的大小
        try {
            if (SDCardUtils.isSDCardEnable()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (this.getCacheDir() == null || this.getCodeCacheDir() == null) {
                        return;
                    }
                }
                LogUtil.d(TAG, "cache:" + this.getCacheDir().getCanonicalPath());
                String cacheSize = CacheUtil.getCacheSize(this.getCacheDir());
                tvSetCacheSize.setText(cacheSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R2.id.rl_set_clean_cache, R2.id.rl_set_bind})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.rl_set_clean_cache){
            DialogUtils.showProgressMsgDialog(this, "正在清除缓存");
            try {
                LogUtil.d(TAG, "cache:" + this.getCacheDir().getCanonicalPath());
                CacheUtil.cleanInternalCache(mContext);
            } catch (Exception e) {
                LogUtil.d(TAG, "cache:" + e.toString());
            }

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    initCacheSize();
                    DialogUtils.dismissProgressDialog();
                }
            };
            handler.postDelayed(runnable, 1000);
        }else if(viewId == R.id.rl_set_bind){
            DialogUtils.showProgressMsgDialog(this, "正在绑定");
            Runnable runnable3 = new Runnable() {
                @Override
                public void run() {
                    DialogUtils.dismissProgressDialog();
                }
            };
            handler.postDelayed(runnable3, 2000);
        }
    }
}
