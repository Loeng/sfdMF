package com.ekfans.base.product.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品扩展字段详情实体类
 * 
 * @Title: ProductInfoDetail.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2013-12-25
 * @version 1.0
 */
public class ProductInfoDetail extends BasicBean {
	
	// 序列化
	private static final long serialVersionUID = 1L;

	// 商品ID
	private String productId = "";

	// 模板明细ID (显示位置为1)
	private String infoId1 = "";

	// 扩展信息名
	private String infoName1 = "";

	// 扩展信息值
	private String infoValue1 = "";

	// 模板值的图片
	private String infoPic1 = "";

	// 模板明细ID (显示位置为2)
	private String infoId2 = "";

	// 扩展信息名
	private String infoName2 = "";

	// 扩展信息值
	private String infoValue2 = "";

	// 模板值的图片
	private String infoPic2 = "";

	// 模板明细ID (显示位置为3)
	private String infoId3 = "";

	// 扩展信息名
	private String infoName3 = "";

	// 扩展信息值
	private String infoValue3 = "";

	// 模板值的图片
	private String infoPic3 = "";

	// 模板明细ID (显示位置为4)
	private String infoId4 = "";

	// 扩展信息名
	private String infoName4 = "";

	// 扩展信息值
	private String infoValue4 = "";

	// 模板值的图片
	private String infoPic4 = "";

	// 扩展信息类型
	private String infoType = "";

	// 库存
	private int quantity = 0;

	// 价格
	private BigDecimal price = new BigDecimal("0.00");

	// 显示位置
	private int position = 0;

	// 库存预警数量
	private int quantityWarning = 0;

	public int getQuantityWarning() {
		return quantityWarning;
	}

	public void setQuantityWarning(int quantityWarning) {
		this.quantityWarning = quantityWarning;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getInfoId1() {
		return infoId1;
	}

	public void setInfoId1(String infoId1) {
		this.infoId1 = infoId1;
	}

	public String getInfoName1() {
		return infoName1;
	}

	public void setInfoName1(String infoName1) {
		this.infoName1 = infoName1;
	}

	public String getInfoValue1() {
		return infoValue1;
	}

	public void setInfoValue1(String infoValue1) {
		this.infoValue1 = infoValue1;
	}

	public String getInfoPic1() {
		return infoPic1;
	}

	public void setInfoPic1(String infoPic1) {
		this.infoPic1 = infoPic1;
	}

	public String getInfoId2() {
		return infoId2;
	}

	public void setInfoId2(String infoId2) {
		this.infoId2 = infoId2;
	}

	public String getInfoName2() {
		return infoName2;
	}

	public void setInfoName2(String infoName2) {
		this.infoName2 = infoName2;
	}

	public String getInfoValue2() {
		return infoValue2;
	}

	public void setInfoValue2(String infoValue2) {
		this.infoValue2 = infoValue2;
	}

	public String getInfoPic2() {
		return infoPic2;
	}

	public void setInfoPic2(String infoPic2) {
		this.infoPic2 = infoPic2;
	}

	public String getInfoId3() {
		return infoId3;
	}

	public void setInfoId3(String infoId3) {
		this.infoId3 = infoId3;
	}

	public String getInfoName3() {
		return infoName3;
	}

	public void setInfoName3(String infoName3) {
		this.infoName3 = infoName3;
	}

	public String getInfoValue3() {
		return infoValue3;
	}

	public void setInfoValue3(String infoValue3) {
		this.infoValue3 = infoValue3;
	}

	public String getInfoPic3() {
		return infoPic3;
	}

	public void setInfoPic3(String infoPic3) {
		this.infoPic3 = infoPic3;
	}

	public String getInfoId4() {
		return infoId4;
	}

	public void setInfoId4(String infoId4) {
		this.infoId4 = infoId4;
	}

	public String getInfoName4() {
		return infoName4;
	}

	public void setInfoName4(String infoName4) {
		this.infoName4 = infoName4;
	}

	public String getInfoValue4() {
		return infoValue4;
	}

	public void setInfoValue4(String infoValue4) {
		this.infoValue4 = infoValue4;
	}

	public String getInfoPic4() {
		return infoPic4;
	}

	public void setInfoPic4(String infoPic4) {
		this.infoPic4 = infoPic4;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
