package com.smart.ui.module.other.design.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.R;
import com.smart.ui.module.other.design.bean.PageBean;

import java.util.List;


public class GridViewAdapter extends BaseAdapter {
    private List<PageBean> dataList;
    private int page;
    private int pageSize;

    public GridViewAdapter(List<PageBean> datas, int page, int pageSize) {
        dataList = datas;
        this.page = page;
        this.pageSize = pageSize;
    }

    @Override
    public int getCount() {
        return dataList.size() > (page + 1) * pageSize ?
                pageSize : (dataList.size() - page * pageSize);
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i + page * pageSize);
    }

    @Override
    public long getItemId(int i) {
        return i + page * pageSize;
    }

    @Override
    public View getView(int i, View itemView, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (itemView == null) {
            mHolder = new ViewHolder();
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_view, viewGroup, false);
            mHolder.iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            mHolder.tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            itemView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) itemView.getTag();
        }
        PageBean bean = dataList.get(i + page * pageSize);
        mHolder.tv_text.setText(bean.name);
        return itemView;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_text;
    }
}
