package com.webview.ui.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import com.hint.utils.DialogUtils;
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
    public static final String DEFAULT_TITLE = "WebView";

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

    private void initView() {
        webView = (BridgeWebView) findViewById(R.id.webView);
        Bundle bundle = getIntent().getExtras();
        String urlString = (bundle == null) ? null : bundle.getString(WebConfig.JS_URL);
        String urlName = (bundle == null) ? DEFAULT_TITLE : bundle.getString(WebConfig.JS_NAME, DEFAULT_TITLE);
        ToolbarUtil.setToolbarLeft(toolbar, urlName, null, new View.OnClickListener() {
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
                DialogUtils.dismissLoading();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d(TAG, "onReceivedError:" + description + ":" + failingUrl);
                DialogUtils.dismissLoading();
            }
        });

        // 添加Loading
        DialogUtils.showLoading(this);
        webView.loadUrl(urlString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyWebView(webView);
    }
}
