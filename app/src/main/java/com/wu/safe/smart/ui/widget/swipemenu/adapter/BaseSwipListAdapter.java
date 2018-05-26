package com.wu.safe.smart.ui.widget.swipemenu.adapter;

import android.widget.BaseAdapter;


public abstract class BaseSwipListAdapter extends BaseAdapter {

    public boolean getSwipEnableByPosition(int position) {
        return true;
    }
}
