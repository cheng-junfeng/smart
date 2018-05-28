package com.user.db.helper;

import com.user.db.control.UserBaseDao;
import com.user.db.entity.UserEntity;
import com.user.db.entity.UserEntityDao;

import java.util.List;

public class UserHelper extends UserBaseDao<UserEntity> {
    private volatile static UserHelper instance;

    private UserHelper() {
        super();
    }

    public static UserHelper getInstance() {
        if (instance == null) {
            synchronized (UserHelper.class) {
                if (instance == null) {
                    instance = new UserHelper();
                }
            }
        }
        return instance;
    }

    public boolean insert(UserEntity user) {
        if (user == null) {
            return false;
        }

        try {
            daoSession.insert(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(UserEntity user) {
        super.deleteObject(user);
    }

    public void clear() {
        List<UserEntity> allEntity = queryList();
        for (UserEntity entity : allEntity) {
            delete(entity);
        }
    }

    public boolean update(UserEntity user) {
        try {
            daoSession.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserEntity> queryList() {
        UserEntityDao messDao = daoSession.getUserEntityDao();
        return messDao.queryBuilder()
                .orderDesc(UserEntityDao.Properties.User_lasttime).list();
    }

    public UserEntity queryByUserName(String userName) {
        UserEntityDao messDao = daoSession.getUserEntityDao();
        UserEntity user = messDao.queryBuilder().where(UserEntityDao.Properties.User_name.eq(userName)).unique();
        return user;
    }
}
