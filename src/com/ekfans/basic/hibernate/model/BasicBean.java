package com.ekfans.basic.hibernate.model;

import java.io.Serializable;

/**
 * Hibernate基类
 * 
 * @ClassName: BasicBean
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class BasicBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
