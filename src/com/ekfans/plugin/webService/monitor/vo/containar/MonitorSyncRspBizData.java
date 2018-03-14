package com.ekfans.plugin.webService.monitor.vo.containar;

import java.io.Serializable;

import com.ekfans.plugin.webService.monitor.util.MonitorSyncConst;

/**
 * 监控平台 -> 三分地平台,业务响应主体
 * @author 成都易科远见有限公司
 *
 */
public class MonitorSyncRspBizData implements Serializable {
	private static final long serialVersionUID = 7404420272873183329L;
	// 返回状态：成功状态,默认false
	private boolean status = MonitorSyncConst.FAILED;
	// 返回消息
	private String msg = "";

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
