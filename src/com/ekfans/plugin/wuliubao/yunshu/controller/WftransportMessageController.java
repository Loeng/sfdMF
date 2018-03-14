package com.ekfans.plugin.wuliubao.yunshu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.CategoryGoods;
import com.ekfans.base.store.model.TankMaterial;
import com.ekfans.base.store.model.VehicleType;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.store.service.ICategoryGoodsService;
import com.ekfans.base.store.service.ITankMaterialService;
import com.ekfans.base.store.service.IVehicleTypeService;
import com.ekfans.base.store.service.IWfpysService;
import com.ekfans.base.store.service.IWftransportService;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.controllers.ccwccApp.Response;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.service.ISystemConfigCacheService;
import com.ekfans.plugin.cache.service.SystemConfigCacheService;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Address;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Attention;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Capacity;
import com.ekfans.plugin.wuliubao.yunshu.base.model.CarCKG;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Version;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IAddressService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IAttentionService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.ICapacityService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.ICarCKGService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IVersionService;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddAddressForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CheListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.GetAddressForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoResponse;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.ModifyAddressForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.VersionForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.JGUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 危废运输控制类,可以得到车辆类型,车辆罐体材质,车辆的长宽高信息,货物类型,货物类别等
 * 
 * @author yf
 * @2017年7月11日08:54:42
 *
 */
@Controller
@RequestMapping("/wlbApp")
public class WftransportMessageController {

	@Autowired
	private IWftransportService wftransportService;
	
	@Autowired
	private IVehicleTypeService vehicleTypeService;

	@Autowired
	private ITankMaterialService tankMaterialService;

	@Autowired
	private ICarCKGService carCKGService;

	@Autowired
	private ICapacityService capcityService;

	@Autowired
	private ICategoryGoodsService goodsService;

	@Autowired
	private IWfpysService wfpysService;

	@Autowired
	private IAddressService addressService;

	@Autowired
	private IVersionService versionService;
	
	@Autowired
	private IAttentionService attentionService;
	
    @Autowired
    private IWlbAppUserDao userdao;

	@Autowired
	private IWftransportService service;
	
	@Autowired
	private IUserService userService;
	
	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(WftransportMessageController.class);

	/**
	 * 得到车辆类型
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getVehicleType", method = RequestMethod.GET)
	@ResponseBody
	public Response getVehicleType() {
		// 得到所有的车辆类型
		List<VehicleType> allVehicleType = vehicleTypeService.getAllVehicleType();
		Map<String, Object> map = new HashMap<String, Object>();
		for (VehicleType vehicleType : allVehicleType) {
			// 得到罐体材质
			List<TankMaterial> l = tankMaterialService.getTankMaterialList(vehicleType.getId());
			vehicleType.setChildList(l);
		}
		map.put("CarSourceList", allVehicleType);
		return new Response().success(map);
	}

	/**
	 * 得到车辆罐体材质
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getTankMaterial")
	@ResponseBody
	public Response getTankMaterial() {
		List<TankMaterial> list = tankMaterialService.getList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("TankMaterial", list);
		return new Response().success(map);
	}

	/**
	 * 得到车辆的长宽高信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCarCKG")
	@ResponseBody
	public Response getCarCKG() {
		List<CarCKG> list = carCKGService.getAllCarCKG();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CarCKG", list);
		return new Response().success(map);
	}

	/**
	 * 得到车辆的载重信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCapacity")
	@ResponseBody
	public Response getCapacity() {
		List<Capacity> list = capcityService.getAllCapacity();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Capacity", list);
		return new Response().success(map);
	}

	// 得到地址
	@RequestMapping(value = "/getArea/{areaId}")
	@ResponseBody
	public Response getArea(@PathVariable("areaId") String areaId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusCode ", "1");
		if (areaId.equals("1")) {
			ISystemConfigCacheService systemConfigService = new SystemConfigCacheService();
			List<SystemArea> allProvinces = systemConfigService.allProvinces();
			map.put("Area", allProvinces);
			return new Response().success(map);
		}

		Map<String, SystemArea> areas = Cache.getSystemAreas();
		if (areas == null || areas.size() <= 0) {
			map.put("statusCode ", "0");
			map.put("Area", "");
			return new Response().success(map);
		}

		SystemArea area = areas.get(areaId);
		List<SystemArea> childList = null;
		if (area != null) {
			childList = area.getChildList();
		}
		map.put("Area", childList);
		return new Response().success(map);
	}

	@RequestMapping(value = "/getArea")
	@ResponseBody
	public Response getArea() {
		Map<String, Object> map = new HashMap<String, Object>();
		ISystemConfigCacheService systemConfigService = new SystemConfigCacheService();
		// 得到所有的省
		List<SystemArea> allProvinces = systemConfigService.allProvinces();
		// 得到所有的地区
		Map<String, SystemArea> areas = Cache.getSystemAreas();
		if (areas == null || areas.size() <= 0) {
			map.put("statusCode ", "0");
			map.put("Area", "");
			return new Response().success(map);
		}

		for (SystemArea systemArea : allProvinces) {
			SystemArea area = areas.get(systemArea.getId());
			List<SystemArea> childList = null;
			if (area != null) {
				childList = area.getChildList();
			}
			systemArea.setChildList(childList);
		}
		return new Response().success(allProvinces);

	}

	/**
	 * 得到货物类别 如:固体
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getGoodCategory")
	@ResponseBody
	public Response getGoodCategory() {
		List<CategoryGoods> categoryGoodsList = goodsService.getCategoryGoodsList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("GoodCategory", categoryGoodsList);
		return new Response().success(map);
	}

	/**
	 * 得到货物种类:如一类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getGoodWfpy")
	@ResponseBody
	public Response getGoodWfpy() {
		List<Wfpys> wfpys = wfpysService.getList(true, null, null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("GoodWfpy", wfpys);
		return new Response().success(map);
	}

	/**
	 * 得到用户的常用地址
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getAddress")
	@ResponseBody
	public Response getAddress(HttpServletRequest request, @RequestBody byte[] by) {
		try {
			GetAddressForm gf = (GetAddressForm) new GetAddressForm().getObject(by);
			Pager page = new Pager();
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(gf.currentpageStr)) {
				currentPage = Integer.parseInt(gf.currentpageStr);
			}
			// 设置要查询的页码
			page.setCurrentPage(currentPage);
			page.setRowsPerPage(10);
			// 获取用户token
			String token = request.getHeader("WLB-Token");
			// 通过token获取用户id
			String[] userid = token.split("_");

			if (!StringUtil.isEmpty(userid[0])) {
				List<Address> allAddress = addressService.getAllAddress(page, userid[0]);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Pager", page);
				map.put("Address", allAddress);
				return new Response().success(map);
			}
			return new Response().failure("未得到用户信息!");
		} catch (Exception e) {
			// TODO: handle exception
			return new Response().failure();
		}

	}

	/**
	 * 根据ids删除用户地址,如果ids中有,,说明是多个id
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteAddress/{ids}", method = RequestMethod.GET)
	@ResponseBody
	public Response deleteHuo(@PathVariable String ids) {
		if (!StringUtil.isEmpty(ids)) {
			boolean b = addressService.deleteAddress(ids);
			if (b) {
				return new Response().success("删除成功");
			}
			return new Response().failure("删除失败!");
		}
		return new Response().failure("传来的ids为空!");
	}

	/**
	 * 添加用户的常用地址
	 * 
	 * @param by
	 * @return
	 */
	@RequestMapping(value = "/addAddress")
	@ResponseBody
	public Response addAddress(@RequestBody byte[] by, HttpServletRequest request) {
		try {
			AddAddressForm aa = (AddAddressForm) new AddAddressForm().getObject(by);
			Address ad = new Address();
			// 根据token得到用户id
			String token = request.getHeader("WLB-Token");
			String[] userids = token.split("_");
			String userId = userids[0];
			if (StringUtil.isEmpty(userId)) {
				return new Response().failure("未找到用户信息!");
			}
			System.out.println("provinceName  =========" + aa.provinceName);
			ad.setUserId(userId);
			ad.setProvinceId(aa.provinceId);
			ad.setCityId(aa.cityId);
			ad.setAreaId(aa.areaId);
			ad.setProvinceName(aa.provinceName);
			ad.setCityName(aa.cityName);
			ad.setAreaName(aa.areaName);
			ad.setHabitatAddress(aa.habitatAddress);
			boolean b = addressService.addAddress(ad);
			if (b) {
				Map<String, Address> map = new HashMap<String, Address>();
				map.put("Address", ad);
				return new Response().success(map);
			}
			return new Response().failure("添加地址失败!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}

	/**
	 * 编辑(修改)用户的常用地址
	 * 
	 * @param by
	 * @return
	 */
	@RequestMapping(value = "/modifyAddress")
	@ResponseBody
	public Response modifyAddress(@RequestBody byte[] by) {
		try {
			ModifyAddressForm aa = (ModifyAddressForm) new ModifyAddressForm().getObject(by);
			if (StringUtil.isEmpty(aa.id)) {
				return new Response().failure("地址id为空!");
			}
			Address ad = addressService.getAddress(aa.id);
			if (ad == null) {
				return new Response().failure("地址id不正确!");
			}
			ad.setProvinceId(aa.provinceId);
			ad.setCityId(aa.cityId);
			ad.setAreaId(aa.areaId);
			ad.setProvinceName(aa.provinceName);
			ad.setCityName(aa.cityName);
			ad.setAreaName(aa.areaName);
			ad.setHabitatAddress(aa.habitatAddress);
			boolean b = addressService.modifyAddress(ad);
			if (b) {
				Map<String, Address> map = new HashMap<String, Address>();
				map.put("address", ad);
				return new Response().success(map);
			}
			return new Response().failure("修改地址失败!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}

	/**
	 * 更新物流宝版本号
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkupdate", method = RequestMethod.POST)
	public Response checkUpdate(@RequestBody byte[] by) {
		try {
			VersionForm vf = (VersionForm) new VersionForm().getObject(by);
			Map<String, Object> paramMap = new HashMap<>();
			// 类型(0:运输IOS;1:运输Android;2:产生IOS;3:产生Android)
			if (!StringUtil.isEmpty(vf.type)) {
				Version version = versionService.getnewVersion(vf.type);
				if (version == null) {
					// 后台暂未发布APP的最新版本
					paramMap.put("statusCode", "2");
					version = new Version();
				} else if (StringUtil.isEmpty(vf.num) || !vf.num.equals(version.getNum())) {
					// 用户当前版本为旧版本
					paramMap.put("statusCode", "0");
				} else {
					// 用户当前版本为最新版本
					paramMap.put("statusCode", "1");
				}
				paramMap.put("Version", version);
				return new Response().success(paramMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response().failure();
	}

	/**
	 * 商品议价信息减一接口(可传多个id)
	 * 
	 * @param wftransportIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/barCount/{wftransportIds}", method = RequestMethod.GET)
	public Response barCount(@PathVariable String wftransportIds) {
		if (StringUtil.isEmpty(wftransportIds)) {
			return new Response().failure("商品id为空!");
		}
		// 只传一个id
		if (wftransportIds.indexOf(",") == -1) {
			Wftransport wftransport = service.getWftransportById(wftransportIds);
			if (wftransport != null && wftransport.getBarCount() > 0) {
				wftransport.setBarCount(wftransport.getBarCount() - 1);
				service.updateWftransport(wftransport);
				return new Response().success("议价消息已减一");
			}
		} else {
			// 多个id
			String[] wftransportId = wftransportIds.split(",");
			for (String id : wftransportId) {
				Wftransport wftransport = service.getWftransportById(id);
				if (wftransport != null && wftransport.getBarCount() > 0) {
					wftransport.setBarCount(wftransport.getBarCount() - 1);
					service.updateWftransport(wftransport);
				}
			}
			return new Response().success("议价消息已减一");
		}
		return new Response().failure("议价消息减一失败!");
	}

	/**
	 * 添加关注
	 * @param friendId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addAndDelAttention/{friendId}",method=RequestMethod.GET)
	public Response addAttention(@PathVariable String friendId,HttpServletRequest request) {

		if (StringUtil.isEmpty(friendId)) {
			return new Response().failure("关注用户的id为空!");
		}
		//查询用户的所有关注对象id
		List<Attention> allAttention = attentionService.getAllAttention(null, request);
		for (Attention attention : allAttention) {
			if(friendId.equals(attention.getFriendId())){
				boolean b = attentionService.deleteAttention(friendId, request);
				if(b){
					//根据关注用户的id查询该用户发布的信息,并将发布的信息改为取消关注
					List<Wftransport> list = wftransportService.getByStoreId(friendId, "");
					for (Wftransport wftransport : list) {
						wftransport.setIsAttention("0");
						wftransportService.updateWftransport(wftransport);
					}
				
				   return new Response().success("取消关注成功!!");
				}
			}
		}
		
		boolean b = attentionService.addAttention(friendId, request);
		if(b){
			
			//根据关注用户的id查询该用户发布的信息,并将发布的信息改为已关注
			List<Wftransport> list = wftransportService.getByStoreId(friendId, "");
			for (Wftransport wftransport : list) {
				wftransport.setIsAttention("1");
				wftransportService.updateWftransport(wftransport);
			}
		
			
//			//消息推送
//			try {
//				//1.通过关注对象id得到registrationID
//				User attentionUser = (User) userdao.get(friendId);
//				String registrationID = attentionUser.getRegistrationID();
//				Map<String,String> dataMap = new HashMap<String,String>();
//				if(registrationID!=null){
//				  JGUtil.wlbAPPsendMessages(registrationID, dataMap,"有用户关注了你!");
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			return new Response().success("关注成功!");
		}
		return new Response().failure("关注失败!");
	}
	
//	/**
//	 * 取消对某个用户的关注
//	 * @param friendId
//	 * @param request
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/deleteAttention/{friendId}",method=RequestMethod.GET)
//	public Response deleteAttention(@PathVariable String friendId,HttpServletRequest request){
//		if (StringUtil.isEmpty(friendId)) {
//			return new Response().failure("取消关注用户的id为空!");
//		}
//		boolean b = attentionService.deleteAttention(friendId, request);
//		if(b){
//			return new Response().success("取消关注成功!");
//		}
//		
//		return new Response().failure("取消关注失败!");
//	}
	
	/**
	 * 得到我关注所有用户的信息
	 * @param currentpageStr
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllAttention/{currentpageStr}",method=RequestMethod.GET)
	public Response getAllAttention(@PathVariable String currentpageStr,HttpServletRequest request){
		Pager page = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		
		List<Attention> allAttention = attentionService.getAllAttention(page, request);
		//获取被关注用户的详细信息
		//新建一个list集合用来返回数据
		List<WlbUser> list=new ArrayList<WlbUser>();
		for (Attention attention : allAttention) {
			String friendId = attention.getFriendId();
			//根据id查询用户信息和资质认证信息
			try {
				User user = (User) userdao.get(friendId);                                                                             
				Aptitude aptitude = userdao.getUserAptitude(friendId);
				if(aptitude==null){
					aptitude=new Aptitude();
				}
				if(user==null){
					user=new User();
				}
				WlbUser wlbUser = new WlbUser(user, aptitude, null);
				list.add(wlbUser);
			} catch (Exception e) {
				e.printStackTrace();
				return new Response().failure("获取用户信息失败!");
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("page", page);
		map.put("allAttention", list);
		return new Response().success(map);
	}
	
	/**
	 * 得到我关注过的用户推送给我消息列表
	 * @param currentpageStr
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTuiSongMessage/{currentpageStr}",method=RequestMethod.GET)
	public Response getTuiSongMessage(@PathVariable String currentpageStr,HttpServletRequest request){
		//1.得到我关注用户的id
		List<Attention> allAttention = attentionService.getAllAttention(null, request);
		//新建一个list存放查询得到的信息
		List<Wftransport> newList=new ArrayList<Wftransport>();
		for (Attention attention : allAttention) {
			//我关注用户的id
			String friendId = attention.getFriendId();
			//我关注的时间
			String createTime = attention.getCreateTime();
			//2.根据用户的id查询该用户发布的已经审核通过的车货信息
		    List<Wftransport> wfts =wftransportService.getByStoreId(friendId, "1");
			for (Wftransport wftransport : wfts) {
				//得到审核时间,如果关注时间小于审核时间,则放入信息
				String checkTime = wftransport.getCheckTime();
				if(createTime.compareTo(checkTime)<0){
					newList.add(wftransport);
				}
			}
		}
		
		//按checkTime排序
		Comparator<Wftransport> comparator = new Comparator<Wftransport>(){
			public int compare(Wftransport w1, Wftransport w2) {
				int result;
		        long longstr1 = Long.valueOf(w1.getCheckTime().replaceAll("[-\\s:]",""));
		        long longstr2 = Long.valueOf(w2.getCheckTime().replaceAll("[-\\s:]",""));
		        if (longstr1 < longstr2){
					result = -1;
				}else{
					result = 1;
				}
				return result;
		   }
		};
		try{
			Collections.sort(newList,comparator);
		}catch (Exception e) {
			e.printStackTrace();
		}	

	    //再将数据包装后返回
		List<Object> MessageList=new ArrayList<Object>();
	 	 for (Wftransport wftransport : newList) {
	 		try {
				if(wftransport.getType()==1){
					MessageList.add(new HuoResponse(wftransport,new HuoListForm(),request));
				}else if(wftransport.getType()==0){
					MessageList.add(new CarSource(wftransport, new CheListForm(),request));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	 	Map<String,Object> map = new HashMap<String,Object>();
		//因为是从多个地方获得的数据,自己手工分页
	 	Pager page = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		//总记录数
		int rows=MessageList.size();
		//每页显示的记录数
		int pageCount=10; 
		//页数
		int sum=(rows-1)/pageCount+1;
		//计算需要显示的结果数据
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(pageCount);
		page.setTotalRow(rows);
		page.setTotalPage(sum);
		map.put("page", page);
		List<Object> flightPageList = new ArrayList<Object>();
		for (int i = ((currentPage-1) * pageCount); 
		i < MessageList.size() && i < ((currentPage) * pageCount) && currentPage > 0; i++) {
		  flightPageList.add(MessageList.get(i));	
		}
		map.put("attentionMessage", flightPageList);
		return new Response().success(map);
	
		
		
	}
	

}
