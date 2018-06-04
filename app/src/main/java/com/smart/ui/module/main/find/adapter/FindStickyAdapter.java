package com.smart.ui.module.main.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.smart.R;
import com.smart.ui.widget.BGABadgeTextView;


public class FindStickyAdapter extends DelegateAdapter.Adapter<FindStickyAdapter.FindStickyHolder> {
    private Context context;
    private LayoutHelper layoutHelper;
    private int count = 0;
    private int unReadCount = 1;

    public FindStickyAdapter(Context context, LayoutHelper layoutHelper, int count) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public FindStickyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindStickyHolder(LayoutInflater.from(context).inflate(R.layout.item_sticky_item, parent,false));
    }

    @Override
    public void onBindViewHolder(final FindStickyHolder holder, int position) {
        holder.lay_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unReadCount++;
                if (unReadCount > 99) {
                    holder.badgeTextView.showTextBadge("99+");
                } else {
                    holder.badgeTextView.showTextBadge(String.valueOf(unReadCount));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class FindStickyHolder extends RecyclerView.ViewHolder {
        public BGABadgeTextView badgeTextView;
        public RelativeLayout lay_root;

        public FindStickyHolder(View itemView) {
            super(itemView);
            lay_root = (RelativeLayout) itemView.findViewById(R.id.re_root_layout);
            badgeTextView = (BGABadgeTextView) itemView.findViewById(R.id.re_front_icon);
            badgeTextView.showTextBadge("1");
        }
    }
}
