package com.ekfans.controllers.web;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IAccountService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 登陆Controller
 *
 * @author ek_lq
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: RegistrationController
 * @date 2014-9-20 上午11:55:14
 */
@Controller
public class UserAndStoreLoginController extends BasicController {

    @Resource
    private IUserService userService;
    @Resource
    private IStoreService storeService;

    /**
     * 跳转到登陆页面
     *
     * @param type 会员类型（0：个人会员，1：供应商，2：采购商，3：核心企业）
     */
    @RequestMapping(value = "/web/login/{type}")
    public String jumpLoginPage(@PathVariable String type) {
        // 1:登录，2:注册
        getRequest().setAttribute("regLogin", 1);

        return "redirect:/web/login"; // （处置企业，采购企业，供应企业，运输企业）登陆页面
    }

    /**
     * （个人，核心企业，采购商，供应商）登陆验证
     */
    @RequestMapping(value = "/web/login/userLoginUtil", method = RequestMethod.POST)
    @ResponseBody
    public int userLogin() {
        String name = getRequest().getParameter("name"); // 用户名
        String password = getRequest().getParameter("password"); // 密码
        String yanzheng = getRequest().getParameter("code"); // 输入的验证码
        String oldyz = getSession().getAttribute("SESSION_SECURITY_CODE").toString(); // session中的验证码

        // 0:禁用，1:启用，2:删除，3:用户名不存在，4:密码错误，5:验证码错误，6:账号未激活, 7:帐号信息需要完善
        int mark = -1;
        if ((yanzheng.trim()).equalsIgnoreCase(oldyz)) {
            // 查询用户信息
            User user = this.userService.getUserByName(name);
            Account account = null;
            // 如果用户对象为空，则查找子账户
            if (user == null) {
                // 反射机制获取Service
                IAccountService accountService = SpringContextHolder.getBean(IAccountService.class);
                // 调用Service根据用户名查询对象
                account = accountService.getAccountLoginByName(name);
            }

            if (user == null && account == null) {
                mark = 3;
                return mark;
            }

            String oldP = ""; // 用户密码
            String newP = new MD5Util().getMD5ofStr(password); // 用户输入的密码

            if (user == null) {
                oldP = account.getPassword();
                user = userService.getUser(account.getStoreId());
            } else {
                oldP = user.getPassword();
            }
            if (user == null) {
                mark = 3;
                return mark;
            }

            if (user.getVerificationStatus()) {
                if (user.getStatus() == 1 && oldP.equals(newP)) {
                    if ("1".equals(user.getTouristType())) {
                        getSession().setAttribute(SystemConst.USER_NEED_SUP, user);
                        mark = 7;
                        return mark;
                    }
                    if (account != null) {
                        getSession().setAttribute(SystemConst.ACCOUNT, account);
                    }
                    getSession().setAttribute(SystemConst.USER, user);
                    getSession().setAttribute(SystemConst.STORE, this.storeService.getStore(user.getId()));
                    mark = 1;
                    return mark;
                } else if (user.getStatus() == 1 && !oldP.equals(newP)) {
                    mark = 4;
                } else {
                    mark = user.getStatus();
                }
            } else {
                mark = 6;
            }
        } else {
            mark = 5;
        }

        return mark;
    }

    /**
     * 完善信息跳转
     *
     * @return
     */
    @RequestMapping(value = "/web/util/jumpSupplementPage")
    public String jumpSupplementPage() {
        Object obj = getSession().getAttribute(SystemConst.USER_NEED_SUP);
        if (obj == null) {
            return "redirect:/store/index";
        }
        return "/web/purchase/login/supplementUser";
    }

    /**
     * 登陆成功后跳转
     */
    @RequestMapping(value = "/web/util/jumpZhongPage")
    public String jumpZhongPage() {
        Object obj = getSession().getAttribute(SystemConst.USER);

//		Cookie[] cookies = getRequest().getCookies();
//		if (cookies != null && cookies.length > 0) {
//			for (int i = 0; i < cookies.length; i++) {
//				Cookie cookie = cookies[i];
//				if (cookie != null && "gylReturnUrl".equals(cookie.getName())) {
//					String returnUrl = cookie.getValue();
//					if (!StringUtil.isEmpty(returnUrl)) {
//						if (obj != null) {
//							User user = (User) obj;
//							if ("1".equals(user.getType()) && URLDecoder.decode(returnUrl).indexOf("01-") != -1) {
//								return "redirect:/store/index";
//							}
//						}
//						return "redirect:" + URLDecoder.decode(returnUrl);
//					}
//				}
//			}
//		}

        if (obj != null) {
            User user = (User) obj;
//			if ("0".equals(user.getType())) {
//				return "redirect:/user/center/index";
//			} else {
            return "redirect:/store/index";
//			}
        }
        return "";
    }

    /**
     * 完善成功后跳转
     */
    @RequestMapping(value = "/web/util/jumpZhongPageBySup")
    public String jumpZhongPageBySup() {
        Object obj = getSession().getAttribute(SystemConst.USER_NEED_SUP);

        if (obj != null) {
            User user = (User) obj;
            // 重新初始化登陆对象
            getSession().setAttribute(SystemConst.USER, this.userService.getUser(user.getId()));
            getSession().setAttribute(SystemConst.STORE, this.storeService.getStore(user.getId()));
            return "redirect:/store/index";
        }
        return "";
    }

    /**
     * 安全退出操作
     */
    @RequestMapping(value = "/web/logout/userLogout")
    public String userLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        Object obj = session.getAttribute(SystemConst.USER);
        if (obj == null) {
            return "redirect:/web/login";
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie != null && "gylReturnUrl".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setValue(null);
                    response.addCookie(cookie);
                }
            }
        }

        session.removeAttribute(SystemConst.USER);
        String ttype = ((User) obj).getType();
        if (!"0".equals(ttype)) {
            session.removeAttribute(SystemConst.STORE);
        }
        session.removeAttribute(SystemConst.ACCOUNT);
        session.removeAttribute("storePurview");
        session.removeAttribute("purviewId");
        session.removeAttribute("purviewList");

        return "redirect:/web/login";
    }
}
