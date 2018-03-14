package com.ekfans.plugin.webService.bcs;

public class BcsHeaderResponse {
	// 服务方应答码 "00000000"表示成功
	public String ReturnCode = "";
	// 服务提供方渠道号
	public String ProviderChannelId = "";
	// 服务提供方应答时间,格式YYYYMMDDHHmmssSSS
	public String ResponseTime = "";
	// 资金监管系统返回的提示信息或者异常信息
	public String ReturnMessage = "";
	// 服务提供接口流水号
	public String ProviderReference = "";
	// 服务提供方工作日
	public String ProviderWorkingDate = "";

	public String getReturnCode() {
		return ReturnCode;
	}

	public void setReturnCode(String returnCode) {
		ReturnCode = returnCode;
	}

	public String getProviderChannelId() {
		return ProviderChannelId;
	}

	public void setProviderChannelId(String providerChannelId) {
		ProviderChannelId = providerChannelId;
	}

	public String getResponseTime() {
		return ResponseTime;
	}

	public void setResponseTime(String responseTime) {
		ResponseTime = responseTime;
	}

	public String getReturnMessage() {
		return ReturnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		ReturnMessage = returnMessage;
	}

	public String getProviderReference() {
		return ProviderReference;
	}

	public void setProviderReference(String providerReference) {
		ProviderReference = providerReference;
	}

	public String getProviderWorkingDate() {
		return ProviderWorkingDate;
	}

	public void setProviderWorkingDate(String providerWorkingDate) {
		ProviderWorkingDate = providerWorkingDate;
	}

}
