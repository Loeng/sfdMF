package com.ekfans.base.store.model;

/**
 * 企业近期财务数据--实体类
 * 
 * @ClassName: StoreFinancialData
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class StoreFinancialData implements java.io.Serializable {

	private static final long serialVersionUID = 8331239913444048098L;
	// 主键
	private String id;
	// 企业ID
	private String storeId;
	// 时间
	private String dataTime;
	// 数据类型
	private String dataType;
	// 金额（单位：万元）
	private Double money;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getDataTime() {
		return this.dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

}