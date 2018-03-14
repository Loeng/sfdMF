package com.ekfans.base.metal.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 金属行情品目--实体类
 * 
 * @ClassName: Inquiry
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class PreciousMetal extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 品类ID
	private String categoryId = "";
	// 品名
	private String name = "";
	// 规格
	private String spec = "";
	// 计量单位
	private String unit = "";
	// 创建时间
	private String createTime = "";
	//上海有色网金属ID
	private String smmId = "";

	/* 临时数据 */
	private PreciousMetalDetail detail = null;
	private double riseAndDrop = 0; // 跌涨价格
	private String runUpPer = ""; // 跌涨百分比

	public double getRiseAndDrop() {
		return riseAndDrop;
	}

	public void setRiseAndDrop(double riseAndDrop) {
		this.riseAndDrop = riseAndDrop;
	}

	public String getRunUpPer() {
		return runUpPer;
	}

	public void setRunUpPer(String runUpPer) {
		this.runUpPer = runUpPer;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public PreciousMetalDetail getDetail() {
		return detail;
	}

	public void setDetail(PreciousMetalDetail detail) {
		this.detail = detail;
	}

	public String getSmmId() {
		return smmId;
	}

	public void setSmmId(String smmId) {
		this.smmId = smmId;
	}
}
