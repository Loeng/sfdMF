package com.ekfans.plugin.webService.bcs;

import java.util.Map;
import java.util.Map.Entry;

import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;

/**
 * 长沙银行资金监管XML帮助类
 * 
 * @author liuguoyu
 * 
 */
public class BcsXmlHelper {
	/**
	 * 将Map转成XML格式
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToXml(String methodName, String mId, Map<String, Object> map, AccountBankLog bankLog) {
		try {
			StringBuffer xmlStr = new StringBuffer("");
			xmlStr.append("<Service>");
			xmlStr.append("<Header>");
			// 资金监管系统对每一个标准服务都有一个唯一的服务编码，外围系统在访问时需要提供
			xmlStr.append("<ServiceCode>").append(methodName).append("</ServiceCode>");
			// 指接入系统的渠道号，渠道号由银行统一维护
			xmlStr.append("<ChannelId>").append(Cache.getBCSResource("ChannelId")).append("</ChannelId>");
			// 渠道流水号，由渠道号代表的系统来保证在该系统内的唯一性，用来帮助服务提供者堵重
			xmlStr.append("<ExternalReference>").append(mId).append("</ExternalReference>");
			// 原渠道号
			xmlStr.append("<OriginalChannelId>").append(Cache.getBCSResource("OriginalChannelId")).append("</OriginalChannelId>");
			// 原渠道流水号
			xmlStr.append("<OriginalReference>").append(Cache.getBCSResource("OriginalReference")).append("</OriginalReference>");
			// 指接入系统调用本服务时的机器时间，格式为YYYYMMDDHHmmssSSS。
			xmlStr.append("<RequestTime>").append(DateUtil.getFullSysDateTimeString()).append("</RequestTime>");
			// 指该笔业务的发生日期，若无交易日期信息，则填入请求日期相同的值。YYYYMMDD
			xmlStr.append("<TradeDate>").append(DateUtil.getNoSpSysDateString()).append("</TradeDate>");
			// 用来表示当前服务使用的报文头版本，版本由资金监管系统发布。若无特殊要求，默认填写1.0
			xmlStr.append("<Version>").append(Cache.getBCSResource("Version")).append("</Version>");
			// 指接入系统调用本服务时使用的机构代号
			xmlStr.append("<RequestBranchCode>").append(Cache.getBCSResource("RequestBranchCode")).append("</RequestBranchCode>");
			// 指接入系统调用本服务时使用的服务提供方柜员代号，根据业务场景填写，可能是实际柜员代号，也可能是虚拟柜员代号
			xmlStr.append("<RequestOperatorId>").append(Cache.getBCSResource("RequestBranchCode")).append("</RequestOperatorId>");
			// 指请求柜员的类型，0-实柜员，1-虚柜员。
			xmlStr.append("<RequestOperatorType>").append(Cache.getBCSResource("RequestOperatorType")).append("</RequestOperatorType>");
			// 柜员或是机具的钱箱号
			xmlStr.append("<BankNoteBoxID>").append(Cache.getBCSResource("BankNoteBoxID")).append("</BankNoteBoxID>");
			// 柜面交易等需要授权的交易，需要上送授权柜员号
			xmlStr.append("<AuthorizerID>").append(Cache.getBCSResource("AuthorizerID")).append("</AuthorizerID>");
			// 指面向客户服务的终端类型，如自助取款机、多媒体查询机等。若交易非通过终端发起，则填写默认值“00000”。
			xmlStr.append("<TermType>").append(Cache.getBCSResource("TermType")).append("</TermType>");
			// 指面向客户服务终端的编号，需要保证终端类型+终端号为全行唯一。若交易非通过终端发起，则填写默认值“0000000000”。
			xmlStr.append("<TermNo>").append(Cache.getBCSResource("TermNo")).append("</TermNo>");
			// 取值：0：正常 1：测试 2：重发
			xmlStr.append("<RequestType>").append(Cache.getBCSResource("RequestType")).append("</RequestType>");
			// 0:明文 1:密文
			xmlStr.append("<Encrypt>").append(Cache.getBCSResource("Encrypt")).append("</Encrypt>");

			StringBuffer bodyStr = new StringBuffer("<Body><Request>");
			if (map != null && map.size() > 0) {
				for (Entry<String, Object> entry : map.entrySet()) {
					bodyStr.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
				}
			}
			bodyStr.append("</Request></Body>");

			// 对整个Body报文体做签名
			xmlStr.append("<SignData>").append("1".equals(Cache.getBCSResource("Encrypt")) ? BcsSignHelper.sign(bodyStr.toString()) : "").append("</SignData>");

			xmlStr.append("</Header>");

			// StringBuffer logStr = new StringBuffer(xmlStr);
			xmlStr.append(bodyStr);
			xmlStr.append("</Service>");

			// logStr.append(bodyStr2);
			// logStr.append("</Service>");
			bankLog.setMessage(xmlStr.toString());
			return xmlStr.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 将Map转成XML格式
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToServiceXml(Map<String, Object> map) {
		StringBuffer xmlStr = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlStr.append("<Service>");
		xmlStr.append("<Header>");
		// 资金监管系统对每一个标准服务都有一个唯一的服务编码，外围系统在访问时需要提供
		xmlStr.append("<ServiceCode>").append(map.get("ServiceCode")).append("</ServiceCode>");
		// 指接入系统的渠道号，渠道号由银行统一维护
		xmlStr.append("<ChannelId>").append(Cache.getBCSResource("ChannelId")).append("</ChannelId>");
		// 渠道流水号，由渠道号代表的系统来保证在该系统内的唯一性，用来帮助服务提供者堵重
		xmlStr.append("<ExternalReference>").append(map.get("ExternalReference")).append("</ExternalReference>");
		// 原渠道号
		xmlStr.append("<OriginalChannelId>").append(Cache.getBCSResource("OriginalChannelId")).append("</OriginalChannelId>");
		// 原渠道流水号
		xmlStr.append("<OriginalReference>").append(Cache.getBCSResource("OriginalReference")).append("</OriginalReference>");
		// 指接入系统调用本服务时的机器时间，格式为YYYYMMDDHHmmssSSS。
		xmlStr.append("<RequestTime>").append(DateUtil.getFullSysDateTimeString()).append("</RequestTime>");
		// 指该笔业务的发生日期，若无交易日期信息，则填入请求日期相同的值。YYYYMMDD
		xmlStr.append("<TradeDate>").append(DateUtil.getNoSpSysDateString()).append("</TradeDate>");
		// 用来表示当前服务使用的报文头版本，版本由资金监管系统发布。若无特殊要求，默认填写1.0
		xmlStr.append("<Version>").append(Cache.getBCSResource("Version")).append("</Version>");
		// 指接入系统调用本服务时使用的机构代号
		xmlStr.append("<RequestBranchCode>").append(Cache.getBCSResource("RequestBranchCode")).append("</RequestBranchCode>");
		// 指接入系统调用本服务时使用的服务提供方柜员代号，根据业务场景填写，可能是实际柜员代号，也可能是虚拟柜员代号
		xmlStr.append("<RequestOperatorId>").append(Cache.getBCSResource("RequestBranchCode")).append("</RequestOperatorId>");
		// 指请求柜员的类型，0-实柜员，1-虚柜员。
		xmlStr.append("<RequestOperatorType>").append(Cache.getBCSResource("RequestOperatorType")).append("</RequestOperatorType>");
		// 柜员或是机具的钱箱号
		xmlStr.append("<BankNoteBoxID>").append(Cache.getBCSResource("BankNoteBoxID")).append("</BankNoteBoxID>");
		// 柜面交易等需要授权的交易，需要上送授权柜员号
		xmlStr.append("<AuthorizerID>").append(Cache.getBCSResource("AuthorizerID")).append("</AuthorizerID>");
		// 指面向客户服务的终端类型，如自助取款机、多媒体查询机等。若交易非通过终端发起，则填写默认值“00000”。
		xmlStr.append("<TermType>").append(Cache.getBCSResource("TermType")).append("</TermType>");
		// 指面向客户服务终端的编号，需要保证终端类型+终端号为全行唯一。若交易非通过终端发起，则填写默认值“0000000000”。
		xmlStr.append("<TermNo>").append(Cache.getBCSResource("TermNo")).append("</TermNo>");
		// 取值：0：正常 1：测试 2：重发
		xmlStr.append("<RequestType>").append(Cache.getBCSResource("RequestType")).append("</RequestType>");
		// 0:明文 1:密文
		xmlStr.append("<Encrypt>").append(Cache.getBCSResource("Encrypt")).append("</Encrypt>");
		xmlStr.append("<ReturnCode>").append(map.get("ReturnCode")).append("</ReturnCode>");
		xmlStr.append("<ReturnMessage>").append(map.get("ReturnMessage")).append("</ReturnMessage>");
		StringBuffer bodyStr = new StringBuffer("<Body><Response>");
		if (map != null && map.size() > 0) {
			for (Entry<String, Object> entry : map.entrySet()) {
				bodyStr.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
			}
		}
		bodyStr.append("</Response></Body>");

		// 对整个Body报文体做签名
		xmlStr.append("<SignData>").append("1".equals(Cache.getBCSResource("Encrypt")) ? BcsSignHelper.sign(bodyStr.toString()) : "").append("</SignData>");

		xmlStr.append("</Header>");
		xmlStr.append(bodyStr);
		xmlStr.append("</Service>");
		return xmlStr.toString();
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.getNoSpSysDateString());
	}
}
