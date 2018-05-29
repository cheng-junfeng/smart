package com.base.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.base.R;
import com.base.app.listener.OnRetryListener;


public abstract class BaseAppCompatActivity extends RxAppCompatActivity {

    protected Toolbar toolbar;
    protected FrameLayout body_view;
    protected View mainView;
    protected View errorView;
    protected FrameLayout footer_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.base_root_activity, null);
        setContentView(view);
        body_view = (FrameLayout)view.findViewById(R.id.base_body);
        footer_view = (FrameLayout)view.findViewById(R.id.base_footer);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if(setToolbar()){
            toolbar.setVisibility(View.VISIBLE);
        }else{
            toolbar.setVisibility(View.GONE);
        }

        mainView = LayoutInflater.from(getBaseContext()).inflate(layoutResID, null);
        body_view.addView(mainView);

        int footResId = setFootView();
        if(footResId > 0){
            View footView = LayoutInflater.from(getBaseContext()).inflate(footResId, null);
            footer_view.addView(footView);
            footer_view.setVisibility(View.VISIBLE);
        }else{
            footer_view.setVisibility(View.GONE);
        }
    }

    protected boolean setToolbar(){
        return true;
    }

    protected abstract int setContentView();

    protected int setErrorView(){
        return -1;
    }

    protected int setFootView(){
        return -1;
    }

    protected void showErrorView(final OnRetryListener onRetryListener){
        if(errorView != null){
            body_view.removeAllViews();
            body_view.addView(errorView);
        }else{
            int resId = setErrorView();
            if(resId > 0){
                errorView = LayoutInflater.from(getBaseContext()).inflate(resId, null);
                body_view.removeAllViews();
                body_view.addView(errorView);
                TextView retryView = (TextView) errorView.findViewById(R.id.ly_retry);
                if(retryView != null){
                    retryView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(onRetryListener != null){
                                hideErrorView();
                                onRetryListener.onClick();
                            }
                        }
                    });
                }
            }else{
                return;
            }
        }
    }

    protected void hideErrorView(){
        body_view.removeAllViews();
        body_view.addView(mainView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    protected void finishAll(){
        AppManager.getAppManager().finishAllActivity();
    }

    protected void appExit(){
        AppManager.getAppManager().appExit();
    }

    protected void readGo(Class<?> cls){
        readGo(cls, null);
    }

    protected void readGo(Class<?> cls, Bundle bundle){
        Intent inten = new Intent(this, cls);
        if(bundle != null){
            inten.putExtras(bundle);
        }
        startActivity(inten);
    }

    protected void readGoFinish(Class<?> cls){
        readGoFinish(cls, null);
    }

    protected void readGoFinish(Class<?> cls, Bundle bundle){
        readGo(cls, bundle);
        finish();
    }

    protected void readGoFinishAnim(Class<?> cls){
        readGoFinishAnim(cls, null);
    }

    protected void readGoFinishAnim(Class<?> cls, Bundle bundle){
        readGoFinish(cls, bundle);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
    }
}
