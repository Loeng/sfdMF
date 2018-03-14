package com.ekfans.controllers.system.bargain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.order.service.IBargainService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemBargainController extends BasicController{

private Logger log = LoggerFactory.getLogger(SystemBargainController.class);
	
	@Autowired
	private IBargainService bargainService;
	@Autowired
	private IProductService productService;
	
	/**
	 * 后台查找议价列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/bargain/list/{type}")
	public String sysBargainList(@PathVariable String type, HttpServletRequest request){
		String productName = request.getParameter("productName"); //商品名
		String pageNum = request.getParameter("pageNum"); // 从页面获取页码
 		String fbType = request.getParameter("fbType"); //发布类型（游客或企业）
		
		// 定义分页
        Pager pager = new Pager();
        int currentPage = 1;
        if (!StringUtil.isEmpty(pageNum)) {
        	currentPage = Integer.parseInt(pageNum);
        }
        pager.setCurrentPage(currentPage);
        
        // 查找列表
        
        List<Bargain> bargains = null;
        if("cp".equals(type)){ //成品议价列表
        	 bargains = bargainService.getBargainList(pager, productName, null, null, null, null);
        	 request.setAttribute("fbType", fbType);
             request.setAttribute("type", type);
             request.setAttribute("bargains", bargains);
             request.setAttribute("productName", productName);
             request.setAttribute("pager", pager);
     		 return "/system/bargain/bargainList";
        }else{ //车源议价列表
        	bargains = bargainService.getSysBargainTransList(pager, productName, fbType);
        	request.setAttribute("fbType", fbType);
        	request.setAttribute("type", type);
        	request.setAttribute("bargains", bargains);
        	request.setAttribute("productName", productName);
        	request.setAttribute("pager", pager);
        	return "/system/bargain/transBargainList";
        }
        
	}
	
	/**
	 * 查看成品详情
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/bargain/detail/{id}")
	public String bargainDetail(@PathVariable String id,HttpServletRequest request){
		Bargain ba = bargainService.getDetailById(id);
		request.setAttribute("ba", ba);
		return "/system/bargain/bargainDetail";
	}
	
	/**
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/bargain/transDetail/{id}/{fbType}")
	public String transBargainDetail(@PathVariable String id,@PathVariable String fbType, HttpServletRequest request){
		if(("1").equals(fbType)){
			Bargain ba = bargainService.getWfDetailByIdFromYouKe(id);
			request.setAttribute("ba", ba);
		}else{
			Bargain ba = bargainService.getWfDetailById(id);
			request.setAttribute("ba", ba);
		}
		return "/system/bargain/transBargainDetail";
	}
	
}
