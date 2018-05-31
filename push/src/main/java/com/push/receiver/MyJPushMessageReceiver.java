package com.push.receiver;

import android.content.Context;

import com.base.config.GlobalConfig;
import com.base.utils.LogUtil;
import com.base.utils.ShareUtil;
import com.push.app.control.TagAliasOperatorHelper;
import com.base.app.event.RxBusHelper;
import com.push.app.event.MsgEvent;
import com.push.app.event.MsgType;
import com.push.config.PushSharePre;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 * */
public class MyJPushMessageReceiver extends JPushMessageReceiver {
    private final static String TAG = "JIGUANG-MyJPushMessageReceiver";

    @Override
    public void onTagOperatorResult(Context context,JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        int errorCode = jPushMessage.getErrorCode();
        LogUtil.d(TAG, "onAliasOperatorResult code:"+errorCode);
        if(errorCode == 6022){
            return;//ignore while 6022, for doing alias
        }

        //check start
        boolean JPUSH_IS_LOGIN = false;
        String userName = "";
        if(errorCode == 0){//only check while success
            if(!JPushInterface.isPushStopped(context)){
                String myAlias = jPushMessage.getAlias();
                userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
                if(userName.equals(myAlias)){
                    JPUSH_IS_LOGIN = true;
                }
            }
        }else{
            JPUSH_IS_LOGIN = false;
        }
        LogUtil.d(TAG, "onAliasOperatorResult usename" + userName+" login:"+JPUSH_IS_LOGIN);
        ShareUtil.put(PushSharePre.JPUSH_IS_LOGIN, JPUSH_IS_LOGIN);
        MsgEvent event = new MsgEvent.Builder(MsgType.STATUS)
                .build();
        RxBusHelper.post(event);
        //check end
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
