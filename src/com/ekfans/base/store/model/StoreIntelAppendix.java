package com.ekfans.base.store.model;

/**
 * 资质认证附件--实体类
 * 
 * @ClassName: StoreIntelAppendix
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class StoreIntelAppendix implements java.io.Serializable {

	private static final long serialVersionUID = -3707610447988133029L;
	// 主键
	private String id = "";
	// 资质认证ID
	private String storeIntelId = "";
	// 资质证明
	private String file = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreIntelId() {
		return storeIntelId;
	}

	public void setStoreIntelId(String storeIntelId) {
		this.storeIntelId = storeIntelId;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
