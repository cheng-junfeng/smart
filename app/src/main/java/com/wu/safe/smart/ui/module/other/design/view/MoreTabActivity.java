package com.wu.safe.smart.ui.module.other.design.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.jsbridge.ui.view.JSWebViewNormalFragment;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.ui.module.other.design.bean.GroupData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MoreTabActivity extends BaseCompatActivity {
    private final static String TAG = "MoreTabActivity";
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private InnerAdapter adapter;
    private List<Fragment> fragments;
    private List<GroupData> groupDatas;

    @Override
    protected int setContentView() {
        return R.layout.activity_tab_more;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "连续表头", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initData();
        initView();
    }

    private void initData() {
        fragments = new ArrayList<>();
        groupDatas = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            GroupData data = new GroupData();
            data.setGroupName("tab" + (i + 1));
            data.setId(i + 1 + "");
            data.setIsCommon(0);
            groupDatas.add(data);
        }
    }

    private void initView() {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabTextColors(Color.parseColor("#b3ffffff"), Color.WHITE);

        for (GroupData data : groupDatas) {
            JSWebViewNormalFragment f = JSWebViewNormalFragment.newInstance("http://www.baidu.com/");
            fragments.add(f);
        }
        adapter = new InnerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class InnerAdapter extends FragmentPagerAdapter {

        public InnerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (groupDatas != null) {
                return groupDatas.get(position).getGroupName();
            }
            return null;
        }
    }
}
