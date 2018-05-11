package com.baidu.track.model;


import com.baidu.track.activity.BmapBaseCompatActivity;

public class ItemInfo {
    public int titleIconId;
    public int titleId;
    public int descId;
    public Class<? extends BmapBaseCompatActivity> clazz;

    public ItemInfo(int titleIconId, int titleId, int descId, Class<? extends BmapBaseCompatActivity> clazz) {
        this.titleIconId = titleIconId;
        this.titleId = titleId;
        this.descId = descId;
        this.clazz = clazz;
    }
}
