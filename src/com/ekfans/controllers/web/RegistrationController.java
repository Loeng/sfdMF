package com.ekfans.controllers.web;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.message.service.UserMessageService;
import com.ekfans.pub.util.EncryptionAndDecryption;
import com.ekfans.pub.util.StringUtil;

/**
 * 注册Controller
 * 
 * @ClassName: RegistrationController
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class RegistrationController extends BasicController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IStoreService storeService;

	/**
	 * 统一注册入口
	 */
	@RequestMapping(value = "/web/storeReg")
	public @ResponseBody
	boolean storeReg(HttpServletRequest request) {
		// 获取页面的传值
		String type = request.getParameter("type");
		String companyName = request.getParameter("companyName");
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String mail = request.getParameter("mail");
		String checkNo = request.getParameter("checkNo");
		Object oldyz = getRequest().getSession().getAttribute(SystemConst.PHONESESSIONKEY);
		// 存储企业信息
		if (checkNo.equals(oldyz)) {
			if (userService.saveUserOrStore(phone, password, companyName, type, mail, request)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查用户名是否重复
	 * 
	 * @return true：有该用户，false：没有
	 */
	@RequestMapping(value = "/web/checkUserName")
	@ResponseBody
	public Boolean checkUserName() {
		String name = getRequest().getParameter("name"); // 用户名
		return userService.checkUserName(name);
	}

	/**
	 * 检查企业名称是否重复
	 * 
	 * @return true：有该用户，false：没有
	 */
	@RequestMapping(value = "/web/checkStoreName")
	@ResponseBody
	public Boolean checkStoreName() {
		String companyName = getRequest().getParameter("companyName"); // 企业名称
		return storeService.checkStoreName(companyName);
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @param type
	 *            会员类型（0：个人会员，1：供应商，2：采购商，3：核心企业）
	 */
	@RequestMapping(value = "/web/jumpRegPage")
	public String jumpRegPage() {
		return "/web/purchase/reg/storeReg";
	}

	// ==========================================================================================

	/**
	 * （个人，采购商，供应商）邮箱注册
	 * 
	 * @param type
	 *            会员类型（0：个人会员，1：供应商，2：采购商，3：核心企业）
	 */
	@RequestMapping(value = "/web/{type}/storeMailReg")
	@ResponseBody
	public Boolean storeMailReg(@PathVariable String type, HttpServletRequest request) {
		String companyName = getRequest().getParameter("companyName"); // 企业名称
		String name = getRequest().getParameter("name"); // 用户名
		String password = getRequest().getParameter("password"); // 密码

		return userService.saveUserOrStore(name, password, companyName, String.valueOf(changeType(type)), "", request);
	}

	/**
	 * （个人，采购商，供应商）跳转到邮件注册第二步页面
	 */
	@RequestMapping(value = "/web/{type}/mailTwo")
	public String mailTwo(@PathVariable String type) {
		String name = getRequest().getParameter("name"); // 用户名

		// 绑定页面显示需要的数据
		getRequest().setAttribute("regLogin", 2); // 1:登录，2:注册
		getRequest().setAttribute("mark", 1); // 1:邮箱注册，2:手机注册
		getRequest().setAttribute("name", name);

		if (type.equalsIgnoreCase("zero")) {
			return "/web/shop/reg/userReg_two";
		}
		return "/web/purchase/reg/storeReg_two";
	}

	/**
	 * 完善注册账户信息
	 * @return
	 */
	@RequestMapping(value = "/web/supplementUser")
	public @ResponseBody boolean supplementUser(HttpServletRequest request) {
		// 在这里比较特殊，如果使用SystemConst.USER缓存用户对象，可能会导致用户还没有完善的情况下登陆成功
		// 故单独使用一个字段表示标识，这样相对安全
		Object loginedRegUser =  getSession().getAttribute(SystemConst.USER_NEED_SUP);
		if (loginedRegUser == null) {
			return false;
		}
		User user = (User) loginedRegUser;

		// 获取页面的传值
		String type = request.getParameter("type");
		String companyName = request.getParameter("companyName");
		String mail = request.getParameter("mail");

		if (userService.supplementUser(user, type, companyName, mail)) {
			return true;
		}
		return false;
	}


	/**
	 * （个人，采购商，供应商）发送邮件
	 */
	@RequestMapping(value = "/web/reg/sendRegMial")
	@ResponseBody
	public Boolean sendRegMial() {
		String type = getRequest().getParameter("type"); // 用户类型
		String name = getRequest().getParameter("name"); // 用户名

		User user = userService.getUserName(name, String.valueOf(changeType(type)));

		UserMessageService service = new UserMessageService(getRequest(), "userRegisteVerifyEmail", user, "", "");
		service.run();

		return true;
	}

	/**
	 * （个人，采购商，供应商）激活账户
	 */
	@RequestMapping(value = "/web/reg/activation")
	public String activationAccount() {
		String type = getRequest().getParameter("type"); // 用户类型
		String name = getRequest().getParameter("name"); // 用户名
		String tempcurrent = getRequest().getParameter("tempcurrent"); // 时间戳
		String returnUrl = getRequest().getParameter("returnUrl");
		// tempcurrent = URLDecoder.decode(tempcurrent);
		tempcurrent = tempcurrent.substring(2);
		tempcurrent = tempcurrent.substring(0, (tempcurrent.length() - 2));
		String oldDate = EncryptionAndDecryption.decrypt(SystemConst.DESKEY, tempcurrent);

		long longOld = Long.valueOf(oldDate);
		long longNew = new Date().getTime();
		long cha = 1000 * 60 * 30;

		// mark（1:激活成功，2:激活链接失效，3:激活失败）
		if ((longNew - longOld) < cha) {
			if (this.userService.activationUser(name, type)) {
				User user = userService.getUserName(name, type);
				getSession().setAttribute(SystemConst.USER, user);
				if (!"0".equals(user.getType())) {
					getSession().setAttribute(SystemConst.STORE, storeService.getStore(user.getId()));
				}

				getRequest().setAttribute("mark", 1);
			} else {
				getRequest().setAttribute("mark", 3);
			}
		} else {
			getRequest().setAttribute("mark", 2);
		}

		if ("0".equals(type)) {
			type = "zero";
		} else if ("1".equals(type)) {
			type = "one";
		} else if ("2".equals(type)) {
			type = "two";
		} else if ("3".equals(type)) {
			type = "three";
		} else if ("4".equals(type)) {
			type = "four";
		}
		getRequest().setAttribute("type", type);
		getRequest().setAttribute("regLogin", 2); // 1:登录，2:注册
		getRequest().setAttribute("jud", 1); // 1:邮箱注册，2：手机注册
		getRequest().setAttribute("name", name);
		getRequest().setAttribute("returnUrl", returnUrl);

		if ("0".equals(type)) {
			return "/web/shop/reg/userReg_three";
		}
		return "/web/purchase/reg/storeReg_three";
	}

	/**
	 * （个人，采购商，供应商）发送手机验证码
	 */
	@RequestMapping(value = "/web/phone/sendPhoneYan", method = RequestMethod.POST)
	@ResponseBody
	public Integer sendPhoneYan(HttpServletRequest request) {
		String phone = getRequest().getParameter("name"); // 手机号

		int pms = 0; // 标记（1:输入手机号，2:手机号格式有问题，3:发送成功，4:发送失败）
		if (StringUtil.isEmpty(phone)) {
			pms = 1;
		} else {
			phone = phone.trim();
			if (Pattern.compile("^(1[0-9][0-9])\\d{8}$").matcher(phone).matches()) {
				getSession().removeAttribute(SystemConst.PHONESESSIONKEY);

				// Random r = new Random();
				// String verifyCode = Integer.toString(r.nextInt(999999));
				String verifyCode = "111111";
				System.out.println("手机验证码：" + verifyCode);
				getSession().setAttribute(SystemConst.PHONESESSIONKEY, verifyCode);

				User user = userService.getUserByName(phone);
				if (user == null) {
					user = new User();
					user.setMobile(phone);
				}
				UserMessageService service = new UserMessageService(getRequest(), "userRegisteVerifyMobile", user, verifyCode, "");
				service.run();
				pms = 3;
			} else {
				pms = 2;
			}
		}
		return pms;
	}

	/**
	 * （个人，采购商，供应商）手机注册
	 */
	@RequestMapping(value = "/web/reg/storePhoneReg")
	@ResponseBody
	public Integer storePhoneReg(HttpServletRequest request, HttpServletResponse response) {
		String type = getRequest().getParameter("type"); // 用户类型
		String companyName = getRequest().getParameter("companyName"); // 企业名称
		String name = getRequest().getParameter("name"); // 用户名
		String password = getRequest().getParameter("password"); // 密码
		String yanzheng = getRequest().getParameter("yanzheng");// 验证码
		Object oldyz = getRequest().getSession().getAttribute(SystemConst.PHONESESSIONKEY); // 获取HttpSession中验证码
		String email = getRequest().getParameter("email");
		int mark = 0;
		if (oldyz == null) {
			mark = 2;
		} else {
			if (yanzheng.equals(oldyz)) {
				if (userService.saveUserOrStore(name, password, companyName, type, email, request)) {
					mark = 1;
				} else {
					mark = 3;
				}
			} else {
				mark = 2;
			}
		}

		// mark（1:已注册成功，2:验证码失效，3:注册失败）
		return mark;
	}

	/**
	 * （个人，采购商，供应商）跳转到注册成功页面
	 */
	@RequestMapping(value = "/web/reg/storeReg/{phone}")
	public String jumpSuccessPage(@PathVariable String phone) {
		// String name = getRequest().getParameter("name"); // 用户名
		getRequest().setAttribute("regLogin", 2); // 1:登录，2:注册
		// getRequest().setAttribute("mark", 2); // 1:邮箱注册，2：手机注册
		getRequest().setAttribute("name", phone);

		User user = userService.getUserName(phone, null);
		getSession().setAttribute(SystemConst.USER, user);
		// if (!"0".equals(user.getType())) {
		// getSession().setAttribute(SystemConst.STORE,
		// this.storeService.getStore(user.getId()));
		// }

		// if (changeType(type) == 0) {
		return "/web/shop/reg/userReg_three";
		// }
		// return "/web/purchase/reg/storeReg_two";
	}

	/**
	 * 核心企业申请
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/reg/coreStoreReg")
	@ResponseBody
	public Boolean coreStoreReg(HttpServletRequest request) {
		String companyName = request.getParameter("companyName");
		String unitType = request.getParameter("unitType");
		String contactName = request.getParameter("contactName");
		String contactSex = request.getParameter("contactSex");
		String contactPhone = request.getParameter("contactPhone");
		String contactTime = request.getParameter("contactTime");
		String type = "3";

		return this.userService.saveCoreStore(companyName, unitType, contactName, contactSex, contactPhone, contactTime, type);
	}

	/**
	 * 跳转到核心企业申请成功页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/reg/jumpCoreSuccessPage")
	public String jumpCoreSuccessPage(HttpServletRequest request) {
		String contactPhone = request.getParameter("contactPhone");
		String contactTime = request.getParameter("contactTime");

		request.setAttribute("type", 3);
		request.setAttribute("contactPhone", contactPhone);
		request.setAttribute("contactTime", contactTime);

		return "/web/purchase/reg/coreReg_two";
	}

	private int changeType(String type) {
		int ttype = -1;
		if ("zero".equalsIgnoreCase(type)) {
			ttype = 0;
		} else if ("one".equalsIgnoreCase(type)) {
			ttype = 1;
		} else if ("two".equalsIgnoreCase(type)) {
			ttype = 2;
		} else if ("three".equalsIgnoreCase(type)) {
			ttype = 3;
		} else if ("four".equalsIgnoreCase(type)) {
			ttype = 4;
		}
		return ttype;
	}

	public static void main(String[] args) {
		URL url;
		try {
			url = new URL("https://www.baidu111.com/");
			InputStream in = url.openStream();
			System.out.println("连接可用");
		} catch (Exception e1) {
			System.out.println("连接打不开!");
			url = null;
		}
		System.out.println("连接打不开!1111");
	}
}
