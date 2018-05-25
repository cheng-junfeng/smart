package com.wu.safe.user.config;

import com.smart.base.BuildConfig;
import com.smart.base.utils.ShareUtil;


public interface NetConfig {
    //net sharePre
    String PRE_SERVICE_IP = "pre.service.ip";

    String IPADRESS_RELEASE = "http://172.16.93.32:5000";
    String IPADRESS_DEBUG = "http://172.16.93.34:5000";
    String IPADRESS_DEFALUT = BuildConfig.RELEASE ? IPADRESS_RELEASE : IPADRESS_DEBUG;

    String IP_ADDRESS = ShareUtil.getString(PRE_SERVICE_IP, IPADRESS_DEFALUT);

    //请求头
    String AUTHORIZATION = "Authorization";
    String CONTENT_TYPE = "Content-Type";
}
