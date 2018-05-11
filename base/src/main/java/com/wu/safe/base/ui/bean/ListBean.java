package com.wu.safe.base.ui.bean;

public class ListBean {
    private String content;
    private ListBean(Builder builder){
        this.content = builder.content;
    }
    public String getContent(){
        return this.content;
    }

    public static class Builder{
        String content;
        public Builder(){}

        public Builder content(String cont){
            this.content = cont;
            return this;
        }

        public ListBean build(){
            return new ListBean(this);
        }
    }
}
