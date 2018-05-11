package com.wu.safe.user.ui.event;

public class CountryEvent {
    private String code;
    private String name;

    private CountryEvent(Builder builder) {
        this.code = builder.code;
        this.name = builder.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static class Builder {
        private String code;
        private String name;

        public Builder code(String cod) {
            this.code = cod;
            return this;
        }

        public Builder name(String nam) {
            this.name = nam;
            return this;
        }

        public CountryEvent build() {
            return new CountryEvent(this);
        }
    }
}
