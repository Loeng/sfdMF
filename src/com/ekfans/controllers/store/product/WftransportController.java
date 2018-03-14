package com.ekfans.controllers.store.product;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.Commend;
import com.ekfans.base.store.dao.IWftransportDao;
import com.ekfans.base.store.model.CategoryGoods;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.TankMaterial;
import com.ekfans.base.store.model.VehicleType;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.store.service.ICategoryGoodsService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.store.service.ITankMaterialService;
import com.ekfans.base.store.service.IVehicleTypeService;
import com.ekfans.base.store.service.IWfpysService;
import com.ekfans.base.store.service.IWftransportService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.ccwccApp.Response;
import com.ekfans.plugin.wftong.util.HxUtil;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IAttentionService;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.JGUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @ClassName: WftransportController  
 * @Description: TODO(危废运输Controller) 
 * @Copyright: Copyright (c) 2016年3月15日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年3月15日下午6:07:11
 * @version 1.0
 */
@Controller
public class WftransportController extends BasicController{

	@Autowired
	private IWftransportService service;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private ISystemAreaService areaService;
	@Autowired
	private ICategoryGoodsService goodsService;
	@Autowired
	private IVehicleTypeService vehicleTypeService;
	@Autowired
	private IWfpysService wfpysService;
	@Autowired 
	private ITankMaterialService tankMaterialService;
	@Autowired
	private IWlbAppUserDao userdao;
	
	@Autowired
	private IWftransportDao dao;
	@Autowired
	private IAttentionService attentionService;
	/**
	 * @Title add
	 * @Description TODO(跳转到添加页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:07:54
	 */
	@RequestMapping("/store/wftransport/add/{type}")
	public String add(@PathVariable String type,HttpServletRequest request){
		// 查找所有车辆类型
		List<VehicleType> vts = vehicleTypeService.getAllVehicleType();
		// 查找货物类别
		List<CategoryGoods> categoryGoods = goodsService.getCategoryGoodsList();
		// 危废品分类标准
		List<Wfpys> wfpys = wfpysService.getList(true, null,null);
		//查找罐体材质
		List<TankMaterial> tms = tankMaterialService.getList();
		
		request.setAttribute("vts", vts);
		request.setAttribute("wfpys", wfpys);
		request.setAttribute("tms", tms);
		request.setAttribute("type", type);
		request.setAttribute("categoryGoods", categoryGoods);
		request.setAttribute("method", "add");
		return "/userCenter/store/product/wfysAdd";
	}
	
	/**
	 * @Title save
	 * @Description TODO(保存危废运输信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param wftransport
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:13:49
	 */
	@RequestMapping("/store/wftransport/save")
	public @ResponseBody boolean save(Wftransport wftransport,HttpServletRequest request){
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if(store != null){
			wftransport.setStoreId(store.getId());
			wftransport.setStoreName(store.getStoreName());
		}
		// 单位空默认为:吨
		if(StringUtil.isEmpty(wftransport.getUnit())){
			wftransport.setUnit("吨");
		}
		// 设置时间
		wftransport.setCreateTime(DateUtil.getSysDateTimeString());
		wftransport.setUpdateTime(DateUtil.getSysDateTimeString());
		return service.add(wftransport);
	}
	
	
	/**
	 * @Title save
	 * @Description TODO(保存危废运输信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param wftransport
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:13:49
	 */
	@RequestMapping("/web/wftransport/ykSave")
	public @ResponseBody boolean ykSave(Wftransport wftransport,HttpServletRequest request){
		if(wftransport.getType() == 0){
			wftransport.setStoreName("车主");
		}else{
			wftransport.setStoreName("货主");
		}
		// 单位空默认为:吨
		if(StringUtil.isEmpty(wftransport.getUnit())){
			wftransport.setUnit("吨");
		}
		// 设置时间
		wftransport.setCreateTime(DateUtil.getSysDateTimeString());
		wftransport.setUpdateTime(DateUtil.getSysDateTimeString());
		return service.add(wftransport);
	}
	
	/**
	 * @Title getWftransport
	 * @Description TODO(根据ID查询运输信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:22:48
	 */
	@RequestMapping("/store/wftransport/getWftransport/{id}/{type}")
	public String getWftransport(@PathVariable String id,@PathVariable String type, HttpServletRequest request){
		Wftransport wftransport = (Wftransport) service.getWftransportById(id);
		request.setAttribute("wftransport", wftransport);
		if(type.equals("show")){
			request.setAttribute("show", "show");
		}else{
			request.setAttribute("show", "edit");
		}
		// 查找所有车辆类型
		List<VehicleType> vts = vehicleTypeService.getAllVehicleType();
		// 查找货物类别
		List<CategoryGoods> categoryGoods = goodsService.getCategoryGoodsList();
		// 危废品分类标准
		List<Wfpys> wfpys = wfpysService.getList(true, null,null);
		//查找罐体材质
		List<TankMaterial> tms = tankMaterialService.getList();
		request.setAttribute("vts", vts);
		request.setAttribute("wfpys", wfpys);
		request.setAttribute("tms", tms);
		request.setAttribute("categoryGoods", categoryGoods);
		request.setAttribute("method", "edit");
		return "/userCenter/store/product/wfysView";
	}
	
	/**
	 * @Title updateWftransport
	 * @Description TODO(修改) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param wftransport
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:24:15
	 */
	@RequestMapping("/store/wftransport/updateWftransport")
	public @ResponseBody boolean updateWftransport(Wftransport wftransport){
		// 当用户修改之后,需要重新审核
		if(wftransport.getCheckStatus() == 1){
			wftransport.setCheckStatus(0);
		}
		// 更新时间
		wftransport.setUpdateTime(DateUtil.getSysDateTimeString());
		return service.updateWftransport(wftransport);
	}
	
	
	/**
	 * @Title deleteByIds
	 * @Description TODO(根据ID删除) 详细业务流程: (可批量删除)
	 * @param request
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:26:32
	 */
	@RequestMapping("/store/wftransport/deleteById/{id}")
	public @ResponseBody boolean deleteByIds(@PathVariable String id,HttpServletRequest request){
		return service.deleteWftransportById(id);
	}
	
	/**
	 * @Title getList
	 * @Description TODO(按条件搜索运输列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:27:22
	 */
	@RequestMapping("/store/wftransport/getList/{type}")
	public String getList(@PathVariable String type,HttpServletRequest request){
		String name = request.getParameter("name");
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		String checkStatus = request.getParameter("checkStatus");
		if(!StringUtil.isEmpty(checkStatus)){
			if(checkStatus.equals("未审核")){
				checkStatus = "0";
			}else{
				checkStatus = "1";
			}
		}
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
		List<Wftransport> list = service.getList(page, null, null, name, type, store != null ? store.getId() : null, checkStatus, null, null, null, null, null, true);
		List<CarSource> li = new ArrayList<CarSource>();
		for(Wftransport w : list){
			li.add(new CarSource(w));
		}
		request.setAttribute("li", li);
		request.setAttribute("list", list);
		request.setAttribute("pager", page);
		request.setAttribute("name", name);
		request.setAttribute("type", type);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("checkStatus", checkStatus);
		return "/userCenter/store/product/wfysList";
	}
	/**
	 * -------------------------------------------------------------------------------------------------------
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping("/store/wftransport/getList/{type}/{status}")
	public String getList(@PathVariable String type,@PathVariable String status,HttpServletRequest request){
		String name = request.getParameter("name");
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
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
		try {
			List<Wftransport> list = null;
			if(!StringUtil.isEmpty(name)){
				list = service.getList(page, null, null, name, type, store != null ? store.getId() : null, null, null, null, null, null, null, true);
			}else{
			    list=dao.getUserWftransport(store != null ? store.getId() : null,Integer.parseInt(type),page,status);
			}
			List<Commend> li = new ArrayList<Commend>();
			for(Wftransport w : list){
				li.add(new Commend(w));
			}
			request.setAttribute("li", li);
			request.setAttribute("time", DateUtil.getSysDateTimeString());
			request.setAttribute("list", list);
			request.setAttribute("pager", page);
			request.setAttribute("name", name);
			request.setAttribute("type", type);
			request.setAttribute("status", status);
			request.setAttribute("currentPage", currentPage);
			return "/userCenter/store/product/wfysList";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/userCenter/store/product/wfysList";
	}
	
	/**
	 * 商品上下架
	 * @param id
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping("/store/wftransport/shelvesProduct/{id}/{status}")
	public @ResponseBody boolean shelvesProduct(@PathVariable String id,@PathVariable String status,HttpServletRequest request){
		Wftransport wftransport = service.getWftransportById(id);
	    if(wftransport!=null&&!StringUtil.isEmpty(status)){
	    	wftransport.setStatus(Integer.parseInt(status));
	    	// 更新时间
	    	wftransport.setUpdateTime(DateUtil.getSysDateTimeString());
			 boolean b = service.updateWftransport(wftransport);
			 if(b){
				 return true;
			 }else{
				 return false; 
			 }
	    }
		         return false;
	}
	
	/**
	 * @Title newWfList
	 * @Description TODO(查看最新运输信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param type
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016上午10:57:06
	 */
	@RequestMapping("/web/wftransport/newWfList/{type}")
	public String newWfList(@PathVariable String type, HttpServletRequest request){
		List<Wftransport> wftransports = service.getNewList(type);
		request.setAttribute("wftransports", wftransports);
		return "/web/channel/newList";
	}
	
	
	/**
	 * @Title getList
	 * @Description TODO(按条件搜索运输列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:27:22
	 */
	@RequestMapping("/web/wftransport/getWftransportList/{type}/{channel}")
	public String getWftransportList(@PathVariable String type, @PathVariable String channel, HttpServletRequest request){
		// 起始地
		String starPlace = request.getParameter("starPlace");
		// 目的地
		String endPlace = request.getParameter("endPlace");
		// 搜索全部
		if("all".equals(endPlace)){
			endPlace = "";
		}
		// 首页搜索目的地
		String endPlaceForIndex = request.getParameter("endPlaceForIndex");
		// 名称
		String name = request.getParameter("pName");
		// 车辆类型
		String carName = request.getParameter("carName");
		// 罐体材质
		String tankMaterialName = request.getParameter("tankMaterialName");
		// 货物类别名称
		String categoryName = request.getParameter("categoryName");
		// 分类Id
		String wfpysParentId = request.getParameter("wfpysParentId");
		
		// 转码后的字符串
		String str = "";
		try {
			// 参数不为空or不为undefined 转码
			if(!StringUtil.isEmpty(name) && !"undefined".equals(name)){
				// 转码
				str = new String(name.getBytes("ISO-8859-1"),"utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		// 根据地区ID查询
		if(!StringUtil.isEmpty(endPlaceForIndex) && StringUtil.isEmpty(endPlace)){
			SystemArea area = areaService.getSystemAreaById(endPlaceForIndex);
			// 获取地区名称
			endPlace = area.getAreaName();
		}
		Pager page = new Pager();
		// 从页面获取页码
		String pageNum = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			currentPage = Integer.parseInt(pageNum);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		List<Wftransport> wftransports = service.getList(page, starPlace, endPlace, str, type, null, "1", "1",carName, tankMaterialName, categoryName, wfpysParentId, true);
		request.setAttribute("wftransports", wftransports);
		request.setAttribute("pager", page);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("starPlace", starPlace);
		request.setAttribute("endPlace", endPlace);
		if("0".equals(channel)){
			return "/web/channel/hyList";
		}else if("1".equals(channel)){
			return "/web/channel/wfcyList";
		}else{
			return "/web/channel/wfhyList";
		}
	}
	
	/**
	 * @Title getwf
	 * @Description TODO(车源议价弹出层) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:16:12
	 */
	@RequestMapping("/web/wftransport/getwf/{id}/{type}")
	public String getwf(@PathVariable String id,@PathVariable String type,HttpServletRequest request){
		Wftransport wftransport = service.getWftransportById(id);
		request.setAttribute("wftransport", wftransport);
		// 0 议价 1询价
		request.setAttribute("type", type);
		return "/web/channel/wfys/loadWf";
	}
	
	/**
	 * @Title getIndexWf
	 * @Description TODO(首页获取危废运输列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param type
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午4:20:15
	 */
	@RequestMapping("/web/wftransport/getIndexWf/{type}")
	public String getIndexWf(@PathVariable String type,HttpServletRequest request){
		String endFullPath =request.getParameter("endFullPath");
		Pager page = new Pager();
		page.setCurrentPage(1);
		page.setRowsPerPage(3);
		List<Wftransport> wftransports = service.getList(page, null, endFullPath, null, type, null, "1", "1", null, null, null, null, true);
		request.setAttribute("wftransports", wftransports);
		if("0".equals(type)){
			return "/web/channel/loadCy";
		}else{
			return "/web/channel/loadHy";
		}
	}
	
	/**
	 * @Title release
	 * @Description TODO(游客发布运输信息弹出层) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午4:20:50
	 */
	@RequestMapping("/web/wftransport/release")
	public String release(HttpServletRequest request){
		// 查找所有车辆类型
		List<VehicleType> vts = vehicleTypeService.getAllVehicleType();
		// 查找货物类别
		List<CategoryGoods> categoryGoods = goodsService.getCategoryGoodsList();
		// 危废品分类标准
		List<Wfpys> wfpys = wfpysService.getList(true, null,null);
		//查找罐体材质
		List<TankMaterial> tms = tankMaterialService.getList();
		
		request.setAttribute("vts", vts);
		request.setAttribute("wfpys", wfpys);
		request.setAttribute("tms", tms);
		request.setAttribute("categoryGoods", categoryGoods);
		return "/web/channel/wfys/releaseWf";
	}
		
	// ============================================================ 后台功能 =============================================//
	
	/**
	 * @Title checkWftransport
	 * @Description TODO(审核运输信息) 详细业务流程: (后台审核运输信息)
	 * @param wftransport
	 * @param request
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:43:19
	 */
	@RequestMapping("/system/wftransport/checkWftransport")
	public String checkWftransport(Wftransport wftransport, HttpServletRequest request){
		Manager manager =(Manager) request.getSession().getAttribute(SystemConst.MANAGER);
		Wftransport w = service.getWftransportById(wftransport.getId());
		w.setCheckMan(manager.getRealName());
		w.setCheckTime(DateUtil.getSysDateTimeString());
		w.setEndTime(wftransport.getEndTime());
		w.setContent(wftransport.getContent());
		w.setCheckNote(wftransport.getCheckNote());
		w.setCheckStatus(wftransport.getCheckStatus());
		service.updateWftransport(w);
		try {
			//获取该商品的用户信息
			User user = (User) userdao.get(wftransport.getStoreId());
			if(user.getHxUserName()!=null){
			//物流宝极光推送车货源审核结果信息
			Map<String,String> dataNode = new HashMap<String,String>();
			//messageType:"3",为商品车货源审核信息
			dataNode.put("messageType", "3");
			//审核状态 1:通过 2:未通过
			dataNode.put("checkType", wftransport.getCheckStatus()+"");
			//审核备注
			dataNode.put("remarks", wftransport.getCheckNote());
			//商品id
			dataNode.put("id", wftransport.getId());
			//推送极光消息
			if(w.getType()==0){
				JGUtil.wlbAPPsendMessages(user.getRegistrationID(),dataNode,"您有一条车源审核消息!");
			}else{
			    JGUtil.wlbAPPsendMessages(user.getRegistrationID(),dataNode,"您有一条货源审核消息!");
			}
			//用户消息审核通过后推送给关注他的用户
			if(1==wftransport.getCheckStatus()){
				//推送的信息
				Map<String,String> dataMap = new HashMap<String,String>();
				dataMap.put("messageType", "5");
				//公司名称
				dataMap.put("nickName", user.getNickName());
				//商品id
				dataMap.put("id", wftransport.getId());
				//发布信息的名称
				dataMap.put("supplyName",wftransport.getSupplyName());
				//得到关注过该用户的id
				List<String> userIds = attentionService.getUserId(user.getId());
				if(userIds!=null){
					for (String userId : userIds) {
						//根据id得到关注者信息
						User attentionUser = (User) userdao.get(userId);
						String registrationID = attentionUser.getRegistrationID();
						if(registrationID!=null){
						  JGUtil.wlbAPPsendMessages(registrationID, dataMap, user.getNickName()+"发布了新消息,赶快看看吧!");
						}
					}
				}
			}
			
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		return "redirect:/sysytem/wftransport/getListToSystem/" + wftransport.getType();
	}

	
	/**
	 * @Title getListToSystem
	 * @Description TODO(后台查看运输列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午6:47:55
	 */
	@RequestMapping("/sysytem/wftransport/getListToSystem/{type}")
	public String getListToSystem(@PathVariable String type, HttpServletRequest request){
		String startPlace = (String) request.getParameter("startPlace");
		String endPlace = request.getParameter("endPlace");
		String name = request.getParameter("name");
		String isStore = request.getParameter("isStore");
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
		List<Wftransport> list = service.getSysList(page, startPlace, endPlace, name, type, isStore, "0", null);
		request.setAttribute("list", list);
		request.setAttribute("pager", page);
		request.setAttribute("startPlace", startPlace);
		request.setAttribute("endPlace", endPlace);
		request.setAttribute("name", name);
		request.setAttribute("isStore", isStore);
		request.setAttribute("type", type);
		return "/system/wftransport/wfysList";
	}
	
	/**
	 * 后台危废运输货源管理(查看货源列表)
	 * @date 2017年8月24日09:47:14
	 * @param request
	 * @return
	 */
	@RequestMapping("/sysytem/wftransport/wfysGL/{ty}")
	public String getWfysHG(HttpServletRequest request,@PathVariable String ty){
		String name = request.getParameter("name");
		String type = request.getParameter("type");
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
		List<Wftransport> list=null;
		//全部货源
		if(type==null||type.equals("")){
			 list = service.getSysList(page, null, null, name, ty, "", "", null);
		}
		//企业发布的货源
		if(type!=null&&type.equals("1")){
			 list = service.getSysList(page, null, null, name, ty, "store", "", null);
		}
		//游客发布的货源
		if(type!=null&&type.equals("2")){
			 list = service.getSysList(page, null, null, name, ty, "tourist", "", null);
		}
		//上架
		if(type!=null&&type.equals("3")){
			 list = service.getSysList(page, null, null, name, ty, "", "", "1");
		}
		//下架
		if(type!=null&&type.equals("4")){
			 list = service.getSysList(page, null, null, name, ty, "", "", "0");
		}
		//已审核
		if(type!=null&&type.equals("5")){
			 list = service.getSysList(page, null, null, name, ty, "", "1", null);
		}
		//未审核
		if(type!=null&&type.equals("6")){
			 list = service.getSysList(page, null, null, name, ty, "", "0", null);
		}
		//审核未通过
		if(type!=null&&type.equals("7")){
			 list = service.getSysList(page, null, null, name, ty, "", "2", null);
		}
		request.setAttribute("ty", ty);
		request.setAttribute("list", list);
		request.setAttribute("pager", page);
		request.setAttribute("name", name);
		request.setAttribute("type", type);
		return "/system/wftransport/wfysGLList";
	}

	
	/**
	 * @Title check
	 * @Description TODO(审核运输信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016上午9:46:21
	 */
	@RequestMapping("/sysytem/wftransport/check/{id}/{type}")
	public String checkwftransport(@PathVariable String id, @PathVariable String type, HttpServletRequest request){
		Wftransport wftransport = service.getWftransportById(id);
		request.setAttribute("wftransport", wftransport);
		return "/system/wftransport/wfysView";
	}
	/**
	 * 查看商品详情 
	 * @param id
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping("/sysytem/wftransport/see/{id}/{type}")
	public String seewftransport(@PathVariable String id, @PathVariable String type, HttpServletRequest request){
		Wftransport wftransport = service.getWftransportById(id);
		request.setAttribute("wftransport", wftransport);
		return "/system/wftransport/wfysGLView";
	}
	
	
}
