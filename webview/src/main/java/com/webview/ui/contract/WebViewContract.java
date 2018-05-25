package com.webview.ui.contract;


import android.webkit.WebView;

public interface WebViewContract {

    interface View  {
        void setBean(String name, String type, String id, String url);
    }

    interface Presenter  {
        void settingWebView(WebView webView);
        void destroyWebView(WebView webView);
    }
}
