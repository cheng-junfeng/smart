package com.jmolsmobile.videoplay.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jmolsmobile.videocapture.R;
import com.jmolsmobile.videocapture.R2;
import com.jmolsmobile.videocapture.app.activity.VideoBaseCompatActivity;
import com.jmolsmobile.videocapture.utils.ToolbarUtil;

import butterknife.BindView;


public class WebViewActivity extends VideoBaseCompatActivity {

    public static final String TAG = "WebViewActivity";

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.webview)
    WebView webview;

    @Override
    protected int setContentView() {
        return R.layout.video_web_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "WebView", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        settingWebView(webview);
        webview.loadUrl("file:///android_asset/videoplay.html");
    }

    public void settingWebView(WebView webView) {
        WebSettings webViewSettings = webView.getSettings();
        //支持缩放
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setSupportZoom(true);

        //支持双指缩放并且隐藏缩放工具
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setDisplayZoomControls(false);
        //设置 缓存模式
        webViewSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webViewSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webViewSettings.setDatabaseEnabled(true);

        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
//        webViewSettings.setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
//        webViewSettings.setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webview!=null) {
            ViewParent parent = webview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webview);
            }

            webview.stopLoading();
            webview.getSettings().setJavaScriptEnabled(false);
            webview.clearHistory();
            webview.clearView();
            webview.removeAllViews();
            webview.destroy();
        }
    }
}