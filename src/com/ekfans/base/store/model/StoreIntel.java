package com.ekfans.base.store.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 资质认证--实体类
 * 
 * @ClassName: StoreIntel
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class StoreIntel implements java.io.Serializable {

	private static final long serialVersionUID = -3707610447988133029L;
	// 主键
	private String id = "";
	// 企业ID
	private String storeId = "";
	// 资质类型（0:处置资质,1:产生资质,2:流通资质）
	private String type = "0";
	// 资质ID
	private String intelId = "";
	// 状态（0:不启用,1:启用)
	private Boolean status = false;
	// 审核状态（0:待审核,1:审核通过,2:审核不通过）
	private String checkStatus = "0";
	// 审核时间
	private String checkTime = "";
	// 审核人
	private String checkMan = "";
	// 创建时间
	private String createTime = "";
	// 修改时间
	private String updateTime = "";
	// 备注
	private String note = "";
	// 组织结构
	private String orgId = "";
	
	// ==================  临时字段  ==================
	private String intelName = ""; // 资质名称
	private List<StoreIntelAppendix> sialist = new ArrayList<StoreIntelAppendix>(); // 资质对应的文件集合

	
	// ==================  geter seter  ==================
	public String getId() {
		return id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIntelId() {
		return intelId;
	}

	public void setIntelId(String intelId) {
		this.intelId = intelId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getIntelName() {
		return intelName;
	}

	public void setIntelName(String intelName) {
		this.intelName = intelName;
	}

	public List<StoreIntelAppendix> getSialist() {
		return sialist;
	}

	public void setSialist(List<StoreIntelAppendix> sialist) {
		this.sialist = sialist;
	}

}
