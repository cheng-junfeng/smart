package com.webview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import com.webview.R;
import com.webview.R2;
import com.webview.app.activity.WebBaseCompatFragment;
import com.webview.app.control.MyWebViewClient;
import com.webview.config.WebConfig;
import com.webview.ui.presenter.WebViewPresenter;
import com.webview.ui.view.ViewLoading;
import com.webview.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class WebViewNormalFragment extends WebBaseCompatFragment {
    private final static String TAG = "WebViewNormalFragment";

    @BindView(R2.id.webView)
    BridgeWebView bridgeWebView;
    @BindView(R2.id.ly_empty)
    FrameLayout lyEmpty;

    private String urlStr;
    private String loadingStr;

    private ViewLoading loading;
    private WebViewPresenter presenter;

    public static WebViewNormalFragment newInstance(String url){
        WebViewNormalFragment newFragment = new WebViewNormalFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebConfig.JS_URL, url);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static WebViewNormalFragment newInstance(String url, String loading){
        WebViewNormalFragment newFragment = new WebViewNormalFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebConfig.JS_URL, url);
        bundle.putString(WebConfig.JS_LOADING, loading);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected int setContentView() {
        return R.layout.js_fragment_normal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        LogUtil.d(TAG, "onCreateView:");
        presenter = new WebViewPresenter();

        initView();
        return containerView;
    }

    private void initView() {
        Bundle args = getArguments();
        urlStr = "";
        loadingStr = "";
        if (args != null) {
            urlStr = args.getString(WebConfig.JS_URL, "");
            loadingStr = args.getString(WebConfig.JS_LOADING, "");
        }

        presenter.settingWebView(bridgeWebView);
        bridgeWebView.setWebViewClient(new MyWebViewClient(bridgeWebView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.d(TAG, "onPageFinished:" + url);
                if(loading != null && loading.isShowing()){
                    loading.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d(TAG, "onReceivedError:" + description + ":" + failingUrl);
                showEmpty();
                if(loading != null && loading.isShowing()){
                    loading.dismiss();
                }
            }
        });

        showLoading();
    }

    private void showLoading(){
        lyEmpty.setVisibility(View.GONE);
        bridgeWebView.setVisibility(View.VISIBLE);
        loading = new ViewLoading(this.getContext(), loadingStr) {
            @Override
            protected void loadCancel() {
                if(loading != null && !loading.isShowing()){
                    loading.dismiss();
                }
            }
        };
        if(loading != null && !loading.isShowing()){
            loading.show();
        }
        bridgeWebView.loadUrl(urlStr);
    }

    private void showEmpty(){
        bridgeWebView.setVisibility(View.GONE);
        lyEmpty.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.ly_retry)
    public void onViewClicked() {
        showLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(TAG, "onDestroyView:");
        presenter.destroyWebView(bridgeWebView);
    }
}
