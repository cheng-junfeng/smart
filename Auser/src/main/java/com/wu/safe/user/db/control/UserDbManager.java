package com.wu.safe.user.db.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wu.safe.user.db.entity.DaoMaster;
import com.wu.safe.user.db.entity.DaoSession;


public class UserDbManager {

    private static UserDbManager instance;

    public static UserDbManager init(Context context) {
        if (instance == null) {
            synchronized (UserDbManager.class) {
                if (instance == null) {
                    instance = new UserDbManager(context);
                }
            }
        }
        return instance;
    }

    public static UserDbManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("please init this in Application");
        }
        return instance;
    }

    private DaoSession daoSession;

    private UserDbManager(Context context) {
        UserGreenDaoOpenHelper mHelper = new UserGreenDaoOpenHelper(context, "user.db");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        if (daoSession != null) {
            daoSession.clear();
        }
    }
}