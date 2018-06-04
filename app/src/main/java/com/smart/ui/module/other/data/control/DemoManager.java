package com.smart.ui.module.other.data.control;

import android.content.Context;
import android.content.res.Resources;

import com.base.utils.LogUtil;
import com.smart.db.entity.DataEntity;
import com.smart.db.entity.NoteEntity;
import com.smart.db.helper.DataHelper;
import com.push.db.helper.MessageHelper;
import com.push.db.entity.MessagesEntity;
import com.smart.db.helper.NoteHelper;

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

        NoteHelper noteHelper = NoteHelper.getInstance();
        List<NoteEntity> allData = noteHelper.queryList();
        if(allData == null || allData.size() == 0){
            NoteEntity entity1 = new NoteEntity();
            entity1.set_id(System.currentTimeMillis());
            entity1.setNote_content("今天天气好");
            entity1.setNote_lasttime("5-1");
            noteHelper.insert(entity1);

            NoteEntity entity2 = new NoteEntity();
            entity2.setNote_content("今天下雨");
            entity2.setNote_lasttime("5-1");
            noteHelper.insert(entity2);

            NoteEntity entity3 = new NoteEntity();
            entity3.setNote_content("今天起风");
            entity3.setNote_lasttime("5-3");
            noteHelper.insert(entity3);

            NoteEntity entity4 = new NoteEntity();
            entity4.setNote_content("今天出太阳");
            entity4.setNote_lasttime("5-4");
            noteHelper.insert(entity4);

            NoteEntity entity5 = new NoteEntity();
            entity5.setNote_content("今天休息");
            entity5.setNote_lasttime("5-4");
            noteHelper.insert(entity5);
        }
    }
}
