package com.wu.safe.user.ui.bean;

public class CountryBean {
    public String letter;
    public String name;
    public String code;
    public int type;//type == 1 表示是标题
    /**
     * 中文名称的拼音，用于存储key
     */
    public String py;
}
