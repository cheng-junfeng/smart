package com.push.db.helper;

import com.push.db.control.PushBaseDao;
import com.push.db.entity.MessagesEntity;
import com.push.db.entity.MessagesEntityDao;


import java.util.List;

public class MessageHelper extends PushBaseDao<MessagesEntity> {
    private volatile static MessageHelper instance;

    private MessageHelper() {
        super();
    }

    public static MessageHelper getInstance() {
        if (instance == null) {
            synchronized (MessageHelper.class) {
                if (instance == null) {
                    instance = new MessageHelper();
                }
            }
        }
        return instance;
    }

    public boolean insert(MessagesEntity message) {
        MessagesEntity oldEntity = queryListByMessId(message.mess_id);
        if(oldEntity != null){
            return false;
        }
        try {
            daoSession.insert(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(MessagesEntity message) {
        try {
            daoSession.delete(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(List<MessagesEntity> messages) {
        for (MessagesEntity entity : messages) {
            delete(entity);
        }
    }

    public void clear(int userId) {
        List<MessagesEntity> listEntity = queryListByUserId(userId);
        delete(listEntity);
    }

    public boolean update(MessagesEntity user) {
        try {
            daoSession.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MessagesEntity> queryListByUserId(int userId) {
        MessagesEntityDao messDao = daoSession.getMessagesEntityDao();
        List<MessagesEntity> userList = messDao.queryBuilder().where(MessagesEntityDao.Properties.UserId.eq(userId))
                .orderDesc(MessagesEntityDao.Properties.InTime).list();
        if (userList == null || userList.size() == 0) {
            return null;
        } else {
            return userList;
        }
    }

    public List<MessagesEntity> queryListByTypeName(String typeName, int userId) {
        MessagesEntityDao messDao = daoSession.getMessagesEntityDao();
        List<MessagesEntity> userList = messDao.queryBuilder().where(MessagesEntityDao.Properties.TypeName.eq(typeName))
                .where(MessagesEntityDao.Properties.UserId.eq(userId))
                .orderDesc(MessagesEntityDao.Properties.InTime).list();
        if (userList == null || userList.size() == 0) {
            return null;
        } else {
            return userList;
        }
    }

    public MessagesEntity queryListByMessId(String messId) {
        MessagesEntityDao messDao = daoSession.getMessagesEntityDao();
        MessagesEntity mess = messDao.queryBuilder().where(MessagesEntityDao.Properties.Mess_id.eq(messId)).unique();
        if (mess == null) {
            return null;
        } else {
            return mess;
        }
    }
}
