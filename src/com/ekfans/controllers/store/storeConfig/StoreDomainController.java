package com.ekfans.controllers.store.storeConfig;

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
import com.ekfans.basic.controller.BasicController;

@Controller
public class StoreDomainController extends BasicController {
    @Autowired private IStoreService storeService;

    /**
     * 
    * @Title: getDomainInfo
    * @Description: TODO 用户获取登陆的Store对象的信息
    * 详细业务流程:通过session获取残缺的登陆对象Store，通过Store对象的ID查找数据库中完整的Store对象
    * 并传到前台隐藏域用于后续设置域名获取参数使用
    * (详细描述此方法相关的业务处理流程)
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/domain/setup")
    public String getDomainInfo(HttpSession session,HttpServletRequest request) {
	if(session == null) {
	    return "redirect:/store/login";
	}
	try {
	    // 获取登录对象
	    Store store = (Store)session.getAttribute(SystemConst.STORE);
	    // 通过ID查询数据库中的完整Store对象
	    Store storeReal = storeService.getStore(store.getId());
	    // 将完整Store对象传到前台供后面修改使用
	    request.setAttribute("store",storeReal);
	    return "store/config/domain/domainSetup";
	}
	catch(Exception e) {
	    e.printStackTrace();
	    
	}
	return "error";
    }

    /**
     * @throws Exception 
     * 
    * @Title: DomainSetUp
    * @Description: TODO 给店家设置域名
    * 详细业务流程: 从dispatcher Servlet 那获取的url解析 查找一个ModelAndView返回给servlet
    * (详细描述此方法相关的业务处理流程)
    * @param @param request 用户获取页面用户输入的域名
    * @param @param session 用户获取store对象
    * @param @return    设定文件
    * @return ModelAndView    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/domain/update")
    public String DomainSetUp(HttpServletRequest request) {
	// 利用service的方法更新数据库中的Store对象
	try {
	    String storeId = request.getParameter("storeId");
	    String domain = request.getParameter("domain");
	    Store store = storeService.getStore(storeId);
	    store.setDomain(domain);
	    if(storeService.updateDomain(store)) {	        
	        // 将完整Store对象传到前台供后面修改使用
	        request.setAttribute("store",store);
		// 给前端视图传递设置成功的参数
		request.setAttribute("updateOk","true");
	    }else{
		request.setAttribute("updateOk","false");
	    }
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	return "store/config/domain/domainSetup";
    }

    /**
     * @throws Exception 
     * 
    * @Title: check 检查域名是否存在
    * @Description: TODO 通过用户传入的域名信息从数据库检查是否有重复的域名店铺，有的话就提示重复，没有就提示可以添加
    * 详细业务流程: 从传入的的domain参数 检测是否有  已使用该域名的店铺
    * (详细描述此方法相关的业务处理流程)
    * @param @param domain  用户输入的域名信息
    * @param @return    返回一个JSON来 提示是否有重复域名，是否可以添加
    * @return Object    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/domain/exist/{domain}")
    @ResponseBody
    public Object check(@PathVariable String domain,HttpSession session) {
	if(session == null){
	    return "redirect:/store/login";
	}
	//获取登陆的对象
	Store store=(Store)session.getAttribute(SystemConst.STORE);
	// 根据Service的方法 根据用户输入的 domain 来查询是否有重复的域名
	try {
	    if(storeService.existDomain(domain,store.getId())) {
		// 可以添加，返回true
		
		return true;
	    }
	}catch(Exception e) {
	    e.printStackTrace();
	}
	return false;
    }
}
