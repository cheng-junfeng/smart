package com.smart.db.control;

import android.content.Context;

import com.smart.db.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

public class GreenDaoOpenHelper extends DaoMaster.DevOpenHelper {

    public GreenDaoOpenHelper(Context context, String name) {
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
