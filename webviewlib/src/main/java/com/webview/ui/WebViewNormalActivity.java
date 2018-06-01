package com.webview.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.webview.R;
import com.webview.R2;
import com.webview.app.activity.WebBaseCompatActivity;
import com.webview.app.control.MyWebViewClient;
import com.webview.config.WebConfig;
import com.webview.ui.presenter.WebViewPresenter;
import com.webview.ui.view.ViewLoading;
import com.webview.utils.LogUtil;
import com.webview.utils.ToolbarUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class WebViewNormalActivity extends WebBaseCompatActivity {

    public static final String TAG = "WebViewNormalActivity";

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.webView)
    BridgeWebView webView;
    @BindView(R2.id.ly_empty)
    FrameLayout lyEmpty;

    private String urlStr;
    private String loadingStr;

    private ViewLoading loading;
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
        urlStr = (bundle == null) ? null : bundle.getString(WebConfig.JS_URL);
        String titleStr = (bundle == null) ? WebConfig.DEFAULT_TITLE : bundle.getString(WebConfig.JS_NAME, WebConfig.DEFAULT_TITLE);
        loadingStr = (bundle == null) ? "" : bundle.getString(WebConfig.JS_LOADING, "");
        ToolbarUtil.setToolbarLeft(toolbar, titleStr, null, new View.OnClickListener() {
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
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d(TAG, "onReceivedError:" + description + ":" + failingUrl);
                showEmpty();
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }
            }
        });

        // 添加Loading
        showLoading();
    }

    private void showLoading(){
        lyEmpty.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        loading = new ViewLoading(this, loadingStr) {
            @Override
            protected void loadCancel() {
                if (loading != null && !loading.isShowing()) {
                    loading.dismiss();
                }
            }
        };
        if (loading != null && !loading.isShowing()) {
            loading.show();
        }
        webView.loadUrl(urlStr);
    }

    private void showEmpty(){
        webView.setVisibility(View.GONE);
        lyEmpty.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.ly_retry)
    public void onViewClicked() {
        showLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (loading != null && loading.isShowing()) {
            loading.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyWebView(webView);
    }
}
