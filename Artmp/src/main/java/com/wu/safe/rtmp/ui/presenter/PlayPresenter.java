package com.wu.safe.rtmp.ui.presenter;

import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wu.safe.rtmp.ui.contract.PlayContract;


public class PlayPresenter implements PlayContract.Presenter {
    private final static String TAG = "PlayPresenter";

    private PlayContract.View mView;
    private RxAppCompatActivity activity;

    public PlayPresenter(PlayContract.View androidView) {
        this.activity = androidView.getRxActivity();
    }

    @Override
    public void settingWebView(WebView webView) {
        WebSettings webViewSettings = webView.getSettings();

        //支持缩放
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setSupportZoom(true);

        //支持双指缩放并且隐藏缩放工具
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setDisplayZoomControls(false);


        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
//        webViewSettings.setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
//        webViewSettings.setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    public void destroyWebView(WebView webView) {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
    }
}
