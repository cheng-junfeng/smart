package com.wu.safe.smart.ui.module.other.data.control;

import android.content.Context;
import android.content.res.Resources;

import com.base.utils.LogUtil;
import com.wu.safe.smart.db.entity.DataEntity;
import com.wu.safe.smart.db.helper.DataHelper;
import com.wu.safe.push.db.helper.MessageHelper;
import com.wu.safe.push.db.entity.MessagesEntity;

import java.util.List;
import java.util.Random;

public class DemoManager {
    private final static String TAG = "DemoManager";

    public static void init(Context mContext) {
        DataHelper helper = DataHelper.getInstance();
        List<DataEntity> allEntity = helper.queryList();
        if (allEntity != null && allEntity.size() > 0) {
            LogUtil.d(TAG, "initDemo:" + allEntity.size());
        } else {
            LogUtil.d(TAG, "initDemo");
            Resources res = mContext.getResources();
            long current = System.currentTimeMillis();

            for (int i = 0; i < 100; i++) {
                long id = current + i;
                DataEntity temp = new DataEntity();
                temp.setData_id(id);
                temp.setData_name((i+1)+" "+new Random().nextInt(100));
                temp.setData_lasttime(Long.toString(current - (10 * i)));
                helper.insert(temp);
            }
        }

        MessageHelper msgHelper = MessageHelper.getInstance();
        List<MessagesEntity> allMsgEntity = msgHelper.queryListByUserId(0);
        if (allMsgEntity != null && allMsgEntity.size() > 0) {
            LogUtil.d(TAG, "initMsgDemo:" + allMsgEntity.size());
        } else {
            LogUtil.d(TAG, "initMsgDemo");
            long current = System.currentTimeMillis();

            MessagesEntity temp = new MessagesEntity();
            temp.setMess_id(current + "");
            temp.setTitle("公告");
            temp.setContent("您有新消息，请注意查收");
            temp.setInTime(current + "");
            temp.setHadRead(0);
            msgHelper.insert(temp);
        }
    }
}
