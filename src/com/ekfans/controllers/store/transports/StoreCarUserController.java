package com.ekfans.controllers.store.transports;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.CarUser;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.ICarUserService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.util.UUIDUtil;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
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
public class StoreCarUserController extends BasicController {

	@Autowired
	private ICarUserService userService;

	/**
	 * 新增车辆人员信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/transport/caruser/add/{userType}")
	public String add(@PathVariable String userType, HttpServletRequest request) {
		request.setAttribute("userType", userType);
		return "/userCenter/store/driver/driver_add";
	}

	@RequestMapping(value = "/store/driver/carUser/save")
	@ResponseBody
	public String save(CarUser user, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		user.setId(UUIDUtil.getMarkedUUID32(CarUser.SINGLE_MARK));
		if (userService.save(user, request, response)) {
			return "1";
		} else {
			return "0";
		}
	}

	@RequestMapping(value = "/store/driver/carUser/delCars")
	@ResponseBody
	public String delCars(String[] carUserIds, HttpServletRequest request) {
		if (carUserIds != null && carUserIds.length > 0) {
			if (userService.delete(carUserIds)) {
				return "1";
			}
		}
		return "0";
	}

	@RequestMapping(value = "/store/driver/carUser/delCar")
	@ResponseBody
	public String delCar(String carUserId, HttpServletRequest request) {
		if (!StringUtil.isEmpty(carUserId)) {
			if (userService.deleteById(carUserId)) {
				return "1";
			}
		}
		return "0";
	}

	@RequestMapping(value = "/store/driver/carUser/update")
	@ResponseBody
	public String update(CarUser user, String id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		user.setStoreId(store.getId());
		user.setUpdateTime(DateUtil.getSysDateString());
		String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
		if (user.getType().equals("0")) {
			// 调用方法获取分类图标，返回图标路径
			String driverFile = FileUploadHelper.uploadFile("driverFile", currentPath, request, response);
			user.setDriverFile(driverFile);
			String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
			user.setLicenseFile(licenseFile);
		} else {
			String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
			user.setLicenseFile(licenseFile);
		}
		if (userService.updateBean(user)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/driver/carUser/list/{userType}")
	public String getCarUserList(@PathVariable String userType, String checkStatus, String mobile, String name, HttpServletRequest request, HttpServletResponse response) {
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
		List<CarUser> list = new ArrayList<CarUser>();
		list = userService.getCarUsersByStoreId(page, request, response, userType, name, mobile, checkStatus);

		request.setAttribute("userList", list);
		request.setAttribute("pager", page);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("userType", userType);

		return "/userCenter/store/driver/driverList";
	}

	@RequestMapping(value = "/store/driver/carUser/getCarUserById")
	public String getCarUserById(int seeType, String carUserId, HttpServletRequest request) {
		if (!StringUtil.isEmpty(carUserId)) {
			CarUser user = userService.getCarUserById(carUserId);

			request.setAttribute("user", user);
			if (seeType == 1) {
				return "/userCenter/store/driver/driver_show";
			} else {
				return "/userCenter/store/driver/driver_update";
			}
		}
		return null;
	}

	// ---------------以下是后台的方法----------------------

	@RequestMapping(value = "/system/store/driver/listSytem/{userType}")
	public String getCarUserListSystem(@PathVariable String userType, String storeName, HttpServletRequest request, HttpServletResponse response) {
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
		List<CarUser> list = new ArrayList<CarUser>();
		list = userService.getCarUserByPageSystem(userType, page, storeName, request, response);
		request.setAttribute("userList", list);
		request.setAttribute("pager", page);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("storeName", storeName);
		request.setAttribute("userType", userType);

		return "/system/store/driver/driver_List";
	}

	@RequestMapping(value = "/system/store/driver/getCarUserByIdSystem")
	public String getCarUserByIdSystem(String carUserId, HttpServletRequest request) {
		if (!StringUtil.isEmpty(carUserId)) {
			CarUser user = userService.getCarUserById(carUserId);
			request.setAttribute("user", user);

			return "/system/store/driver/driver_check";
		}
		return null;
	}

	@RequestMapping(value = "/system/store/driver/checkCarUser")
	@ResponseBody
	public String checkCarUser(CarUser user, String id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 得到登录人对象
		Manager manager = (Manager) request.getSession().getAttribute(SystemConst.MANAGER);
		if (!StringUtil.isEmpty(id) && user != null) {
			// 设置审核人
			user.setCheckMan(manager.getName());
			user.setCheckTime(DateUtil.getSysDateString());
			user.setUpdateTime(DateUtil.getSysDateString());
			user.setCheckStatus("1");

			// 设置图标保存路径
			String currentPath = "/customerfiles/store/" + user.getStoreId() + "/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			if (user.getType().equals("0")) {
				String driverFile = FileUploadHelper.uploadFile("driverFile", currentPath, request, response);
				user.setDriverFile(driverFile);
				String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
				user.setLicenseFile(licenseFile);
			} else {
				String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
				user.setLicenseFile(licenseFile);
			}
			if (userService.updateBean(user)) {
				return "1";
			}
			return "0";
		}
		return "";
	}

}