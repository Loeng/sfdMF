package com.ekfans.controllers.store.cooperation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Cooperation;
import com.ekfans.base.store.service.ICooperationService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 合作机构Controller
 * @ClassName CooperationController
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月15日
 */
@Controller
public class CooperationController {

	private Logger log = LoggerFactory.getLogger(CooperationController.class);
	
	@Autowired 
	private ICooperationService  cooperationService;
	
	/**
	 * 查看合作机构列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/cooperation/list")
	public String cooperationList(HttpServletRequest request){
		
		// 获取搜索数据
		String orgName = request.getParameter("orgName"); // 机构名称
		String type = request.getParameter("type");	// 机构类型
		String contactMan = request.getParameter("contactMan"); // 联系人
		String contactPhone = request.getParameter("contactPhone"); // 联系人电话
		String pageNum = request.getParameter("pageNum"); // 页码
		
		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPage = Integer.parseInt(pageNum);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		
		// 查询合作机构列表
		List<Cooperation> cps = cooperationService.listCooperation(pager, orgName, type, contactMan, contactPhone);
		
		// 设置页面所需数据
		request.setAttribute("cps", cps);
		request.setAttribute("orgName", orgName);
		request.setAttribute("type", type);
		request.setAttribute("contactMan", contactMan);
		request.setAttribute("contactPhone", contactPhone);
		request.setAttribute("pager", pager);
		
		return "/system/cooperation/cooperation_list";
	}
	
	/**
	 * 根据id查找合作机构
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/cooperation/query/{id}")
	public String queryCooperation(@PathVariable String id,HttpServletRequest request){
		Cooperation c = cooperationService.findById(id);
		request.setAttribute("c", c);
		return "/system/cooperation/cooperation_detail";
	}
	
	/**
	 * 跳转至修改页面
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/cooperation/jumpToModify/{id}")
	public String jumpTomodify(@PathVariable String id,HttpServletRequest request){
		Cooperation c = cooperationService.findById(id);
		request.setAttribute("c", c);
		return "/system/cooperation/cooperation_update";
	}
	
	/**
	 * 更新合作机构信息
	 * @param c
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "system/cooperation/update")
	public @ResponseBody boolean updateCooperation(@ModelAttribute Cooperation c,HttpServletRequest request){
		
		boolean flag = cooperationService.updateCooperation(c);
		return flag;
	}
	
	/**
	 * 跳转至新增页面
	 * @return
	 */
	@RequestMapping(value = "/system/cooperation/add")
	public String jumpToAdd(){
		return "/system/cooperation/cooperation_add";
	}
	
	/**
	 * 添加合作机构
	 * @param cp
	 * @return
	 */
	@RequestMapping(value = "/system/cooperation/save")
	public @ResponseBody boolean saveCooperation(@ModelAttribute Cooperation cp){
		// 调用service添加合作机构
		boolean flag = cooperationService.addCooperation(cp);
		return flag;
	}
	
	/**
	 * 页面添加合作机构
	 * @param cp
	 * @return
	 */
	@RequestMapping(value = "/web/cooperation/add")
	public @ResponseBody boolean addCooperation(@ModelAttribute Cooperation cp){
		// 调用service添加合作机构
		boolean flag = cooperationService.addCooperation(cp);
		return flag;
	}
	
	/**
	 * 根据ids批量（单个）删除合作机构
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/cooperation/delete")
	@ResponseBody
	public boolean deleteCooperation(HttpServletRequest request) {
		// 获取数据
		String ids = request.getParameter("ids");
		// 执行删除操作
		boolean flag = cooperationService.delCooperation(ids);
		return flag;
	}
	
}
