package com.wu.safe.push.app.control;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.base.config.GlobalConfig;
import com.base.utils.LogUtil;
import com.base.utils.NotificationUtil;
import com.base.utils.ShareUtil;
import com.wu.safe.push.R;
import com.wu.safe.push.config.PushConfig;
import com.wu.safe.push.config.PushSharePre;
import com.wu.safe.push.service.MqttService;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import static com.wu.safe.push.app.control.TagAliasOperatorHelper.ACTION_DELETE;
import static com.wu.safe.push.app.control.TagAliasOperatorHelper.ACTION_GET;
import static com.wu.safe.push.app.control.TagAliasOperatorHelper.ACTION_SET;


public class Push {
    public final static String TAG = "JIGUANG-Push";

    //初始化
    public static void init(Context context) {
        LogUtil.d(TAG, "init [0503]");
        JPushInterface.setDebugMode(GlobalConfig.IS_DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(context);                      // 初始化 Push
        JPushInterface.stopPush(context);
        if (Build.VERSION.SDK_INT >= 26){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PushConfig.CHANNEL_ID, PushConfig.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        setStyleBasic(context);
    }

    //恢复
    public static void resume(Context context){
        LogUtil.d(TAG, "resume:");
        JPushInterface.resumePush(context);
        if(!ShareUtil.getBoolean(PushSharePre.JPUSH_IS_LOGIN, false)){
            String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
            LogUtil.d(TAG, "---resume set alias:" + userName);
            if(!TextUtils.isEmpty(userName)){
                setAliasAction(context, userName, ACTION_SET);
            }
        }else{
            LogUtil.d(TAG, "---resume get alias:");
            setAliasAction(context, "", ACTION_GET);
        }
    }

    //恢复mqtt
    public static void resumeMqtt(Context context){
        LogUtil.d(TAG, "resumeMqtt:");//only by self
        if(!ShareUtil.getBoolean(PushSharePre.MQTT_IS_LOGIN, false)){
            MqttService.startMqtt(context);
        }
    }

    //关闭
    public static void stop(Context context){
        LogUtil.d(TAG, "stop and clean:");
        ShareUtil.put(PushSharePre.JPUSH_IS_LOGIN, false);
        setAliasAction(context, "", ACTION_DELETE);
        NotificationManager nm =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
        JPushInterface.stopPush(context);
        MqttService.stopMqtt(context);
    }

    //获取状态
    public static boolean getStatus(Context context){
        boolean isJpush = ShareUtil.getBoolean(PushSharePre.JPUSH_IS_LOGIN, false);
        boolean isMqtt = ShareUtil.getBoolean(PushSharePre.MQTT_IS_LOGIN, false);
        LogUtil.d(TAG, "getStatus :"+isJpush+":"+isMqtt);
        return isJpush || isMqtt;//only one, for service to resume autoly
    }

    //通知
    public static void showNotification(Context context, String title, String msg){
        NotificationUtil.showNotification(context, R.mipmap.msg_notifi, title, msg);
    }

    //通知
    public static void showNotification(Context context, Intent intent, String title, String msg){
        NotificationUtil.showNotification(context, intent, R.mipmap.msg_notifi, title, msg);
    }

    private static void setAliasAction(Context mContext, String alias, int action) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        tagAliasBean.alias = alias;
        int sequence = TagAliasOperatorHelper.sequence;
        sequence++;
        tagAliasBean.isAliasAction = true;
        LogUtil.d(TAG, "alias:"+sequence+" "+alias);
        TagAliasOperatorHelper.getInstance().handleAction(mContext, sequence, tagAliasBean);
    }

    /**
     * 设置通知栏属性
     */
    private static void setStyleBasic(Context context){
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.mipmap.msg_notifi;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    /**
     * 设置通知栏样式
     */
    private static void setStyleCustom(Context context){
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.msg_notifi;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
    }
}