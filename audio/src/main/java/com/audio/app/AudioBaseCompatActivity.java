package com.audio.app;

import android.os.Bundle;

import com.base.app.activity.BaseAppCompatActivity;

import butterknife.ButterKnife;

public abstract class AudioBaseCompatActivity extends BaseAppCompatActivity {

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
    }
}
