package com.wu.safe.push.db.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wu.safe.push.db.entity.DaoMaster;
import com.wu.safe.push.db.entity.DaoSession;

public class PushDbManager {

    private static PushDbManager instance;

    public static PushDbManager init(Context context) {
        if (instance == null) {
            synchronized (PushDbManager.class) {
                if (instance == null) {
                    instance = new PushDbManager(context);
                }
            }
        }
        return instance;
    }

    public static PushDbManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("please init this in Application");
        }
        return instance;
    }

    private DaoSession daoSession;

    private PushDbManager(Context context) {
        PushGreenDaoOpenHelper mHelper = new PushGreenDaoOpenHelper(context, "msg.db");
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