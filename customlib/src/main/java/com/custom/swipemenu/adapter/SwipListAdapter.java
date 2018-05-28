package com.custom.swipemenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.custom.R;
import com.custom.swipemenu.model.SwipListItem;

import java.util.List;


public class SwipListAdapter extends BaseAdapter {
    private List<SwipListItem> layers;
    private Context context;

    public SwipListAdapter(Context context, List<SwipListItem> layers){
        this.context = context;
        this.layers = layers;
    }
    public interface ItemClickListener{
        void onMapItemClick(SwipListItem layer);
    }
    ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override

    public int getCount() {
        return layers.size();
    }

    @Override
    public SwipListItem getItem(int position) {
        return layers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.hm_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SwipListItem layer = getItem(position);
        holder.tvName.setText(layer.name);
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }
}
