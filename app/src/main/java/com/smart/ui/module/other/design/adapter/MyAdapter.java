package com.smart.ui.module.other.design.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.smart.R;


public class MyAdapter extends DelegateAdapter.Adapter<MyAdapter.MainViewHolder> {
    private Context context;
    private LayoutHelper layoutHelper;
    private int count = 0;

    public MyAdapter(Context context, LayoutHelper layoutHelper, int count) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.tv1.setText(Integer.toString(position));

        if (position > 7) {
            holder.itemView.setBackgroundColor(0x66cc0000 + (position - 6) * 128);
        } else if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(0xaa22ff22);
        } else {
            holder.itemView.setBackgroundColor(0xccff22ff);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public MainViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.hm_content);
        }
    }
}
