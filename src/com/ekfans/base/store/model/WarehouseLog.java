package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 仓储日志表
 * 
 * @Title: Store.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class WarehouseLog extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 商品ID
	private String productId = "";
	// 数量
	private int number = 0;
	// 出入库类型(true:入库，false:出库)
	private boolean type = false;
	// 出入库时间
	private String createTime = "";
	// 备注
	private String note = "";
	// 操作人
	private String creator = "";
	// 订单id
	private String orderId = "";
	// 凭证
	private String pic = "";
	// 仓库id
    private String wareHouse = "";
    
    public String getWareHouse() {
        return wareHouse;
    }
    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public boolean isType() {
        return type;
    }
    public void setType(boolean type) {
        this.type = type;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
	
	
	
}