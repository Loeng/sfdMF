package com.ekfans.controllers.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.user.service.rwdReset.IUserPwdResetService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.message.util.MessageUtil;

/**
 * 
 * @ClassName: PwdResetController
 * @Description: TODO(前台密码重置)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-17 上午10:28:05
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class PwdResetController extends BasicController {

	@Autowired
	private IUserPwdResetService userPwdResetService;

	/**
	 * 
	 * @Title: goToPwdReset
	 * @Description: TODO(跳转到密码重置页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/pwdReset")
	public String goToForget() {
		return "/web/pwdReset/forget";
	}

	/**
	 * 
	 * @Title: goTofindByPhone
	 * @Description: TODO(根据手机号码找回密码) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/findByPhone")
	public String goTofindByPhone(HttpServletRequest request) {
		String account = request.getParameter("userAccount");
		request.setAttribute("userAccountType", "phone");
		request.setAttribute("userAccount", account);
		return "/web/pwdReset/findByPhone";
	}

	/**
	 * 
	 * @Title: goTofindByEmail
	 * @Description: TODO(根据邮箱找回密码) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param userAccount
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/findByEmail")
	public String goTofindByEmail(HttpServletRequest request) {
		String account = request.getParameter("userAccount");
		int judgment = Integer.valueOf(request.getParameter("judgment"));

		// 找回密码方式，1：手机短信找回，2：邮件找回
		if (1 == judgment) {

		} else if (2 == judgment) {
			String emailTitle = "找回密码";
			String emailContent = "重置密码连接：" + getBasePath() + "/web/user/resetPwdUrl?address=" + account + "&type=email";
			// 发送邮件
			MessageUtil.sendMail(account, emailTitle, emailContent);

			/*
			 * if(MessageUtil.sendMail(account, emailTitle, emailContent)){ User
			 * user = userPwdResetService.emailReset(account); user.setTemp(new
			 * Md5Util().getMD5ofStr("111111"));
			 * userPwdResetService.updateTempPwd(user); }
			 */
		}
		request.setAttribute("userAccountType", "email");
		request.setAttribute("userAccount", account);
		return "/web/pwdReset/findByMail";
	}

	/**
	 * @throws IOException
	 * 
	 * @Title: goToPwdReset
	 * @Description: TODO(验证身份后进行修改密码操作) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param userAccount
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/goToPwdReset")
	public String goToPwdReset(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String type = request.getParameter("userAccountType");
		String account = request.getParameter("userAccount");
		/*
		 * request.setAttribute("userAccountType",type);
		 * request.setAttribute("userAccount",account);
		 */
		if ("phone".equals(type)) {

		} else if ("email".equals(type)) {
			String emailTitle = "找回密码";
			String emailContent = "重置密码连接：" + getBasePath() + "/web/user/resetPwdUrl?address=" + account + "&type=email";
			// 发送邮件
			PrintWriter writer = response.getWriter();
			if (MessageUtil.sendMail(account, emailTitle, emailContent)) {
				writer.print("true");
			} else {
				writer.print("false");
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: testPhone
	 * @Description: TODO(验证手机对应的账户是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/testAccountPhone")
	@ResponseBody
	public Object testPhone(HttpServletRequest request) {
		String phone = request.getParameter("phone");
		boolean test = userPwdResetService.testAccountForPhone(phone);
		return test;
	}

	/**
	 * 
	 * @Title: testEmail
	 * @Description: TODO(验证邮箱对应的账户是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/testAccountEmail")
	@ResponseBody
	public Object testEmail(HttpServletRequest request) {
		String email = request.getParameter("email");
		boolean test = userPwdResetService.testAccountForEmail(email);
		return test;
	}

	/**
	 * 
	 * @Title: resetPwd
	 * @Description: TODO(执行密码重置功能) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/ResetPwd")
	@ResponseBody
	public Object resetPwd(HttpServletRequest request) {
		String accountUser = request.getParameter("account");
		String newPassword = request.getParameter("newPassword");
		String userAccountType = request.getParameter("userAccountType");
		String strong = request.getParameter("strong");
		boolean modifyOk = userPwdResetService.resetPWD(accountUser, userAccountType, newPassword, strong);
		return modifyOk;
	}

	/**
	 * 
	 * @Title: sendYZM
	 * @Description: TODO(向用户手机发送一个验证码) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/sendYZM")
	@ResponseBody
	public Object sendYZM(HttpServletRequest request, HttpSession session) {
		// 获得手机号码
		String phoneNum = request.getParameter("phoneNum");

		boolean sendOk = userPwdResetService.sendCommand(phoneNum, session);
		return sendOk;
	}

	/**
	 * 
	 * @Title: checkYZM
	 * @Description: TODO(验证用户输入的验证码是否正确) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/user/checkYZM")
	@ResponseBody
	public Object checkYZM(HttpServletRequest request, HttpSession session) {
		String uyzm = request.getParameter("uyzm");
		boolean checkOk = userPwdResetService.checkYZM(uyzm, session);
		return checkOk;
	}

	/**
	 * 重置密码跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/web/user/resetPwdUrl")
	public String resetPwdUrl(HttpServletRequest request) {
		request.setAttribute("userAccount", request.getParameter("address"));
		request.setAttribute("userAccountType", request.getParameter("type"));
		return "/web/pwdReset/resetPwd";
	}
}
