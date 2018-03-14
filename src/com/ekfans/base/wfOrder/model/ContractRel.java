package com.ekfans.base.wfOrder.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 合同关系表实体类
 * 
 * @author Ekfans_zhangJin
 * @date 2015-1-12 上午10:46:03
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ContractRel extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 危废处置合同ID
	private String contractId = "";
	// 危废运输合同ID
	private String ysContractId = "";
	// 危废运输公司ID
	private String ysStoreId = "";

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId; 
	}

	public String getYsContractId() {
		return ysContractId;
	}

	public void setYsContractId(String ysContractId) {
		this.ysContractId = ysContractId;
	}

	public String getYsStoreId() {
		return ysStoreId;
	}

	public void setYsStoreId(String ysStoreId) {
		this.ysStoreId = ysStoreId;
	}

}
