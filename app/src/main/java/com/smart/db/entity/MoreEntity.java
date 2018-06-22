package com.smart.db.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "entry_info",
        generateConstructors = false
)
public class MoreEntity {

    @Id(autoincrement = true)
    private Long _id;

    /**
     * type
     */
    public int entry_type;

    public int index;

    public int image_id;
    public String title;

    public boolean bottom;

    public MoreEntity() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getEntry_type() {
        return entry_type;
    }

    public void setEntry_type(int entry_type) {
        this.entry_type = entry_type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public boolean getBottom() {
        return this.bottom;
    }
}
