package com.ekfans.plugin.wftong.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.IAppCollectService;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.wftong.AppServerController;
import com.ekfans.plugin.wftong.base.friend.service.IFriendRelationService;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonObject;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.google.gson.JsonArray;

/**
 * 供需 Controller
 * 
 * @Title: AppSupplyController
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年4月20日17:17:56
 * @version 1.0
 */
public class AppSupplyController {
	// 响应
	private MyJSONObject jsonResponse;
	// 请求主体
	private MyJSONObject jsonRequest;
	// request
	private HttpServletRequest request;
	// 当前用户对象
	private AppUser appUser;

	private IUserService userService = SpringContextHolder.getBean(IUserService.class);
	private ISupplyBuyService supplyBuyService = SpringContextHolder.getBean(ISupplyBuyService.class);
	private IProductCategoryService productCategoryService = SpringContextHolder.getBean(IProductCategoryService.class);
	private IFriendRelationService friendService = SpringContextHolder.getBean(IFriendRelationService.class);

	/**

	 * 构造
	 * @param jsonRequest 请求主体，用于获取请求参数
	 * @param jsonResponse 响应主体，用于返回结果数据
	 * @param appUser 当前用户
	 * @param request 
	 */
	public AppSupplyController(MyJSONObject jsonRequest, MyJSONObject jsonResponse, AppUser appUser, HttpServletRequest request) {
		this.jsonRequest = jsonRequest;
		this.jsonResponse = jsonResponse;
		this.appUser = appUser;
		this.request = request;
	}

	/**
	 * 查看供应
	 */
	public void getGyList() throws Exception {
		getListCommon("0", null);
	}

	/**
	 * 发布供应信息
	 */
	public void addGy() throws Exception {
		addSupplyCommon("0");
	}

	/**
	 * 我的供应
	 */
	public void getMyGyList() throws Exception {
		// 当前用户企业id
		String storeId = this.appUser.getStoreId();
		getListCommon("0", storeId);
	}

	/**
	 * 供应详情
	 */
	public void getGyDetails() throws Exception {
		getDetailsCommon();
	}

	private void getDetailsCommon() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "supplyId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();

		String supplyId = jsonRequest.getString("supplyId");

		SupplyBuy supplyBuy = supplyBuyService.getSupplyBuyById(supplyId);

		// 查询并附加字段：收藏信息
		if (supplyBuy != null) {
			String infoId = supplyBuy.getStoreId();
			supplyBuy.setFriendStatus(friendService.getFriendStatus(userId, infoId));
			AppUser appUser = userService.getAppUserById(infoId);
			if (appUser != null) {
				supplyBuy.setHxUserName(appUser.getHxUserName());
			}

			// 分类名称
			String cId = supplyBuy.getCategoryId();
			if (!StringUtil.isEmpty(cId)) {
				ProductCategory productCategory = productCategoryService.getCategoryById(supplyBuy.getCategoryId());
				if (productCategory != null) {
					supplyBuy.setCategoryName(productCategory.getName());
				}
			}
		}

		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		jsonResponse.putBean(supplyBuy);
	}

	/**
	 * 查看需求
	 */
	public void getXqList() throws Exception {
		getListCommon("1", null);
	}

	/**
	 * 发布需求信息
	 */
	public void addXq() throws Exception {
		addSupplyCommon("1");
	}

	/**
	 * 我的需求
	 */
	public void getMyXqList() throws Exception {
		// 当前用户企业id
		String storeId = this.appUser.getStoreId();
		getListCommon("1", storeId);
	}

	/**
	 * 需求详情
	 */
	public void getXqDetails() throws Exception {
		getDetailsCommon();
	}

	/**
	 * 原料分类列表
	 */
	public void getGyCategory() throws Exception {
		List<ProductCategory> list = productCategoryService.getCategoriesByType("1");
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");

		jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
	}

	/**
	 * 添加供需（供需由type区分）
	 */
	private void addSupplyCommon(String type) throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "categoryId", "title", "qualityLevel", "unit", "futurePrices", "number", "contactName", "contactPhone", "deliveryType",
				"areaId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 原料分类
		String categoryId = jsonRequest.getString("categoryId");
		// 供应标题
		String title = jsonRequest.getString("title");
		// 质量等级
		String qualityLevel = jsonRequest.getString("qualityLevel");
		// 计量单位
		String unit = jsonRequest.getString("unit");
		// 预估单价
		String futurePrices = jsonRequest.getString("futurePrices");
		// 预估数量
		String number = jsonRequest.getString("number");
		// 联系人
		String contactName = jsonRequest.getString("contactName");
		// 联系电话
		String contactPhone = jsonRequest.getString("contactPhone");
		// 交货地ID
		String areaId = jsonRequest.getString("areaId");
		// 交货地
		String destination = jsonRequest.getString("destination");
		// 详细地址
		String address = jsonRequest.getString("address");
		// 交货方式(0自提;1送货上门)
		String deliveryType = jsonRequest.getString("deliveryType");
		// 有效时间
		String endTime = jsonRequest.getString("endTime");
		// 详细描述
		String content = jsonRequest.getString("content");
		// 需要资质
		JSONArray intelIds = jsonRequest.getJSONArray("intelIds");

		// 拼接交货地
		String intelligenceIds = "";
		if (intelIds != null && intelIds.length() > 0) {
			for (int i = 0; i < intelIds.length(); i++) {
				if (i == 0) {
					intelligenceIds = intelligenceIds + intelIds.getString(i);
				} else {
					intelligenceIds = intelligenceIds + "-" + intelIds.getString(i);
				}
			}
		}

		SupplyBuy sb = new SupplyBuy();
		sb.setType(type);
		sb.setCategoryId(categoryId);
		sb.setTitle(title);
		sb.setQualityLevel(qualityLevel);
		sb.setUnit(unit);
		sb.setFuturePrices(new BigDecimal(futurePrices));
		sb.setNumber(new BigDecimal(number));
		sb.setContactName(contactName);
		sb.setContactPhone(contactPhone);
		sb.setAreaId(areaId);
		sb.setDestination(destination);
		sb.setAddress(address);
		sb.setDeliveryType(Integer.parseInt(deliveryType));
		sb.setEndTime(endTime);
		sb.setContent(content);
		// 设置企业
		sb.setStoreId(this.appUser.getStoreId());
		sb.setStoreName(this.appUser.getCompanyName());
		sb.setIntelligenceIds(intelligenceIds);
		// 设置状态正常
		sb.setStatus("1");
		sb.setCheckStatus(0);
		// 用户中心写死了，这也先写死
		sb.setProductType("1");
		// 设置创建时间
		sb.setCreateTime(DateUtil.getSysDateTimeString());

		if (supplyBuyService.add(sb)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	/**
	 * 获取供需列表
	 * @param type
	 * @throws Exception
	 */
	private void getListCommon(String type, String storeId) throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "pageNo");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();
		// 供应标题
		String title = jsonRequest.getString("title");
		// 品类
		String categoryId = jsonRequest.getString("categoryId");
		// 交货地
		String destination = jsonRequest.getString("destination");
		// 页码
		String pageNo = jsonRequest.getString("pageNo");

		// 分页信息
		int currentPageNo = Integer.parseInt(pageNo);
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(15);

		List<SupplyBuy> list = new ArrayList<SupplyBuy>();
		// 企业id不为空，获取“我的”
		if (!StringUtil.isEmpty(storeId)) {
			// checkStatus为所有状态
			list = supplyBuyService.listSupplyApp(pager, type, storeId, title, categoryId, destination, null, false);
		} else {
			list = supplyBuyService.listSupplyApp(pager, type, storeId, title, categoryId, destination, "1", true);
		}

		// 附加字段
		if (list != null) {
			for (SupplyBuy supplyBuy : list) {
				// 收藏信息
				String infoId = supplyBuy.getStoreId();
				supplyBuy.setFriendStatus(friendService.getFriendStatus(userId, infoId));

				// 环信ID
				AppUser appUser = userService.getAppUserById(infoId);
				if (appUser != null) {
					supplyBuy.setHxUserName(appUser.getHxUserName());
				}

				// 分类名称
				String cId = supplyBuy.getCategoryId();
				if (!StringUtil.isEmpty(cId)) {
					ProductCategory productCategory = productCategoryService.getCategoryById(supplyBuy.getCategoryId());
					if (productCategory != null) {
						supplyBuy.setCategoryName(productCategory.getName());
					}
				}
			}
		}

		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		jsonResponse.put("pageNo", currentPageNo);
		jsonResponse.put("totalPage", pager.getTotalPage());
		jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
	}

	// ------------------需求变动，注释----------------------------
	// public void collectSupply() throws Exception {
	// // 供/求类型
	// String contentType = "0";
	// // 供需ID
	// String contentId = jsonRequest.getString("contentId");
	// // 类型 0：收藏 1：取消收藏
	// String collectStatus = jsonRequest.getString("collectStatus");
	//
	// String userId = this.appUser.getUserId();
	//
	// // 收藏
	// boolean success = false;
	// if ("0".equals(collectStatus)) {
	// // 先验证是否已收藏，如果已收藏，则直接返回成功，如果没收藏，则新增后返回
	// Boolean hasStatus = collectService.checkUserHasCollect(contentType,
	// contentId, userId);
	// if (!hasStatus) {
	// AppCollect collect = new AppCollect();
	// collect.setContentId(contentId);
	// collect.setContentType(contentType);
	// collect.setUpdateTime(DateUtil.getSysDateTimeString());
	// collect.setUserId(userId);
	// collect.setCreateTime(DateUtil.getSysDateTimeString());
	// success = collectService.save(collect);
	// }
	// } else {
	// // 取消收藏
	// success = collectService.delByUser(userId, contentType, contentId);
	// }
	//
	// // 成功
	// if (success) {
	// jsonResponse.put(AppMainController.STATUS_RETURN, "1");
	// }
	// }
	//
	// public void collectSupplyList() throws Exception {
	// // 供/求类型
	// String contentType = "0";
	//
	// String userId = this.appUser.getUserId();
	// List<AppCollect> list = collectService.getMyCollectList(contentType,
	// userId);
	//
	// jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
	// jsonResponse.put(AppMainController.STATUS_RETURN, "1");
	// }
	//
	// public void reportSupply() throws Exception {
	// // 供/求类型
	// String contentType = "0";
	//
	// String userId = this.appUser.getUserId();
	// // 供需ID
	// String contentId = jsonRequest.getString("contentId");
	// // 举报说明
	// String note = jsonRequest.getString("note");
	//
	// IAppReportService reportService =
	// SpringContextHolder.getBean(IAppReportService.class);
	// // 先验证是否已收藏，如果已收藏，则直接返回成功，如果没收藏，则新增后返回
	// boolean hasReported = reportService.checkUserHasReport(contentType,
	// contentId, userId);
	// if (hasReported) {
	// jsonResponse.put(AppMainController.STATUS_RETURN, "1");
	// } else {
	// AppReport report = new AppReport();
	// report.setContentId(contentId);
	// report.setContentType(contentType);
	// report.setUpdateTime(DateUtil.getSysDateTimeString());
	// report.setUserId(userId);
	// report.setCreateTime(DateUtil.getSysDateTimeString());
	// report.setNote(note);
	// if (reportService.save(report)) {
	// jsonResponse.put(AppMainController.STATUS_RETURN, "1");
	// }
	// }
	// }
	//
	// public void forwardingSupply() throws Exception {
	// // 供/求类型
	// String contentType = "0";
	// String userId = this.appUser.getUserId(); // 操作人
	// String recipientId = jsonRequest.getString("recipientId"); // 接收者id
	// String recipientType = jsonRequest.getString("recipientType"); //
	// 接收者类型0用户,1群组
	// String contentId = jsonRequest.getString("contentId"); // 供需id
	//
	// IAppForwardingService forwardService =
	// SpringContextHolder.getBean(IAppForwardingService.class);
	// AppForwarding forwarding = new AppForwarding();
	// forwarding.setContentId(contentId);
	// forwarding.setContentType(contentType);
	// forwarding.setCreateTime(DateUtil.getSysDateTimeString());
	// forwarding.setUpdateTime(DateUtil.getSysDateTimeString());
	// forwarding.setRecipientId(recipientId);
	// forwarding.setRecipientType(recipientType);
	// forwarding.setUserId(userId);
	//
	// if (forwardService.save(forwarding)) {
	// jsonResponse.put(AppMainController.STATUS_RETURN, "1");
	// }
	// }
}
