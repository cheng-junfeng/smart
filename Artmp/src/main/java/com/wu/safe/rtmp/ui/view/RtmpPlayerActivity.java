package com.wu.safe.rtmp.ui.view;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.smart.base.utils.LogUtil;
import com.smart.base.utils.ToolbarUtil;
import com.wu.safe.rtmp.R;
import com.wu.safe.rtmp.R2;
import com.wu.safe.rtmp.app.activity.RtmpCompatActivity;
import com.wu.safe.rtmp.ui.contract.PlayContract;
import com.wu.safe.rtmp.ui.presenter.PlayPresenter;

import butterknife.BindView;

public class RtmpPlayerActivity extends RtmpCompatActivity implements PlayContract.View{

    private final static String TAG = "RtmpPlayerActivity";
    @BindView(R2.id.iv_webview)
    WebView ivWebview;

    PlayContract.Presenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.activity_live_play;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "播放rtmp", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        presenter = new PlayPresenter(this);
        initWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivWebview.onResume();
    }

    private void initWebView(){
        ivWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受所有网站的证书
                LogUtil.d(TAG, "onReceivedSslError");
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                LogUtil.d(TAG, "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                if(getRxActivity() == null || getRxActivity().isFinishing()){
                    return;
                }
                super.onPageFinished(view, url);
                LogUtil.d(TAG, "onPageFinished");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d(TAG, "onReceivedError:"+description+":");
            }
        });

        presenter.settingWebView(ivWebview);
        ivWebview.loadUrl("file:///android_asset/rtmp/rtmp.html");
    }

    @Override
    protected void onPause() {
        ivWebview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyWebView(ivWebview);
        LogUtil.d(TAG, "onDestroy");
    }

    @Override
    public RxAppCompatActivity getRxActivity(){
        return this;
    }
}
