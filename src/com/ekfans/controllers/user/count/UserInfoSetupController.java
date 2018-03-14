package com.ekfans.controllers.user.count;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.UserInfo;
import com.ekfans.base.user.service.IUserInfoService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 用于获取用户的信息，并修改验证其中的一些信息
 * 
 * @ClassName: UserInfoSetupController
 * @Description:
 * @author liulin
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class UserInfoSetupController {

	private Logger log = LoggerFactory.getLogger(UserInfoSetupController.class);
	@Resource
	private IUserService userService;
	@Resource
	private IUserInfoService userInfoService;

	/**
	 * 跳转到个人资料页面
	 */
	@RequestMapping(value = "/user/count/showUserInfo")
	public String showUserInfo(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		
		UserInfo ui = this.userInfoService.getUserInfoByUserId(user.getId());
		if(ui == null){
			ui = new UserInfo();
		}
		
		request.setAttribute("userInfo", ui);
		request.setAttribute("cur", "showUserInfo");
		
		return "/userCenter/customer/count/userInfoSetup";
	}

	/**
	 * 修改个人资料
	 */
	@RequestMapping(value = "/user/count/userInfoSetup")
	@ResponseBody
	public Boolean userInfoSetup(HttpServletRequest request, HttpServletResponse response) {
		String realName = request.getParameter("realName"); // 真实姓名
		String cardNumber = request.getParameter("cardNumber"); // 身份证号
		String mobile = request.getParameter("mobile"); // 手机号码
		String sex = request.getParameter("sex"); // 性别
		String areaId = request.getParameter("areaId"); // 省市区
		String address = request.getParameter("address"); // 详细地址
		String birthday = request.getParameter("birthday"); // 出生日期
		String homePhone = request.getParameter("homePhone"); // 固定电话
		String nickName = request.getParameter("nickName"); // 昵称
		// 头像
		String headPortrait = "/customerfiles/customer/headPortrait/" + DateUtil.getNoSpSysDateString() + "/";
		headPortrait = FileUploadHelper.uploadFile("headPortrait", headPortrait, request, response);
		
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		UserInfo ui = this.userInfoService.getUserInfoByUserId(user.getId());
		if(ui == null){
			ui = new UserInfo();
			ui.setId("");
			ui.setUserId(user.getId());
		}
		
		ui.setRealName(realName);
		user.setCardNumber(cardNumber);
		user.setMobile(mobile);
		user.setNickName(nickName);
		user.setHeadPortrait(headPortrait);
		ui.setSex("男".equals(sex)?true:false);
		ui.setAreaId(areaId);
		ui.setAddress(address);
		ui.setBirthday(birthday);
		ui.setHomePhone(homePhone);
		
		if(this.userService.updateUserAndUserInfo(user, ui)){
			HttpSession session = request.getSession();
			session.removeAttribute(SystemConst.USER);
			session.setAttribute(SystemConst.USER, user);
			
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkUserInfo
	 * @Description: TODO 验证昵称是否重复 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/count/checkNickName/{nickName}")
	@ResponseBody
	public Object checkUserInfo(@PathVariable String nickName, HttpSession session) {
		if (session == null) {
			return "/user/login";
		}
		try {
			// 获取完整User对象
			User user = (User) session.getAttribute(SystemConst.USER);
			// 利用userService方法检测昵称是否重复
			if (userService.existNickName(nickName, user.getId())) {
				// 昵称不重复，返回true
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
