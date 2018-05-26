package com.wu.safe.push.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.base.utils.LogUtil;
import com.base.utils.ShareUtil;
import com.wu.safe.push.config.PushConfig;
import com.wu.safe.push.config.PushSharePre;
import com.wu.safe.push.ui.bean.MqttBean;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import cn.jpush.android.api.JPushInterface;


public class MqttService extends IntentService {
    public static final String TAG = "MQTT-MqttService";

    public static final String ACTION_START_MQTT = "com.intent.action.START_MQTT";
    public static final String ACTION_CHECK_MQTT = "com.intent.action.CHECK_MQTT";

    public static final long ALARM_SHORT = 10 * 60 * 1000; //10 min
    private static MqttClient client;
    private MqttConnectOptions options;
    private Context mContext;

    public MqttService() {
        super(TAG);
    }

    public static void startMqtt(Context context) {
        LogUtil.d(TAG, TAG + ":startMqtt");
        Intent intent = new Intent(context, MqttService.class);
        intent.setAction(ACTION_START_MQTT);
        context.startService(intent);
    }

    public static void stopMqtt(Context context){
        LogUtil.d(TAG, TAG + ":stopMqtt");
        try {
            if(client != null){
                client.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Intent checkIntent = new Intent(ACTION_CHECK_MQTT);
        PendingIntent pi = PendingIntent.getService(context, 0, checkIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(pi);

        Intent intent = new Intent(context, MqttService.class);
        context.stopService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (intent != null) {
            mContext = this;
            String action = intent.getAction();
            LogUtil.d(TAG, "onHandleIntent :" + action);
            if(ACTION_START_MQTT.equals(action)) {
                setClient();

                Intent checkIntent = new Intent(ACTION_CHECK_MQTT);
                PendingIntent pi = PendingIntent.getService(this, 0, checkIntent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                mgr.cancel(pi);

                LogUtil.d(TAG, "start mqtt: ");
                mgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_SHORT, pi);
            } else if(ACTION_CHECK_MQTT.equals(action)){
                LogUtil.d(TAG, "check mqtt: ");
                setClient();
            }
        }
    }

    private void setClient(){
        LogUtil.d(TAG, "setClient: ");
        if(client == null){
            initClient();
        }
        if (!client.isConnected()) {
            connect();
        }else {
            ShareUtil.put(PushSharePre.MQTT_IS_LOGIN, true);
        }
        try {
            client.subscribe(PushConfig.myTopic, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initClient() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(PushConfig.host, "test",
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(PushConfig.userName);
            //设置连接的密码
            options.setPassword(PushConfig.passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    LogUtil.d(TAG, "connectionLost----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    LogUtil.d(TAG, "deliveryComplete---------" + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                    //subscribe后得到的消息会执行到这里面
                    Gson gson = new Gson();
                    MqttBean msg = gson.fromJson(message.toString(), MqttBean.class);
                    LogUtil.d(TAG, "[MqttService] processCustomMessage - " + message);
                    if(msg == null){
                        return;
                    }

                    Intent intent = new Intent(JPushInterface.ACTION_MESSAGE_RECEIVED);
                    Bundle bundle = new Bundle();
                    bundle.putString(JPushInterface.EXTRA_TITLE, msg.title);
                    bundle.putString(JPushInterface.EXTRA_MESSAGE, msg.content);
                    bundle.putString(JPushInterface.EXTRA_MSG_ID, msg.id);
                    bundle.putString(JPushInterface.EXTRA_EXTRA, "");
                    bundle.putString(JPushInterface.EXTRA_CONTENT_TYPE, PushConfig.TYPE_MQTT);
                    intent.putExtras(bundle);
                    mContext.sendBroadcast(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                    LogUtil.d(TAG, "set success---------");
                    ShareUtil.put(PushSharePre.MQTT_IS_LOGIN, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                    LogUtil.d(TAG, "set failed---------");
                    ShareUtil.put(PushSharePre.MQTT_IS_LOGIN, false);
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                LogUtil.d(TAG, "connect success---------");
                try {
                    client.subscribe(PushConfig.myTopic, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 3) {
                LogUtil.d(TAG, "connect failed---------");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
    }
}
