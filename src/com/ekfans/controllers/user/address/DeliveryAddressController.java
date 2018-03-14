package com.ekfans.controllers.user.address;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.UserAddress;
import com.ekfans.base.user.service.IUserAddressService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: DeliveryAddressController
 * @Description: TODO(会员中心-收货地址簿)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-22 下午06:29:31
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@Scope("prototype")
public class DeliveryAddressController extends BasicController {

	@Autowired
	private IUserAddressService userAddressService;
	@Autowired
	private ISystemAreaService systemAreaService;

	// 地址簿的查询缓存
	// private IUserAddressCacheService userAddressCacheService = new UserAddressCacheService();

	/**
	 * 跳转到（个人）收货地址列表页面
	 */
	@RequestMapping(value = "/user/deliveryAddress")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);

		List<UserAddress> udlist = this.userAddressService.listAddress(user.getId());
		/*
		if (udlist != null && udlist.size() > 0) {
			for (UserAddress ud : udlist) {
				// 拼接省市区
				String address = this.systemAreaService.getAreaFullNameByAreaIdAndDelimiter(ud.getAreaId(), "/");
				ud.setAreaId(address);
			}
		}
		*/

		request.setAttribute("udlist", udlist);
		request.setAttribute("cur", "deliveryAddress");

		return "/userCenter/customer/address/deliveryAddress";
	}

	/**
	 * 添加（个人）收货地址
	 */
	@RequestMapping(value = "/user/addressSave")
	@ResponseBody
	public Boolean save(UserAddress ud, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone3 = request.getParameter("phone3");

		String phone = "";
		if (!StringUtil.isEmpty(phone1)) {
			phone += phone1 + "-";
		}
		if (!StringUtil.isEmpty(phone2)) {
			phone += phone2;
		}
		if (!StringUtil.isEmpty(phone3)) {
			phone += "-" + phone3;
		}
		setProCityArea(ud);
		ud.setPhone(phone);
		ud.setUserId(user.getId());

		return this.userAddressService.addAdddress(ud);
	}

	/**
	 * 设置（个人）默认收货地址
	 */
	@RequestMapping(value = "/user/setDefaultAddress/{id}")
	@ResponseBody
	public Object setDefaultUserAddress(@PathVariable String id, HttpServletRequest request) {
		String udstatus = request.getParameter("udstatus");

		if (this.userAddressService.setDefaultAddress(id, udstatus)) {
			// 刷新缓存
			// String userId = ((User) request.getSession().getAttribute(SystemConst.USER)).getId();
			// userAddressCacheService.refreshUserAddressCache(userId);
			
			return true;
		}
		return false;
	}

	/**
	 * 删除（个人）收货地址
	 */
	@RequestMapping(value = "/user/delete/{id}")
	@ResponseBody
	public int delete(@PathVariable String id, HttpServletRequest request) {
		String udstatus = request.getParameter("udstatus");
		if (id.equals(udstatus)) {
			return 3;
		}

		if (this.userAddressService.deleteAdddress(id)) {
			// 刷新缓存
			// String userId = ((User) request.getSession().getAttribute(SystemConst.USER)).getId();
			// this.userAddressCacheService.refreshUserAddressCache(userId);

			return 1;
		}
		return 2;
	}

	/**
	 * 跳转到（个人）修改收货地址页面
	 */
	@RequestMapping(value = "/user/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		UserAddress ua = this.userAddressService.getUserAddressById(id);
		List<SystemArea> salist = null;
		if(!StringUtil.isEmpty(ua.getArea())){
			salist = this.systemAreaService.findAreaName(ua.getProvincial(), ua.getCity());
			for (SystemArea sa : salist) {
				if(sa.getAreaName().equals(ua.getArea())){
					ua.setArea(sa.getId());
					break;
				}
			}
		} else if(StringUtil.isEmpty(ua.getArea()) && !StringUtil.isEmpty(ua.getCity())){
			salist = this.systemAreaService.findCityName(ua.getProvincial());
			for (SystemArea sa : salist) {
				if(sa.getAreaName().equals(ua.getCity())){
					ua.setArea(sa.getId());
					break;
				}
			}
		} else{
			SystemArea sa = this.systemAreaService.getSystemAreaById(ua.getProvincial());
			ua.setArea(sa.getId());
		}
		request.setAttribute("ud", ua);
		request.setAttribute("cur", "deliveryAddress");

		return "/userCenter/customer/address/deliveryAddressModify";
	}

	/**
	 * 修改（个人）收货地址
	 */
	@RequestMapping(value = "/user/modify")
	@ResponseBody
	public Boolean modify(UserAddress ud, HttpServletRequest request) {
		String userId = ((User) request.getSession().getAttribute(SystemConst.USER)).getId();
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone3 = request.getParameter("phone3");

		String phone = "";
		if (!StringUtil.isEmpty(phone1)) {
			phone += phone1 + "-";
		}
		if (!StringUtil.isEmpty(phone2)) {
			phone += phone2;
		}
		if (!StringUtil.isEmpty(phone3)) {
			phone += "-" + phone3;
		}
		setProCityArea(ud);
		ud.setPhone(phone);
		ud.setUserId(userId);

		if (userAddressService.modifyAddress(ud)) {
			// 刷新缓存
			// this.userAddressCacheService.refreshUserAddressCache(userId);
			return true;
		}
		return false;
	}

	/**
	 * 用于购物车的添加收货地址
	 */
	@RequestMapping(value = "/user/adress/load")
	@ResponseBody
	public Object userAdressLoad(UserAddress userAddress, HttpServletRequest request, HttpSession session) {
		/*
		 * if(session == null) { return "/web/login"; }
		 */
		// 获取登陆用户的信息,完整的对象
		User user = (User) session.getAttribute(SystemConst.USER);
		// String userId = user.getId();
		String pca = request.getParameter("pca");
		
		String address3 = request.getParameter("address3");
		if(!StringUtil.isEmpty(pca)){
			String[] tempArea = pca.split(" ");
			if(tempArea.length == 1){
				userAddress.setProvincial(tempArea[0]);
			}else if(tempArea.length == 2){
				userAddress.setProvincial(tempArea[0]);
				userAddress.setCity(tempArea[1]);
			}else if(tempArea.length == 3){
				userAddress.setProvincial(tempArea[0]);
				userAddress.setCity(tempArea[1]);
				userAddress.setArea(tempArea[2]);
			}
		}
		
		
		/*
		 * for (int i = 0; i < tempArea.length; i++) { if (i == 0) { String
		 * provincial = tempArea[i].trim();
		 * userAddress.setProvincial(provincial); userAddress.setCity(null);
		 * userAddress.setArea(null); } else if (i == 1) { String city =
		 * tempArea[i].trim(); userAddress.setCity(city);
		 * userAddress.setAddress(null); } else if (i == 2) { String area =
		 * tempArea[i].trim(); userAddress.setArea(area); } }
		 */

		// 查询默认收货地址
		UserAddress tempUA = userAddressService.getDefaultAddess(user.getId());
		if (null != tempUA) {
			// 取消以前的收货地址
			tempUA.setStatus(false);
			// 取消默认收货地址操作
			userAddressService.cancelDefaultAddress(tempUA);
		}

		// 设置新添加的收货地址为默认收货地址
		userAddress.setStatus(true);
		userAddress.setAddress(address3);
		userAddress.setUserId(user.getId());
		if (userAddressService.addAdddress(userAddress)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存完成load出确认地址的页面
	 */
	@RequestMapping(value = "/user/adress/shopCartload")
	public String userShopCartAdressLoad(HttpSession session, HttpServletRequest request) {
		if (session == null) {
			return "/web/login";
		}
		// 获取登陆用户的信息,完整的对象
		User user = (User) session.getAttribute(SystemConst.USER);
		String userId = user.getId();
		// 通过userID得到地址集合
		List<UserAddress> uas = userAddressService.listAddress(userId);
		request.setAttribute("uas", uas);
		return "/userCenter/customer/cart/shopCartAdressLoad";
	}

	/**
	 * 在购物出用于修改收货地址
	 */
	@RequestMapping(value = "/user/addressUpdate/load/{id}")
	public String addressUpdateLoad(@PathVariable String id, HttpServletRequest request) {
		String judgment = request.getParameter("judgment");
		UserAddress userAddress = userAddressService.getUserAddressById(id);
		List<SystemArea> areaList = this.systemAreaService.getChildAreasByParentId(Cache.getResource("area.parent.code"));

		List<SystemArea> cityList = this.systemAreaService.findCityName(userAddress.getProvincial());
		List<SystemArea> quarea = this.systemAreaService.findAreaName(userAddress.getProvincial(), userAddress.getCity());
		if (userAddress != null) {
			request.setAttribute("userAddress2", userAddress);
			request.setAttribute("areaList", areaList);
			request.setAttribute("cityList", cityList);
			request.setAttribute("quarea", quarea);
			return "/userCenter/customer/cart/shopCartAdressUpdateLoad";
		}
		return "error";
	}

	/**
	 * 用于购物车的修改操作UserAddress
	 */
	@RequestMapping(value = "/user/shopCartAdress/modify")
	@ResponseBody
	public Object shopAdrssModify(UserAddress userAddress2, HttpServletRequest request) {
		try {
			String pca2 = request.getParameter("pca2");
			// 拆分规则、根据空格拆分
			String[] pcaArray = pca2.split(" +");
			// 设置地址省市区

			if (pcaArray != null) {
				if (pcaArray.length == 1) {
					userAddress2.setProvincial(pcaArray[0]);
				} else if (pcaArray.length == 2) {
					userAddress2.setProvincial(pcaArray[0]);
					userAddress2.setCity(pcaArray[1]);
				} else if (pcaArray.length == 3) {
					userAddress2.setProvincial(pcaArray[0]);
					userAddress2.setCity(pcaArray[1]);
					userAddress2.setArea(pcaArray[2]);
				}
			}
			User user = (User)request.getSession().getAttribute(SystemConst.USER);
			userAddress2.setUserId(user.getId());

			if (userAddressService.modifyAddress(userAddress2)) {
				request.setAttribute("modifyOk", true);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(value = "/user/shopCartAdress/change/utils")
	public void selectAddressCheck(HttpServletRequest request, HttpServletResponse response) {
		String tempStr = "";
		// 获取选中地址Id
		String parentId = request.getParameter("parentId").split(",")[1];
		// 查询选中的下一级子项集合
		List<SystemArea> systemAreaList = this.systemAreaService.getChildAreasByParentId(parentId);
		if (systemAreaList != null && systemAreaList.size() > 0) {
			for (SystemArea sa : systemAreaList) {
				// 拼接查询结果
				tempStr += ";" + sa.getAreaName() + "," + sa.getId();
			}
		} else {
			// 没有结果设置
			tempStr = "11";
		}
		// 没有结果设置
		if (StringUtil.isEmpty(tempStr)) {
			tempStr = "11";
		}
		
		backWriteStr(response, tempStr);
	}
	
	// ===============================================
	//                企业中心
	// ===============================================
	/**
	 * 跳转到（采购商）收货地址列表页面
	 */
	@RequestMapping(value = "/store/address/deliveryAddress")
	public String jumpAddressPage(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);

		List<UserAddress> udlist = this.userAddressService.listAddress(user.getId());
		request.setAttribute("udlist", udlist);
		request.setAttribute("cur", "deliveryAddress");

		return "/userCenter/store/address/address_list";
	}
	
	/**
	 * 添加（采购商）收货地址
	 */
	@RequestMapping(value = "/store/address/addressSave")
	@ResponseBody
	public Boolean saveStoreAddress(UserAddress ud, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone3 = request.getParameter("phone3");

		String phone = "";
		if (!StringUtil.isEmpty(phone1)) {
			phone += phone1 + "-";
		}
		if (!StringUtil.isEmpty(phone2)) {
			phone += phone2;
		}
		if (!StringUtil.isEmpty(phone3)) {
			phone += "-" + phone3;
		}
		setProCityArea(ud);
		ud.setPhone(phone);
		ud.setUserId(user.getId());

		return this.userAddressService.addAdddress(ud);
	}
	
	/**
	 * 设置（采购商）默认收货地址
	 */
	@RequestMapping(value = "/store/address/setDefaultAddress/{id}")
	@ResponseBody
	public Object setDefaultAddress(@PathVariable String id, HttpServletRequest request) {
		String udstatus = request.getParameter("udstatus");

		if (this.userAddressService.setDefaultAddress(id, udstatus)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除（采购商）收货地址
	 */
	@RequestMapping(value = "/store/address/delete/{id}")
	@ResponseBody
	public int deleteStoreAddress(@PathVariable String id, HttpServletRequest request) {
		String udstatus = request.getParameter("udstatus");
		if (id.equals(udstatus)) {
			return 3;
		}

		if (this.userAddressService.deleteAdddress(id)) {
			return 1;
		}
		return 2;
	}
	
	/**
	 * 跳转到（采购商）修改收货地址页面
	 */
	@RequestMapping(value = "/store/address/detail/{id}")
	public String jumpUpdateAddressPage(@PathVariable String id, HttpServletRequest request) {
		UserAddress ua = this.userAddressService.getUserAddressById(id);
		List<SystemArea> salist = null;
		if(!StringUtil.isEmpty(ua.getArea())){
			salist = this.systemAreaService.findAreaName(ua.getProvincial(), ua.getCity());
			for (SystemArea sa : salist) {
				if(sa.getAreaName().equals(ua.getArea())){
					ua.setArea(sa.getId());
					break;
				}
			}
		} else if(StringUtil.isEmpty(ua.getArea()) && !StringUtil.isEmpty(ua.getCity())){
			salist = this.systemAreaService.findCityName(ua.getProvincial());
			for (SystemArea sa : salist) {
				if(sa.getAreaName().equals(ua.getCity())){
					ua.setArea(sa.getId());
					break;
				}
			}
		} else{
			SystemArea sa = this.systemAreaService.getSystemAreaById(ua.getProvincial());
			ua.setArea(sa.getId());
		}
		request.setAttribute("ud", ua);
		request.setAttribute("cur", "deliveryAddress");

		return "/userCenter/store/address/address_update";
	}
	
	/**
	 * 修改（个人）收货地址
	 */
	@RequestMapping(value = "/store/address/modify")
	@ResponseBody
	public Boolean updateStoreAddress(UserAddress ud, HttpServletRequest request) {
		String userId = ((User) request.getSession().getAttribute(SystemConst.USER)).getId();
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone3 = request.getParameter("phone3");

		String phone = "";
		if (!StringUtil.isEmpty(phone1)) {
			phone += phone1 + "-";
		}
		if (!StringUtil.isEmpty(phone2)) {
			phone += phone2;
		}
		if (!StringUtil.isEmpty(phone3)) {
			phone += "-" + phone3;
		}
		setProCityArea(ud);
		ud.setPhone(phone);
		ud.setUserId(userId);

		if (userAddressService.modifyAddress(ud)) {
			// 刷新缓存
			// this.userAddressCacheService.refreshUserAddressCache(userId);
			return true;
		}
		return false;
	}
	
	/**
	 * 设置省市区
	 */
	private void setProCityArea(UserAddress ud) {
		String[] address = this.systemAreaService.getAreaFullNameByAreaIdAndDelimiter(ud.getArea(), "/").split("/");
		for (int i = 0; i < address.length; i++) {
			switch (i) {
			case 0:
				ud.setProvincial(address[i]);
				ud.setCity("");
				ud.setArea("");
				break;
			case 1:
				ud.setCity(address[i]);
				ud.setArea("");
				break;
			case 2:
				ud.setArea(address[i]);
				break;
			}
		}
	}
}
