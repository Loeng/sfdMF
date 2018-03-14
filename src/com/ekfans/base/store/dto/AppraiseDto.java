package com.ekfans.base.store.dto;

public class AppraiseDto {
    
   private String id;
   //评价人
   private String appraiseName;
   //产品的名称
   private String productName;
   //产品图片的 url
   private String productImgUrl;
   //产品简称
   private String productSortName;
   //产品描述
   private String productDescription;
   //评价类容
   private String appraiseContent;
   //是否回复
   private boolean replyStatus;
   //回复 评价的人
   private String parentName;
   //回复 评价的id
   private String parentId;
   //回复的内容
   private String replyContent;
   private String createTime;
   
    public String getCreateTime()
{
    return createTime;
}
public void setCreateTime(String createTime)
{
    this.createTime = createTime;
}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAppraiseName() {
        return appraiseName;
    }
    public void setAppraiseName(String appraiseName) {
        this.appraiseName = appraiseName;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductImgUrl() {
        return productImgUrl;
    }
    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }
    public String getProductSortName() {
        return productSortName;
    }
    public void setProductSortName(String productSortName) {
        this.productSortName = productSortName;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    public String getAppraiseContent() {
        return appraiseContent;
    }
    public void setAppraiseContent(String appraiseContent) {
        this.appraiseContent = appraiseContent;
    }
    public boolean isReplyStatus() {
        return replyStatus;
    }
    public void setReplyStatus(boolean replyStatus) {
        this.replyStatus = replyStatus;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getReplyContent() {
        return replyContent;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
       
       
   
}
