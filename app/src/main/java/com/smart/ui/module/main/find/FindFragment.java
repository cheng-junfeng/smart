package com.smart.ui.module.main.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.base.app.event.RxBusHelper;
import com.smart.R;
import com.smart.app.activity.BaseCompatFragment;
import com.smart.app.adapter.BaseDelegateAdapter;
import com.smart.app.event.NoteEvent;
import com.smart.app.event.NoteType;
import com.smart.ui.module.main.find.adapter.FindSingle2Adapter;
import com.smart.ui.module.main.find.adapter.FindSingleAdapter;
import com.smart.ui.module.main.find.adapter.FindStickyAdapter;
import com.smart.ui.module.main.find.contract.FindFragmentContract;
import com.smart.ui.module.main.find.presenter.FindFragmentPresenter;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class FindFragment extends BaseCompatFragment implements FindFragmentContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FindFragmentContract.Presenter presenter;
    private List<DelegateAdapter.Adapter> mAdapters;

    DelegateAdapter delegateAdapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_find;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        RxBusHelper.doOnMainThread(this, NoteEvent.class, new RxBusHelper.OnEventListener<NoteEvent>() {
            @Override
            public void onEvent(NoteEvent noteEvent) {
                if((noteEvent.getType() == NoteType.NEW) || (noteEvent.getType() == NoteType.DELETE)){
                    reloadNote();
                }
            }
        });
        presenter = new FindFragmentPresenter(this, this);
        initView();
        return containerView;
    }

    public void initView() {
        mAdapters = new LinkedList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        delegateAdapter = presenter.initRecyclerView(recyclerView);
        //把轮播器添加到集合
        BaseDelegateAdapter bannerAdapter = presenter.initMenu1();
        mAdapters.add(bannerAdapter);

        //设置Sticky布局
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        stickyLayoutHelper.setStickyStart(true);
        mAdapters.add(new FindStickyAdapter(this.getContext(), stickyLayoutHelper, 1));

        BaseDelegateAdapter bannerAdapter2 = presenter.initMenu2();
        mAdapters.add(bannerAdapter2);

        //设置通栏布局
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        mAdapters.add(new FindSingle2Adapter(this.getContext(), singleLayoutHelper, 1));

        BaseDelegateAdapter bannerAdapter3 = presenter.initMenu3();
        mAdapters.add(bannerAdapter3);

        //设置通栏布局
        mAdapters.add(new FindSingleAdapter(this.getContext(), singleLayoutHelper, 1));

        DelegateAdapter.Adapter bannerAdapter4 = presenter.initMenu4();
        mAdapters.add(bannerAdapter4);

        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
    }

    private void reloadNote(){
        initView();
    }
}
