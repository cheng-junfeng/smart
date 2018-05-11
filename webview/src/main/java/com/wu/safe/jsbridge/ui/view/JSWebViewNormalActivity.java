package com.wu.safe.jsbridge.ui.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.wu.safe.jsbridge.R;
import com.wu.safe.jsbridge.R2;
import com.wu.safe.jsbridge.app.activity.JSBaseCompatActivity;
import com.wu.safe.jsbridge.app.control.MyWebViewClient;
import com.wu.safe.jsbridge.config.JSConfig;
import com.wu.safe.jsbridge.ui.presenter.WebViewPresenter;
import com.wu.safe.jsbridge.utils.LogUtil;
import com.wu.safe.jsbridge.utils.ToolbarUtil;

import butterknife.BindView;


public class JSWebViewNormalActivity extends JSBaseCompatActivity {

    public static final String TAG = "JSWebViewNormalActivity";

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
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d(TAG, "onReceivedError:" + description + ":" + failingUrl);
            }
        });

        getNetData();
    }

    private void getNetData() {
        //"http://172.16.93.111:8090/index.html"
        Bundle bundle = getIntent().getExtras();
        String urlString = (bundle == null) ? null : bundle.getString(JSConfig.JS_URL);
        webView.loadUrl(urlString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyWebView(webView);
    }
}
