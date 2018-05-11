package com.wu.safe.smart.ui.module.other.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wu.safe.base.app.listener.OnClickLongListener;
import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.base.ui.adapter.BaseListAdapter;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.base.ui.bean.ListBean;
import com.wu.safe.base.utils.NotificationUtil;
import com.wu.safe.user.ui.view.MySettingActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class NotificationActivity extends BaseCompatActivity {
    private final static String TAG = "NotificationActivity";
    @BindView(R.id.main_view)
    RecyclerView mainView;

    Context mContext;
    List<ListBean> allData;
    BaseListAdapter mAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_notification;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "Notification", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initData();
        initView();
    }

    private void initData() {
        allData = new ArrayList<>();
        ListBean bean1 = new ListBean.Builder().content("1 sendDefaultNotice").build();
        ListBean bean2 = new ListBean.Builder().content("2 cancel").build();
        ListBean bean3 = new ListBean.Builder().content("3 showNotification").build();
        ListBean bean4 = new ListBean.Builder().content("4 showProgressNotification").build();
        ListBean bean5 = new ListBean.Builder().content("5 showDoneNotification").build();
        ListBean bean6 = new ListBean.Builder().content("6 showErrorNotification").build();

        allData.add(bean1);
        allData.add(bean2);
        allData.add(bean3);
        allData.add(bean4);
        allData.add(bean5);
        allData.add(bean6);
    }

    private void initView() {
        if (allData == null || allData.size() <= 0) {
            return;
        }

        if (mAdapter == null) {
            mAdapter = new BaseListAdapter(allData);

            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 2: {
                            NotificationUtil.showNotification(mContext, R.mipmap.ic_launcher, "Title2", "content2");
                        }
                        break;
                        case 3: {
                            NotificationUtil.showProgressNotification(mContext, R.mipmap.ic_launcher, "Title3", "content3", 100, 10);
                        }
                        break;
                        case 4: {
                            NotificationUtil.showDoneNotification(mContext, R.mipmap.ic_launcher, "Title4", "content4", new File("file:///storage/emulated/0/Download/_temp%40com.wutos.wesafe.apk"));
                        }
                        break;
                        case 5: {
                            NotificationUtil.showErrorNotification(mContext, new Intent(mContext, MySettingActivity.class), R.mipmap.ic_launcher, "Title1", "content1");
                        }
                        break;
                        default:break;
                    }
                }

                @Override
                public void onItemLongClick(int position) {

                }
            });

            mainView.setLayoutManager(new LinearLayoutManager(mContext));
            mainView.setHasFixedSize(true);
            mainView.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(allData);
            mAdapter.notifyDataSetChanged();
        }
    }
}
