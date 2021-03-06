package com.user.app.acitvity;

import android.os.Bundle;

import com.base.app.activity.BaseAppActivity;
import com.base.app.event.RxBusHelper;

import butterknife.ButterKnife;

public abstract class UserBaseActivity extends BaseAppActivity {

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
}
