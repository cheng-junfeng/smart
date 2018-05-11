package com.wu.safe.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.wu.safe.base.config.GlobalConfig;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.push.R;
import com.wu.safe.push.app.control.Push;
import com.wu.safe.push.config.PushConfig;
import com.wu.safe.push.utils.PrintUtil;
import com.wu.safe.base.app.event.RxBusHelper;
import com.wu.safe.push.app.event.MsgEvent;
import com.wu.safe.push.app.event.MsgType;
import com.wu.safe.push.utils.MessageUtil;


import cn.jpush.android.api.JPushInterface;

public class MsgReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-MsgReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtil.d(TAG, "[MsgReceiver] onReceive - " + intent.getAction() + ", extras: " + PrintUtil.printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtil.d(TAG, "[MsgReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtil.d(TAG, "[MsgReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtil.d(TAG, "[MsgReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                if (TextUtils.isEmpty(title)) {
                    title = context.getString(R.string.app_name);
                    bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, title);
                }
                String message = bundle.getString(JPushInterface.EXTRA_ALERT);
                String messId = bundle.getString(JPushInterface.EXTRA_MSG_ID);

                boolean hadInsert = MessageUtil.insertMessageDb(context, 0, messId, title, message);
                if (hadInsert) {
                    MsgEvent event = new MsgEvent.Builder(MsgType.NEW)
                            .title(title)
                            .content(message)
                            .build();
                    RxBusHelper.post(event);
                }
                LogUtil.d(TAG, "[MsgReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtil.d(TAG, "[MsgReceiver] 用户点击打开了通知");

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtil.d(TAG, "[MsgReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtil.d(TAG, "[MsgReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String netTitle = bundle.getString(JPushInterface.EXTRA_TITLE);
        if (TextUtils.isEmpty(netTitle)) {
            netTitle = context.getString(R.string.app_name);
            bundle.putString(JPushInterface.EXTRA_TITLE, netTitle);
        }
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String messId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String contentType = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
        int type = 1;//normal
        if (PushConfig.TYPE_ERROR.equalsIgnoreCase(contentType)) {
            type = 2;
        } else if (PushConfig.TYPE_MQTT.equalsIgnoreCase(contentType)) {
            type = 3;
        }

        boolean hadInsert = MessageUtil.insertMessageDb(context, type, messId, netTitle, message, extras);
        if (hadInsert) {
            Push.showNotification(context, getViewIntent(context), netTitle, message);
            MsgEvent event = new MsgEvent.Builder(MsgType.NEW)
                    .title(netTitle)
                    .content(message)
                    .build();
            RxBusHelper.post(event);
        }
    }

    private Intent getViewIntent(Context context) {
        Intent intent = new Intent(GlobalConfig.MAIN_INTENT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
