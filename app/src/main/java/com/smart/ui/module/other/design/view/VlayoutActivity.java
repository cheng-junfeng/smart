package com.smart.ui.module.other.design.view;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.ui.module.other.design.adapter.MyAdapter;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class VlayoutActivity extends BaseCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int setContentView() {
        return R.layout.activity_recycle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "Vlayout", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        init();
    }

    private void init() {
        //绑定VirtualLayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //设置Item之间的间隔
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(5, 5, 5, 5);
            }
        });

        //设置Adapter列表
        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        //设置线性布局
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        //设置Item个数
        linearLayoutHelper.setItemCount(4);
        //设置间隔高度
        linearLayoutHelper.setDividerHeight(1);
        //设置布局底部与下个布局的间隔
        linearLayoutHelper.setMarginBottom(100);
        adapters.add(new MyAdapter(this, linearLayoutHelper, 4) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (position == 0) {
                    holder.tv1.setText("linearLayout");
                }
            }
        });

        //设置Grid布局
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        //是否自动扩展
        gridLayoutHelper.setAutoExpand(false);
        //自定义设置某些位置的Item的占格数
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position > 15) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        gridLayoutHelper.setMarginBottom(100);
        adapters.add(new MyAdapter(this, gridLayoutHelper, 23) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (position == 0) {
                    holder.tv1.setText("gridLayout");
                }
            }
        });

        //设置固定布局
        FixLayoutHelper fixLayoutHelper = new FixLayoutHelper(FixLayoutHelper.TOP_RIGHT, 0, 0);
        adapters.add(new MyAdapter(this, fixLayoutHelper, 1) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(300, 80);
                holder.tv1.setText("fixLayout");
                holder.itemView.setLayoutParams(layoutParams);
                holder.itemView.setBackgroundColor(Color.BLUE);
            }
        });

        //设置滚动固定布局
        ScrollFixLayoutHelper scrollFixLayoutHelper = new ScrollFixLayoutHelper(ScrollFixLayoutHelper.TOP_LEFT, 0, 0);
        scrollFixLayoutHelper.setShowType(ScrollFixLayoutHelper.SHOW_ON_LEAVE);
        adapters.add(new MyAdapter(this, scrollFixLayoutHelper, 1) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(300, 90);
                holder.tv1.setText("scrollFixLayout");
                holder.itemView.setLayoutParams(layoutParams);
                holder.itemView.setBackgroundColor(Color.BLUE);
            }
        });

        //设置浮动布局
        FloatLayoutHelper floatLayoutHelper = new FloatLayoutHelper();
        //设置初始位置
        floatLayoutHelper.setDefaultLocation(20, 250);
        adapters.add(new MyAdapter(this, floatLayoutHelper, 1) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(300, 80);
                holder.tv1.setText("floatLayout");
                holder.itemView.setLayoutParams(layoutParams);
                holder.itemView.setBackgroundColor(Color.RED);
            }
        });

        //设置栏格布局
        ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
        columnLayoutHelper.setItemCount(5);
        columnLayoutHelper.setWeights(new float[]{30, 10, 30, 20, 10});
        columnLayoutHelper.setMarginBottom(100);
        adapters.add(new MyAdapter(this, columnLayoutHelper, 5) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (position == 0) {
                    holder.tv1.setText("columnLayout");
                }
            }
        });

        //设置Sticky布局
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        stickyLayoutHelper.setStickyStart(false);
        adapters.add(new MyAdapter(this, stickyLayoutHelper, 1) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.tv1.setText("stickyLayout");
            }
        });

        //设置通栏布局
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        singleLayoutHelper.setMarginBottom(100);
        adapters.add(new MyAdapter(this, singleLayoutHelper, 1) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.tv1.setText("singleLayout");
            }
        });

        //设置一拖N布局
        OnePlusNLayoutHelper onePlusNLayoutHelper = new OnePlusNLayoutHelper(5);
        onePlusNLayoutHelper.setMarginBottom(100);
        adapters.add(new MyAdapter(this, onePlusNLayoutHelper, 5) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
                holder.itemView.setLayoutParams(layoutParams);
                if (position == 0) {
                    holder.tv1.setText("onePlusNLayout");
                }
            }
        });

        //绑定delegateAdapter
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.setAdapters(adapters);
        recyclerView.setAdapter(delegateAdapter);
    }
}
