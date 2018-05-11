package com.baidu.track.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.track.R;
import com.baidu.track.model.ItemInfo;

import java.util.List;

public class MyAdapter extends BaseAdapter
        implements AbsListView.OnScrollListener {

    private Context mContext;
    private List<ItemInfo> itemInfos = null;

    public MyAdapter(Context context, List<ItemInfo> itemInfos) {
        this.mContext = context;
        this.itemInfos = itemInfos;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public int getCount() {
        return itemInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return itemInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null != convertView) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(mContext, R.layout.item_list, null);
            viewHolder = new ViewHolder();

            viewHolder.titleIcon = (ImageView) convertView
                    .findViewById(R.id.iv_all_title);
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.tv_all_title);
            viewHolder.desc = (TextView) convertView
                    .findViewById(R.id.tv_all_desc);
            convertView.setTag(viewHolder);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewHolder.titleIcon.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),
                    itemInfos.get(position).titleIconId, null));
        } else {
            viewHolder.titleIcon.setBackgroundDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                    itemInfos.get(position).titleIconId, null));
        }
        viewHolder.title.setText(itemInfos.get(position).titleId);
        viewHolder.desc.setText(itemInfos.get(position).descId);
        return convertView;
    }

    public class ViewHolder {
        ImageView titleIcon;
        TextView title;
        TextView desc;
    }
}