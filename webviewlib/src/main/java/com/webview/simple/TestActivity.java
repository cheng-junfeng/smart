package com.webview.simple;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.webview.R;
import com.webview.R2;
import com.webview.app.activity.WebBaseCompatActivity;
import com.webview.utils.LogUtil;
import com.webview.utils.ToolbarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends WebBaseCompatActivity {
    public final static String TAG = "TestActivity";

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.webView)
    BridgeWebView mywebView;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.js_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "测试", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        mywebView.getSettings().setJavaScriptEnabled(true);
        mywebView.getSettings().setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        mywebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
        mywebView.getSettings().setAllowFileAccess(true);
        mywebView.getSettings().setAppCacheEnabled(true);
        String appCachePath = getApplication().getCacheDir().getAbsolutePath();
        mywebView.getSettings().setAppCachePath(appCachePath);
        mywebView.getSettings().setDatabaseEnabled(true);
        mywebView.addJavascriptInterface(new AppClass(getBaseContext()), "android");
        mywebView.setWebChromeClient(new MyWebChromeClient());
//        mywebView.loadUrl("file:///android_asset/video.html");
        mywebView.loadUrl("http://172.16.93.111:8080/javascript.html");
        mywebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.d(TAG, "onPageFinished:");
                String val = "Bearer fadfadfafadfsadfasdfasdfadf";
                String js = "window.localStorage.setItem('applicationToken','" + val + "', true);";

//                localStorage.setItem("userAgent",param,true);
                String jsUrl = "javascript:(function({" +
                        "var localStorage = window.localStorage;" +
                        "localStorage.setItem('applicationToken','" + val + "')})()";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    LogUtil.d(TAG, "js");
                    view.evaluateJavascript(js, null);
                } else {
                    LogUtil.d(TAG, "jsUrl");
                    view.loadUrl(jsUrl);
                    view.reload();
                }
            }
        });
    }

    @OnClick(R2.id.tv_sure)
    public void onViewClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mywebView.evaluateJavascript("javascript:getData()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String result) {
//                    LogUtil.d(TAG, "result:" + result);
                }
            });
        }
    }

    private class AppClass {
        private Context c;

        public AppClass(Context baseContext) {
            this.c = baseContext;
        }

        @JavascriptInterface
        public void getUserKey(String userKey) {
            Toast.makeText(c, "-----" + userKey + "", Toast.LENGTH_SHORT).show();
            LogUtil.e(TAG, "getUserKey : " + userKey);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            LogUtil.e(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast.makeText(mContext, "-----" + message + "", Toast.LENGTH_SHORT).show();
            // return false 不拦截，弹出Js对话框
            return false;
        }
    }
}
