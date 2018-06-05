package com.smart.ui.module.main.find.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.smart.R;
import com.webview.config.WebConfig;
import com.webview.ui.WebViewNormalActivity;


public class FindSingle2Adapter extends DelegateAdapter.Adapter<FindSingle2Adapter.FindSingleHolder> {
    private Context context;
    private LayoutHelper layoutHelper;
    private int count = 0;

    public FindSingle2Adapter(Context context, LayoutHelper layoutHelper, int count) {
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
        return new FindSingleHolder(LayoutInflater.from(context).inflate(R.layout.item_single2_item, parent,false));
    }

    @Override
    public void onBindViewHolder(FindSingleHolder holder, int position) {
        holder.marqueeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(WebConfig.JS_NAME, "我的主页");
                bundle.putString(WebConfig.JS_URL, "http://www.chengjunfeng.cn");
                Intent intent = new Intent(context, WebViewNormalActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
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
