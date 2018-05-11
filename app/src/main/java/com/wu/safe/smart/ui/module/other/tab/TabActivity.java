package com.wu.safe.smart.ui.module.other.tab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.jsbridge.ui.view.JSWebViewNormalFragment;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.ui.module.other.tab.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TabActivity extends BaseCompatActivity {
    private final static String TAG = "InfoActivity";

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    Context mContext;
    JSWebViewNormalFragment toDoFragment;
    JSWebViewNormalFragment toTrackFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private TabAdapter mAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_tab_change;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "Tab", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();

        toDoFragment = JSWebViewNormalFragment.newInstance("http://www.taobao.com/");
        toTrackFragment = JSWebViewNormalFragment.newInstance("http://www.hwdoc.com/");
        fragmentList.add(toDoFragment);
        fragmentList.add(toTrackFragment);

        titleList.add("监控");
        titleList.add("地图");

        mAdapter = new TabAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
