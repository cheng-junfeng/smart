package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hint.listener.OnConfirmListener;
import com.hint.utils.DialogUtils;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.user.ui.presenter.LoginPresenter;
import com.user.ui.view.MyLoginActivity;
import com.user.ui.view.MySettingActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class LeftViewActivity extends BaseCompatActivity {
    private final static String TAG = "LeftViewActivity";
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private View view;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_left_view;
    }

    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        initView();
    }

    private void initView() {
        toolbarTitle.setText("左侧滑入");
        view = navView.inflateHeaderView(R.layout.nav_header_main);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.tv_image_left, R.id.setting, R.id.quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_image_left:
                onBackPressed();
                break;
            case R.id.setting:
                drawerLayout.closeDrawer(GravityCompat.START);
                readGo(MySettingActivity.class);
                break;
            case R.id.quit:
                drawerLayout.closeDrawer(GravityCompat.START);
                DialogUtils.showConfirmDialog(this, "确定退出当前账号？", new OnConfirmListener() {
                    @Override
                    public void onClickPositive() {
                        new LoginPresenter().logout(mContext);
                        readGoFinishAnim(MyLoginActivity.class);
                        finishAll();
                    }

                    @Override
                    public void onClickNegative() {
                    }
                });
                break;
        }
    }
}
