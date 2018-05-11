package com.wu.safe.jsbridge.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.wu.safe.jsbridge.R;
import com.wu.safe.jsbridge.R2;
import com.wu.safe.jsbridge.app.activity.JSBaseCompatFragment;
import com.wu.safe.jsbridge.app.control.MyWebViewClient;
import com.wu.safe.jsbridge.config.JSConfig;
import com.wu.safe.jsbridge.ui.presenter.WebViewPresenter;
import com.wu.safe.jsbridge.utils.LogUtil;

import butterknife.BindView;


public class JSWebViewNormalFragment extends JSBaseCompatFragment {
    private final static String TAG = "JSWebViewNormalFragment";

    @BindView(R2.id.webView)
    BridgeWebView bridgeWebView;

    private WebViewPresenter presenter;

    public static JSWebViewNormalFragment newInstance(String url){
        JSWebViewNormalFragment newFragment = new JSWebViewNormalFragment();
        Bundle bundle = new Bundle();
        bundle.putString(JSConfig.JS_URL, url);
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

    public void initView() {
        presenter.settingWebView(bridgeWebView);
        bridgeWebView.setWebViewClient(new MyWebViewClient(bridgeWebView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.d(TAG, "onPageFinished:" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d(TAG, "onReceivedError:" + description + ":" + failingUrl);
            }
        });

        loadUrl();
    }

    private void loadUrl(){
        Bundle args = getArguments();
        String url = "";
        if (args != null) {
            url = args.getString(JSConfig.JS_URL);
        }
        bridgeWebView.loadUrl(url);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(TAG, "onDestroyView:");
        presenter.destroyWebView(bridgeWebView);
    }
}
