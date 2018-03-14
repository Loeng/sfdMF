package com.ekfans.controllers.store.bargain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.order.service.IBargainService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;


/**
 * 议价controller
 * @ClassName StoreBargainController
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月16日
 */
@Controller
public class StoreBargainController extends BasicController {

	private Logger log = LoggerFactory.getLogger(StoreBargainController.class);
	
	@Autowired
	private IBargainService bargainService;
	@Autowired
	private IProductService productService;
	
	
	/**
	 * 加载议价商品信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/bargain/bargainDetail")
	public String loadBargain(HttpServletRequest request,HttpSession session){
		// 议价之前应查看所议价商品是否为自己所售商品
		String id = request.getParameter("id");
		Product p = productService.getProDteailById(id);
		request.setAttribute("p", p);
		return "/web/channel/xsgp/loadYijia";
	}
	
	/**
	 * 新增议价
	 * @param b
	 * @param session
	 * @return
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/store/bargain/add")
	public @ResponseBody String addBargain(@ModelAttribute Bargain b,HttpSession session,HttpServletRequest request){
		String sourceType = request.getParameter("sourceType"); // 获取来源类型：0商品 1货源
		User user = (User) session.getAttribute(SystemConst.USER); //获取缓存中登陆用户
		String userId = user.getId(); //获取当前用户id
		//如果卖家id与当前系统登录用户id相同 提示
		if(b.getSellerId().equals(userId)){ //不能用==
			return "error";
		}else{ 
			b.setType(sourceType);
			b.setBuyerId(userId);
			// 将订单议价状态设为议价中
			b.setStatus("0");
			// 下单状态设为未下单
			b.setOrderStatus("0");
			b.setCreateTime(DateUtil.getSysDateTimeString());
			boolean flag = bargainService.addBargain(b);
			
			if(flag){
				return "true";
			}else{
				return "false";
			}
		}
	}
	
	/**
	 * 查询议价列表
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/bargain/list/{sourceType}")
	public String bargainList(@PathVariable String sourceType, HttpServletRequest request, HttpSession session){
		User user = (User) session.getAttribute(SystemConst.USER); //获取缓存中登陆用户
		if(user == null){
			return "redirect:/web/login";
		}
		String userId = user.getId(); // 用户id
		String productName = request.getParameter("productName"); //商品名
		String contactMan = request.getParameter("contactMan"); //联系人
		String contactPhone = request.getParameter("contactPhone"); //联系电话
 		String pageNum = request.getParameter("pageNum"); // 从页面获取页码
 		String type = request.getParameter("type"); //页面传的议价类型（0我发布的议价，1别人给我的议价）
 		String userType = request.getParameter("userType"); //发布议价类型0企业 1游客
 		// 定义分页
        Pager pager = new Pager();
        int currentPage = 1;
        if (!StringUtil.isEmpty(pageNum)) {
        	currentPage = Integer.parseInt(pageNum);
        }
        pager.setCurrentPage(currentPage);
        
        // 查找议价列表
        List<Bargain> bargains = null;
        if("0".equals(sourceType)||"2".equals(sourceType)){
        	bargains = bargainService.getBargainList(pager, productName, contactMan, contactPhone, userId, type);
        }else{
        	bargains = bargainService.getBargainTransList(pager, productName, contactMan, contactPhone, userId, type,userType);
        }
		
        request.setAttribute("userType", userType);
        request.setAttribute("bargains", bargains);
        request.setAttribute("productName", productName);
        request.setAttribute("contactMan", contactMan);
        request.setAttribute("contactPhone", contactPhone);
        request.setAttribute("type", type);
        request.setAttribute("pager", pager);
        request.setAttribute("sourceType", sourceType);
        request.setAttribute("userId", userId);
        if("0".equals(sourceType)){
        	return "userCenter/store/bargain/bargainList";
        }else if("2".equals(sourceType)){
        	return "userCenter/store/bargain/greenBargainList";
        }else{
        	return "userCenter/store/bargain/wfcyBargainList";
        }
	}
	
	/**
	 * 核价根据id查找议价信息
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/bargain/auditDetail/{type}")
	public String bargainDetail(@PathVariable String type, HttpServletRequest request){
		
		String id = request.getParameter("id");
		Bargain ba = null;
		if("0".equals(type)){
			ba = bargainService.getDetailById(id);
		}else{
			ba = bargainService.getWfDetailById(id);
		}
		
		request.setAttribute("ba", ba);
		return "userCenter/store/bargain/loadHejia";
	}
	
	/**
	 * 议价详细信息
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/bargain/detail/{type}")
	public String detail(@PathVariable String type, HttpServletRequest request){
		
		String id = request.getParameter("id");
		String userType = request.getParameter("userType");
		Bargain ba = null;
		if("0".equals(type)||"2".equals(type)){
			ba = bargainService.getDetailById(id);
		}else{
			if("0".equals(userType)||StringUtil.isEmpty(userType)){
				ba = bargainService.getWfDetailById(id);
			}else{
				ba = bargainService.getWfDetailByIdFromYouKe(id);
			}
		}
		request.setAttribute("ba", ba);
		request.setAttribute("type", type);
		request.setAttribute("userType", userType);
		
		return "userCenter/store/bargain/loadDetail";
	}
	
	/**
	 * 保存核价信息
	 * @param b
	 * @return
	 */
	@RequestMapping(value = "/store/bargain/saveAudit")
	public @ResponseBody boolean auditSave(@ModelAttribute Bargain b){
		boolean flag = bargainService.saveAuditBargain(b);
		return flag;
	}
	
	/**
	 * 删除(取消)议价
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/bargain/delete")
	public @ResponseBody boolean delete(HttpServletRequest request){
		String id = request.getParameter("id");
		boolean flag = bargainService.deleteById(id);
		return flag;
	}
	
}
