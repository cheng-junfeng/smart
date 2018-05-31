package com.txlive.ui.contract;

import android.webkit.WebView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


public interface PlayContract {

    interface View  {
        RxAppCompatActivity getRxActivity();
    }

    interface Presenter  {
        void settingWebView(WebView webView);
        void destroyWebView(WebView webView);
    }
}
