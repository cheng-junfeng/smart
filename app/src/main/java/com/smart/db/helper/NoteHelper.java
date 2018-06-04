package com.smart.db.helper;

import com.smart.db.control.BaseDao;
import com.smart.db.entity.NoteEntity;
import com.smart.db.entity.NoteEntityDao;

import java.util.List;

public class NoteHelper extends BaseDao<NoteEntity> {
    private volatile static NoteHelper instance;

    private NoteHelper() {
        super();
    }

    public static NoteHelper getInstance() {
        if (instance == null) {
            synchronized (NoteHelper.class) {
                if (instance == null) {
                    instance = new NoteHelper();
                }
            }
        }
        return instance;
    }

    public boolean insert(NoteEntity message) {
        try {
            daoSession.insert(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(NoteEntity message) {
        super.deleteObject(message);
    }

    public boolean update(NoteEntity user) {
        try {
            daoSession.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<NoteEntity> queryList() {
        NoteEntityDao messDao = daoSession.getNoteEntityDao();
        return messDao.queryBuilder()
                .orderDesc(NoteEntityDao.Properties.Note_lasttime)
                .list();
    }
}
