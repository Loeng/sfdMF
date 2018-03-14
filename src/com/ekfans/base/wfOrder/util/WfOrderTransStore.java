package com.ekfans.base.wfOrder.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.base.wfOrder.model.WfOrderTrans;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO临时MODEL，不做数据库操作，仅做展示使用
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class WfOrderTransStore extends BasicBean {
	private String storeId = "";
	private String storeName = "";

	// ------临时数据，不做数据库保存操作
	private List<WfOrderTrans> transList = new ArrayList<WfOrderTrans>();

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public List<WfOrderTrans> getTransList() {
		return transList;
	}

	public void setTransList(List<WfOrderTrans> transList) {
		this.transList = transList;
	}

}
