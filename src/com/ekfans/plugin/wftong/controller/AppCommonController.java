package com.ekfans.plugin.wftong.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;

import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.wftong.AppServerController;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.JsonObject;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 公共 Controller
 * 
 * @Title: AppCommonController
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年4月26日18:31:42
 * @version 1.0
 */
public class AppCommonController {
	// 响应
	private MyJSONObject jsonResponse;
	// 请求主体
	private MyJSONObject jsonRequest;
	// request
	private HttpServletRequest request;
	// 当前用户对象
	private AppUser appUser;

	private ICarInfoService carInfoService = SpringContextHolder.getBean(ICarInfoService.class);
	private IUserService userService = SpringContextHolder.getBean(IUserService.class);
	private ISupplyBuyService supplyBuyService = SpringContextHolder.getBean(ISupplyBuyService.class);
	private IProductCategoryService productCategoryService = SpringContextHolder.getBean(IProductCategoryService.class);

	/**
	 * 构造
	 * @param jsonRequest 请求主体，用于获取请求参数
	 * @param jsonResponse 响应主体，用于返回结果数据
	 * @param appUser 当前用户
	 * @param request 
	 */
	public AppCommonController(MyJSONObject jsonRequest, MyJSONObject jsonResponse, AppUser appUser, HttpServletRequest request) {
		this.jsonRequest = jsonRequest;
		this.jsonResponse = jsonResponse;
		this.appUser = appUser;
		this.request = request;
	}

	public void privatySet() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "friendPhoneSwitch", "friendValidSwitch", "allPhoneSwitch");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String frP = jsonRequest.getString("friendPhoneSwitch");
		String frV = jsonRequest.getString("friendValidSwitch");
		String allP = jsonRequest.getString("allPhoneSwitch");
		AppUser appUser = userService.getAppUserById(this.appUser.getUserId());
		appUser.setAllPhoneSwitch(Boolean.valueOf(allP));
		appUser.setFriendValidSwitch(Boolean.valueOf(frV));
		appUser.setFriendPhoneSwitch(Boolean.valueOf(frP));
		boolean success = userService.updateAppUser(appUser);
		if (success) {
			// 成功
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	/**
	 * 上传图片
	 */
	public void picUpload() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "doCode", "doId", "stream");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String pathURL = null;

		// 操作类型
		String doCode = jsonRequest.getString("doCode");
		// 所属对象id
		String doId = jsonRequest.getString("doId");
		// 新头像字符串流
		String stream = jsonRequest.getString("stream");

		BasicRequest br = new BasicRequest(request);
		switch (doCode) {
		case "001":
			// 设置图片路径
			String relativePath = FileUtil.baseStringToImage(request, stream, "/customerfiles/avatar/");

			// 根据id查找
			AppUser appUser = userService.getAppUserById(doId);
			String avatar = br.getImage(relativePath);
			appUser.setAvatar(avatar);
			if (!StringUtil.isEmpty(relativePath) && userService.updateAppUser(appUser)) {
				pathURL = avatar;
			}
			break;
		case "002":
			String xszPath = FileUtil.baseStringToImage(request, stream, "/customerfiles/store/" + doId + "/xsz/");
			// 根据id查找
			CarInfo carInfo1 = carInfoService.getCarInfoById(doId);
			carInfo1.setXszFile(xszPath);
			if (!StringUtil.isEmpty(xszPath) && carInfoService.updateBean(carInfo1, request)) {
				pathURL = br.getImage(xszPath);
			}
			break;
		case "003":
			String yszPath = FileUtil.baseStringToImage(request, stream, "/customerfiles/store/" + doId + "/ysz/");
			// 根据id查找
			CarInfo carInfo2 = carInfoService.getCarInfoById(doId);
			carInfo2.setYszFile(yszPath);
			if (!StringUtil.isEmpty(yszPath) && carInfoService.updateBean(carInfo2, request)) {
				pathURL = br.getImage(yszPath);
			}
			break;

		default:
			break;
		}

		// 绑定数据
		if (!StringUtil.isEmpty(pathURL)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("pathURL", pathURL);
		}
	}

	public void getChildSysArea() throws Exception {
		String areaId = jsonRequest.getString("areaId");

		ISystemAreaService areaService = SpringContextHolder.getBean(ISystemAreaService.class);
		// 定义返回数据
		List<Map<String, String>> alist = new LinkedList<Map<String, String>>();

		List<SystemArea> areaList = areaService.getSystemAreaList(areaId);
		if (areaList != null && areaList.size() > 0) {
			for (SystemArea area : areaList) {
				if (area != null) {
					Map<String, String> aMap = new HashMap<String, String>();
					aMap.put("areaId", area.getId());
					aMap.put("name", area.getAreaName());
					alist.add(aMap);
				}
			}
		}

		jsonResponse.put("details", alist);
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
	}

	public void checkUpdate() throws Exception {
		// 传来的数据
		String version = jsonRequest.getString("version");

		String systemVersion = Cache.getSystemParamConfig("版本号");
		// 判断是否需要更新
		if (!systemVersion.equals(version)) {
			String desc = Cache.getSystemParamConfig("更新说明");
			String url = Cache.getSystemParamConfig("下载地址");
			jsonResponse.put("desc", desc);
			jsonResponse.put("url", url);
			jsonResponse.put("isUpdate", "1");
		} else {
			jsonResponse.put("isUpdate", "0");
		}

		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
	}

}
