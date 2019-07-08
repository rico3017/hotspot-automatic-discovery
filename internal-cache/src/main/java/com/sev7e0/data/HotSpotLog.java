package com.sev7e0.data;


import java.util.List;

/**
 * 日志实体
 */

public class HotSpotLog {

    public static class Builder {

        private String methodName;

        private String className;

        private List<Object> value;

        public Builder() {

        }

        public Builder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder value(List<Object> value) {
            this.value = value;
            return this;
        }

        public HotSpotLog builder() {
            return new HotSpotLog(this);
        }
    }

    public HotSpotLog(Builder builder) {
        this.methodName = builder.methodName;
        this.className = builder.className;
        this.value = builder.value;
    }


    private String methodName;

    private String className;

    private List<Object> value;

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

    public List<Object> getValue() {
        return value;
    }
}
