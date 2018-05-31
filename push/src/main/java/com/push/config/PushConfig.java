package com.push.config;

public interface PushConfig {
    String CHANNEL_ID = "channel";
    String CHANNEL_NAME = "channel_push";
    String DEFAULT_ALERT = "alert";
    String DEFAULT_TYPE = "base";

    String TYPE_ERROR = "error";
    String TYPE_MQTT = "mqtt";

    //mqtt
    String host = "tcp://172.16.93.111:61613";
    String userName = "admin";
    String passWord = "password";
    String myTopic = "test/topic";
    String myId = "test";
}
