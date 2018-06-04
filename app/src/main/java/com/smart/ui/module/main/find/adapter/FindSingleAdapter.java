package com.smart.ui.module.main.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.smart.R;


public class FindSingleAdapter extends DelegateAdapter.Adapter<FindSingleAdapter.FindSingleHolder> {
    private Context context;
    private LayoutHelper layoutHelper;
    private int count = 0;

    public FindSingleAdapter(Context context, LayoutHelper layoutHelper, int count) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public FindSingleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindSingleHolder(LayoutInflater.from(context).inflate(R.layout.item_single_item, parent,false));
    }

    @Override
    public void onBindViewHolder(FindSingleHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class FindSingleHolder extends RecyclerView.ViewHolder {
        public TextView marqueeView;
        public FindSingleHolder(View itemView) {
            super(itemView);
            marqueeView = (TextView) itemView.findViewById(R.id.hm_marquee);
        }
    }
}
