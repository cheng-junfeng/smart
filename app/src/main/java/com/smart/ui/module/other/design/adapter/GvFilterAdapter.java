package com.smart.ui.module.other.design.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.smart.R;
import com.smart.ui.module.other.design.bean.FilterAreaBean;

import java.util.ArrayList;
import java.util.List;

public class GvFilterAdapter extends BaseAdapter {

    private static final int BASE_TYPE = 0;

    private Context mContext;
    private List<FilterAreaBean> allData = new ArrayList<>();
    private List<FilterAreaBean> listData = new ArrayList<>();
    private ArrayList<String> selectStr = new ArrayList<>();
    private String parentId = "0";
    private boolean clickable = true;

    public GvFilterAdapter(Context context, List<FilterAreaBean> data) {
        this.mContext = context;
        this.allData.addAll(data);
        refreshList();
    }

    public void reset(){
        clickable = true;
        parentId = "0";
        selectStr.clear();
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

    public String getSelectStr(){
        StringBuffer result = new StringBuffer();
        for(String str : selectStr){
            result.append(str+"-");
        }
        return result.toString();
    }

    public void setSelect(int position) {
        FilterAreaBean bean = listData.get(position);
        if(clickable){
            selectStr.add(bean.getName());
        }
        if(bean.getAreaType() == BASE_TYPE){
            clickable = false;
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
        return view;
    }

    public class ViewHolder {
        private TextView NameTv;
    }
}
