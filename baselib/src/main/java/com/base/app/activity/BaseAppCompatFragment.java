package com.base.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;
import com.base.R;
import com.base.app.listener.OnRetryListener;

public abstract class BaseAppCompatFragment extends RxAppCompatDialogFragment {

    protected  FrameLayout frameBody_view;
    protected View frameMainView;
    protected View frameErrorView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = LayoutInflater.from(getContext()).inflate(R.layout.base_root_fragment, null);
        frameMainView = LayoutInflater.from(getContext()).inflate(setContentView(), null);
        frameBody_view = (FrameLayout)containerView.findViewById(R.id.base_fragment_body);

        frameBody_view.addView(frameMainView);
        return containerView;
    }

    protected abstract int setContentView();

    protected int setErrorView(){
        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void showErrorView(final OnRetryListener onRetryListener){
        if(frameErrorView != null){
            frameBody_view.removeAllViews();
            frameBody_view.addView(frameErrorView);
        }else{
            int resId = setErrorView();
            if(resId > 0){
                frameErrorView = LayoutInflater.from(getContext()).inflate(resId, null);
                frameBody_view.removeAllViews();
                frameBody_view.addView(frameErrorView);
                TextView retryView = (TextView) frameErrorView.findViewById(R.id.ly_retry);
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
        frameBody_view.removeAllViews();
        frameBody_view.addView(frameMainView);
    }

    protected void readGo(Class<?> cls){
        readGo(cls, null);
    }

    protected void readGo(Class<?> cls, Bundle bundle){
        Intent inten = new Intent(getActivity(), cls);
        if(bundle != null){
            inten.putExtras(bundle);
        }
        startActivity(inten);
    }
}
