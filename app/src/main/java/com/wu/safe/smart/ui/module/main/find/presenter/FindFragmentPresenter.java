package com.wu.safe.smart.ui.module.main.find.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.audio.ui.AudioMainActivity;
import com.baidu.track.activity.MapMainActivity;
import com.hikvison.ui.HikTestActivity;
import com.hint.utils.ToastUtils;
import com.hyphenate.simple.EaseMainActivity;
import com.txlive.ui.view.RtmpMainActivity;
import com.video.ui.view.VideoMainActivity;
import com.webview.config.WebConfig;
import com.webview.ui.WebViewNormalActivity;
import com.wu.safe.smart.BuildConfig;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.adapter.BaseDelegateAdapter;
import com.wu.safe.smart.app.adapter.BaseViewHolder;
import com.wu.safe.smart.config.Constant;
import com.wu.safe.smart.jni.JniActivity;
import com.wu.safe.smart.service.AlarmService;
import com.wu.safe.smart.service.FloatWindowService;
import com.wu.safe.smart.ui.module.main.find.contract.FindFragmentContract;
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

import java.util.ArrayList;
import java.util.List;


public class FindFragmentPresenter implements FindFragmentContract.Presenter {

    private FindFragmentContract.View mView;
    private Fragment mFragment;
    private Context mContext;

    public FindFragmentPresenter(FindFragmentContract.View androidView, Fragment activity) {
        this.mView = androidView;
        this.mFragment = activity;
        this.mContext = activity.getContext();
    }

    @Override
    public DelegateAdapter initRecyclerView(RecyclerView recyclerView) {
        //初始化
        //创建VirtualLayoutManager对象
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }

    @Override
    public BaseDelegateAdapter initMenu1() {
        //menu
        // 在构造函数设置每行的网格个数
        final TypedArray proPic = mContext.getResources().obtainTypedArray(R.array.find_gv_image1);
        final String[] proName = mContext.getResources().getStringArray(R.array.find_gv_title1);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<proName.length ; a++){
            images.add(proPic.getResourceId(a, R.mipmap.ic_category_0));
        }
        proPic.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 16, 0, 16);
        gridLayoutHelper.setVGap(16);   // 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(0);    // 控制子元素之间的水平间距
        gridLayoutHelper.setBgColor(Color.WHITE);
        return new BaseDelegateAdapter(mContext, gridLayoutHelper, R.layout.item_find, 5, Constant.viewType.typeGv) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.hm_content, proName[position]);
                holder.setImageResource(R.id.hm_image, images.get(position));
                holder.getView(R.id.hm_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (!Settings.canDrawOverlays(mContext)) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                        mFragment.startActivity(intent);
                                    } else {
                                        startFloat();
                                    }
                                } else {
                                    startFloat();
                                }
                            }
                            break;
                            default:
                                ToastUtils.showToast(mContext, "No More");
                                break;
                        }
                    }
                });
            }
        };
    }

    @Override
    public BaseDelegateAdapter initMenu2() {
        //item1 gird
        final TypedArray list1_image = mContext.getResources().obtainTypedArray(R.array.find_gv_image2);
        final String[] list1_title = mContext.getResources().getStringArray(R.array.find_gv_title2);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<list1_title.length ; a++){
            images.add(list1_image.getResourceId(a, R.mipmap.ic_category_0));
        }
        list1_image.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setMargin(0, 10, 0, 0);
        gridLayoutHelper.setPadding(0, 20, 0, 10);
        gridLayoutHelper.setVGap(10);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(0);// 控制子元素之间的水平间距
        gridLayoutHelper.setWeights(new float[]{25, 25, 25 , 25});
        gridLayoutHelper.setBgColor(Color.WHITE);
        //gridLayoutHelper.setAutoExpand(true);//是否自动填充空白区域
        return new BaseDelegateAdapter(mContext, gridLayoutHelper, R.layout.item_find, 6, Constant.viewType.typeGvSecond) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.hm_content, list1_title[position]);
                holder.setImageResource(R.id.hm_image, images.get(position));
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                });
            }
        };
    }

    @Override
    public BaseDelegateAdapter initMenu3() {
        final TypedArray list3_image = mContext.getResources().obtainTypedArray(R.array.find_gv_image3);
        final String[] list3_title = mContext.getResources().getStringArray(R.array.find_gv_title3);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<list3_title.length ; a++){
            images.add(list3_image.getResourceId(a, R.mipmap.ic_category_0));
        }
        list3_image.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setMargin(0, 10, 0, 0);
        gridLayoutHelper.setPadding(0, 20, 0, 10);
        gridLayoutHelper.setBgColor(Color.WHITE);
        //gridLayoutHelper.setAspectRatio(1.5f);  // 设置设置布局内每行布局的宽与高的比

        // gridLayoutHelper特有属性（下面会详细说明）
        //设置每行中 每个网格宽度 占 每行总宽度 的比例
//        gridLayoutHelper.setWeights(new float[]{30, 40, 30});
        gridLayoutHelper.setVGap(0);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(5);// 控制子元素之间的水平间距
        //gridLayoutHelper.setAutoExpand(false);//是否自动填充空白区域
        //gridLayoutHelper.setSpanCount(6);   // 设置每行多少个网格
        return new BaseDelegateAdapter(mContext, gridLayoutHelper, R.layout.item_find, 14, Constant.viewType.typeList) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.hm_content, list3_title[position]);
                holder.setImageResource(R.id.hm_image, images.get(position));
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                });
            }
        };
    }

    @Override
    public BaseDelegateAdapter initMenu4() {
        //menu
        // 在构造函数设置每行的网格个数
        final TypedArray proPic = mContext.getResources().obtainTypedArray(R.array.find_gv_image4);
        final String[] proName = mContext.getResources().getStringArray(R.array.find_gv_title4);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<proName.length ; a++){
            images.add(proPic.getResourceId(a, R.mipmap.ic_category_0));
        }
        proPic.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setMargin(0, 10, 0, 0);
        gridLayoutHelper.setPadding(0, 16, 0, 16);
        gridLayoutHelper.setVGap(16);   // 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(0);    // 控制子元素之间的水平间距
        gridLayoutHelper.setBgColor(Color.WHITE);
        return new BaseDelegateAdapter(mContext, gridLayoutHelper, R.layout.item_find, 9, Constant.viewType.typeGv) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.hm_content, proName[position]);
                holder.setImageResource(R.id.hm_image, images.get(position));
                holder.getView(R.id.hm_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                });
            }
        };
    }

    private void startFloat() {
        Intent intent = new Intent(mContext, FloatWindowService.class);
        mFragment.getActivity().startService(intent);
        mFragment.getActivity().finish();
    }

    private void readGo(Class<?> cls){
        readGo(cls, null);
    }

    private void readGo(Class<?> cls, Bundle bundle){
        Intent inten = new Intent(mContext, cls);
        if(bundle != null){
            inten.putExtras(bundle);
        }
        mFragment.startActivity(inten);
    }
}
