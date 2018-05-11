package com.wu.safe.smart.ui.module.main.home.bean;

public class HomeListBean {
    private String content;
    private HomeListBean(Builder builder){
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

        public HomeListBean build(){
            return new HomeListBean(this);
        }
    }
}
