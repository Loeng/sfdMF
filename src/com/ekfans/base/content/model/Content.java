package com.ekfans.base.content.model;

import com.ekfans.basic.hibernate.model.BasicBean;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * 资讯实体类
 *
 * @author zgm
 * @version 1.0
 * @Title: Content.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @date 2013-12-23
 */
@Entity
public class Content extends BasicBean {

    // 序列化
    private static final long serialVersionUID = 1L;

    // 资讯名称
    private String contentName = "";

    // 创建时间
    private String createTime = "";

    // 店铺ID
    private String storeId = "";

    // 审核状态
    private boolean checkStatus = false;

    // 审核时间
    private String checkTime = "";

    // 审核说明
    private String checkNote = "";

    // 排序位置
    private int position = 0;

    // 导航文字
    private String navigationText = "";

    // 导航图片
    private String navigationImage = "";

    // 更新时间
    private String updateTime = "";

    // 状态
    private boolean status = false;

    //keywords
    private String keyWords = "";

    //descprion
    private String description = "";

    // 作者
    private String author = "";

    private String appPic1 = "";

    private String appPic2 = "";

    private String appPic3 = "";

    private String contentType = "0";

    private String contentLabel = "";

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    //虚拟数据ContentCategoryRel
    // 内容ID -- 对应资讯表(Content)主键
    private String contentId = "";

    // 分类ID
    private String categoryId = "";


    //虚拟数据ContentCategory
    // 全路径
    private String fullPath = "";
    // 父分类ID
    private String parentId = "";
    // 链接路径 - 临时字段，不需要保存到数据库
    private String linkUrl = "";
    // 关联频道Id
    private String ids;

    //虚拟数据父分类名字集合
    private List<String> ParentName = new ArrayList<String>();
    // 关联频道名称
    private String channelNames = "";

    public List<String> getParentName() {
        return ParentName;
    }

    public void setParentName(List<String> parentName) {
        ParentName = parentName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckNote() {
        return checkNote;
    }

    public void setCheckNote(String checkNote) {
        this.checkNote = checkNote;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNavigationText() {
        return navigationText;
    }

    public void setNavigationText(String navigationText) {
        this.navigationText = navigationText;
    }

    public String getNavigationImage() {
        return navigationImage;
    }

    public void setNavigationImage(String navigationImage) {
        this.navigationImage = navigationImage;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getChannelNames() {
        return channelNames;
    }

    public void setChannelNames(String channelNames) {
        this.channelNames = channelNames;
    }

    public String getAppPic1() {
        return appPic1;
    }

    public void setAppPic1(String appPic1) {
        this.appPic1 = appPic1;
    }

    public String getAppPic2() {
        return appPic2;
    }

    public void setAppPic2(String appPic2) {
        this.appPic2 = appPic2;
    }

    public String getAppPic3() {
        return appPic3;
    }

    public void setAppPic3(String appPic3) {
        this.appPic3 = appPic3;
    }


    public String getContentLabel() {
        return contentLabel;
    }

    public void setContentLabel(String contentLabel) {
        this.contentLabel = contentLabel;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}