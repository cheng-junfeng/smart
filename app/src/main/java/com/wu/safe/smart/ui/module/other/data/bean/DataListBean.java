package com.wu.safe.smart.ui.module.other.data.bean;

public class DataListBean {
    private final long id;
    private final String content;

    private DataListBean(Builder builder) {
        this.id = builder.id;
        this.content = builder.content;
    }

    public long getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public static class Builder {
        private final long id;
        private String content;

        public Builder(long mId) {
            this.id = mId;
        }

        public Builder content(String cont) {
            this.content = cont;
            return this;
        }

        public DataListBean build() {
            return new DataListBean(this);
        }
    }
}
