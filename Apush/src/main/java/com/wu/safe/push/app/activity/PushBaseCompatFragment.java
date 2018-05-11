package com.wu.safe.push.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wu.safe.base.app.activity.BaseAppCompatFragment;
import com.wu.safe.base.app.event.RxBusHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class PushBaseCompatFragment extends BaseAppCompatFragment {
    protected int pageIndex = 0;
    protected int pageSize = 20;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, containView);
        return containView;
    }

    protected abstract int setContentView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBusHelper.unSubscribe(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected void loadMore() {
        pageIndex = pageIndex + pageSize;
    }

    protected void loadFresh() {
        pageIndex = pageIndex - pageSize;
        if(pageIndex < 0){
            pageIndex = 0;
        }
    }
}
