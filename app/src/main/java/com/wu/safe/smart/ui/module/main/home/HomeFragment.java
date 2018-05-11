package com.wu.safe.smart.ui.module.main.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.track.activity.MapMainActivity;
import com.jmolsmobile.videocapture.ui.view.VideoMainActivity;
import com.wu.safe.apermission.ui.PermissionActivity;
import com.wu.safe.apermission.ui.PlugActivity;
import com.wu.safe.base.app.listener.OnClickLongListener;
import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.jsbridge.config.JSConfig;
import com.wu.safe.jsbridge.ui.view.JSWebViewNormalActivity;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.ui.module.main.home.adapter.HomeListAdapter;
import com.wu.safe.smart.ui.module.main.home.bean.HomeListBean;
import com.wu.safe.smart.ui.module.other.data.DataActivity;
import com.wu.safe.smart.ui.module.other.info.InfoActivity;
import com.wu.safe.smart.ui.module.other.notification.NotificationActivity;
import com.wu.safe.smart.ui.module.other.tab.TabActivity;
import com.wu.safe.smart.ui.widget.BGABadgeTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseCompatFragment {

    @BindView(R.id.main_view)
    RecyclerView mainView;
    @BindView(R.id.re_front_icon)
    BGABadgeTextView reFrontIcon;

    List<HomeListBean> allData;
    HomeListAdapter mAdapter;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.main_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        mContext = this.getContext();

        initData();
        initView();
        return containerView;
    }

    private void initData() {
        allData = new ArrayList<>();
        HomeListBean bean1 = new HomeListBean.Builder().content("分页").build();
        HomeListBean bean2 = new HomeListBean.Builder().content("通知").build();
        HomeListBean bean3 = new HomeListBean.Builder().content("权限").build();
        HomeListBean bean4 = new HomeListBean.Builder().content("角标").build();
        HomeListBean bean21 = new HomeListBean.Builder().content("网页").build();
        HomeListBean bean22 = new HomeListBean.Builder().content("Tab").build();
        HomeListBean bean23 = new HomeListBean.Builder().content("Info").build();
        HomeListBean bean24 = new HomeListBean.Builder().content("Map").build();
        HomeListBean bean31 = new HomeListBean.Builder().content("视频").build();
        HomeListBean bean32 = new HomeListBean.Builder().content("插件").build();
        HomeListBean bean33 = new HomeListBean.Builder().content("More").build();
        HomeListBean bean34 = new HomeListBean.Builder().content("More").build();

        allData.add(bean1);
        allData.add(bean2);
        allData.add(bean3);
        allData.add(bean4);

        allData.add(bean21);
        allData.add(bean22);
        allData.add(bean23);
        allData.add(bean24);

        allData.add(bean31);
        allData.add(bean32);
        allData.add(bean33);
        allData.add(bean34);
    }

    private void initView() {
        if (allData == null || allData.size() <= 0) {
            return;
        }

        if (mAdapter == null) {
            mAdapter = new HomeListAdapter(allData);

            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0: {
                            readGo(DataActivity.class, null);
                        }
                        break;
                        case 1: {
                            readGo(NotificationActivity.class, null);
                        }
                        break;
                        case 2: {
                            readGo(PermissionActivity.class, null);
                        }
                        break;
                        case 3: {
                            addBadge();
                        }
                        break;
                        case 4: {
                            Bundle bundle = new Bundle();
                            bundle.putString(JSConfig.JS_URL, "http://www.baidu.com");
                            readGo(JSWebViewNormalActivity.class, bundle);
                        }
                        break;
                        case 5: {
                            readGo(TabActivity.class, null);
                        }
                        break;
                        case 6: {
                            readGo(InfoActivity.class, null);
                        }
                        break;
                        case 7: {
                            readGo(MapMainActivity.class, null);
                        }
                        break;
                        case 8: {
                            readGo(VideoMainActivity.class, null);
                        }
                        break;
                        case 9: {
                            readGo(PlugActivity.class, null);
                        }
                        break;
                        default: {
                            DialogUtils.showToast(mContext, "no more");
                        }
                        break;
                    }
                }

                @Override
                public void onItemLongClick(int position) {

                }
            });

            mainView.setLayoutManager(new GridLayoutManager(getContext(), 4));
//            mainView.addItemDecoration(new DividerGridItemDecoration(getContext()));
            mainView.setHasFixedSize(true);
            mainView.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(allData);
            mAdapter.notifyDataSetChanged();
        }
    }

    private int unReadCount = 0;
    private void addBadge() {
        unReadCount++;
        if (unReadCount > 0) {
            if (unReadCount > 99) {
                reFrontIcon.showTextBadge("a");
                reFrontIcon.showTextBadge("99+");
            } else {

                reFrontIcon.showTextBadge(String.valueOf(unReadCount));
            }
        } else {
            reFrontIcon.hiddenBadge();
        }
    }
}
