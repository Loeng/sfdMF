package com.ekfans.base.store.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 
* @ClassName: StoreConsult
* @Description: TODO(店铺中心  咨询管理的实体类)
* @author 成都易科远见科技有限公司
* @date 2014-5-4 上午09:41:33
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Entity
public class Consult extends BasicBean {
    
    // 序列化
    private static final long serialVersionUID = 1L;
    
    // 创建者  userId
    private String creator = "";
    
    // 需要回答的问题和回复
    private String questionAnswer = "";
    
    // 是否显示 0,显示;1,显示
    private String status = "";
    
    //是否　回复
    private boolean replyStatus = false;
    
    // 问题的类型 0:商品咨询  1:库存及配送 2:支付问题 3:发票及保修 4:促销及赠品
    private String type = "";
    
    // 咨询、留言   0:咨询  1:留言
    private String consultType = "";
    
    // 商品id
    private String productId = "";
    
    // 商店id
    private String storeId = "";
    
    // 父id
    private String parentId = "";
    
    // 创建时间
    private String createTime = "";
    
    // 审核人
    private String checkMan = "";
    
    // 审核时间
    private String checkTime = "";
    
    // 审核状态
    private int checkStatus = 0; 
    
    // 审核说明
    private String checkNote = "";
    
    // ======================== 虚拟数据 ===========================
       // 回复该问题的子集,默认情况只有一条记录
    private List<Consult> childList = new ArrayList<Consult>(); 
       // 操作者
    private String creatorName = "";
      // 商品名称
    private String productName = "";
      // 店铺名称
    private String storeName = "";
      // 审核人名字
    private String checkManName = "";
      // 商品的原图
    private String productPicture = "";
    // 回复内容
    private String replyContent = "";
    
	public String getReplyContent() {
        return replyContent;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
    public List<Consult> getChildList()
    {
        return childList;
    }
    public void setChildList(List<Consult> childList)
    {
        this.childList = childList;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getQuestionAnswer() {
        return questionAnswer;
    }
    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getConsultType() {
        return consultType;
    }
    public void setConsultType(String consultType) {
        this.consultType = consultType;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public boolean isReplyStatus() {
        return replyStatus;
    }
    public void setReplyStatus(boolean replyStatus) {
        this.replyStatus = replyStatus;
    }
    public String getStoreName()
    {
        return storeName;
    }
    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }
    public String getCreatorName() {
        return creatorName;
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    public String getCheckMan() {
        return checkMan;
    }
    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }
    public String getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
    public int getCheckStatus() {
        return checkStatus;
    }
    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }
    public String getCheckNote() {
        return checkNote;
    }
    public void setCheckNote(String checkNote) {
        this.checkNote = checkNote;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getCheckManName() {
        return checkManName;
    }
    public void setCheckManName(String checkManName) {
        this.checkManName = checkManName;
    }
    public String getProductPicture() {
        return productPicture;
    }
    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }
	@Override
	public String toString() {
		return "Consult [creator=" + creator + ", questionAnswer="
				+ questionAnswer + ", status=" + status + ", replyStatus="
				+ replyStatus + ", type=" + type + ", consultType="
				+ consultType + ", productId=" + productId + ", storeId="
				+ storeId + ", parentId=" + parentId + ", createTime="
				+ createTime + ", checkMan=" + checkMan + ", checkTime="
				+ checkTime + ", checkStatus=" + checkStatus + ", checkNote="
				+ checkNote + ", childList=" + childList + ", creatorName="
				+ creatorName + ", productName=" + productName + ", storeName="
				+ storeName + ", checkManName=" + checkManName
				+ ", productPicture=" + productPicture + "]";
	}
    
}
