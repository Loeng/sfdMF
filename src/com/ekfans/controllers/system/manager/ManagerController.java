package com.ekfans.controllers.system.manager;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.log.service.ILoginLogService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.model.ShopRole;
import com.ekfans.base.system.service.IManagerService;
import com.ekfans.base.system.service.IShopRoleService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.CookieUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.IpUtil;
import com.ekfans.pub.util.ResourceBundleUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.DesUtil;

@Controller
@Scope("prototype")
public class ManagerController extends BasicController {

	private final String KEY_2 = "EKFANS_KEY_2";
	private final String KEY_3 = "EKFANS_KEY_3";
	private Logger log = LoggerFactory.getLogger(ManagerController.class);
	@Resource
	private IManagerService managerService;
	@Resource
	private IShopRoleService shopRoleService;
	@Resource
	private ILoginLogService loginLogService;

	/**
	 * 跳转到系统登陆页面
	 */
	@RequestMapping(value = "/system/login", method = RequestMethod.GET)
	public String login(HttpServletResponse response) {
		// Cookie工具类
		CookieUtil cookieUtil = new CookieUtil(getRequest(), response);

		String cookieName = cookieUtil.getValueByKey("name");
		String cookiePsd = cookieUtil.getValueByKey("password");
		String isCookie = cookieUtil.getValueByKey("managerRemeber");
		
		String key1 = DateUtil.getSysDateTimeString(); // key1
		String key2 = new MD5Util().getMD5ofStr(KEY_2); // key2
		String key3 = new MD5Util().getMD5ofStr(KEY_3); // key3
		
		getRequest().setAttribute("currentDate", key1);
		getRequest().setAttribute("key1", (new MD5Util().getMD5ofStr(key1)).substring(0, 15));
		getRequest().setAttribute("key2", key2.substring(0, 13));
		getRequest().setAttribute("key3", key3.substring(0, 11));
		getRequest().setAttribute("cookieName", !StringUtil.isEmpty(cookieName) ? cookieName : "");
		getRequest().setAttribute("cookiePsd", !StringUtil.isEmpty(cookiePsd) ? cookiePsd : "");
		getRequest().setAttribute("isCookie", !StringUtil.isEmpty(isCookie) ? isCookie : "");

		return "/system/login";

	}

	/**
	 * 系统用户登陆操作
	 */
	@RequestMapping(value = "/system/loginCheck", method = RequestMethod.POST)
	@ResponseBody
	public int loginCheck(HttpServletResponse response) {
		String currentDate = getRequest().getParameter("currentDate");
		String name = getRequest().getParameter("name"); // 用户名
		String password = getRequest().getParameter("password"); // 密码
		String verifyCode = getRequest().getParameter("verifyCode"); // 验证码
		String isCookie = getRequest().getParameter("isCookie"); // 是否保存密码
		String ip = IpUtil.getIpAddr(getRequest()); // 登陆IP
		
		System.out.println("********************************");
		System.out.println("********************************");
		System.out.println(ip);
		System.out.println("********************************");
		System.out.println("********************************");
		
		String key1 = (new MD5Util().getMD5ofStr(currentDate)).substring(0, 15); // 解密密匙1
		String key2 = (new MD5Util().getMD5ofStr(KEY_2)).substring(0, 13); // 解密密匙2
		String key3 = (new MD5Util().getMD5ofStr(KEY_3)).substring(0, 11); // 解密密匙3
		
		name = DesUtil.strDec(name, key1, key2, key3); // 解密用户名
		password = DesUtil.strDec(password, key1, key2, key3); // 解密密码

		int mark = managerService.sysUserLogin(name, password, verifyCode, ip, getSession());
		
		// 定义Cookie工具类
		CookieUtil cookieUtil = new CookieUtil(getRequest(), response);
		if(mark == 9 && "true".equals(isCookie)){
			cookieUtil.addCookie("managerRemeber", "true");
			cookieUtil.addCookie("name", name);
			cookieUtil.addCookie("password", password);
		} else {
			cookieUtil.removeCookieByKey("name");
			cookieUtil.removeCookieByKey("password");
			cookieUtil.removeCookieByKey("managerRemeber");
		}

		return mark;
	}

	/**
	 * 系统用户注销操作
	 */
	@RequestMapping(value = "/system/manager/logout", method = RequestMethod.GET)
	public String logout() {
		Manager manager = (Manager)getSession().getAttribute(SystemConst.MANAGER);
		String ip = IpUtil.getIpAddr(getRequest()); // IP
		
		String note = "用户(" + manager.getName() + ")于(" + DateUtil.getSysDateTimeString() + ")注销";
		if (loginLogService.addLoginLog(manager.getId(), ip, 2, 1, note)) {
			getSession().removeAttribute(SystemConst.MANAGER);
		}
		
		return "redirect:/system";
	}

	/**
	 * 跳转到新增系统后台用户页面
	 */
	@RequestMapping(value = "/system/manager/add")
	public String add() {
		// 查询系统角色
		getRequest().setAttribute("shopRoles", shopRoleService.listRole(null, null));
		
		return "/system/manager/manager_add";
	}

	/**
	 * 新增系统后台用户操作（添加，新增）
	 */
	@RequestMapping(value = "/system/manager/save")
	@ResponseBody
	public int save(@ModelAttribute Manager manager, HttpServletRequest request) {
		return managerService.addManager(manager);
	}
	
	/**
	 * 查找管理员列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/manager/list")
	public String list() {
		String name = getRequest().getParameter("name"); // 管理员名
		String roleId = getRequest().getParameter("roleId"); // 角色ID
		String status = getRequest().getParameter("status"); // 状态
		String mobile = getRequest().getParameter("mobile"); // 手机号
		String email = getRequest().getParameter("email"); // email
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
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		
		// 绑定页面显示需要的数据
		getRequest().setAttribute("shopRoles", shopRoleService.listRole(null, null));
		getRequest().setAttribute("managers", managerService.listManager(pager, name, roleId, status, mobile, email));
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("name", name);
		getRequest().setAttribute("roleId", roleId);
		getRequest().setAttribute("status", status);
		getRequest().setAttribute("mobile", mobile);
		getRequest().setAttribute("email", email);
		
		return "/system/manager/manager_list";
	}

	/**
	 * 删除管理员
	 */
	@RequestMapping(value = "/system/manager/delete")
	@ResponseBody
	public Boolean delete() {
		String ids = getRequest().getParameter("ids");
		
		return managerService.deleteManager(ids);
	}

	/**
     * 清缓存
     */
    @RequestMapping(value = "/system/index/qhc")
    @ResponseBody
    public Boolean qhc() {
        ResourceBundleUtil rbu = new ResourceBundleUtil();
        Cache.isMemcached = Boolean.parseBoolean(rbu.getProperty("system.cache.memcache"));
        Cache.init();
        return true;
    }
	/**
	 * 根据id查询管理员信息
	 */
	@RequestMapping(value = "/system/manager/detail/{id}")
	public String detail(@PathVariable String id) {
		// 利用Service的方法根据id查找广告
		Manager manager = managerService.getManagerByID(id);
		// 查询角色
		List<ShopRole> shopRoles = shopRoleService.listRole(null, null);
		
		getRequest().setAttribute("shopRoles", shopRoles);
		getRequest().setAttribute("manager", manager);
		
		return "/system/manager/manager_update";
	}
	
	/**
	 * 修改管理员
	 */
	@RequestMapping(value = "/system/manager/modify")
	@ResponseBody
	public int modify(@ModelAttribute Manager manager) {
		
		return managerService.modifyManager(manager);
	}

	/**
	 * 查询出对应的详细页面
	 */
	@RequestMapping(value = "/system/manager/query/{id}")
	public String query(@PathVariable String id) {
		// 利用Service的方法根据id查找广告
		getRequest().setAttribute("manager", managerService.getManagerByID(id));
		
		return "/system/manager/manager_query";
	}

}
