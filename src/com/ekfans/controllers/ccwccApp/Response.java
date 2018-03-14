package com.ekfans.controllers.ccwccApp;

/**
 * 响应
 * @author lh
 * @since 1.0.0
 */
public class Response {
    private static final String OK = "ok";
    private static final String ERROR = "error";

    private Meta meta;
    private Object data;

    public Response success() {
        Meta meta = new Meta(true, OK);
        this.meta = meta;
        return this;
    }

    public Response success(Object data) {
        Meta meta = new Meta(true, OK);
        this.meta = meta;
        this.data = data;
        return this;
    }

    public Response failure() {
        Meta meta = new Meta(false, ERROR);
        this.meta = meta;
        this.data="";
        return this;
    }

    public Response failure(String message) {
        Meta meta = new Meta(false, message);
        this.meta = meta;
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getData() {
        return data;
    }

    public class Meta {
        private boolean success;
        private String message;

        public Meta(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
