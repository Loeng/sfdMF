package com.ekfans.base.system.model;

public class MessageRead implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    // id
    private String id;
    // 消息表id
    private String messageId;
    // 企业或个人
    private String customerId;
    // 状态【0:未读,1:已度】
    private Boolean status = false;


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}