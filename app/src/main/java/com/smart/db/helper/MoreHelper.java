package com.smart.db.helper;



import com.smart.db.control.BaseDao;
import com.smart.db.entity.MoreEntity;
import com.smart.db.entity.MoreEntityDao;

import java.util.List;

public class MoreHelper extends BaseDao<MoreEntity> {
    private volatile static MoreHelper instance;

    private MoreHelper() {
        super();
    }

    public static MoreHelper getInstance() {
        if (instance == null) {
            synchronized (MoreHelper.class) {
                if (instance == null) {
                    instance = new MoreHelper();
                }
            }
        }
        return instance;
    }

    public boolean insert(MoreEntity message) {
        try {
            daoSession.insert(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(MoreEntity user) {
        try {
            daoSession.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MoreEntity> queryListBottom(boolean isBottom) {
        MoreEntityDao messDao = daoSession.getMoreEntityDao();
        return messDao.queryBuilder()
                .where(MoreEntityDao.Properties.Bottom.eq(isBottom))
                .orderAsc(MoreEntityDao.Properties.Index).list();
    }

    public List<MoreEntity> queryList() {
        MoreEntityDao messDao = daoSession.getMoreEntityDao();
        return messDao.queryBuilder()
                .orderAsc(MoreEntityDao.Properties.Index).list();
    }
}
