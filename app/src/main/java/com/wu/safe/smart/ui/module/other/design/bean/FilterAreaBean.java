package com.wu.safe.smart.ui.module.other.design.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterAreaBean implements Parcelable {

    private String id;                              // 区域ID
    private String parentId;                       // 父节点
    private String name;                           // 区域名称
    private int areaType;                          // 区域层级关系                1目前代表最底层

    public FilterAreaBean() {
    }

    protected FilterAreaBean(Parcel in) {
        id = in.readString();
        parentId = in.readString();
        areaType = in.readInt();
        name = in.readString();
    }

    public static final Creator<FilterAreaBean> CREATOR = new Creator<FilterAreaBean>() {
        @Override
        public FilterAreaBean createFromParcel(Parcel in) {
            return new FilterAreaBean(in);
        }

        @Override
        public FilterAreaBean[] newArray(int size) {
            return new FilterAreaBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public int getAreaType() {
        return areaType;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(parentId);
        parcel.writeString(name);
        parcel.writeInt(areaType);
    }
}
