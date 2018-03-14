package com.ekfans.base.metal.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 金属行情品类-实体类
 * 
 * @ClassName: Inquiry
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class PreciousMetalCategory extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 品类名称
	private String name = "";
	// 创建时间
	private String createTime = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
