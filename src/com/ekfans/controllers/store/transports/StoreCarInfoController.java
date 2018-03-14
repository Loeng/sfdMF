package com.ekfans.controllers.store.transports;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 
 * 
 * @Title: StoreCarController.java
 * @Description:易科三分地平台
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司 
 * @date 2014-3-19 上午11:22:03 
 * @version V1.0
 */
@Controller
public class StoreCarInfoController extends BasicController {

	@Autowired
	private ICarInfoService carInfoService;

	/**
	 * 新增车辆信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/transport/car/save")
	@ResponseBody
	public String saveOrUpdate(CarInfo info, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (carInfoService.saveOrUpdate(info, request, response)) {
			// 异步加载数据到危废通
			// WftSyncService wftService = new WftSyncService(getRequest(),
			// response, null, info, null, "synchroCardata");
			// Thread thread = new Thread(wftService);
			// thread.start();
			return "1";
		} else {
			return "0";
		}
	}

	@RequestMapping(value = "/store/transport/car/update")
	@ResponseBody
	public String update(CarInfo info, String id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (StringUtil.isEmpty(id)) {
			return "0";
		}
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		info.setStoreId(store.getId());
		info.setUpdateTime(DateUtil.getSysDateString());
		// 设置图标保存路径
		String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
		// 调用方法获取分类图标，返回图标路径
		String xszFile = FileUploadHelper.uploadFile("xszFile", currentPath, request, response);
		String yszFile = FileUploadHelper.uploadFile("yszFile", currentPath, request, response);
		info.setXszFile(xszFile);
		info.setYszFile(yszFile);
		if (carInfoService.updateBean(info, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/transport/car/list")
	public String getCarInfoList(String checkStatus, String carNo, HttpServletRequest request, HttpServletResponse response) {
		Pager page = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		List<CarInfo> list = new ArrayList<CarInfo>();
		list = carInfoService.getCarInfosByStoreId(page, carNo, checkStatus, request, response);
		request.setAttribute("carInfoList", list);
		request.setAttribute("pager", page);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("checkStatus", checkStatus);
		request.setAttribute("carNo", carNo);
		request.setAttribute("orgId", Cache.getResource("orgId"));
		request.setAttribute("sign", Cache.getResource("key"));
		request.setAttribute("monitorUrl", Cache.getResource("monitor.url.prefx"));

		return "/userCenter/store/transport/carInfoList";
	}

	/**
	 * 进入新增车辆信息页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/transport/car/add")
	public String add() {
		return "/userCenter/store/transport/carInfo_add";
	}

	@RequestMapping(value = "/store/transport/car/delCars")
	@ResponseBody
	public String delCars(String[] carInfoIds, HttpServletRequest request) {
		if (carInfoService.delete(carInfoIds, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/transport/car/delCar")
	@ResponseBody
	public String delCar(String carInfoId, HttpServletRequest request) {
		if (carInfoService.deleteById(carInfoId, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/transport/car/getCarInfoById")
	public String getCarInfoById(int type, String carInfoId, HttpServletRequest request) {
		if (!StringUtil.isEmpty(carInfoId)) {

			CarInfo info = carInfoService.getCarInfoById(carInfoId);

			request.setAttribute("info", info);
			if (type == 1) {// 进入查看页面
				return "/userCenter/store/transport/carInfo_show";
			} else if (type == 2) {// 进入修改页面
				return "/userCenter/store/transport/carInfo_update";
			}
		}
		return null;
	}

	@RequestMapping(value = "/store/transport/car/checkCarNo")
	@ResponseBody
	public String checkCarNo(String carNo) {
		if (!StringUtil.isEmpty(carNo)) {
			List<CarInfo> list = carInfoService.getAllCarInfo();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCarNo().equals(carNo)) {
					return "0";
				}
			}
		}
		return "1";
	}

	// ---------------以下是后台的方法----------------------

	@RequestMapping(value = "/system/store/transport/car/listSytem")
	public String getCarInfoListSystem(String storeName, HttpServletRequest request, HttpServletResponse response) {
		Pager page = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		List<CarInfo> list = new ArrayList<CarInfo>();
		list = carInfoService.getCarInfoSystem(page, storeName, request, response);
		request.setAttribute("carInfoList", list);
		request.setAttribute("pager", page);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("storeName", storeName);
		return "/system/store/transport/carInfo_list";
	}

	@RequestMapping(value = "/system/store/transport/car/getCarInfoByIdSystem")
	public String getCarInfoByIdSystem(String carInfoId, HttpServletRequest request) {
		if (!StringUtil.isEmpty(carInfoId)) {
			CarInfo info = carInfoService.getCarInfoById(carInfoId);
			request.setAttribute("info", info);

			return "/system/store/transport/carInfo_shenhe";
		}
		return null;
	}

	@RequestMapping(value = "/system/store/transport/car/checkCarInfo")
	@ResponseBody
	public String checkCarInfo(CarInfo info, String id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 得到登录人对象
		Manager manager = (Manager) request.getSession().getAttribute(SystemConst.MANAGER);
		if (!StringUtil.isEmpty(id) && info != null) {
			// 设置审核人
			info.setCheckMan(manager.getName());
			info.setCheckTime(DateUtil.getSysDateString());
			info.setUpdateTime(DateUtil.getSysDateString());
			info.setCheckStatus("1");

			// 设置图标保存路径
			String currentPath = "/customerfiles/store/" + info.getStoreId() + "/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			String xszFile = FileUploadHelper.uploadFile("xszFile", currentPath, request, response);
			String yszFile = FileUploadHelper.uploadFile("yszFile", currentPath, request, response);
			info.setXszFile(xszFile);
			info.setYszFile(yszFile);

			if (carInfoService.updateBean(info, request)) {
				return "1";
			}
			return "0";
		}

		return "";
	}

	/**
	 * 更改三分地数据carInfo表的deviceId数据,然后同步到监控平台
	 * 
	 * @param carInfoId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/transport/car/bindDeviceId")
	public @ResponseBody
	boolean bindDeviceId(String carInfoId, String deviceId, HttpServletRequest request, HttpServletResponse response) {
		CarInfo carInfo = carInfoService.getCarInfoById(carInfoId);
		// 只有审核通过的才能绑定，并且绑定的设备ID不能为空
		if ("1".equals(carInfo.getCheckStatus()) && !StringUtil.isEmpty(deviceId)) {
			carInfo.setDeviceId(deviceId);
			// 更新成功将车辆信息同步到监控平台
			carInfoService.updateBean(carInfo, request);

			return true;
		}
		return false;
	}
}