package com.push.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.push.config.PushConfig;
import com.push.db.entity.MessagesEntity;
import com.push.db.helper.MessageHelper;
import com.push.ui.bean.JpushBean;



public class MessageUtil {
    public static boolean insertMessageDb(Context mContext, int type, String messId, String title, String content){
        return insertMessageDb(mContext, type, messId, title, content, "");
    }

    public static boolean insertMessageDb(Context mContext, int type, String messId, String title, String content, String extra){
        MessagesEntity newMsg = new MessagesEntity();
        newMsg.setMess_id(messId);
        newMsg.setType(type);
        newMsg.setTitle(title);
        newMsg.setContent(content);
        newMsg.setInTime(Long.toString(System.currentTimeMillis()));
        newMsg.setTypeName(getTypeName(type, extra));
        newMsg.setExtra(extra);
        int userId = 0;
        newMsg.setUserId(userId);
        newMsg.setHadRead(0);

        MessageHelper helper = MessageHelper.getInstance();
        return helper.insert(newMsg);
    }

    private static String getTypeName(int type, String extra){
        if(type == 0){
            return PushConfig.DEFAULT_ALERT;
        }

        Gson gson = new Gson();
        JpushBean msg = gson.fromJson(extra, JpushBean.class);
        if(msg != null && !TextUtils.isEmpty(msg.Type)){
            return msg.Type;
        }else{
            return PushConfig.DEFAULT_TYPE;
        }
    }
}
