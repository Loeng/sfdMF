package com.ekfans.controllers.system.inquiry;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.service.IInquiryService;
import com.ekfans.base.system.model.ShopPurview;
import com.ekfans.base.system.model.ShopRole;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemGyInquiryController extends BasicController {
	// 定义Service
	@Autowired
	private IInquiryService inquiryService;

	
	/**
	 * 供应商的列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/gyInquiry/list")
	public String list(HttpServletRequest request){
	    String status = request.getParameter("status"); // 状态
        String name = request.getParameter("name"); // 商品名字
        String userId = request.getParameter("userId"); // 用户名
        String beginDate = request.getParameter("beginDate"); // 开始时间
        String endDate = request.getParameter("endDate"); // 结束时间
        String pageNum = request.getParameter("pageNum"); // 页码
	    Pager pager = new Pager();
        int currentPage = 1;
        if (!StringUtil.isEmpty(pageNum)) {
            try {
                currentPage = Integer.parseInt(pageNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pager.setCurrentPage(currentPage);
        
        List<Inquiry> inquirys = inquiryService.getSystemGyInquiryList(pager, status, name, userId, beginDate, endDate);
        
        // 绑定页面显示需要的数据
        request.setAttribute("status", status);
        request.setAttribute("pager", pager);
        request.setAttribute("name", name);
        request.setAttribute("userId", userId);
        request.setAttribute("inquirys", inquirys);
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("endDate", endDate);
        return "/system/inquiry/gyInquiryList";	
	}

	/**
	 * 跳转到详情
	 */
    @RequestMapping(value = "/system/gyInquiry/detail/{id}")
    public String query(@PathVariable String id, HttpServletRequest request) {
        Inquiry i = inquiryService.getById(id);
        request.setAttribute("i", i);
        return "/system/inquiry/gyInquiryDetail";
    }
}
