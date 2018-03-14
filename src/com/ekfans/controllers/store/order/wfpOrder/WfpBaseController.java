package com.ekfans.controllers.store.order.wfpOrder;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.ValuationCategory;
import com.ekfans.base.order.service.IValuationCategoryService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class WfpBaseController {
	@Autowired
	private IValuationCategoryService vcService;
	@RequestMapping(value="/system/store/order/wfpOrder/getVCList")
	public String getVCList(HttpServletRequest request){
		Pager pager = new Pager();
		// 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        //含量名称
        String contentName=request.getParameter("contentName");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            currentPage = Integer.parseInt(currentpageStr);
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        pager.setRowsPerPage(10);
        
        List<ValuationCategory> list=vcService.getVCByPage(pager, contentName);
        request.setAttribute("vcList", list);
        request.setAttribute("pager", pager);
        request.setAttribute("currentpageStr", currentpageStr);
        request.setAttribute("contentName", contentName);
        
		return "/system/order/wfpBase/wfpBaseList";
	}
	@RequestMapping(value="/system/wfpOrder/addVC/{specName}/{statusStr}")
	public String addVC(@PathVariable String specName,@PathVariable String statusStr,HttpServletRequest request){
		if(!StringUtil.isEmpty(specName)){
			ValuationCategory vc = new ValuationCategory();
			vc.setSpecName(specName);
			vc.setStatus(statusStr);
			vcService.add(vc);
			request.setAttribute("addFlog",1);
		}
		return getVCList(request);
	}
	@RequestMapping(value="/system/wfpOrder/delVC")
	public String delVC(String id,HttpServletRequest request){
		if(!StringUtil.isEmpty(id)){
			vcService.del(id);
			request.setAttribute("delFlog",1);
		}
		return getVCList(request);
	}
	@RequestMapping(value="/system/wfpOrder/updateVC/{specName}/{statusStr}/{idStr}/{createTime}")
	public String updateVC(@PathVariable String specName,@PathVariable String statusStr,@PathVariable String createTime,@PathVariable String idStr,HttpServletRequest request){
		if(!StringUtil.isEmpty(idStr)){
			ValuationCategory vc = new ValuationCategory();
			vc.setId(idStr);
			vc.setStatus(statusStr);
			vc.setSpecName(specName);
			vc.setCreateTime(createTime);
			vcService.update(vc);
			request.setAttribute("updateFlog",1);
		}
		return getVCList(request);
	}
}
