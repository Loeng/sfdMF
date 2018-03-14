package com.ekfans.controllers.store.storeConfig;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;

/**
 * 
 * 店铺商品Controller
 * 
 * @Title: StoreProductController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-23 下午05:23:47 
 * @version V1.0
 */
@Controller
public class StoreMapController extends BasicController {
	@Autowired
	private IStoreService storeService;

	/**
	 * 
	 * @param store 店铺中心的地图设置跳转页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/map/setup")
	public String list(HttpServletRequest request,
            HttpServletResponse response, HttpSession session) {
		try{
	    	// 从Session中获取store对象
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		//如果没有得到store对象，则返回登陆页面
		if(store == null){
		   return "/store/login";
		}
		//由store的id得到Store对象
		Store storeMap = storeService.getStore(store.getId());
		
//		storeMap.getCoordinateX();
//		storeMap.getCoordinateY();
		
			request.setAttribute("storeMap",storeMap);
			return "/store/map/StoreMap";
	}
	catch(Exception e){
		e.printStackTrace();
	}
		return "error";
	}
	
	/**
	 * 
	* @Title: updateMap
	* @Description: TODO 保存坐标
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param request
	* @param session
	* @return String 返回类型
	* @throws
	 */
	@RequestMapping(value="/store/map/updateMap")
	public String updateMap(HttpServletRequest request,HttpSession session){
	    try {
	        Store store = (Store) session.getAttribute(SystemConst.STORE);
	        Store storeMap = storeService.getStore(store.getId());
	        String x = request.getParameter("coordinateX");
	        String y = request.getParameter("coordinateY");
//	        storeMap.setCoordinateX(x);
//	        storeMap.setCoordinateY(y);
	        if (storeService.updateNotes(storeMap)) {
	            request.setAttribute("modifyOK", true);
	            request.setAttribute("storeMap",storeMap);
	            return "/store/map/StoreMap";
            }
	       
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return "error";
	}

}