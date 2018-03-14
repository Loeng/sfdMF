package com.ekfans.controllers.system.store;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.store.model.StorePurviewRel;
import com.ekfans.base.store.model.StoreRole;
import com.ekfans.base.store.service.IStorePurviewRelService;
import com.ekfans.base.store.service.IStorePurviewService;
import com.ekfans.base.store.service.IStoreRoleService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.service.IStoreCacheService;
import com.ekfans.plugin.cache.service.StoreCacheService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemStoreRoleController extends BasicController {
	
	@Resource
	private IStoreRoleService storeRoleService;
	@Resource
	private IStorePurviewService storePurviewService;
	@Resource
	private IStorePurviewRelService storePurviewRelService;

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/system/store/role/add")
	public String add(HttpServletRequest request) {
		try {
			// 得到所有的店铺权集合
			List<StorePurview> sps = storePurviewService.storePurviewList();
			// 将所有集合绑定到页面显示
			request.setAttribute("sps", sps);
			return "/system/store/role/roleAdd";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 新增角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/store/role/save")
	public String save(StoreRole storeRole, HttpServletRequest request) {
		try {
			// 如果角色名为空 则新增失败
			if (StringUtil.isEmpty(storeRole.getName())
					|| storeRole.getName().length() < 3
					|| storeRole.getName().length() > 20) {
				request.setAttribute("addOk", false);
				return "/system/store/role/roleAdd";
			}
			// 从页面上获得所勾选的权限id
			String[] purviewIds = request.getParameterValues("purviewId");

			// 利用Service的方法添加角色
			if (storeRoleService.addRole(storeRole)) {
				request.setAttribute("addOk", true);
				// 添加成功，返回

			}
			// 循环添加
			if (purviewIds != null && purviewIds.length > 0) {
				for (int i = 0; i < purviewIds.length; i++) {
					if (purviewIds[i] != null && purviewIds[i] != ""
							&& purviewIds[i] != " ") {
						StorePurviewRel spr = new StorePurviewRel();
						spr.setRoleId(storeRole.getId());
						spr.setPurviewId(purviewIds[i]);
						storePurviewRelService.addStorePurviewRel(spr);
					}
				}
			}
			// 得到所有的店铺权集合
			List<StorePurview> sps = storePurviewService.storePurviewList();
			// 将所有集合绑定到页面显示
			request.setAttribute("sps", sps);
			IStoreCacheService cacheService = new StoreCacheService();
			cacheService.refreshStorePurview(storeRole.getId());
			return "/system/store/role/roleAdd";
		} catch (Exception e) {
			// 添加失败，返回false
			request.setAttribute("addOk", false);
		}
		return "error";
	}

	/**
	 * 删除角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/store/role/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		try {
			// 利用Service的方法根据id删除角色
			if (storeRoleService.deleteRole(id)
					&& storePurviewRelService.getRoleIdDelete(id)) {
				IStoreCacheService cacheService = new StoreCacheService();
				cacheService.refreshStorePurview(id);
				// 删除成功，返回true
				return true;
			}
		} catch (Exception e) {
			// 删除失败，返回false
			return false;
		}
		return "error";
	}

	/**
	 * 修改角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/store/role/modify")
	public String modify(StoreRole storeRole, HttpServletRequest request) {
		try {
			// 从页面上获得所勾选的权限id
			String[] purviewIds = request.getParameterValues("purviewId");
			// 得到对用的roleId
			String roleId = request.getParameter("id");
			// 如果角色名为空 则新增失败
			if (StringUtil.isEmpty(storeRole.getName())
					|| storeRole.getName().length() < 3
					|| storeRole.getName().length() > 20) {
				request.setAttribute("modifyOk", false);
				return "/system/store/role/roleModify";
			}
			// 利用Service的方法修改角色
			if (storeRoleService.modifyRole(storeRole)) {
				// 修改成功，返回true
				request.setAttribute("modifyOk", true);
				request.setAttribute("storeRole", storeRole);

			}
			storePurviewRelService.getRoleIdDelete(roleId);
			// 循环添加
			if (purviewIds != null && purviewIds.length > 0) {
				for (int i = 0; i < purviewIds.length; i++) {
					if (purviewIds[i] != null && purviewIds[i] != ""
							&& purviewIds[i] != " ") {
						StorePurviewRel spr = new StorePurviewRel();
						spr.setRoleId(storeRole.getId());
						spr.setPurviewId(purviewIds[i]);
						storePurviewRelService.addStorePurviewRel(spr);
					}
				}
			}
			// 得到所有的店铺权集合
			List<StorePurview> sps = storePurviewService.sPurviewList(roleId);
			// 将所有集合绑定到页面显示
			request.setAttribute("sps", sps);

			IStoreCacheService cacheService = new StoreCacheService();
			cacheService.refreshStorePurview(roleId);
			return "system/store/role/roleModify";
		} catch (Exception e) {
			// 修改失败，返回false
			request.setAttribute("modifyOk", false);
		}
		return "error";
	}

	/**
	 * 查询角色信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/store/role/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找角色
			StoreRole storeRole = storeRoleService.getRoleById(id);
			request.setAttribute("storeRole", storeRole);
			// 得到所有的店铺权集合
			List<StorePurview> sps = storePurviewService.sPurviewList(id);
			// 将所有集合绑定到页面显示
			request.setAttribute("sps", sps);
			return "system/store/role/roleModify";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "error";
	}

	/**
	 * 查找角色列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/store/role/list")
	public String list(HttpServletRequest request) {
		try {
			// 定义分页
			Pager pager = new Pager();

			// 从页面获取角色名称
			String name = request.getParameter("name");
			// 从页面获取页码
			String currentpageStr = request.getParameter("pageNum");
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(currentpageStr)) {
				try {
					currentPage = Integer.parseInt(currentpageStr);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			// 设置要查询的页码
			pager.setCurrentPage(currentPage);
			// 利用Service的方法查找角色
			List<StoreRole> storeRoles = storeRoleService.listRole(pager, name);
			request.setAttribute("storeRoles", storeRoles);
			request.setAttribute("pager", pager);
			request.setAttribute("name", name);
			return "/system/store/role/roleList";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "error";
	}

	/**
	 * 根据角色ID获取角色名称
	 * 
	 * @return
	 */

	@RequestMapping(value = "/system/store/role/getRoleNameById/{roleId}")
	public String getRoleNameById(HttpServletResponse response,
			@PathVariable String roleId) {

		// 如果传进来的角色ID不为空，则调用Service查询并打入页面
		if (StringUtil.isEmpty(roleId)) {
			return null;
		}
		String roleName = storeRoleService.getRoleNameById(roleId);
		roleName = "角色 " + roleName;
		// 指定输出内容类型和编码
		response.setContentType("UTF-8");
		// 定义输出流
		PrintWriter out = null;

		try {
			// 获取输出流，然后使用
			out = response.getWriter();
			// 直接进行文本操作
			out.print(roleName);
			out.flush();
		} catch (Exception ex) {
			out.println(ex.toString());
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 查询角色详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/store/role/query/{id}")
	public String query(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找角色
			// 得到所有的店铺权集合
			List<StorePurview> sps = storePurviewService
					.getStorePurviewByRoleId(id);
			// 将所有集合绑定到页面显示
			request.setAttribute("sps", sps);
			StoreRole storeRole = storeRoleService.getRoleById(id);
			request.setAttribute("storeRole", storeRole);
			return "system/store/role/roleQuery";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "error";
	}
}
