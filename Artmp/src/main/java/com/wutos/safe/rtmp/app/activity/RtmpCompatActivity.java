package com.wutos.safe.rtmp.app.activity;

import android.os.Bundle;

import com.wu.safe.base.app.activity.BaseAppCompatActivity;
import com.wu.safe.base.app.event.RxBusHelper;

import butterknife.ButterKnife;

public abstract class RtmpCompatActivity extends BaseAppCompatActivity {

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
