package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 企业资质认证资料关系中间表实体类
 * 
 * @ClassName: CarInfo
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class WfpysRel extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 资质认证资料ID
	private String accreditId = "";
	// 资质明细ID
	private String wfpysId = "";
	// 排序
	private int position = 0;

	public String getAccreditId() {
		return accreditId;
	}

	public void setAccreditId(String accreditId) {
		this.accreditId = accreditId;
	}


	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getWfpysId() {
		return wfpysId;
	}

	public void setWfpysId(String wfpysId) {
		this.wfpysId = wfpysId;
	}
	
}