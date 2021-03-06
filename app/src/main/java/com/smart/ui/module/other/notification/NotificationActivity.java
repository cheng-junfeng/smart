package com.smart.ui.module.other.notification;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.base.app.listener.OnClickLongListener;
import com.base.app.adapter.BaseListAdapter;
import com.base.utils.NotificationUtil;
import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.user.ui.view.MySettingActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class NotificationActivity extends BaseCompatActivity {
    private final static String TAG = "NotificationActivity";
    @BindView(R.id.main_view)
    RecyclerView mainView;

    Context mContext;
    List<String> allData;
    BaseListAdapter mAdapter;
    @BindView(R.id.switch_notif)
    TextView switchNotif;

    @Override
    protected int setContentView() {
        return R.layout.activity_notification;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "通知", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initData();
        initView();
    }

    @Override
    public void onResume(){
        super.onResume();
        boolean isOpen = NotificationUtil.isNotificationEnabled(mContext);
        if(isOpen){
            switchNotif.setText("open");
        }else{
            switchNotif.setText("close");
        }
    }

    private void initData() {
        allData = new ArrayList<>();
        allData.add("1 sendDefaultNotice");
        allData.add("2 cancel");
        allData.add("3 showNotification");
        allData.add("4 showProgressNotification");
        allData.add("5 showDoneNotification");
        allData.add("6 showErrorNotification");
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
                        default:
                            break;
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

    @OnClick(R.id.status_notif)
    public void onViewClicked() {
        try {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }
}
