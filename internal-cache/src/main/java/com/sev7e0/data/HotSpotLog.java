package com.sev7e0.data;


import java.io.Serializable;

/**
 * 日志实体
 */
public class HotSpotLog implements Serializable {

    public static class Builder {

        private String dataTime;

        private String method;

        private String className;

        private String value;

        public Builder() {

        }

        public Builder dataTime(String dataTime) {
            this.dataTime = dataTime;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public HotSpotLog builder() {
            return new HotSpotLog(this);
        }
    }

    public HotSpotLog(Builder builder) {
        this.dataTime = builder.dataTime;
        this.method = builder.method;
        this.className = builder.className;
        this.value = builder.value;
    }

    private String dataTime;

    private String method;

    private String className;

    private String value;

    @Override
    public String toString() {
        return "HotSpotLog{" +
                "dataTime='" + dataTime + '\'' +
                ", method='" + method + '\'' +
                ", className='" + className + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
