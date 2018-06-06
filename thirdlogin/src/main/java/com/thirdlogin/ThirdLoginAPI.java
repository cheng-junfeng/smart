package com.thirdlogin;

import android.app.Application;
import android.content.Context;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


public class ThirdLoginAPI extends Application {

    /**
     * only work for sina
     * qq and wechat , waiting for audit
     * */
    public static void init(Context context) {
        UMShareAPI.get(context);//初始化sdkzan'wei
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;

        //微信平台的配置，微信
//        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
//        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("475062812", "e860fab29e61f8651657fcb047f10db2","http://sns.whalecloud.com/sina2/callback");
//        //QQ
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
}