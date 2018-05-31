package com.push.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;


@Entity(
        nameInDb = "message_info",
        generateConstructors = false
)
public class MessagesEntity {
    @Id(autoincrement = true)
    @Property(nameInDb = "_id")
    private Long _id;

    /**
     * 消息ID
     */
    @Property(nameInDb = "mess_id")
    public String mess_id;

    /**
     * 类型
     */
    @Property(nameInDb = "type")
    public int type;

    /**
     * 类型名
     */
    @Property(nameInDb = "typename")
    public String typeName;
    /**
     * 主题
     */
    @Property(nameInDb = "title")
    public String title;
    /**
     * 内容
     */
    @Property(nameInDb = "content")
    public String content;
    /**
     * 达到时间
     */
    @Property(nameInDb = "intime")
    public String inTime;
    /**
     * 用户ID
     */
    @Property(nameInDb = "userid")
    public int userId;

    /**
     * 自定义JSON
     */
    public String extra;

    /**
     * 消息ID
     */
    @Property(nameInDb = "hadread")
    public int hadRead;

    public MessagesEntity() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHadRead() {
        return hadRead;
    }

    public void setHadRead(int hadRead) {
        this.hadRead = hadRead;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
