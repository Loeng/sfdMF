package com.ekfans.controllers.user.count;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;

/**
 * 设置头像Controller
 */
@Controller
@Scope("prototype")
public class UserLogoController {
	
	private Logger log = LoggerFactory.getLogger(UserLogoController.class);
	@Resource
	private IUserService userService;

	/**
	 * 跳转到用户头像设置的页面
	 */
	@RequestMapping(value = "/user/count/userLogo")
	public String toUpdateUserLogo(HttpServletRequest request) {
		request.setAttribute("cur", "userLogo");
		
		return "/userCenter/customer/count/userLogo";

	}

	/**
	 * 修改用户头像
	 */
	@RequestMapping(value = "/user/logo/modify")
	@ResponseBody
	public Boolean updateUserLogo(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(SystemConst.USER);
		
		User user = userService.getUser(u.getId());
		// 获取用户头像图片地址
		String headPortrait = "/customerfiles/customer/headPortrait/" + DateUtil.getNoSpSysDateString() + "/";
		headPortrait = FileUploadHelper.uploadFile("headPortrait", headPortrait, request, response);
		
		user.setHeadPortrait(headPortrait);
		
		try {
			if(userService.updateUser(user)){
				session.removeAttribute(SystemConst.USER);
				session.setAttribute(SystemConst.USER, user);
				
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	   /**
	    * @Title: safelist
	    * @Description: TODO(安全设置跳转页面)
	    * 详细业务流程:
	    * (详细描述此方法相关的业务处理流程)
	    * @param request
	    * @param session
	    * @return String 返回类型
	    * @throws
	    */
	    
	    @RequestMapping (value="/user/security/setting")
	    public String safelist(HttpServletRequest request,HttpSession session){
	        Store store = (Store)session.getAttribute((String)SystemConst.STORE); // 从登陆中获取用户store
	        User user =(User)session.getAttribute((String)SystemConst.USER); // 从登陆中获取用户user
	        user = userService.getUser(user.getId());
	        request.setAttribute("store",store);
	        request.setAttribute("user",user);
	        request.setAttribute("cur", "sesetting");
	        
	        return "/userCenter/customer/security/account_setting";
	    }
}
