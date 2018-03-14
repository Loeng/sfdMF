package com.ekfans.plugin.webService.monitor;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

import com.ekfans.pub.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.ekfans.base.wfOrder.dao.IWfOrderTransDao;

public class test {
	@Autowired
	private static IWfOrderTransDao dao;

	public static void main(String[] args) {
//		try {
//			ResourceBundle merConfigInfo = ResourceBundle.getBundle("merchantCfg", Locale.getDefault());
//		} catch (Exception var2) {
//			var2.printStackTrace();
//			System.out.println("******client  util: 未找到商户配置文件  ******");
//			return;
//		}
//		String orderId = "201601180000001";
		String dateString = DateUtil.getSysDateString();
		dateString = dateString.replaceAll("-","");
		dateString = dateString.substring(2,dateString.length());
		dateString = dateString.substring(0,6);
		String bankOrderId = "w" + dateString + "001";
//		bankOrderId = bankOrderId.substring(0,6) + bankOrderId.substring(bankOrderId.length()-4,bankOrderId.length());
		System.out.println(bankOrderId);

	}
}
