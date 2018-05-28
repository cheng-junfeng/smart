package com.webview.ui.contract;


import android.webkit.WebView;

public interface WebViewContract {

    interface Presenter  {
        void settingWebView(WebView webView);
        void destroyWebView(WebView webView);
    }
}
