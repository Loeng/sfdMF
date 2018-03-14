package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 货物类别表
 * 
 * @ClassName: Store
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
public class CategoryGoods  extends BasicBean {
	// 名称
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}