package com.webview.app.control;


import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.webview.utils.LogUtil;


public class MyBridgeHandler implements BridgeHandler{
    private static final String TAG = "MyBridgeHandler";

    @Override
    public void handler(String data, CallBackFunction function) {
        LogUtil.e(TAG, "get from js："+data);
        function.onCallBack("Android says: got :"+data);
    }
}
