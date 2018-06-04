package com.smart.ui.module.other.design.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {

    List<Fragment> allData;
    List<String> allTitle;

    public TabAdapter(FragmentManager fm, List data, List titles) {
        super(fm);
        this.allData = data;
        this.allTitle = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return allData.get(position);
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return allTitle.get(position);
    }
}