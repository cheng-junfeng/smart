package com.webview.ui.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import com.hintlib.widget.ViewLoading;
import com.webview.R;
import com.webview.R2;
import com.webview.app.activity.WebBaseCompatActivity;
import com.webview.app.control.MyWebViewClient;
import com.webview.config.WebConfig;
import com.webview.ui.presenter.WebViewPresenter;
import com.webview.utils.LogUtil;
import com.webview.utils.ToolbarUtil;

import butterknife.BindView;


public class WebViewNormalActivity extends WebBaseCompatActivity {

    public static final String TAG = "WebViewNormalActivity";

    @BindView(R2.id.webView)
    BridgeWebView webView;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private WebViewPresenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.js_webview_normal;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WebViewPresenter();

        initView();
    }

    ViewLoading mLoading;
    private void initView() {
        webView = (BridgeWebView) findViewById(R.id.webView);
        ToolbarUtil.setToolbarLeft(toolbar, "WebView", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        presenter.settingWebView(webView);
        webView.setWebViewClient(new MyWebViewClient(webView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.d(TAG, "onPageFinished:" + url);
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d(TAG, "onReceivedError:" + description + ":" + failingUrl);
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        });

        // 添加Loading
        mLoading = new ViewLoading(this, 1,"") {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
        getNetData();
    }

    private void getNetData() {
        Bundle bundle = getIntent().getExtras();
        String urlString = (bundle == null) ? null : bundle.getString(WebConfig.JS_URL);
        webView.loadUrl(urlString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyWebView(webView);
    }
}
