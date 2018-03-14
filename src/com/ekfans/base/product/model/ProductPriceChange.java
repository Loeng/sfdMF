package com.ekfans.base.product.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品价格修改记录
 * 
 * @ClassName: ProductPriceChange
 * @Description: 
 * @author zgm
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ProductPriceChange extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 商品id
	private String productId = "";
	//商品价格
	private BigDecimal price=new BigDecimal("0.00");
	//创建时间
	private String createTime="";
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}