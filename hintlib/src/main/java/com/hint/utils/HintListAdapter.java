package com.hint.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hint.R;
import com.hint.listener.OnChooseListener;

import java.util.List;

class HintListAdapter extends BaseAdapter {
    private List<String> layers;
    private Context context;
    OnChooseListener mOnClickListener;

    public HintListAdapter(Context context, List<String> layers){
        this.context = context;
        this.layers = layers;
    }


    public void setOnListener(OnChooseListener mOnItemClickListener) {
        this.mOnClickListener = mOnItemClickListener;
    }

    @Override

    public int getCount() {
        return layers.size();
    }

    @Override
    public String getItem(int position) {
        return layers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.hint_list_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.hm_content);
            holder.tvLayout = (LinearLayout) convertView.findViewById(R.id.hm_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String layer = getItem(position);
        holder.tvName.setText(layer);
        holder.tvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onPositive(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout tvLayout;
        TextView tvName;
    }
}
