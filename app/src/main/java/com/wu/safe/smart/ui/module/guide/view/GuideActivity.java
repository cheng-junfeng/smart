package com.wu.safe.smart.ui.module.guide.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.base.utils.LogUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.base.app.thread.WeakHandler;
import com.wu.safe.smart.ui.module.main.MainActivity;
import com.user.ui.view.MyLoginActivity;
import com.user.ui.presenter.LoginPresenter;

public class GuideActivity extends BaseCompatActivity {
    private final static String TAG = "GuideActivity";

    private boolean isLogin = false;
    private Context mContext;
    private LoginPresenter presenter;

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mContext = this;
        presenter = new LoginPresenter();

        isLogin = presenter.isLogin(this);
        WeakHandler handler = new WeakHandler(this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LogUtil.d(TAG, "isLogin "+isLogin);
                if (isLogin) {
                    presenter.initLogin(mContext);
                    readGoFinishAnim(MainActivity.class);
                } else {
                    readGoFinishAnim(MyLoginActivity.class);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    /**
     * 屏蔽返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
