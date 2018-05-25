package com.wu.safe.smart.app.activity;

import android.os.Bundle;

import com.smart.base.app.activity.BaseAppActivity;
import com.smart.base.app.event.RxBusHelper;

import butterknife.ButterKnife;

public abstract class BaseActivity extends BaseAppActivity {

    protected int pageIndex = 0;
    protected int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        ButterKnife.bind(this);
    }

    protected abstract int setContentView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusHelper.unSubscribe(this);
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
