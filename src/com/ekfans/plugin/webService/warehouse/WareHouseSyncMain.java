package com.ekfans.plugin.webService.warehouse;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.ekfans.plugin.webService.monitor.vo.containar.MonitorSyncReqBizData;
import com.ekfans.plugin.webService.monitor.vo.containar.MonitorSyncRspBizData;
import com.ekfans.plugin.webService.warehouse.util.WareHouseSyncConst;
import com.ekfans.pub.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 同步到三分地监控主要类
 * @author 成都易科远见有限公司
 * Client
 *
 */
public class WareHouseSyncMain extends Thread {

	private static final String URL = WareHouseSyncConst.REQ_URL;
	// gson
	private Gson gson = new Gson();
	// 同步数据主体
	private MonitorSyncReqBizData dataMain = null;

	/**
	 * 001 新增/更新企业信息 
	 * 002 删除企业 
	 * 101 新增/更新车辆信息 
	 * 102 删除车辆 
	 * 201 新增/更新车辆人员信息 
	 * 202 删除车辆人员信息 
	 * 301 订单信息数据同步 
	 */
	private String source = null;

	/**
	 * 通过请求数据主体，和请求数据接口号，构造一个请求线程类，用于执行同步请求
	 * @param dataMain 业务请求主体
	 * @param source 接口号
	 */
	public WareHouseSyncMain(MonitorSyncReqBizData dataMain, String source) {
		this.dataMain = dataMain;
		this.source = source;
	}

	@Override
	public void run() {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		String rspStr = "";
		try {
			reqMap.put(WareHouseSyncConst.REQ_SOURCE, this.source);
			// URLEncoder.encode 加密json,防止出现中文乱码,客户端在解密
			reqMap.put(WareHouseSyncConst.REQ_BODY, URLEncoder.encode(gson.toJson(this.dataMain), "utf-8"));
			rspStr = HttpUtil.doPost(URL, reqMap);
		} catch (Exception e) {
			// 向外抛出异常，注意这里是线程异常，要通过特殊的方式捕获，比如通过线程池来捕获
			throw new RuntimeException(e);
		}
		Type type = new TypeToken<MonitorSyncRspBizData>() {
		}.getType();
		MonitorSyncRspBizData rspData = gson.fromJson(rspStr, type);
		// 获取结果，目前暂只做查看
		if (rspData != null && rspData.getStatus()) {
			System.out.println("Sync To WareHouseSys Success：" + source);
		} else {
			System.out.println("Sync To WareHouseSys Failed：" + source + "(" + rspData.getMsg() + ")");
		}
	}

}
