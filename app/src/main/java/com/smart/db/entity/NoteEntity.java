package com.smart.db.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "note_info",
        generateConstructors = false
)
public class NoteEntity {

    @Id(autoincrement = true)
    @Property(nameInDb = "_id")
    private Long _id;
    /**
     * data_id
     */
    @Property(nameInDb = "note_id")
    public long note_id;

    /**
     * 内容
     */
    public String note_content;

    /**
     * 时间
     */
    public String note_lasttime;
    /**
     * 用户ID
     */
    public String userId;

    public NoteEntity() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(long note_id) {
        this.note_id = note_id;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }

    public String getNote_lasttime() {
        return note_lasttime;
    }

    public void setNote_lasttime(String note_lasttime) {
        this.note_lasttime = note_lasttime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
