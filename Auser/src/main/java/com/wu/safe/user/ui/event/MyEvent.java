package com.wu.safe.user.ui.event;

public class MyEvent {
    private final MyType type;
    private String userName;
    private String email;

    private MyEvent(Builder builder) {
        this.type = builder.type;
        this.userName = builder.userName;
        this.email = builder.email;
    }

    public MyType getType() {
        return this.type;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public static class Builder {
        private final MyType type;
        private String userName;
        private String email;

        public Builder(MyType mType) {
            this.type = mType;
        }

        public Builder userName(String username) {
            this.userName = username;
            return this;
        }

        public Builder email(String memail) {
            this.email = memail;
            return this;
        }

        public MyEvent build() {
            return new MyEvent(this);
        }
    }
}
