package com.wu.safe.push.app.event;

public class MsgEvent {
    private final MsgType type;
    private String title;
    private String content;

    private MsgEvent(Builder builder) {
        this.type = builder.type;
        this.title = builder.title;
        this.content = builder.content;
    }

    public MsgType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public static class Builder {
        private final MsgType type;
        private String title;
        private String content;

        public Builder(MsgType mType) {
            this.type = mType;
        }

        public Builder title(String mTitle) {
            this.title = mTitle;
            return this;
        }

        public Builder content(String mContent) {
            this.content = mContent;
            return this;
        }

        public MsgEvent build() {
            return new MsgEvent(this);
        }
    }
}
