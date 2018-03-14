package com.ekfans.plugin.webService.monitor.vo;

import java.io.Serializable;

/**
 * Hibernate基类
 * 
 * @ClassName: BasicBean
 * @author 成都易科远见科技有限公司
 * @Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company:成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年1月30日 上午11:28:43
 */
public class VoBasicBean implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String orgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
