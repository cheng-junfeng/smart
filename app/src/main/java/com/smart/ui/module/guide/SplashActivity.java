package com.smart.ui.module.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.base.utils.ShareUtil;
import com.custom.widget.DotView;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.config.SharePre;
import com.smart.ui.module.guide.view.GuideActivity;
import com.smart.ui.module.guide.adapter.SplashViewPagerAdapter;
import com.user.ui.view.MyLoginActivity;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseCompatActivity {

    @BindView(R.id.vp_pager)
    ViewPager vpPager;
    @BindView(R.id.ll_dot)
    LinearLayout llDot;
    @BindView(R.id.btn_go)
    Button btnGo;

    private ArrayList<View> views;
    private DotView[] dotViews;
    private int currentIndex;

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    public void initView() {
        if (ShareUtil.getBoolean(SharePre.KEY_FIRST_SPLASH, true)) {
            initSplashView();
            initSplashListener();
        } else {
            readGoFinish(GuideActivity.class);
        }
    }

    private void initSplashView() {
        View view1 = View.inflate(this, R.layout.activity_splash_view, null);
        View view2 = View.inflate(this, R.layout.activity_splash_view, null);
        View view3 = View.inflate(this, R.layout.activity_splash_view, null);
        View view4 = View.inflate(this, R.layout.activity_splash_view, null);

        ((ImageView) view1.findViewById(R.id.iv_image)).setImageResource(R.mipmap.bg_1);
        ((ImageView) view2.findViewById(R.id.iv_image)).setImageResource(R.mipmap.bg_2);
        ((ImageView) view3.findViewById(R.id.iv_image)).setImageResource(R.mipmap.bg_3);
        ((ImageView) view4.findViewById(R.id.iv_image)).setImageResource(R.mipmap.bg_4);

        views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
    }

    private void initSplashListener() {
        SplashViewPagerAdapter adapter = new SplashViewPagerAdapter(views);
        vpPager.setAdapter(adapter);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
                if (position + 1 == vpPager.getAdapter().getCount()) {
                    if (btnGo.getVisibility() != View.VISIBLE) {
                        btnGo.setVisibility(View.VISIBLE);
                        btnGo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                    }
                } else {
                    if (btnGo.getVisibility() != View.GONE) {
                        btnGo.setVisibility(View.GONE);
                        btnGo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dotViews = new DotView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dotViews[i] = (DotView) llDot.getChildAt(i);
        }
        currentIndex = 0;
        dotViews[currentIndex].setIsSelected(true);
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1 || currentIndex == position) {
            return;
        }
        dotViews[currentIndex].setIsSelected(false);
        dotViews[position].setIsSelected(true);
        currentIndex = position;
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

    @OnClick(R.id.btn_go)
    public void onViewClicked() {
        ShareUtil.put(SharePre.KEY_FIRST_SPLASH, false);
        readGoFinishAnim(MyLoginActivity.class);
    }
}
