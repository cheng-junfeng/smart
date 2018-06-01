package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.webview.ui.WebViewNormalFragment;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.ui.module.other.design.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TabActivity extends BaseCompatActivity {
    private final static String TAG = "TabActivity";

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    Context mContext;
    WebViewNormalFragment toDoFragment1;
    WebViewNormalFragment toDoFragment2;
    WebViewNormalFragment toDoFragment3;
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
        ToolbarUtil.setToolbarLeft(toolbar, "固定表头", null, new View.OnClickListener() {
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

        toDoFragment1 = WebViewNormalFragment.newInstance("http://www.baidu.com/");
        toDoFragment2 = WebViewNormalFragment.newInstance("http://www.baidu.com/");
        toDoFragment3 = WebViewNormalFragment.newInstance("http://www.baidu.com/");
        fragmentList.add(toDoFragment1);
        fragmentList.add(toDoFragment2);
        fragmentList.add(toDoFragment3);

        titleList.add("百度1");
        titleList.add("百度2");
        titleList.add("百度3");

        mAdapter = new TabAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
