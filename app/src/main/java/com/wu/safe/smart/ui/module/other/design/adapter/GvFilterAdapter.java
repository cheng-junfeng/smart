package com.wu.safe.smart.ui.module.other.design.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.wu.safe.smart.R;
import com.wu.safe.smart.ui.module.other.design.bean.FilterAreaBean;

import java.util.ArrayList;
import java.util.List;

public class GvFilterAdapter extends BaseAdapter {

    private static final int BASE_TYPE = 1;

    private Context mContext;
    private List<FilterAreaBean> allData = new ArrayList<>();
    private List<FilterAreaBean> listData = new ArrayList<>();
    private String parentId = "0";
    private int mPos = -1;

    public GvFilterAdapter(Context context, List<FilterAreaBean> data) {
        this.mContext = context;
        this.allData.addAll(data);
        refreshList();
    }

    private void refreshList() {
        listData.clear();
        for (FilterAreaBean bean : allData) {
            if (bean.getParentId().equalsIgnoreCase(parentId)) {
                listData.add(bean);
            }
        }
        notifyDataSetChanged();
    }

    public void setSelect(int position) {
        this.mPos = position;
        FilterAreaBean bean = listData.get(position);
        if(bean.getAreaType() == BASE_TYPE){
            notifyDataSetChanged();
        }else{
            parentId = bean.getId();
            refreshList();
        }
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_filter, null, false);
            viewHolder = new ViewHolder();
            viewHolder.NameTv = view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.NameTv.setText(listData.get(i).getName());
        if (i == mPos) {
            viewHolder.NameTv.setSelected(true);
        } else {
            viewHolder.NameTv.setSelected(false);
        }
        return view;
    }

    public class ViewHolder {
        private TextView NameTv;
    }
}
