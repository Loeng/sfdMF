package com.ekfans.base.system.model;

/**
 * 待发送信息表
 * 
 * @ClassName: Message
 * @Description: TODO
 * @author ekLiuQuan
 * @date 2014-8-8 上午10:24:29
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Message implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    // id
    private String id;
    // 手机号
    private String mobileNo;
    // 邮箱地址
    private String mailAddress;
    // 消息发送类型【0:短信,1:邮件,2:站内信】
    private Integer sendType;
    // 消息标题
    private String messageTitle;
    // 消息正文
    private String messageContent;
    // 站内信接受范围【0:个人,1:企业,2:所有】
    private Integer messageRange;
    // 状态【true:已发送,false:未发送】
    private Boolean status = false;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
    // 发送时间
    private String issueTime;
    
    //============= 临时数据 ============= 
    //是否已读
    private Boolean readStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getMessageRange() {
        return messageRange;
    }

    public void setMessageRange(Integer messageRange) {
        this.messageRange = messageRange;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }
}
