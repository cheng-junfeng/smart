package com.smart.db.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.smart.db.entity.DaoMaster;
import com.smart.db.entity.DaoSession;


public class AppDbManager {

    private static AppDbManager instance;

    public static AppDbManager init(Context context) {
        if (instance == null) {
            synchronized (AppDbManager.class) {
                if (instance == null) {
                    instance = new AppDbManager(context);
                }
            }
        }
        return instance;
    }

    public static AppDbManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("please init this in Application");
        }
        return instance;
    }

    private DaoSession daoSession;

    private AppDbManager(Context context) {
        GreenDaoOpenHelper mHelper = new GreenDaoOpenHelper(context, "smart.db");
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