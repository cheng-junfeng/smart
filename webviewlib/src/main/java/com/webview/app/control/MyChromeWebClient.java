package com.webview.app.control;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.webview.utils.LogUtil;


public class MyChromeWebClient extends WebChromeClient {
    private final static String TAG = "MyChromeWebClient";

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        LogUtil.e(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId() );
        return true;
    }

    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        LogUtil.d(TAG, "onReceivedTitle:"+title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        LogUtil.d(TAG, "onProgressChanged:"+newProgress);
    }

    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
        LogUtil.d(TAG, "openFileChooser 0 ");
        this.openFileChooser(uploadMsg);
    }

    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
        LogUtil.d(TAG, "openFileChooser 1 ");
        this.openFileChooser(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        LogUtil.d(TAG, "openFileChooser 2 ");
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (!TextUtils.isEmpty(message)) {
            LogUtil.d(TAG, "onJsAlert :"+message);
        }
        result.cancel();// return false 不拦截，弹出Js对话框
        return true;
    }
}
