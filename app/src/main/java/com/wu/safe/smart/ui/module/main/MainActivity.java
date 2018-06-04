package com.wu.safe.smart.ui.module.main;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.user.config.NetConfig;
import com.wu.safe.smart.ui.module.main.find.FindFragment;
import com.wu.safe.smart.ui.module.main.main.contract.MainContract;
import com.wu.safe.smart.ui.module.main.home.HomeFragment;
import com.wu.safe.smart.ui.module.main.home.adapter.ViewPagerAdapter;
import com.push.ui.MsgFragment;
import com.wu.safe.smart.ui.module.main.main.presenter.MainPresenter;
import com.user.ui.MyFragment;
import com.wu.safe.smart.ui.module.other.scan.ScanActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseCompatActivity implements MainContract.View, ViewPager.OnPageChangeListener {

    public static final String TAG = "MainActivity";

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_time)
    RadioButton rbTime;
    @BindView(R.id.rb_msg)
    RadioButton rbMsg;
    @BindView(R.id.rb_my)
    RadioButton rbMy;

    private int normalColor;
    private int selectColor;

    int pre_index = -1;
    MainPresenter mainPresenter;

    @Override
    protected int setContentView() {
        return R.layout.activity_control;
    }

    @Override
    protected int setErrorView() {
        return R.layout.base_empty;
    }

    @Override
    protected int setFootView() {
        return R.layout.item_control_footer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "MyService:"+ NetConfig.IP_ADDRESS);
        normalColor = getResources().getColor(R.color.tab_host_text_normal);
        selectColor = getResources().getColor(R.color.tab_host_text_select);

        initData();
        initToolbar();
        initViewPager();
        initRadio();
        setTabSelect(0);
    }

    private void initData(){
        mainPresenter = new MainPresenter(this);
        mainPresenter.getUserImg();
        mainPresenter.getUserInfo();
    }

    private void initToolbar() {
        ToolbarUtil.setToolbarLeft(toolbar, "首页", "Smart", null);
        ToolbarUtil.setToolbarRight(toolbar, R.mipmap.ic_action_scan, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readGo(ScanActivity.class);
            }
        });
    }

    FindFragment findFragment;
    HomeFragment homeFragment;
    MsgFragment msgFragment;
    MyFragment meFragment;
    Fragment[] mFragments;

    // 初始化数据
    private void initViewPager() {
        findFragment = new FindFragment();
        homeFragment = new HomeFragment();
        msgFragment = new MsgFragment();
        meFragment = new MyFragment();
        mFragments = new Fragment[]{findFragment, homeFragment, msgFragment, meFragment};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vpMain.setAdapter(adapter);
        vpMain.addOnPageChangeListener(this);
        vpMain.setOffscreenPageLimit(4);
    }

    RadioButton[] radioButtons;

    private void initRadio() {
        radioButtons = new RadioButton[4];
        radioButtons[0] = rbHome;
        radioButtons[1] = rbTime;
        radioButtons[2] = rbMsg;
        radioButtons[3] = rbMy;
        radioButtons[0].setChecked(true);
        for (int i = 0; i < radioButtons.length; i++) {
            Drawable[] drawables = radioButtons[i].getCompoundDrawables();
            Rect r = new Rect(0, 0, drawables[1].getMinimumWidth() * 2 / 5, drawables[1].getMinimumHeight() * 2 / 5);
            drawables[1].setBounds(r);
            radioButtons[i].setCompoundDrawables(null, drawables[1], null, null);
        }
    }

    @OnClick({R.id.rb_home, R.id.rb_time, R.id.rb_msg, R.id.rb_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_home:
                setTabSelect(0);
                break;
            case R.id.rb_time:
                setTabSelect(1);
                break;
            case R.id.rb_msg:
                setTabSelect(2);
                break;
            case R.id.rb_my:
                setTabSelect(3);
                break;
        }
    }

    private void setTabSelect(int position) {
        if (position == pre_index) {
            return;
        }
        switch (position) {
            case 0:
                ToolbarUtil.setToolbar(toolbar, "首页");
                break;
            case 1:
                ToolbarUtil.setToolbar(toolbar, "时间");
                break;
            case 2:
                ToolbarUtil.setToolbar(toolbar, "消息");
                break;
            case 3:
                ToolbarUtil.setToolbar(toolbar, "我的");
                break;
        }
        if (pre_index >= 0) {
            radioButtons[pre_index].setChecked(false);
            radioButtons[pre_index].setTextColor(normalColor);
        }

        vpMain.setCurrentItem(position);
        radioButtons[position].setChecked(true);
        radioButtons[position].setTextColor(selectColor);
        pre_index = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setTabSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.showLong("再按一次退出程序");
            firstTime = secondTime;
        } else {
            appExit();
        }
    }

    @Override
    public RxAppCompatActivity getRxActivity() {
        return this;
    }
}
