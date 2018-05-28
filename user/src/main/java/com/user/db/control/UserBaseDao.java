package com.user.db.control;



import com.user.db.entity.DaoSession;

import java.util.List;

public class UserBaseDao<T> {

    public DaoSession daoSession;

    public UserBaseDao() {
        daoSession = UserDbManager.getInstance().getDaoSession();
    }

    public boolean insertObject(T object) {
        boolean flag = false;
        try {
            flag = daoSession.insert(object) != -1 ? true : false;
        } catch (Exception e) {

        }
        return flag;
    }

    public boolean insertMultObject(final List<T> objects) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            flag = false;
        } finally {
//            manager.CloseDataBase();
        }
        return flag;
    }

    public void updateObject(T object) {

        if (null == object) {
            return;
        }
        try {
            daoSession.update(object);
        } catch (Exception e) {

        }
    }

    /**
     * 批量更新数据
     *
     * @param objects
     * @return
     */
    public void updateMultObject(final List<T> objects, Class clss) {
        if (null == objects || objects.isEmpty()) {
            return;
        }
        try {

            daoSession.getDao(clss).updateInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.update(object);
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public boolean deleteAll(Class clss) {
        boolean flag = false;
        try {
            daoSession.deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 删除某个对象
     *
     * @param object
     * @return
     */
    public void deleteObject(T object) {
        try {
            daoSession.delete(object);
        } catch (Exception e) {

        }
    }

    /**
     * 异步批量删除数据
     *
     * @param objects
     * @return
     */
    public boolean deleteMultObject(final List<T> objects, Class clss) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {

            daoSession.getDao(clss).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.delete(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**************************数据库查询操作***********************/

    /**
     * 获得某个表名
     *
     * @return
     */
    public String getTablename(Class object) {
        return daoSession.getDao(object).getTablename();
    }

    /**
     * 根据主键ID来查询
     *
     * @param id
     * @return
     */
    public T QueryById(long id, Class object) {
        return (T) daoSession.getDao(object).loadByRowId(id);
    }

    /**
     * 查询某条件下的对象
     *
     * @param object
     * @return
     */
    public List<T> QueryObject(Class object, String where, String... params) {
        Object obj = null;
        List<T> objects = null;
        try {
            obj = daoSession.getDao(object);
            if (null == obj) {
                return null;
            }
            objects = daoSession.getDao(object).queryRaw(where, params);
        } catch (Exception e) {

        }

        return objects;
    }

    /**
     * 查询所有对象
     *
     * @param object
     * @return
     */
    public List<T> QueryAll(Class object) {
        List<T> objects = null;
        try {
            objects = (List<T>) daoSession.getDao(object).loadAll();
        } catch (Exception e) {

        }
        return objects;
    }
}
