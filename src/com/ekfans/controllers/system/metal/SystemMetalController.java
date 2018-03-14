package com.ekfans.controllers.system.metal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.base.metal.model.PreciousMetalCategory;
import com.ekfans.base.metal.service.IPreciousMetalCategoryService;
import com.ekfans.base.metal.service.IPreciousMetalService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.datacrawling.SmmDataUtil;
import com.ekfans.pub.util.datacrawling.SmmModel;

@Controller
@Scope("prototype")
public class SystemMetalController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemMetalController.class);
	@Resource
	private IPreciousMetalService metalService;
	@Resource
	private IPreciousMetalCategoryService categoryService;

	/**
	 * 跳转到展示品目分类界面
	 */
	@RequestMapping(value = "/system/metalCategory/list")
	public String categoryList(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<PreciousMetalCategory> categorys = categoryService.getCategorys();
			request.setAttribute("categorys", categorys);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "/system/metal/metalCategoryList";
	}

	/**
	 * 跳转到展示品目分类界面
	 */
	@RequestMapping(value = "/system/metalCategory/save")
	@ResponseBody
	public String addCategory(HttpServletRequest request, HttpServletResponse response) {
		try {
			String categoryName = request.getParameter("categoryName");
			if (StringUtil.isEmpty(categoryName)) {
				return null;
			}
			PreciousMetalCategory category = new PreciousMetalCategory();
			category.setCreateTime(DateUtil.getSysDateTimeString());
			category.setName(categoryName);
			boolean saveStatus = categoryService.addOrUpdate(category);
			if (saveStatus) {
				return category.getId();
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 跳转到展示品目分类界面
	 */
	@RequestMapping(value = "/system/metalCategory/update")
	@ResponseBody
	public String updateCategory(HttpServletRequest request, HttpServletResponse response) {
		try {
			String categoryId = request.getParameter("categoryId");
			String categoryName = request.getParameter("categoryName");
			if (StringUtil.isEmpty(categoryId) || StringUtil.isEmpty(categoryName)) {
				return "0";
			}
			PreciousMetalCategory category = categoryService.getCategoryById(categoryId);
			if (category == null) {
				return "0";
			}
			category.setName(categoryName);
			boolean updateStatus = categoryService.addOrUpdate(category);
			if (updateStatus) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "0";
	}

	/**
	 * 跳转到展示品目分类界面
	 */
	@RequestMapping(value = "/system/metalCategory/remove")
	@ResponseBody
	public String removeCategory(HttpServletRequest request, HttpServletResponse response) {
		try {
			String categoryId = request.getParameter("categoryId");
			if (StringUtil.isEmpty(categoryId)) {
				return "0";
			}
			boolean removeStatus = categoryService.deleteById(categoryId);
			if (removeStatus) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "0";
	}

	/**
	 * 品目展示界面（LOAD）
	 */
	@RequestMapping(value = "/system/metal/showmetals/{categoryId}")
	public String showMetals(@PathVariable String categoryId, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<PreciousMetal> metals = metalService.getMetalsByCategoryId(categoryId);
			request.setAttribute("metals", metals);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "/system/metal/metalList";
	}
	
	
	/**
	 * 跳转到展示品目分类界面
	 */
	@RequestMapping(value = "/system/metal/saveallsmm")
	@ResponseBody
	public void saveAllSmm(HttpServletRequest request, HttpServletResponse response) {
		    String log = SmmDataUtil.updateAll();
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("text/html;charset=utf-8");
		    response.setHeader("Cache-Control", "no-cache");
		    PrintWriter out;
			try {
				out = response.getWriter();
				out.print(log);
			    out.flush();
			    out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}

	
	/**
	 * 跳转到展示品目分类界面
	 */
	@RequestMapping(value = "/system/metal/save")
	@ResponseBody
	public String saveMetal(HttpServletRequest request, HttpServletResponse response) {
		try {
			String metalId = request.getParameter("metalId");
			String metalName = request.getParameter("name");
			String spec = request.getParameter("spec");
			String unit = request.getParameter("unit");
			String categoryId = request.getParameter("categoryId");
			String smmId = request.getParameter("smmId");
			PreciousMetal metal = metalService.getMetalById(metalId);
			if (metal == null) {
				metal = new PreciousMetal();
				metal.setCreateTime(DateUtil.getSysDateTimeString());
			}
			metal.setName(metalName);
			metal.setSpec(spec);
			metal.setUnit(unit);
			metal.setCategoryId(categoryId);
			metal.setSmmId(smmId);
			boolean status = metalService.saveOrUpdate(metal);

			if (status) {
				return metal.getId();
			} else {
				return "0";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "0";
	}

	/**
	 * 跳转到展示品目分类界面
	 */
	@RequestMapping(value = "/system/metal/remove")
	@ResponseBody
	public String removeMetal(HttpServletRequest request, HttpServletResponse response) {
		try {
			String metalId = request.getParameter("metalId");
			if (StringUtil.isEmpty(metalId)) {
				return "0";
			}

			boolean status = metalService.deleteById(metalId);

			if (status) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "0";
	}

	/**
	 * 批量删除品目
	 */
	@RequestMapping(value = "/system/metal/removebatch")
	@ResponseBody
	public String removeBatchMetal(HttpServletRequest request, HttpServletResponse response) {
		String[] metalIds = request.getParameterValues("metalCheck");
		try {
			boolean status = metalService.deleteByIds(metalIds);
			if (status) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "0";
	}
}
