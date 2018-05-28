package com.user.db.control;

import android.content.Context;

import com.user.db.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

public class UserGreenDaoOpenHelper extends DaoMaster.DevOpenHelper {

    public UserGreenDaoOpenHelper(Context context, String name) {
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
