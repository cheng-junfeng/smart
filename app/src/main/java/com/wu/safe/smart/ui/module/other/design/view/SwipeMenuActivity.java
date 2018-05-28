package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.hint.utils.ToastUtils;
import com.base.utils.DensityUtils;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.custom.swipemenu.SwipeMenuCreator;
import com.custom.swipemenu.adapter.SwipListAdapter;
import com.custom.swipemenu.model.SwipListItem;
import com.custom.swipemenu.model.SwipeMenu;
import com.custom.swipemenu.model.SwipeMenuItem;
import com.custom.swipemenu.view.SwipeMenuListView;
import com.custom.swipemenu.view.SwipeVerticalRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class SwipeMenuActivity extends BaseCompatActivity {
    private final static String TAG = "SwipeMenuActivity";

    @BindView(R.id.swip_list_view)
    SwipeMenuListView swipListView;
    @BindView(R.id.swipe_layout)
    SwipeVerticalRefreshLayout swipeLayout;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_swipe_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "左滑删除", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        List<SwipListItem> allItems = new ArrayList<>();
        allItems.add(new SwipListItem("List1"));
        allItems.add(new SwipListItem("List2"));
        allItems.add(new SwipListItem("List3"));
        swipListView.setAdapter(new SwipListAdapter(this, allItems));

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);

        //设置左滑按钮
        swipListView.setMenuCreator(swipeMenuCreator);
        swipListView.setOnMenuItemClickListener(menuItemClickListener);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 2000);
                LogUtil.d(TAG, "onRefresh");
            }
        });
    }

    SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem editItem = new SwipeMenuItem(mContext);
            editItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x72, 0xE3)));
            editItem.setWidth(DensityUtils.dp2px(mContext, 80));
            editItem.setTitle("编辑");
            editItem.setTitleSize(18);
            editItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(editItem);


            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x00, 0x00)));
            deleteItem.setWidth(DensityUtils.dp2px(mContext, 80));
            deleteItem.setTitle("删除");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(deleteItem);
        }
    };
    private SwipeMenuListView.OnMenuItemClickListener menuItemClickListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index){
            ToastUtils.showToast(mContext, "click  line:"+position+" pos:"+index);
            return true;
        }
    };
}
