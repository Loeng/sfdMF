package com.ekfans.base.ccwcc.model;

import com.ekfans.basic.hibernate.model.BasicBean;

import javax.persistence.Entity;

/**
 * Created by liuguoyu on 2017/4/19.
 */
@Entity
public class CcwccPush extends BasicBean {
    // 推送标题
    private String title = "";
    // 推送正文
    private String content = "";
    // 推送类型   (0:普通推送，1:资讯推送，2:供应推送，3:求购推送)
    private String type = "";
    // 推送源ID
    private String sourceId = "";
    // 推送源名称
    private String sourceName = ""; 
    // 推送源明细链接（资讯时使用）
    private String linkUrl = "";
    // 推送源分享链接（非普通推送时使用）
    private String sharUrl = "";
    // 状态  (0:取消，1:待推送，2:已推送)
    private String status = "";
    // 创建时间
    private String createTime = "";
    // 推送时间
    private String pushTime = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getSharUrl() {
        return sharUrl;
    }

    public void setSharUrl(String sharUrl) {
        this.sharUrl = sharUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }
}
