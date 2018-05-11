package com.wu.safe.push.db.control;

import android.content.Context;

import com.wu.safe.push.db.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

public class PushGreenDaoOpenHelper extends DaoMaster.DevOpenHelper {

    public PushGreenDaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

    }
}
