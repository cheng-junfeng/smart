package com.wu.safe.smart.ui.module.main.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.audio.ui.AudioMainActivity;
import com.baidu.track.activity.MapMainActivity;
import com.base.app.listener.OnClickLongListener;
import com.hikvison.ui.HikTestActivity;
import com.hint.utils.ToastUtils;
import com.hyphenate.simple.EaseMainActivity;
import com.txlive.ui.view.RtmpMainActivity;
import com.video.ui.view.VideoMainActivity;
import com.webview.config.WebConfig;
import com.webview.ui.WebViewNormalActivity;
import com.wu.safe.smart.BuildConfig;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.jni.JniActivity;
import com.wu.safe.smart.service.AlarmService;
import com.wu.safe.smart.service.FloatWindowService;
import com.wu.safe.smart.ui.module.main.home.adapter.HomeListAdapter;
import com.wu.safe.smart.ui.module.main.home.bean.HomeListBean;
import com.wu.safe.smart.ui.module.other.aidl.AidlActivity;
import com.wu.safe.smart.ui.module.other.bind.BindActivity;
import com.wu.safe.smart.ui.module.other.data.DataActivity;
import com.wu.safe.smart.ui.module.other.design.view.BottomDialogActivity;
import com.wu.safe.smart.ui.module.other.design.view.BottomViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.CountViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.DialogViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.FloatButtonActivity;
import com.wu.safe.smart.ui.module.other.design.view.GridFilterActivity;
import com.wu.safe.smart.ui.module.other.design.view.LeftViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.MoreTabActivity;
import com.wu.safe.smart.ui.module.other.design.view.PageViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.StatusViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.SwipeMenuActivity;
import com.wu.safe.smart.ui.module.other.design.view.TabActivity;
import com.wu.safe.smart.ui.module.other.design.view.TopDraggerActivity;
import com.wu.safe.smart.ui.module.other.info.InfoActivity;
import com.wu.safe.smart.ui.module.other.nfc.NfcActivity;
import com.wu.safe.smart.ui.module.other.notification.NotificationActivity;
import com.wu.safe.smart.ui.module.other.permission.PermissionActivity;
import com.wu.safe.smart.ui.module.other.plug.PlugActivity;
import com.wu.safe.smart.ui.module.other.share.ShareActivity;
import com.wu.safe.smart.ui.module.other.thread.ThreadActivity;
import com.wu.safe.smart.ui.widget.BGABadgeTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseCompatFragment {

    @BindView(R.id.re_front_icon)
    BGABadgeTextView reFrontIcon;

    @BindView(R.id.context_view)
    RecyclerView contextView;
    @BindView(R.id.app_view)
    RecyclerView appView;
    @BindView(R.id.widget_view)
    RecyclerView widgetView;
    @BindView(R.id.function_view)
    RecyclerView functionView;

    List<HomeListBean> allData;
    HomeListAdapter mAdapter;

    List<HomeListBean> allData2;
    HomeListAdapter mAdapter2;

    List<HomeListBean> allData3;
    HomeListAdapter mAdapter3;

    List<HomeListBean> allData4;
    HomeListAdapter mAdapter4;


    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        mContext = this.getContext();

        initAdapter1();
        initAdapter2();
        initAdapter3();
        initAdapter4();
        return containerView;
    }

    private void initAdapter1(){
        allData = new ArrayList<>();
        HomeListBean bean11 = new HomeListBean.Builder().content("信息").build();
        HomeListBean bean12 = new HomeListBean.Builder().content("权限").build();
        HomeListBean bean13 = new HomeListBean.Builder().content("通知").build();
        HomeListBean bean14 = new HomeListBean.Builder().content("插件").build();
        HomeListBean bean15 = new HomeListBean.Builder().content("悬浮窗").build();
        allData.add(bean11);
        allData.add(bean12);
        allData.add(bean13);
        allData.add(bean14);
        allData.add(bean15);

        if (mAdapter == null) {
            mAdapter = new HomeListAdapter(allData);
            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0: {
                            readGo(InfoActivity.class);
                        }
                        break;
                        case 1: {
                            readGo(PermissionActivity.class);
                        }
                        break;
                        case 2: {
                            readGo(NotificationActivity.class);
                        }
                        break;
                        case 3: {
                            readGo(PlugActivity.class);
                        }
                        break;
                        case 4: {
                            FragmentActivity activity = getActivity();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.canDrawOverlays(activity)) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                    startActivity(intent);
                                } else {
                                    startFloat(activity);
                                }
                            } else {
                                startFloat(activity);
                            }
                        }
                        break;
                        default:
                            ToastUtils.showToast(mContext, "No More");
                            break;
                    }
                }

                @Override
                public void onItemLongClick(int position) {
                }
            });
        }
        contextView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        contextView.setHasFixedSize(true);
        contextView.setAdapter(mAdapter);
        contextView.setNestedScrollingEnabled(false);
    }

    private void initAdapter2(){
        allData2 = new ArrayList<>();
        HomeListBean bean21 = new HomeListBean.Builder().content("线程").build();
        HomeListBean bean22 = new HomeListBean.Builder().content("进程").build();
        HomeListBean bean23 = new HomeListBean.Builder().content("JNI").build();
        HomeListBean bean24 = new HomeListBean.Builder().content("Bind").build();
        HomeListBean bean25 = new HomeListBean.Builder().content("Aidl").build();
        HomeListBean bean26 = new HomeListBean.Builder().content("分享").build();
        allData2.add(bean21);
        allData2.add(bean22);
        allData2.add(bean23);
        allData2.add(bean24);
        allData2.add(bean25);
        allData2.add(bean26);

        if (mAdapter2 == null) {
            mAdapter2 = new HomeListAdapter(allData2);
            mAdapter2.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0: {
                            readGo(ThreadActivity.class);
                        }
                        break;
                        case 1: {
                            AlarmService.startAutoCheck(mContext);
                        }
                        break;
                        case 2: {
                            readGo(JniActivity.class);
                        }
                        break;
                        case 3: {
                            readGo(BindActivity.class);
                        }
                        break;
                        case 4: {
                            readGo(AidlActivity.class);
                        }
                        break;
                        case 5: {
                            readGo(ShareActivity.class);
                        }
                        break;
                        default:
                            ToastUtils.showToast(mContext, "No More");
                            break;
                    }
                }

                @Override
                public void onItemLongClick(int position) {
                }
            });
        }
        appView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        appView.setHasFixedSize(true);
        appView.setAdapter(mAdapter2);
    }

    private void initAdapter3(){
        allData3 = new ArrayList<>();
        HomeListBean bean30 = new HomeListBean.Builder().content("分页").build();
        HomeListBean bean31 = new HomeListBean.Builder().content("向上缩进").build();
        HomeListBean bean32 = new HomeListBean.Builder().content("状态栏").build();
        HomeListBean bean33 = new HomeListBean.Builder().content("侧滑").build();
        HomeListBean bean34 = new HomeListBean.Builder().content("左滑").build();
        HomeListBean bean35 = new HomeListBean.Builder().content("Tab").build();
        HomeListBean bean36 = new HomeListBean.Builder().content("连续Tab").build();
        HomeListBean bean37 = new HomeListBean.Builder().content("提示框").build();
        HomeListBean bean38 = new HomeListBean.Builder().content("点选").build();
        HomeListBean bean39 = new HomeListBean.Builder().content("悬浮键").build();
        HomeListBean bean3a = new HomeListBean.Builder().content("多页").build();
        HomeListBean bean3b = new HomeListBean.Builder().content("倒计时").build();
        HomeListBean bean3c = new HomeListBean.Builder().content("底部栏").build();
        HomeListBean bean3d = new HomeListBean.Builder().content("底部框").build();

        allData3.add(bean30);
        allData3.add(bean31);
        allData3.add(bean32);
        allData3.add(bean33);
        allData3.add(bean34);
        allData3.add(bean35);
        allData3.add(bean36);
        allData3.add(bean37);
        allData3.add(bean38);
        allData3.add(bean39);
        allData3.add(bean3a);
        allData3.add(bean3b);
        allData3.add(bean3c);
        allData3.add(bean3d);

        if (mAdapter3 == null) {
            mAdapter3 = new HomeListAdapter(allData3);
            mAdapter3.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0: {
                            readGo(DataActivity.class);
                        }
                        break;
                        case 1: {
                            readGo(TopDraggerActivity.class);
                        }
                        break;
                        case 2: {
                            readGo(StatusViewActivity.class);
                        }
                        break;
                        case 3: {
                            readGo(LeftViewActivity.class);
                        }
                        break;
                        case 4:
                            readGo(SwipeMenuActivity.class);
                            break;
                        case 5:
                            readGo(TabActivity.class);
                            break;
                        case 6:
                            readGo(MoreTabActivity.class);
                            break;
                        case 7:
                            readGo(DialogViewActivity.class);
                            break;
                        case 8:
                            readGo(GridFilterActivity.class);
                            break;
                        case 9:
                            readGo(FloatButtonActivity.class);
                            break;
                        case 10:
                            readGo(PageViewActivity.class);
                            break;
                        case 11:
                            readGo(CountViewActivity.class);
                            break;
                        case 12:
                            readGo(BottomViewActivity.class);
                            break;
                        case 13:
                            readGo(BottomDialogActivity.class);
                            break;
                        case 14:
                            readGo(SwipeMenuActivity.class);
                            break;
                        default:
                            ToastUtils.showToast(mContext, "No More");
                            break;
                    }
                }

                @Override
                public void onItemLongClick(int position) {
                }
            });
        }
        widgetView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        widgetView.setHasFixedSize(true);
        widgetView.setAdapter(mAdapter3);
    }

    private void initAdapter4(){
        allData4 = new ArrayList<>();
        HomeListBean bean41 = new HomeListBean.Builder().content("音频").build();
        HomeListBean bean42 = new HomeListBean.Builder().content("视频").build();
        HomeListBean bean43 = new HomeListBean.Builder().content("环信").build();
        HomeListBean bean44 = new HomeListBean.Builder().content("海康").build();
        HomeListBean bean45 = new HomeListBean.Builder().content("直播").build();
        HomeListBean bean46 = new HomeListBean.Builder().content("网页").build();
        HomeListBean bean47 = new HomeListBean.Builder().content("WS").build();
        HomeListBean bean48 = new HomeListBean.Builder().content("NFC").build();
        HomeListBean bean49 = new HomeListBean.Builder().content("地图").build();

        allData4.add(bean41);
        allData4.add(bean42);
        allData4.add(bean43);
        allData4.add(bean44);
        allData4.add(bean45);
        allData4.add(bean46);
        allData4.add(bean47);
        allData4.add(bean48);
        allData4.add(bean49);

        if (mAdapter4 == null) {
            mAdapter4 = new HomeListAdapter(allData4);
            mAdapter4.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0: {
                            readGo(AudioMainActivity.class);
                        }
                        break;
                        case 1: {
                            readGo(VideoMainActivity.class);
                        }
                        break;
                        case 2: {
                            readGo(EaseMainActivity.class);
                        }
                        break;
                        case 3: {
                            readGo(HikTestActivity.class);
                        }
                        break;
                        case 4: {
                            readGo(RtmpMainActivity.class);
                        }
                        break;
                        case 5: {
                            Bundle bundle = new Bundle();
                            bundle.putString(WebConfig.JS_URL, "http://www.baidu.com");
                            readGo(WebViewNormalActivity.class, bundle);
                        }
                        break;
                        case 6: {
                            Bundle bundle = new Bundle();
                            bundle.putString(WebConfig.JS_NAME, "WS");
                            bundle.putString(WebConfig.JS_URL, "file:///android_asset/video/video.html");
                            readGo(WebViewNormalActivity.class, bundle);
                        }
                        break;
                        case 7: {
                            readGo(NfcActivity.class);
                        }
                        break;
                        case 8: {
                            readGo(MapMainActivity.class);
                        }
                        break;
                        default:
                            ToastUtils.showToast(mContext, "No More");
                            break;
                    }
                }

                @Override
                public void onItemLongClick(int position) {
                }
            });
        }
        functionView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        functionView.setHasFixedSize(true);
        functionView.setAdapter(mAdapter4);
    }

    private void startFloat(FragmentActivity activity) {
        Intent intent = new Intent(activity, FloatWindowService.class);
        activity.startService(intent);
        activity.finish();
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

    @OnClick(R.id.re_itemview)
    public void onViewClicked() {
        addBadge();
    }
}
