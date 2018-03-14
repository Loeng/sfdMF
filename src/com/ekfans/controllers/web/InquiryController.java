package com.ekfans.controllers.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.service.IInquiryService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.controllers.web.vo.InquiryAttributeVO;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 询价Controller
 * 
 * @ClassName: IInquiryService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class InquiryController {

	private Logger log = LoggerFactory.getLogger(InquiryController.class);
	@Resource
	private IInquiryService inquiryService;
	/**
	* @Title: saveBuyerInq
	* @Description: TODO(保存核心企业议价信息)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param vo
	* @param req
	* @return Boolean 返回类型
	* @throws
	 */
	@RequestMapping(value = "/web/inquiry/saveBuyerInq")
	public Boolean saveBuyerInq(@ModelAttribute InquiryAttributeVO vo, HttpServletRequest req){
		return this.inquiryService.saveBuyerInq(vo.getInq());
	}
	@RequestMapping(value="/web/inquiry/saveSupplyInq")
	@ResponseBody
	public Object saveInq(Inquiry in,HttpSession session){
	    if(in == null) return "3";
	    in.setCreateTime(DateUtil.getSysDateTimeString());
	    User user = (User) session.getAttribute(SystemConst.USER);
	    in.setBuyersId(user.getId());
	    return inquiryService.saveBuyerInq(in);
	}
	/**
	 * 
	 * @Title: 询价列表
	 * @Description: TODO()
	 * 详细业务流程:
	 * (详细描述此方法相关的业务处理流程)
	 * @param 
	 * @return 
	 * @throws
	 */
	@RequestMapping(value = "/store/inquiry/list/{productType}/{supplyType}/{sourceType}")
	public String listInquiry(@PathVariable String productType, @PathVariable String supplyType,  @PathVariable String sourceType, HttpServletRequest request){
		//从缓存中读取Store
		Store store =  (Store) request.getSession().getAttribute(SystemConst.STORE);
		String storeId = store.getId();
		//获取页面参数
		String pageNum = (String) request.getParameter("pageNum");
		String linkPerson = (String) request.getParameter("linkPerson");
		String storeName = (String) request.getParameter("storeName");
		String status = (String) request.getParameter("status");
		//定义分页
		Pager pager = new Pager();
		if(!StringUtil.isEmpty(pageNum)){
			int currentpage = Integer.parseInt(pageNum);
			pager.setCurrentPage(currentpage);
		}
		pager.setRowsPerPage(20);
		List<Inquiry> inquirys = inquiryService.getInquiryList(pager, storeId, status, supplyType, productType, sourceType, linkPerson, storeName);
		//返回页面的值
		request.setAttribute("productType", productType);
		request.setAttribute("supplyType", supplyType);
		request.setAttribute("sourceType", sourceType);
		request.setAttribute("inquirys", inquirys);
		request.setAttribute("linkPerson", linkPerson);
		request.setAttribute("storeName", storeName);
		request.setAttribute("status", status);
		request.setAttribute("pager", pager);
		return "/userCenter/store/inquiry/inquiryList";
	}
	/**
	 * 关闭询价
	 */
	@RequestMapping(value="/store/inquiry/closeInquiry/{id}")
	public@ResponseBody Boolean closeInquiry(@PathVariable String id){
		Inquiry inq = inquiryService.getById(id);
		inq.setStatus("1");
		if(inquiryService.updateInquiry(inq)){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @Title: 询价列表
	 * @Description: TODO()
	 * 详细业务流程:
	 * (详细描述此方法相关的业务处理流程)
	 * @param 
	 * @return 
	 * @throws
	 */
	@RequestMapping(value = "/system/inquiry/list/{productType}/{supplyType}/{sourceType}")
	public String systemListInquiry(@PathVariable String productType, @PathVariable String supplyType,  @PathVariable String sourceType, HttpServletRequest request){
		//获取页面参数
		String pageNum = (String) request.getParameter("pageNum");
		String linkPerson = (String) request.getParameter("linkPerson");
		String status = (String) request.getParameter("status");
		String storeName = (String) request.getParameter("storeName");
		//定义分页
		Pager pager = new Pager();
		if(!StringUtil.isEmpty(pageNum)){
			int currentpage = Integer.parseInt(pageNum);
			pager.setCurrentPage(currentpage);
		}
		pager.setRowsPerPage(20);
		List<Inquiry> inquirys = inquiryService.getSystemInquiryList(pager, storeName, status, supplyType, productType, sourceType, linkPerson);
		//返回页面的值
		request.setAttribute("productType", productType);
		request.setAttribute("supplyType", supplyType);
		request.setAttribute("sourceType", sourceType);
		request.setAttribute("inquirys", inquirys);
		request.setAttribute("linkPerson", linkPerson);
		request.setAttribute("storeName", storeName);
		request.setAttribute("status", status);
		request.setAttribute("pager", pager);
		return "/system/inquiry/inquiryList";
	}
	/**
	 * 后台关闭询价
	 */
	@RequestMapping(value="/system/inquiry/closeInquiry/{id}")
	public@ResponseBody Boolean systemCloseInquiry(@PathVariable String id){
		Inquiry inq = inquiryService.getById(id);
		inq.setStatus("1");
		if(inquiryService.updateInquiry(inq)){
			return true;
		}
		return false;
	}
	
}
