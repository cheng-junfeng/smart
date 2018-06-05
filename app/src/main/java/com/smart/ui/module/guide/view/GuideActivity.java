package com.smart.ui.module.guide.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.base.utils.LogUtil;
import com.custom.widget.CountDownView;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.ui.module.main.MainActivity;
import com.user.ui.presenter.LoginPresenter;
import com.user.ui.view.MyLoginActivity;

import butterknife.BindView;

public class GuideActivity extends BaseCompatActivity {
    private final static String TAG = "GuideActivity";
    @BindView(R.id.cdv_time)
    CountDownView cdvTime;

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

        cdvTime.setTime(3);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(new CountDownView.OnLoadingFinishListener() {
            @Override
            public void finish() {
                LogUtil.d(TAG, "isLogin " + isLogin);
                if (isLogin) {
                    presenter.initLogin(mContext);
                    readGoFinishAnim(MainActivity.class);
                } else {
                    readGoFinishAnim(MyLoginActivity.class);
                }
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cdvTime!=null && cdvTime.isShown()){
            cdvTime.stop();
        }
    }
}
