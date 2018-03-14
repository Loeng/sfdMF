package com.ekfans.controllers.store.count;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

@Controller
public class StoreInfoSetupController extends BasicController {
    @Autowired
    IStoreService storeService;

    @Autowired
    IUserService userService;

    /**
     * 
    * @Title: StoreInfo
    * @Description: 获取商户的注册资料
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request  获取商户的注册资料
    * @param @param session  获取登录的店铺对象的信息
    * @param @return    设定文件
    * @return String    返回类型 返回修改页面 成功与否都给予提示框提示
    * @throws
     */
    @RequestMapping(value = "/store/storeInfo/select")
    public String StoreInfo(HttpServletRequest request,HttpSession session) {
	if(session == null){
	    return "/store/login";
	}
	try {
	 // 获取登录的Store对象
		Store store = (Store)session.getAttribute(SystemConst.STORE);
		// 通过ID获取User对象,因为Store的ID和User的ID一样,所以可以通用
		User user = userService.getUser(store.getId());
		// 通过登录Store对象的ID获取数据库里的Store对象
		Store storeReal = storeService.getStore(store.getId());
		
		String delimiter = Cache.getResource("area.name.delimiter");
        if(delimiter==null||"&nbsp;".equals(delimiter)){
            delimiter=" ";
        }
                //拼接显示 的地址
        /*
		StringBuffer area=new StringBuffer();
		if(!StringUtil.isEmpty(storeReal.getProvince())){
		    area.append(storeReal.getProvince()).append(delimiter);
		}
		if(!StringUtil.isEmpty(storeReal.getCity())){
		    area.append(storeReal.getCity()).append(delimiter);
		}
		if(!StringUtil.isEmpty(storeReal.getArea())){
		    area.append(storeReal.getArea()).append(delimiter);
		}
		storeReal.setProvince(area.toString());
		*/
		String returnType=(String) request.getParameter("returnType");
		// 将store对象和user对象的email信息和登录名信息传到前台
		request.setAttribute("userName",user.getName());
		request.setAttribute("userEmail",user.getEmail());
		request.setAttribute("store",storeReal);
		 request.setAttribute("updateOk",returnType);
		return "/store/count/storeInfoSetup";
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	   return "error";
    }

    /**
     * @throws Exception 
     * 
    * @Title: updateStoreInfo
    * @Description: TODO 更新商户的各种注册信息
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request 获取商户输入的信息
    * @param @param session 获取当前登录的Store对象
    * @param @return    设定文件
    * @return String    返回类型 当前页面并给提示
    * @throws
     */
    @RequestMapping(value = "/store/storeInfo/update")
    public String updateStoreInfo(Store store,HttpServletRequest request) {
	    
        //直接刷新 f5
        if(StringUtil.isEmpty(store.getId())){
             store=(Store) request.getSession().getAttribute(SystemConst.STORE);
            
        }
        
        boolean returnType=storeService.updateStoreInfo(store);
       
        // 将store对象和user对象的email信息和登录名信息传到前台
//        User user = userService.getUser(store.getId());
//        request.setAttribute("userName",user.getName());
//        request.setAttribute("userEmail",user.getEmail());
        // 添加成功将结果传到视图页面
	    
	    return "redirect:/store/storeInfo/select?returnType="+returnType;
	    //"/store/count/storeInfo/storeInfoSetup";
	
    }

    /**
     * @throws Exception 
     * 
    * @Title: check
    * @Description: TODO 检测是否有重复的店铺名
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程) 把用户输入的店铺名作为参数和数据库中的店铺名匹配看是否有重复的
    * @param @param storeName 输入的店铺名
    * @param @param session 获取登录对象
    * @param @return    设定文件
    * @return Object    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/storeInfo/exist/{name}")
    @ResponseBody
    public Object check(@PathVariable String name,HttpSession session){
	if(session==null){
	    return "/store/login";
	}
	//获取登陆对象
	Store store = (Store)session.getAttribute(SystemConst.STORE);
	// 根据Service的方法 根据用户输入的 storeName 来查询是否有重复的店铺名
	try {
	    if(storeService.existStoreName(name,store.getId())){
	        // 可以添加，返回true
	        return true;
	    }
	}
	catch(Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return false;
    }
}
