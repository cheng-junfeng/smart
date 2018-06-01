package com.photo.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.app.activity.BaseAppCompatFragment;
import com.photo.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class PhotoBaseCompatFragment extends BaseAppCompatFragment {

    protected int default_res = R.drawable.photo_default_image;
    protected String myPath;
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
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public  void setPath(String path){
        this.myPath = path;
    }

    public String getPath(){
        return this.myPath;
    }
}
