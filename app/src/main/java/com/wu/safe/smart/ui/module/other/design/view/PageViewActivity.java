package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.jsbridge.ui.bean.User;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.ui.module.other.design.adapter.GridViewAdapter;
import com.wu.safe.smart.ui.module.other.design.adapter.GridViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class PageViewActivity extends BaseCompatActivity {
    private final static String TAG = "PageViewActivity";
    public static final int PAGE_GRID_NUM = 9;//每一页中GridView中item的数量
    public static final int PAGE_COLUMNS_NUM = 3;//gridview一行展示的数目

    @BindView(R.id.user_vp)
    ViewPager userVp;

    private List<GridView> gridList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    private GridViewPagerAdapter mAdapter;
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_page_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "分页视图", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        mAdapter = new GridViewPagerAdapter();
        userVp.setAdapter(mAdapter);

        for (int i = 0; i < 20; i++) {
            User tempUser = new User();
            tempUser.name = "Name" + i;
            userList.add(tempUser);
        }

        //计算viewpager一共显示几页
        LayoutInflater inflate = LayoutInflater.from(this);
        final int pageCount = (int) Math.ceil(userList.size() * 1.0 / PAGE_GRID_NUM);
        for (int i = 0; i < pageCount; i++) {
            if (inflate != null) {
                GridView gridView = (GridView) inflate.inflate(R.layout.item_grid_layout, userVp, false);
                GridViewAdapter adapter = new GridViewAdapter(userList, i, PAGE_GRID_NUM);
                gridView.setNumColumns(PAGE_COLUMNS_NUM);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DialogUtils.showToast(mContext, position + "");
                    }
                });
                gridList.add(gridView);
            }
        }
        mAdapter.add(gridList);
    }
}
