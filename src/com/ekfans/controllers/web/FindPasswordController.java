package com.ekfans.controllers.web;

import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.message.service.UserMessageService;
import com.ekfans.pub.util.EncDec.MD5Util;

@Controller
@Scope("prototype")
public class FindPasswordController extends BasicController {

	private Logger log = LoggerFactory.getLogger(FindPasswordController.class);
	private final String CODEPHONE = "VERIFYCODEPHONE-EKFANS-MOBLIE";
	@Resource
	private IUserService userService;
	
	/**
	 * 跳转到找回密码第一步页面
	 */
	@RequestMapping(value = "/web/findpassword/jumpPasswordPage")
	public String jumpPasswordPage( HttpServletRequest request){
		// 绑定页面显示需要的数据
		getRequest().setAttribute("regLogin", 3); // 1:登陆，2:注册，3:找回密码
		
		return "/web/commons/findPassword/zhaoPWD_one";
	}
	
	/**
	 * 发送手机验证码
	 */
	@RequestMapping(value = "/web/findpassword/zhaosendPhoneYan", method = RequestMethod.POST)
	@ResponseBody
	public int sendPhoneYan(){
		HttpSession session = getRequest().getSession();
		String phone = getRequest().getParameter("phone");
		
//		Random r = new Random();
//      String verifyCode = Integer.toString(r.nextInt(999999));
		String verifyCode = "111111";
		System.out.println("手机验证码：" + verifyCode);
		
		session.removeAttribute(CODEPHONE);
		session.setAttribute(CODEPHONE, verifyCode);
		
        UserMessageService  service = new UserMessageService(getRequest(), "userPwdResetMobile", userService.getUserByName(phone), verifyCode, "");
        service.run();
		
		return 1;
	}
	
	/**
	 * 用户名是否存在
	 * 
	 * @param jud 标记（mail:邮箱找回，phone:电话找回）
	 * @return 1:验证码错误，2:不存在， 3:禁用，4:启用，5:删除
	 */
	@RequestMapping(value = "/web/findpassword/checkIsName/{jud}", method = RequestMethod.POST)
	@ResponseBody
	public int checkIsName(@PathVariable String jud){
		String mailName = getRequest().getParameter("name"); // 邮箱(用户名)
		String phoneName = getRequest().getParameter("mobile"); // 电话(用户名)
		String verifyCode = getRequest().getParameter("verifyCode"); // 验证码
		
		if("phone".equalsIgnoreCase(jud)){
			Object newObj = getRequest().getSession().getAttribute(CODEPHONE);
			if(newObj == null || (!verifyCode.equalsIgnoreCase(newObj.toString()))){
				return 1;
			}
		}
		// 根据用户名查找用户类型
		User u = userService.getUserByName(phoneName);
		String type = u.getType();
		
		User user = null;
		if("phone".equalsIgnoreCase(jud)){
			user = userService.getUserName(phoneName, type);
		}else{
			user = userService.getUserName(mailName, type);
		}
		if(user == null){
			return 2;
		}
		return user.getStatus() + 3;
	}
	
	/**
	 * 跳转到找回密码第二步页面
	 */
	@RequestMapping(value = "/web/findpassword/jumpPasswordPageTwo")
	public String jumpPasswordPageTwo(){
		// 获取用户名
		String name = getRequest().getParameter("name");
		String type = getRequest().getParameter("type");
		
		// 绑定页面显示需要的数据
		getRequest().setAttribute("name", name);
		if ("0".equals(type)) {
			getRequest().setAttribute("type", "zero");
		}else if("1".equals(type)){
			getRequest().setAttribute("type", "one");
		}else if("2".equals(type)){
			getRequest().setAttribute("type", "two");
		}else if("3".equals(type)){
			getRequest().setAttribute("type", "three");
		}else if("4".equals(type)){
			getRequest().setAttribute("type", "four");
		}
		getRequest().setAttribute("ttype", type);
		getRequest().setAttribute("regLogin", 3); // 1:登陆，2:注册，3:找回密码
		
		return "/web/commons/findPassword/zhaoPWD_two";
	}
	
	/**
	 * 更新密码
	 * 
	 * @return 1:成功，2:用户不存在，3:失败
	 */
	@RequestMapping(value = "/web/findpassword/updateUserPassword", method = RequestMethod.POST)
	@ResponseBody
	public int updateUserPassword(){
		// 获取电话号码
		String name = getRequest().getParameter("name");
		String type = getRequest().getParameter("type");
		// 获取需更新的密码
		String password = getRequest().getParameter("password").trim();
		
		// 根据用户名查找用户
		User user = userService.getUserName(name, null);
		if(user == null){
			return 2;
		}
		user.setPassword(new MD5Util().getMD5ofStr(password));
		try {
			if(userService.updateUser(user)){
				return 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return 3;
	}
	
	/**
	 * 跳转到找回密码第三步页面
	 */
	@RequestMapping(value = "/web/findpassword/jumpPasswordPageThree")
	public String jumpPasswordPageThree(){
		String type = getRequest().getParameter("type");
		// mark修改状态(1成功)
		String mark = getRequest().getParameter("mark");
		
		getRequest().setAttribute("regLogin", 3); // 1:登陆，2:注册，3:找回密码
		if ("0".equals(type)) {
			getRequest().setAttribute("type", "zero");
		}else if("1".equals(type)){
			getRequest().setAttribute("type", "one");
		}else if("2".equals(type)){
			getRequest().setAttribute("type", "two");
		}else if("3".equals(type)){
			getRequest().setAttribute("type", "three");
		}else if("4".equals(type)){
			getRequest().setAttribute("type", "four");
		}
		getRequest().setAttribute("mark", mark);
		
		return "/web/commons/findPassword/zhaoPWD_three";
	}
	
	/**
	 * 发送邮件
	 */
	@RequestMapping(value = "/web/findpassword/zhaosendRegMial")
	@ResponseBody
	public Boolean sendRegMial(){
		String type = getRequest().getParameter("type"); // 用户类型
		String name = getRequest().getParameter("name"); // 用户名
		
		User user = userService.getUserName(name, type);
		
		UserMessageService  service = new UserMessageService(getRequest(), "userPwdResetEmail", user, "", "");
        service.run();
				
		return true;
	}
	
}
