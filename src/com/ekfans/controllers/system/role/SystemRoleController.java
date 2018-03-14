package com.ekfans.controllers.system.role;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.model.ShopPurview;
import com.ekfans.base.system.model.ShopRole;
import com.ekfans.base.system.service.IShopPurviewService;
import com.ekfans.base.system.service.IShopRoleService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemRoleController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemRoleController.class);
	@Resource
	private IShopRoleService roleService;
	@Resource
    private IShopPurviewService shopPurviewService;
	
	/**
	 * 跳转到角色新增页面
	 */
	@RequestMapping(value = "/system/role/add")
	public String jumpAddPage() {
		
		getRequest().setAttribute("splist", shopPurviewService.getAllShopPurview());
		
		return "/system/role/role_add";
	}
	
	/**
	 * 新增后台角色
	 */
	@RequestMapping(value = "/system/role/save")
	@ResponseBody
	public int save(@ModelAttribute ShopRole role){
		String[] purviewIds = getRequest().getParameterValues("purviewId"); // 权限id
		
		return roleService.addShopRole(role, purviewIds);
	}
	
	/**
	 * 查找角色列表
	 */
	@RequestMapping(value = "/system/role/list")
	public String list() {
		String name = getRequest().getParameter("name"); // 角色名称
		String currentpageStr = getRequest().getParameter("pageNum"); // 页码
		
		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);
		
		List<ShopRole> shopRoles = roleService.listRole(pager, name);
		
		getRequest().setAttribute("shopRoles", shopRoles);
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("name", name);
		
		return "/system/role/role_list";
	}

	/**
	 * 删除角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/role/delete")
	@ResponseBody
	public Boolean delete() {
		String ids = getRequest().getParameter("ids");
		
		return roleService.deleteRole(ids);
	}
	
	/**
	 * 根据角色id查询角色信息并跳转到角色（修改页面或查询页面）
	 */
	@RequestMapping(value = "/system/role/detail/{id}/{type}")
	public String jumpUpdatePage(@PathVariable String id, @PathVariable int type){
		// type:标记（1:修改,2:查看）
		ShopRole sr = roleService.getRoleById(id);
		List<ShopPurview> splist = shopPurviewService.getPurviewsByRoleIdOrMark(sr.getId());
		
		getRequest().setAttribute("sr", sr);
		getRequest().setAttribute("splist", splist);
		getRequest().setAttribute("ttype", type);
		
		return "/system/role/role_update";
	}

	/**
	 * 修改角色及角色所属权限
	 */
	@RequestMapping(value = "/system/role/modify")
	@ResponseBody
	public int modify(@ModelAttribute ShopRole shopRole) {
		String[] purviewIds = getRequest().getParameterValues("purviewId");
		
		return roleService.updateRole(shopRole, purviewIds);
	}

	/**
	 * 根据角色ID获取角色名称
	 */
	@RequestMapping(value = "/system/role/getRoleNameById/{roleId}")
	public void getRoleNameById(@PathVariable String roleId, HttpServletResponse response) {
		String roleName = roleService.getRoleNameById(roleId);
		roleName = "角色 " + roleName;
		
		backWriteStr(response, roleName);
	}
}
