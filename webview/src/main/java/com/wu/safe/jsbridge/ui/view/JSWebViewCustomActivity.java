package com.wu.safe.jsbridge.ui.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.wu.safe.jsbridge.R;
import com.wu.safe.jsbridge.R2;
import com.wu.safe.jsbridge.app.activity.JSBaseCompatActivity;
import com.wu.safe.jsbridge.app.control.MyBridgeHandler;
import com.wu.safe.jsbridge.app.control.MyChromeWebClient;
import com.wu.safe.jsbridge.app.control.MyWebViewClient;
import com.wu.safe.jsbridge.ui.bean.User;
import com.wu.safe.jsbridge.ui.presenter.WebViewPresenter;
import com.wu.safe.jsbridge.utils.ToolbarUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


public class JSWebViewCustomActivity extends JSBaseCompatActivity {

    public static final String TAG = "JSWebViewCustomActivity";

    private static final int RESULT_CODE = 0;

    @BindView(R2.id.webView)
    BridgeWebView webView;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private WebViewPresenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.js_webview;
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

        webView.setDefaultHandler(new MyBridgeHandler());
        //加载本地网页
        //webView.loadUrl("file:///android_asset/demo.html");
        presenter.settingWebView(webView);
        webView.addJavascriptInterface(new FileJavascriptInterface(), "myfile");
        webView.setWebViewClient(new MyWebViewClient(webView));
        webView.setWebChromeClient(new MyChromeWebClient());
        webView.loadUrl("http://172.16.93.111:8090/index.html");

        //js-for-android
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(JSWebViewCustomActivity.this, "From Js:" + data, Toast.LENGTH_SHORT).show();
                //android -back-js
                function.onCallBack("Android says：got " + data);
            }
        });
        //模拟用户获取本地位置
        User user = new User();
        user.name = "Bruce";
        user.testStr = "test";

        //android-to-js onCallBack 1
        webView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                //js-back-android
                Toast.makeText(JSWebViewCustomActivity.this, "Js back:" + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R2.id.tv_sure)
    public void onViewClicked() {
        String time = new Date().toString();
        //android-to-js
        webView.callHandler("functionInJs", "手机时间：" + time, new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                //js-back-android
                Toast.makeText(JSWebViewCustomActivity.this, "Js back:" + data, Toast.LENGTH_SHORT).show();
            }
        });

        //android-to-js
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:backPress()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String result) {
                    //js-back-android
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyWebView(webView);
    }

    class FileJavascriptInterface {

        public FileJavascriptInterface() {
        }

        @JavascriptInterface
        public void openPhtoto() {
            //js-open-android
            Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
            chooserIntent.setType("image/*");
            startActivityForResult(chooserIntent, RESULT_CODE);
        }

        @JavascriptInterface
        public String getToken() {
            //js-gen-android
            return "token";
        }

        @JavascriptInterface
        public void setTitle(String title) {
            //js-set-android
        }
    }
}
