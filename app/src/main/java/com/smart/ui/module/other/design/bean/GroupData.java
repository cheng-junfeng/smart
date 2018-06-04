package com.smart.ui.module.other.design.bean;

import java.io.Serializable;
import java.util.List;


public class GroupData implements Serializable {

    public String id;
    public String groupName;
    public List<MemberListInfo> memberList;
    private int isCommon;

    public static class MemberListInfo implements Serializable {

        private String name;
        private String phone;
        private int isCommon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getIsCommon() {
            return isCommon;
        }

        public void setIsCommon(int isCommon) {
            this.isCommon = isCommon;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<MemberListInfo> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberListInfo> memberList) {
        this.memberList = memberList;
    }

    public int getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(int isCommon) {
        this.isCommon = isCommon;
    }
}
