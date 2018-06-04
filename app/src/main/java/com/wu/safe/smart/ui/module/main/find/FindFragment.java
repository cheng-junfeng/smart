package com.wu.safe.smart.ui.module.main.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.app.adapter.BaseDelegateAdapter;
import com.wu.safe.smart.ui.module.main.find.contract.FindFragmentContract;
import com.wu.safe.smart.ui.module.main.find.presenter.FindFragmentPresenter;
import com.wu.safe.smart.ui.widget.BGABadgeTextView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FindFragment extends BaseCompatFragment implements FindFragmentContract.View {

    @BindView(R.id.re_front_icon)
    BGABadgeTextView reFrontIcon;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FindFragmentContract.Presenter presenter;
    private List<DelegateAdapter.Adapter> mAdapters;

    @Override
    protected int setContentView() {
        return R.layout.fragment_find;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        presenter = new FindFragmentPresenter(this, this);
        initView();
        return containerView;
    }

    public void initView() {
        mAdapters = new LinkedList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        DelegateAdapter delegateAdapter = presenter.initRecyclerView(recyclerView);
        //把轮播器添加到集合
        BaseDelegateAdapter bannerAdapter = presenter.initMenu1();
        mAdapters.add(bannerAdapter);

        BaseDelegateAdapter bannerAdapter2 = presenter.initMenu2();
        mAdapters.add(bannerAdapter2);

        BaseDelegateAdapter bannerAdapter3 = presenter.initMenu3();
        mAdapters.add(bannerAdapter3);

        BaseDelegateAdapter bannerAdapter4 = presenter.initMenu4();
        mAdapters.add(bannerAdapter4);

        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
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
