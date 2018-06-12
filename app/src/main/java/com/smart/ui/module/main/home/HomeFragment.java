package com.smart.ui.module.main.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.app.listener.OnClickLongListener;
import com.hint.utils.ToastUtils;
import com.photo.config.PhotoConfig;
import com.photo.ui.PhotoActivity;
import com.photo.ui.PhotoChooseActivity;
import com.photo.ui.fragment.PicCarouseFragment;
import com.smart.R;
import com.smart.app.activity.BaseCompatFragment;
import com.smart.ui.module.main.home.adapter.HomeListAdapter;
import com.smart.ui.module.main.home.bean.HomeListBean;
import com.smart.ui.module.other.data.DataActivity;
import com.smart.ui.module.other.design.view.BottomDialogActivity;
import com.smart.ui.module.other.design.view.BottomViewActivity;
import com.smart.ui.module.other.design.view.CountViewActivity;
import com.smart.ui.module.other.design.view.DialogViewActivity;
import com.smart.ui.module.other.design.view.FloatButtonActivity;
import com.smart.ui.module.other.design.view.GridFilterActivity;
import com.smart.ui.module.other.design.view.LeftViewActivity;
import com.smart.ui.module.other.design.view.MoreTabActivity;
import com.smart.ui.module.other.design.view.PageViewActivity;
import com.smart.ui.module.other.design.view.StatusViewActivity;
import com.smart.ui.module.other.design.view.SwipeMenuActivity;
import com.smart.ui.module.other.design.view.TabActivity;
import com.smart.ui.module.other.design.view.TopDraggerActivity;
import com.smart.ui.module.other.design.view.VlayoutActivity;
import com.smart.ui.module.other.time.TimeActivity;
import com.webview.config.WebConfig;
import com.webview.ui.WebViewNormalActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseCompatFragment {

    @BindView(R.id.widget_view)
    RecyclerView widgetView;

    List<HomeListBean> allData3;
    HomeListAdapter mAdapter3;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        mContext = this.getContext();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment mCarouseFragment = new PicCarouseFragment();
        transaction.add(R.id.fragment_container, mCarouseFragment);
        transaction.commit();

        initAdapter3();
        return containerView;
    }

    private void initAdapter3() {
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
        HomeListBean bean3e = new HomeListBean.Builder().content("虚拟布局").build();
        HomeListBean bean3f = new HomeListBean.Builder().content("手表").build();
        HomeListBean bean3g = new HomeListBean.Builder().content("画图").build();
        HomeListBean bean3h = new HomeListBean.Builder().content("选图").build();

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
        allData3.add(bean3e);
        allData3.add(bean3f);
        allData3.add(bean3g);
        allData3.add(bean3h);

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
                            readGo(VlayoutActivity.class);
                            break;
                        case 15:
                            readGo(TimeActivity.class);
                            break;
                        case 16:
                            Bundle bundle = new Bundle();
                            bundle.putString(PhotoConfig.PHOTO_TITLE, "画图");
                            bundle.putInt(PhotoConfig.FRAGMENT_POS, PhotoConfig.POS_EDIT);
                            bundle.putString(PhotoConfig.PHOTO_URL, "/storage/emulated/0/1/image.jpg");
                            readGo(PhotoActivity.class, bundle);
                            break;
                        case 17:
                            readGo(PhotoChooseActivity.class);
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
}
