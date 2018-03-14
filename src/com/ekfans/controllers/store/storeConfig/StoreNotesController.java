package com.ekfans.controllers.store.storeConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;

@Controller
public class StoreNotesController extends BasicController {

    
    @Autowired
    private IStoreService storeService;
    
//    @RequestMapping(value="/store/toUpdateNotes")
//    public String toUpdate(HttpServletRequest request,HttpSession session) {
//	Store store = (Store)session.getAttribute(SystemConst.STORE);
//	request.setAttribute("store",store);
//	return "/store/toUpdateNotes";
//    }
    /**
     * 
    * @Title: 去往修改店铺描述页面
    * @Description: TODO 去往修改店铺描述页面
    * @param @param request 
    * @param @param session 
    * @param @return    设定文件
    * @return ModelAndView    返回类型
    * @throws
     */
    @RequestMapping(value="/store/toUpdateNotes")
    public String toUpdate(HttpServletRequest request ,HttpSession session) {
	Store s =(Store)session.getAttribute(SystemConst.STORE);
	Store store = storeService.getStore(s.getId());
	request.setAttribute("notes",store.getNotes());
	return "/store/config/toUpdateNotes";
    }
	
	/**
	* 修改店铺信息
	* @Title: modify
	* @Description: TODO(修改store里面的notes内容)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param @param shopRole
	* @param @param request
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value="/store/updateNotes")
	    public String update(HttpServletRequest request,HttpSession session){
		try {
		    //获取登陆成功的店铺对象
		    Store s =(Store)session.getAttribute(SystemConst.STORE);
		    Store store = storeService.getStore(s.getId());
		    //获取店铺的描述信息
		    String notes = request.getParameter("notes");
		    store.setNotes(notes);
		    if(storeService.updateNotes(store)){
		        request.setAttribute("updateOk",true);
		        request.setAttribute("notes",notes);
		        //添加成功返回
		        return "/store/config/toUpdateNotes";
		    }
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
		return  "error";
		 
	    }

}

