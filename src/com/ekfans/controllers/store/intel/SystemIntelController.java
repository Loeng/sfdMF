package com.ekfans.controllers.store.intel;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Intel;
import com.ekfans.base.store.service.IIntelService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemIntelController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemIntelController.class);
	@Resource
	private IIntelService intelService;
	
	@RequestMapping(value = "/system/intel/jumpAdd")
	public String jumpAddPage(){
		return "/system/intel/intel_add";
	}
	
	@RequestMapping(value = "/system/intel/save")
	@ResponseBody
	public int saveIntel(@ModelAttribute Intel in){
		return intelService.saveIntel(in);
	}
	
	@RequestMapping(value = "/system/intel/list")
	public String jumpIntelList(){
		String name = getRequest().getParameter("name");
		String pageNum = getRequest().getParameter("pageNum"); // 页码
		
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
		getRequest().setAttribute("name", name);
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("ilist", intelService.getIntelList(pager, name));
		
		return "/system/intel/intel_list";
	}
	
	@RequestMapping(value = "/system/intel/delete")
	@ResponseBody
	public Boolean delete() {
		String ids = getRequest().getParameter("ids");
		
		return intelService.deleteIntel(ids);
	}
	
	@RequestMapping(value = "/system/intel/query/{id}")
	public String query(@PathVariable String id) {
		String mark = getRequest().getParameter("mark");
		
		// 利用Service的方法根据id查找广告
		getRequest().setAttribute("intel", intelService.getIntelById(id));
		getRequest().setAttribute("mark", mark);
		
		return "/system/intel/intel_update";
	}
	
	@RequestMapping(value = "/system/intel/update")
	@ResponseBody
	public int updateIntel(@ModelAttribute Intel in) {
		
		return intelService.updateIntel(in);
	}
}
