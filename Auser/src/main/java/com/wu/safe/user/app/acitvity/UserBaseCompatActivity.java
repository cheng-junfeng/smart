package com.wu.safe.user.app.acitvity;

import android.os.Bundle;

import com.base.app.activity.BaseAppCompatActivity;
import com.base.app.event.RxBusHelper;

import butterknife.ButterKnife;

public abstract class UserBaseCompatActivity extends BaseAppCompatActivity {

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
