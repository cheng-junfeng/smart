package com.wu.safe.smart.ui.widget.swipemenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wu.safe.smart.R;
import com.wu.safe.smart.ui.widget.swipemenu.model.ListItem;

import java.util.List;


public class SwipListAdapter extends BaseAdapter {
    private List<ListItem> layers;
    private Context context;

    public SwipListAdapter(Context context, List<ListItem> layers){
        this.context = context;
        this.layers = layers;
    }
    public interface ItemClickListener{
        void onMapItemClick(ListItem layer);
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
    public ListItem getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.base_list_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.hm_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ListItem layer = getItem(position);
        holder.tvName.setText(layer.name);
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }

}
