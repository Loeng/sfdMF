package com.ekfans.plugin.webService.monitor.vo.containar;

/**
 * 三分地平台 -> 监控平台,业务请求头信息
 * @author 成都易科远见有限公司
 *
 */
public class MonitorSyncReqHeader {
	protected String orgId; // 获取orgId
	protected String key; // 用户密钥

	public MonitorSyncReqHeader(String orgId, String key) {
		this.orgId = orgId;
		this.key = key;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
