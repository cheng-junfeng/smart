package com.baidu.map.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.map.ui.BNGuideActivity;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;

public class NavigationRoutePlanListener implements BaiduNaviManager.RoutePlanListener{
    private Activity activity;

    /**
     * 路线方案的节点
     */
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    /**
     * 算路节点
     */
    private BNRoutePlanNode mBNRoutePlanNode = null;

    public NavigationRoutePlanListener(BNRoutePlanNode node, Activity activity) {
        this.activity = activity;
        mBNRoutePlanNode = node;
    }

    /**
     * 导航初始化监听器
     * 路线规划成功，需要跳转到导航过程页面
     */
    @Override
    public void onJumpToNavigator() {
        Intent intent = new Intent(activity, BNGuideActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROUTE_PLAN_NODE,  mBNRoutePlanNode);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    /**
     * 导航初始化监听器
     * 路线规划失败
     */
    @Override
    public void onRoutePlanFailed() {
        Toast.makeText(activity, "算路失败", Toast.LENGTH_SHORT).show();
    }
}
