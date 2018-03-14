package com.ekfans.plugin.wftong.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.plugin.wftong.util.MonitorUrlUtil;
import org.json.JSONObject;

import com.ekfans.base.order.model.AppCollect;
import com.ekfans.base.order.model.AppForwarding;
import com.ekfans.base.order.model.AppReport;
import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.service.IAppCollectService;
import com.ekfans.base.order.service.IAppForwardingService;
import com.ekfans.base.order.service.IAppReportService;
import com.ekfans.base.order.service.IBargainService;
import com.ekfans.base.order.service.IInquiryService;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.CategoryGoods;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.TankMaterial;
import com.ekfans.base.store.model.VehicleType;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.store.service.ICategoryGoodsService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.store.service.ITankMaterialService;
import com.ekfans.base.store.service.IVehicleTypeService;
import com.ekfans.base.store.service.IWfpysService;
import com.ekfans.base.store.service.IWftransportService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.wfOrder.model.WfOrderTrans;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.wftong.AppServerController;
import com.ekfans.plugin.wftong.base.friend.service.IFriendRelationService;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.HttpUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 物流 Controller
 * 
 * @Title: AppTransportController
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年4月26日18:31:42
 * @version 1.0
 */
public class AppTransportController {
	// 响应
	private MyJSONObject jsonResponse;
	// 请求主体
	private MyJSONObject jsonRequest;
	// request
	private HttpServletRequest request;
	// 当前用户对象
	private AppUser appUser;

	private IUserService userService = SpringContextHolder.getBean(IUserService.class);
	private IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);
	private IWftransportService wftransportService = SpringContextHolder.getBean(IWftransportService.class);
	private ISupplyBuyService supplyBuyService = SpringContextHolder.getBean(ISupplyBuyService.class);
	private IProductCategoryService productCategoryService = SpringContextHolder.getBean(IProductCategoryService.class);
	private ISystemAreaService areaService = SpringContextHolder.getBean(ISystemAreaService.class);
	private IFriendRelationService friendService = SpringContextHolder.getBean(IFriendRelationService.class);

	private IVehicleTypeService vehicleTypeService = SpringContextHolder.getBean(IVehicleTypeService.class);
	private ICategoryGoodsService goodsService = SpringContextHolder.getBean(ICategoryGoodsService.class);
	private IWfpysService wfpysService = SpringContextHolder.getBean(IWfpysService.class);
	private ITankMaterialService tankMaterialService = SpringContextHolder.getBean(ITankMaterialService.class);
	private IAppCollectService collectService = SpringContextHolder.getBean(IAppCollectService.class);
	private IAppForwardingService forwardingService = SpringContextHolder.getBean(IAppForwardingService.class);
	private IWfOrderService wfOrderService = SpringContextHolder.getBean(IWfOrderService.class);
	private ICarInfoService carInfoService = SpringContextHolder.getBean(ICarInfoService.class);
	private IBargainService bargainService = SpringContextHolder.getBean(IBargainService.class);
	private IInquiryService inquiryService = SpringContextHolder.getBean(IInquiryService.class);

	/**
	 * 构造
	 * @param jsonRequest 请求主体，用于获取请求参数
	 * @param jsonResponse 响应主体，用于返回结果数据
	 * @param appUser 当前用户
	 * @param request 
	 */
	public AppTransportController(MyJSONObject jsonRequest, MyJSONObject jsonResponse, AppUser appUser, HttpServletRequest request) {
		this.jsonRequest = jsonRequest;
		this.jsonResponse = jsonResponse;
		this.appUser = appUser;
		this.request = request;
	}

	public void addCar() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "name", "startPlace", "endPlace", "categoryName", "wfpysName", "wfpysParentId", "wfpysId", "carName", "tankMaterialName",
				"wfpTotal", "linkMan", "phone", "startFullPath", "endFullPath");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		addTransportCommon(0);
	}

	public void carList() throws Exception {
		transportListCommon("0", null);
	}

	// 运输列表公共方法
	private void transportListCommon(String type, String storeId) throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "pageNo");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();

		// 名称
		String name = jsonRequest.getString("name");
		// 起始地 全部则""
		String starPlace = jsonRequest.getString("starPlace");
		// 目的地 全部则""
		String endPlace = jsonRequest.getString("endPlace");
		// 车辆类型
		String carName = jsonRequest.getString("carName");
		// 罐体材质
		String tankMaterialName = jsonRequest.getString("tankMaterialName");
		// 货物类别名称
		String categoryName = jsonRequest.getString("categoryName");
		// 分类Id
		String wfpysParentId = jsonRequest.getString("wfpysParentId");
		// 状态 0:下架 1:上架
		String status = jsonRequest.getString("status");
		// 从页面获取页码
		String pageNo = jsonRequest.getString("pageNo");

		Pager page = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNo)) {
			currentPage = Integer.parseInt(pageNo);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(15);
		List<Wftransport> list = null;

		// 企业id不为空，获取“我的”
		if (!StringUtil.isEmpty(storeId)) {
			// checkStatus为所有状态
			list = wftransportService.getList(page, starPlace, endPlace, name, type, storeId, null, status, carName, tankMaterialName, categoryName, wfpysParentId, false);
		} else {
			list = wftransportService.getList(page, starPlace, endPlace, name, type, null, "1", "1", carName, tankMaterialName, categoryName, wfpysParentId, true);
		}

		if (list != null) {
			for (Wftransport wftransport : list) {
				String infoId = wftransport.getStoreId();
				// 查询并附加字段：收藏信息
				wftransport.setCollectStatus(collectService.checkUserHasCollect("1", wftransport.getId(), userId) ? "1" : "0");
				// 查询并附加字段：好友信息
				wftransport.setFriendStatus(friendService.getFriendStatus(userId, infoId));
				AppUser appUser = userService.getAppUserById(infoId);
				if (appUser != null) {
					// 查询并附加字段：环信id
					wftransport.setHxUserName(appUser.getHxUserName());
				}
			}
		}

		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
			jsonResponse.put("pageNo", pageNo);
			jsonResponse.put("totalPage", page.getTotalPage());

		}
	}

	public void myCarList() throws Exception {
		// 当前用户企业id
		String storeId = this.appUser.getStoreId();
		transportListCommon("0", storeId);
	}

	public void goodsDetails() throws Exception {
		detailsCommon();
	}

	public void myGoodsList() throws Exception {
		// 当前用户企业id
		String storeId = this.appUser.getStoreId();
		transportListCommon("1", storeId);
	}

	public void goodsList() throws Exception {
		transportListCommon("1", null);
	}

	public void addGoods() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "name", "startPlace", "endPlace", "categoryName", "wfpysName", "wfpysParentId", "wfpysId", "carName", "supplyName",
				"wfpTotal", "linkMan", "phone", "startFullPath", "endFullPath");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		addTransportCommon(1);
	}

	public void carDetails() throws Exception {
		detailsCommon();
	}

	private void detailsCommon() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "id");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();

		String id = jsonRequest.getString("id");

		Wftransport wftransport = wftransportService.getWftransportById(id);

		if (wftransport != null) {
			String infoId = wftransport.getStoreId();
			// 查询并附加字段：收藏信息
			wftransport.setCollectStatus(collectService.checkUserHasCollect("1", wftransport.getId(), userId) ? "1" : "0");
			// 朋友状态
			wftransport.setFriendStatus(friendService.getFriendStatus(userId, infoId));
			AppUser appUser = userService.getAppUserById(infoId);
			if (appUser != null) {
				// 环信id
				wftransport.setHxUserName(appUser.getHxUserName());
			}
		}

		jsonResponse.putBean(wftransport);
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
	}

	// 公共添加
	private void addTransportCommon(int type) throws Exception {
		// 车主名称
		String name = jsonRequest.getString("name");
		// 起点id
		String startPlace = jsonRequest.getString("startPlace");
		// 到达id
		String endPlace = jsonRequest.getString("endPlace");
		// 货物类型
		String categoryName = jsonRequest.getString("categoryName");
		// 货物种类名称 类型名称+子类型名称
		String wfpysName = jsonRequest.getString("wfpysName");
		// 货物种类
		String wfpysParentId = jsonRequest.getString("wfpysParentId");
		// 货物种类子分类
		String wfpysId = jsonRequest.getString("wfpysId");
		// 车辆类型
		String carName = jsonRequest.getString("carName");
		// 货源类型名称
		String supplyName = jsonRequest.getString("supplyName");
		// 罐体材质
		String tankMaterialName = jsonRequest.getString("tankMaterialName");
		// 车辆载重
		String wfpTotal = jsonRequest.getString("wfpTotal");
		// 联系人
		String linkMan = jsonRequest.getString("linkMan");
		// 联系电话
		String phone = jsonRequest.getString("phone");
		// 起始地全路径
		String startFullPath = jsonRequest.getString("startFullPath");
		// 目的地全路径
		String endFullPath = jsonRequest.getString("endFullPath");
		// 发布车源必要参数,货源 不用运价
		String price = jsonRequest.getString("price");

		// 地址详情详细地址如果要加起点和到达点的
		String habitatAddress = jsonRequest.getString("habitatAddress");
		// 截止时间
		String endTime = jsonRequest.getString("endTime");
		// 说明
		String content = jsonRequest.getString("content");
		// 单位
		String unit = jsonRequest.getString("unit");

		Store store = storeService.getStore(this.appUser.getStoreId());
		Wftransport wftransport = new Wftransport();
		wftransport.setType(type);
		wftransport.setName(name);
		wftransport.setStartPlace(startPlace);
		wftransport.setEndPlace(endPlace);
		wftransport.setHabitatAddress(habitatAddress);
		wftransport.setCategoryName(categoryName);
		wftransport.setWfpysParentId(wfpysParentId);
		wftransport.setWfpysId(wfpysId);
		wftransport.setWfpysName(wfpysName);
		wftransport.setCarName(carName);
		wftransport.setTankMaterialName(tankMaterialName);
		wftransport.setWfpTotal(new BigDecimal(StringUtil.isEmpty(wfpTotal) ? "0" : wfpTotal));
		wftransport.setLinkMan(linkMan);
		wftransport.setPhone(phone);
		wftransport.setEndTime(endTime);
		wftransport.setContent(content);
		wftransport.setPrice(new BigDecimal(StringUtil.isEmpty(price) ? "0" : price));
		wftransport.setStartFullPath(startFullPath);
		wftransport.setEndFullPath(endFullPath);
		wftransport.setSupplyName(supplyName);
		wftransport.setUnit(unit);

		if (store != null) {
			wftransport.setStoreId(store.getId());
			wftransport.setStoreName(store.getStoreName());
		}
		// 设置时间
		wftransport.setCreateTime(DateUtil.getSysDateTimeString());
		wftransport.setUpdateTime(DateUtil.getSysDateTimeString());
		// 设置状态默认上架
		wftransport.setStatus(1);

		if (wftransportService.add(wftransport)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	public void getCarTypeList() throws Exception {
		List<VehicleType> list = vehicleTypeService.getAllVehicleType();
		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}
	}

	public void getGoodsCatgList() throws Exception {
		List<CategoryGoods> list = goodsService.getCategoryGoodsList();
		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}
	}

	public void getWfTypeList() throws Exception {
		String parentId = jsonRequest.getString("parentId");

		List<Wfpys> list = null;
		if (!StringUtil.isEmpty(parentId)) {
			list = wfpysService.getList(false, parentId, null);
		} else {
			list = wfpysService.getList(true, null, null);
		}

		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}

	}

	public void getTankList() throws Exception {
		List<TankMaterial> list = tankMaterialService.getList();
		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}
	}

	public void collectTransport() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "contentId", "collectStatus");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 车源/货源类型
		String contentType = "1";
		// 供需ID
		String contentId = jsonRequest.getString("contentId");
		// 类型 0：收藏 1：取消收藏
		String collectStatus = jsonRequest.getString("collectStatus");

		String userId = this.appUser.getUserId();

		// 收藏状态
		Boolean hasStatus = collectService.checkUserHasCollect(contentType, contentId, userId);

		// 收藏
		boolean success = false;
		if ("0".equals(collectStatus)) {
			if (hasStatus) {
				success = true;
			} else {
				AppCollect collect = new AppCollect();
				collect.setContentId(contentId);
				collect.setContentType(contentType);
				collect.setUpdateTime(DateUtil.getSysDateTimeString());
				collect.setUserId(userId);
				collect.setCreateTime(DateUtil.getSysDateTimeString());
				success = collectService.save(collect);
			}
		} else {
			// 取消收藏
			if (!hasStatus) {
				success = true;
			} else {
				success = collectService.delByUser(userId, contentType, contentId);
			}
		}

		// 成功
		if (success) {
			jsonResponse.put("collectStatus", collectStatus);
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}
	
	
	public static void main(String[] args) {
		int collectStatus  = 0;
		int resultStatus = 1;
		
		
	}

	public void collectTransportList() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "pageNo");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 从页面获取页码
		String currentpageStr = jsonRequest.getString("pageNo");

		String userId = this.appUser.getUserId();

		Pager page = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);

		// 车源/货源类型
		String contentType = "1";

		List<AppCollect> collectList = collectService.getMyCollectList(page, contentType, userId);
		List<Wftransport> list = new ArrayList<Wftransport>();
		if (collectList != null) {
			for (AppCollect collect : collectList) {
				Wftransport wftransport = wftransportService.getWftransportById(collect.getContentId());
				if (wftransport != null) {
					list.add(wftransport);
				}
			}
		}

		if (list != null) {
			for (Wftransport wftransport : list) {
				String infoId = wftransport.getStoreId();
				// 查询并附加字段：收藏信息
				wftransport.setCollectStatus(collectService.checkUserHasCollect("1", wftransport.getId(), userId) ? "1" : "0");
				// 查询并附加字段：好友信息
				wftransport.setFriendStatus(friendService.getFriendStatus(userId, infoId));
				AppUser appUser = userService.getAppUserById(infoId);
				if (appUser != null) {
					// 查询并附加字段：环信id
					wftransport.setHxUserName(appUser.getHxUserName());
				}
			}
		}

		jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		jsonResponse.put("pageNo", currentPage);
		jsonResponse.put("totalPage", page.getTotalPage());
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
	}

	public void forwardingList() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "pageNo");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 从页面获取页码
		String currentpageStr = jsonRequest.getString("pageNo");
		String userId = this.appUser.getUserId();

		Pager page = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);

		// 车源/货源类型
		String contentType = "1";

		List<AppForwarding> forwardingList = forwardingService.getMyForwardingList(page, contentType, userId);

		List<Wftransport> list = new ArrayList<Wftransport>();

		if (forwardingList != null) {
			for (AppForwarding forwarding : forwardingList) {
				Wftransport wftransport = wftransportService.getWftransportById(forwarding.getContentId());
				if (wftransport != null) {
					list.add(wftransport);
				}
			}
		}

		if (list != null) {
			for (Wftransport wftransport : list) {
				String infoId = wftransport.getStoreId();
				// 查询并附加字段：收藏信息
				wftransport.setCollectStatus(collectService.checkUserHasCollect("1", wftransport.getId(), userId) ? "1" : "0");
				// 查询并附加字段：好友信息
				wftransport.setFriendStatus(friendService.getFriendStatus(userId, infoId));
				AppUser appUser = userService.getAppUserById(infoId);
				if (appUser != null) {
					// 查询并附加字段：环信id
					wftransport.setHxUserName(appUser.getHxUserName());
				}
			}
		}

		jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		jsonResponse.put("pageNo", currentPage);
		jsonResponse.put("totalPage", page.getTotalPage());
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
	}

	public void changeTransportStatus() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "status", "id");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String status = jsonRequest.getString("status");
		String id = jsonRequest.getString("id");

		Wftransport wftransport = wftransportService.getWftransportById(id);

		if (wftransport != null) {
			wftransport.setStatus(Integer.parseInt(status));
			wftransportService.updateWftransport(wftransport);
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}

	}

	public void yiJia() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "sellerId", "sourceId", "price", "quantity", "contactMan", "contactPhone");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String sourceType = "1"; // 获取来源类型：0商品 1货源
		String sellerId = jsonRequest.getString("sellerId"); // 卖家ID
		String sourceId = jsonRequest.getString("sourceId"); // 货源Id
		String price = jsonRequest.getString("price"); // 商议价格
		String quantity = jsonRequest.getString("quantity"); // 货源总量
		String contactMan = jsonRequest.getString("contactMan"); // 联系人
		String contactPhone = jsonRequest.getString("contactPhone"); // 联系电话

		// 都不能为空
		if (StringUtil.isEmpty(sellerId) || StringUtil.isEmpty(sourceId) || StringUtil.isEmpty(contactMan) || StringUtil.isEmpty(contactPhone) || StringUtil.isEmpty(price)
				|| StringUtil.isEmpty(quantity)) {
			return;
		}

		Bargain b = new Bargain();
		b.setSellerId(sellerId);
		b.setSourceId(sourceId);
		b.setContactMan(contactMan);
		b.setContactPhone(contactPhone);
		b.setPrice(new BigDecimal(price));
		b.setQuantity(new BigDecimal(quantity));

		String userId = this.appUser.getUserId(); // 获取当前用户id
		// 如果卖家id与当前系统登录用户id相同 提示
		if (sellerId.equals(userId)) {
			jsonResponse.put(AppServerController.MSG_RETURN, "不能和自己议价");
			return;
		}

		b.setType(sourceType);
		b.setBuyerId(userId);
		// 将订单议价状态设为议价中
		b.setStatus("0");
		// 下单状态设为未下单
		b.setOrderStatus("0");
		b.setCreateTime(DateUtil.getSysDateTimeString());

		if (bargainService.addBargain(b)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	public void baoJia() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "sellerId", "sourceId", "price", "contactMan", "contactPhone");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		Inquiry i = new Inquiry();

		// 报价
		int type = 1;
		// 货物
		int sourceType = 2;
		String sellerId = jsonRequest.getString("sellerId"); // 卖家ID
		String sourceId = jsonRequest.getString("sourceId"); // 货源Id
		String price = jsonRequest.getString("price"); // 商议价格
		String contactMan = jsonRequest.getString("contactMan"); // 联系人
		String contactPhone = jsonRequest.getString("contactPhone"); // 联系电话

		// 都不能为空
		if (StringUtil.isEmpty(sellerId) || StringUtil.isEmpty(sourceId) || StringUtil.isEmpty(contactMan) || StringUtil.isEmpty(contactPhone) || StringUtil.isEmpty(price)) {
			return;
		}

		Store s = storeService.getStore(this.appUser.getStoreId());
		i.setType(type);
		i.setSourceType(sourceType);
		i.setProductId(sourceId);
		i.setSourceType(2);
		i.setStoreName(s.getStoreName());
		i.setBuyersId(s.getId());
		i.setLinkTel(contactPhone);
		i.setLinkPerson(contactMan);
		i.setPrice(new BigDecimal(price));
		i.setSellersId(sellerId);
		i.setCreateTime(DateUtil.getSysDateTimeString());
		i.setStatus("0");
		// 保存询价信息
		if (inquiryService.saveBuyerInq(i)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	public void reportTransport() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "contentId", "note");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 车源/货源类型
		String contentType = "1";

		String userId = this.appUser.getUserId();
		// 供需ID
		String contentId = jsonRequest.getString("contentId");
		// 举报说明
		String note = jsonRequest.getString("note");

		IAppReportService reportService = SpringContextHolder.getBean(IAppReportService.class);
		// 先验证是否已收藏，如果已收藏，则直接返回成功，如果没收藏，则新增后返回
		boolean hasReported = reportService.checkUserHasReport(contentType, contentId, userId);
		if (hasReported) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		} else {
			AppReport report = new AppReport();
			report.setContentId(contentId);
			report.setContentType(contentType);
			report.setUpdateTime(DateUtil.getSysDateTimeString());
			report.setUserId(userId);
			report.setCreateTime(DateUtil.getSysDateTimeString());
			report.setNote(note);
			if (reportService.save(report)) {
				jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			}
		}
	}

	public void forwardingTransport() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "recipientId", "recipientType", "contentId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 车源/货源类型
		String contentType = "1";
		String userId = this.appUser.getUserId(); // 操作人
		String recipientId = jsonRequest.getString("recipientId"); // 接收者id
		String recipientType = jsonRequest.getString("recipientType"); // 接收者类型0用户,1群组
		String contentId = jsonRequest.getString("contentId"); // 供需id

		IAppForwardingService forwardService = SpringContextHolder.getBean(IAppForwardingService.class);
		AppForwarding forwarding = new AppForwarding();
		forwarding.setContentId(contentId);
		forwarding.setContentType(contentType);
		forwarding.setCreateTime(DateUtil.getSysDateTimeString());
		forwarding.setUpdateTime(DateUtil.getSysDateTimeString());
		forwarding.setRecipientId(recipientId);
		forwarding.setRecipientType(recipientType);
		forwarding.setUserId(userId);

		if (forwardService.save(forwarding)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	// 车辆报备
	public void addCarInfo() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "carNo", "carType", "fullWeight", "startTime", "endTime", "ysNo", "fullNum");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 企业id
		String storeId = this.appUser.getStoreId();
		// 车牌号
		String carNo = jsonRequest.getString("carNo");
		// 车辆类型
		String carType = jsonRequest.getString("carType");
		// 核定载质量
		String fullWeight = jsonRequest.getString("fullWeight");
		// 行驶证期限
		String startTime = jsonRequest.getString("startTime");
		String endTime = jsonRequest.getString("endTime");
		// 运输证号
		String ysNo = jsonRequest.getString("ysNo");
		// 核定载人数
		String fullNum = jsonRequest.getString("fullNum");

		CarInfo carInfo = new CarInfo();
		carInfo.setStoreId(storeId);
		carInfo.setCarNo(carNo);
		carInfo.setCarType(carType);
		carInfo.setFullWeight(Double.valueOf(StringUtil.isEmpty(fullWeight) ? "0" : fullWeight));
		carInfo.setStartTime(startTime);
		carInfo.setEndTime(endTime);
		carInfo.setYsNo(ysNo);
		carInfo.setFullNum(Integer.valueOf(StringUtil.isEmpty(fullNum) ? "0" : fullNum));

		String id = carInfoService.addCarInfo(carInfo);
		if (!StringUtil.isEmpty(id)) {
			jsonResponse.put("id", id);
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		} else {
			jsonResponse.put(AppServerController.STATUS_RETURN, "0");
		}
	}

	// 车辆信息修改
	public void udpCarInfo() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "id", "carNo", "carType", "fullWeight", "startTime", "endTime", "ysNo", "fullNum");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 企业id
		String storeId = this.appUser.getStoreId();
		// carinfoId
		String id = jsonRequest.getString("id");
		// 车牌号
		String carNo = jsonRequest.getString("carNo");
		// 车辆类型
		String carType = jsonRequest.getString("carType");
		// 核定载质量
		String fullWeight = jsonRequest.getString("fullWeight");
		// 行驶证期限
		String startTime = jsonRequest.getString("startTime");
		String endTime = jsonRequest.getString("endTime");
		// 运输证号
		String ysNo = jsonRequest.getString("ysNo");
		// 核定载人数
		String fullNum = jsonRequest.getString("fullNum");

		CarInfo carInfo = carInfoService.getCarInfoById(id);
		if (carInfo != null) {

			carInfo.setStoreId(storeId);
			carInfo.setCarNo(carNo);
			carInfo.setCarType(carType);
			carInfo.setFullWeight(Double.valueOf(StringUtil.isEmpty(fullWeight) ? "0" : fullWeight));
			carInfo.setStartTime(startTime);
			carInfo.setEndTime(endTime);
			carInfo.setYsNo(ysNo);
			carInfo.setFullNum(Integer.valueOf(StringUtil.isEmpty(fullNum) ? "0" : fullNum));

			if (carInfoService.updateBean(carInfo, request)) {
				jsonResponse.put("id", id);
				jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			} else {
				jsonResponse.put(AppServerController.STATUS_RETURN, "0");
			}
		}
	}

	// 车辆详情
	public void carInfoDetails() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "id");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String id = jsonRequest.getString("id");

		CarInfo carInfo = carInfoService.getCarInfoById(id);
		// 图片加上路径
		if (carInfo != null) {
			BasicRequest br = new BasicRequest(request);
			carInfo.setXszFile(br.getImage(carInfo.getXszFile()));
			carInfo.setYszFile(br.getImage(carInfo.getYszFile()));
		}

		// 附加车辆监控地址
		carInfo.setMonitorUrl(MonitorUrlUtil.getUrl(MonitorUrlUtil.MonitorViewTypeEnum.VIEW_CAR, carInfo.getId()));


		// 获取所有在运车辆ID
		List<String> inTransitCarIdList = wfOrderService.getAllInTransitIdList();
		// 附加车辆状态
		carInfo.setTransportationStatus(inTransitCarIdList != null && inTransitCarIdList.contains(carInfo.getId()) ? "1" : "0");


		jsonResponse.putBean(carInfo);
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
	}

	// 删除车辆
	public void delInfo() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "id");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String id = jsonRequest.getString("id");

		if (carInfoService.deleteById(id, request)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	// 获取车辆管理列表
	public void carInfoList() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "pageNo");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String storeId = this.appUser.getStoreId();

		Pager page = new Pager();
		// 从页面获取页码
		String currentpageStr = jsonRequest.getString("pageNo");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		List<CarInfo> list = carInfoService.getCarInfosByStoreId(page, storeId, null, null);

		if (list != null) {
			BasicRequest br = new BasicRequest(request);

			// 获取所有在运车辆ID
			List<String> inTransitCarIdList = wfOrderService.getAllInTransitIdList();

			for (CarInfo carInfo : list) {
				// 图片加上路径
				carInfo.setXszFile(br.getImage(carInfo.getXszFile()));
				carInfo.setYszFile(br.getImage(carInfo.getYszFile()));

				carInfo.setMonitorUrl(MonitorUrlUtil.getUrl(MonitorUrlUtil.MonitorViewTypeEnum.VIEW_CAR, carInfo.getId()));

				// 附加车辆状态
				carInfo.setTransportationStatus(inTransitCarIdList != null && inTransitCarIdList.contains(carInfo.getId()) ? "1" : "0");
			}
            // 根据车辆状态
            Collections.sort(list, new Comparator<CarInfo>() {
                public int compare(CarInfo c1, CarInfo c2) {
                    Integer a = Integer.parseInt(c1.getTransportationStatus());
                    Integer b = Integer.parseInt(c2.getTransportationStatus());
                    // 升序
                    // return a.compareTo(b);
                    // 降序
                    return b.compareTo(a);
                }
            });

			// 附加企业车辆监控地址
			jsonResponse.put("monitorUrl", MonitorUrlUtil.getUrl(MonitorUrlUtil.MonitorViewTypeEnum.VIEW_STORE, storeId));

			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
			jsonResponse.put("pageNo", currentPage);
			jsonResponse.put("totalPage", page.getTotalPage());
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	public void getDeviceList() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "pageNo");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 页码
		String pageNo = jsonRequest.getString("pageNo");
		// 设备编号
		String code = jsonRequest.getString("code");
		// 设备SIM卡号
		String simNum = jsonRequest.getString("simNum");

		// 获取请求必要信息
		String rootUrl = Cache.getResource("monitor.url.prefx");
		String orgId = Cache.getResource("orgId");
		String sign = Cache.getResource("key");
		// 请求url
		String url = rootUrl + "/data/service";

		// 请求主体
		JSONObject requestMain = new JSONObject();
		// 参数主体
		JSONObject param = new JSONObject();
		// 包装参数
		param.put("code", code);
		param.put("pageNo", pageNo);
		param.put("simNum", simNum);
		requestMain.put("source", "001");
		requestMain.put("orgId", orgId);
		requestMain.put("sign", sign);
		requestMain.put("param", param);

		// 调用接口返回数据
		String resultJson = HttpUtil.doPost(url, requestMain);

		JSONObject responseMain = new JSONObject(resultJson);

		boolean success = responseMain.getBoolean("status");
		// 提取数据
		if (success) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("pageNo", responseMain.getString("pageNo"));
			jsonResponse.put("totalPage", responseMain.getString("totalPage"));
			jsonResponse.put("details", responseMain.getJSONArray("deviceList"));
		}
	}

	public void updateDeviceToUse() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "deviceId", "carInfoId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String deviceId = jsonRequest.getString("deviceId");
		String carInfoId = jsonRequest.getString("carInfoId");

		CarInfo carInfo = carInfoService.getCarInfoById(carInfoId);
		if (!StringUtil.isEmpty(deviceId) && carInfo != null) {

			// 请求监控平台接口，执行绑定
			// 获取请求必要信息
			String rootUrl = Cache.getResource("monitor.url.prefx");
			String orgId = Cache.getResource("orgId");
			String sign = Cache.getResource("key");
			// 请求url
			String url = rootUrl + "/data/service";

			// 请求主体
			JSONObject requestMain = new JSONObject();
			// 参数主体
			JSONObject param = new JSONObject();
			// 包装参数
			param.put("id", deviceId);
			requestMain.put("source", "002");
			requestMain.put("orgId", orgId);
			requestMain.put("sign", sign);
			requestMain.put("param", param);

			// 调用接口返回数据
			String resultJson = HttpUtil.doPost(url, requestMain);

			JSONObject responseMain = new JSONObject(resultJson);

			boolean success = responseMain.getBoolean("status");
			// 判断监控平台执行结果
			if (success) {
				carInfo.setDeviceId(deviceId);
				if (carInfoService.updateBean(carInfo, request)) {
					jsonResponse.put(AppServerController.STATUS_RETURN, "1");
				}
			}
		}

	}

	public void getDeviceById() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "deviceId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String deviceId = jsonRequest.getString("deviceId");

		// 获取请求必要信息
		String rootUrl = Cache.getResource("monitor.url.prefx");
		String orgId = Cache.getResource("orgId");
		String sign = Cache.getResource("key");
		// 请求url
		String url = rootUrl + "/data/service";

		// 请求主体
		JSONObject requestMain = new JSONObject();
		// 参数主体
		JSONObject param = new JSONObject();
		// 包装参数
		param.put("id", deviceId);
		requestMain.put("source", "003");
		requestMain.put("orgId", orgId);
		requestMain.put("sign", sign);
		requestMain.put("param", param);

		// 调用接口返回数据
		String resultJson = HttpUtil.doPost(url, requestMain);
		JSONObject responseMain = new JSONObject(resultJson);

		boolean success = responseMain.getBoolean("status");
		// 提取数据
		if (success) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", responseMain.getJSONObject("device"));
		}
	}

}
