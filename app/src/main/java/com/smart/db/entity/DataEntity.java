package com.smart.db.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "data_info",
        generateConstructors = false
)
public class DataEntity {

    @Id(autoincrement = true)
    @Property(nameInDb = "_id")
    private Long _id;

    /**
     * data_id
     */
    @Property(nameInDb = "data_id")
    public long data_id;
    /**
     * URL
     */
    @Property(nameInDb = "data_url")
    public String data_url;
    /**
     * 名称
     */
    public String data_name;
    /**
     * 截图地址
     */
    @Property(nameInDb = "data_path")
    public String data_path;
    /**
     * 最后浏览时间
     */
    public String data_lasttime;
    /**
     * 用户ID
     */
    public String userId;

    public DataEntity() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getData_id() {
        return data_id;
    }

    public void setData_id(long data_id) {
        this.data_id = data_id;
    }

    public String getData_url() {
        return this.data_url;
    }

    public void setData_url(String data_url) {
        this.data_url = data_url;
    }

    public String getData_name() {
        return this.data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }

    public String getData_path() {
        return this.data_path;
    }

    public void setData_path(String data_path) {
        this.data_path = data_path;
    }

    public String getData_lasttime() {
        return this.data_lasttime;
    }

    public void setData_lasttime(String data_lasttime) {
        this.data_lasttime = data_lasttime;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
