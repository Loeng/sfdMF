package com.ekfans.controllers.web;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreApply;
import com.ekfans.base.store.service.IStoreApplyService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.pub.util.CookieUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 店铺登录Controller
 *
 * @author ll
 * @version V1.0
 * @Title: StoreLoginController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @date 2014-5-15 上午9:18:54 
 */
@Controller
public class WebLoginController extends BasicController {

    // 定义SERVICE
    @Autowired
    private IUserService userService;

    @Autowired
    private IStoreService storeService;

    @Autowired
    private IStoreApplyService storeApplyService;
    // 邮件注册用户，转户激活加密字段
    private final String FIELDPWD = "ekfans.com";

    /**
     * @param request
     * @param response
     * @return ModelAndView 返回类型
     * @throws
     * @Title: goLogin
     * @Description: TODO(首页的登陆页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/login")
    public String goLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        // 获取当前登录用户
        User user = (User) request.getSession().getAttribute(SystemConst.USER);
        // 用户存在重定向到企业中心页面
        if (null != user) {
            return "redirect:/store/index";
        }
        return "/web/purchase/login/login";
    }
//	public ModelAndView goLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		
//		// 定义Cookie工具类
//		CookieUtil cookieUtil = new CookieUtil(request, response);
//		
//		// 定义返回参数的Map
//		Map<String, Object> map = new HashMap<String, Object>();
//		
//		// 调用工具类获取管理员的登录Cookies
//		String cookieName = cookieUtil.getStoreLoginNameCookie();
//		String isCookie = cookieUtil.getValueByKey("storeRemeber");
//		map.put("cookieName", !StringUtil.isEmpty(cookieName) ? cookieName : "");
//		map.put("isCookie", !StringUtil.isEmpty(isCookie) ? isCookie : "");
//		map.put("jumpAddressUrl", request.getParameter("jumpAddressUrl"));
//		return new ModelAndView("/web/purchase/login/webLogin", map);
//	}

    /**
     * @return String 返回类型
     * @throws
     * @Title: userRegist
     * @Description: TODO(会员的注册) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/user/regist")
    public String userRegist() {
        return "/web/regist/userRegist";
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: userRegist
     * @Description: TODO(商户的注册) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/store/regist")
    public String storeRegist() {
        return "/web/regist/storeRegist";
    }

    /**
     * @param storeApply
     * @param request
     * @return String 返回类型
     * @throws
     * @Title: storeSave
     * @Description: TODO(店铺申请的保存) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/storeApply/save")
    public String storeSave(StoreApply storeApply, HttpServletRequest request, HttpSession session) {
        // 从SESSION中获取系统生成的验证码
        String sessionCode = (String) session.getAttribute("SESSION_SECURITY_CODE");
        // 从页面获取验证码
        String verifyCode = request.getParameter("verifyCode");
        // 如果从SESSOIN中获取的验证码为空或者两个验证码不匹配，则返回失败
        if (StringUtil.isEmpty(sessionCode) || !sessionCode.equals(verifyCode)) {
            request.setAttribute("verufyError", true);
            request.setAttribute("companyName", storeApply.getCompanyName());
            request.setAttribute("contacts", storeApply.getContacts());
            request.setAttribute("mobile", storeApply.getMobile());
            request.setAttribute("email", storeApply.getEmail());
            request.setAttribute("address", storeApply.getAddress());
            request.setAttribute("zipCode", storeApply.getZipCode());
            request.setAttribute("domain", storeApply.getDomain());
            request.setAttribute("note", storeApply.getNote());
            return "/web/regist/storeRegist";
        }
        // 判断是否添加成功
        if (storeApplyService.addStoreApply(storeApply)) {
            return "/web/regist/storeRegistSuccess";
        } else {
            return "error";
        }

    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: userSave
     * @Description: TODO(手机新增会员保存) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/user/save")
    public String userSave(User user, HttpServletRequest request, HttpSession session) {
        // 验证会员名和密码是否为空
        if (StringUtil.isEmpty(user.getName()) || StringUtil.isEmpty(user.getPassword())) {
            // 如果为空，返回添加失败
            return "/web/regist/userRegist";
        }
        // 设置会员类型为用户
        user.setType(UserConst.USER_TYPE_CUSTOMER);
        // 利用Service的方法添加用户
        if (userService.addUser(user, null, request)) {
            session.setAttribute(SystemConst.USER, user);
            // 将手机和用户名绑定到另一个页面
            request.setAttribute("name", user.getName());
            request.setAttribute("moblie", user.getMobile());
            // 添加成功，返回
            return "/web/regist/registSuccess";
        }
        return "error";
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: userSave
     * @Description: TODO(邮箱新增会员保存) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/user/emailSave")
    public String userEmailSave(User user, HttpServletRequest request, HttpSession session) {
        // 从session获取系统生成的验证码
        String sessionCode = (String) session.getAttribute("SESSION_SECURITY_CODE");
        // 获取客户输入的验证码
        String verifyCode = request.getParameter("verifyCode").trim();

        // 如果从SESSOIN中获取的验证码为空或者两个验证码不匹配，则返回失败
        if (StringUtil.isEmpty(sessionCode) || !sessionCode.equals(verifyCode)) {
            request.setAttribute("verufyError", true);
            request.setAttribute("name", user.getName());
            request.setAttribute("email", user.getEmail());
            return "/web/regist/userRegist";
        }
        /*
		 * if (StringUtil.isEmpty(user.getName()) ||
		 * StringUtil.isEmpty(user.getPassword())) { // 如果为空，返回添加失败 return
		 * "/web/regist/userRegist"; }
		 */
        user.setEmail(user.getEmail().trim());
        user.setName(user.getName().trim());

        // 设置会员类型为用户
        user.setType(UserConst.USER_TYPE_CUSTOMER);
        // 设置账户为未激活
        user.setVerificationStatus(false);
        // 利用Service的方法添加用户
        if (userService.addUser(user, null, request)) {
            // 邮件标题
            String emailTitle = "账户激活";
            // 邮件内容
            String emailContent = "点击：" + getBasePath() + "/web/regist/accountcheck?userName=" + user.getName() + "&code=" + new MD5Util().getMD5ofStr(user.getName() + FIELDPWD);
            // 发送邮件（这是注册后激活账户的邮件）
            if (!MessageUtil.sendMail(user.getEmail(), emailTitle, emailContent)) {
                request.setAttribute("judgmentMail", "邮件发送失败,请点击下方重新发送按钮");
            } else {
                request.setAttribute("judgmentMail", "还差一步，请激活您的帐户");
            }

            request.setAttribute("name", user.getName());
            request.setAttribute("email", user.getEmail());
            // 添加成功，返回
            return "/web/regist/emailSuccess";
        }
        return "error";
    }

    /**
     * 会员登录信息校验
     *
     * @return
     */
    @RequestMapping(value = "/web/loginCheck", method = RequestMethod.POST)
    public String logCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 从SESSION中获取系统生成的验证码
        String sessionCode = (String) session.getAttribute("SESSION_SECURITY_CODE");
        // 从页面获取验证码
        String verifyCode = request.getParameter("verifyCode");
        // 获取页面输入的用户名
        String name = request.getParameter("name");
        // 获取页面输入的密码
        String pwd = request.getParameter("password");
        // 获取页面获取的是否保存用户名
        String isCookie = request.getParameter("isCookie");

        // 如果从SESSOIN中获取的验证码为空或者两个验证码不匹配，则返回失败
        if (StringUtil.isEmpty(sessionCode) || !sessionCode.equals(verifyCode)) {
            request.setAttribute("verufyError", true);
            request.setAttribute("cookieName", name);
            return "webLogin";
        }
        // 调用Service根据用户名获取用户
        User user = userService.getStoreUserByName(name);
        // 如果获取的用户对象为空，则证明用户名不存在
        if (user == null) {
            // 将验证信息放入request，返回视图页面
            request.setAttribute("nameError", true);
            request.setAttribute("cookieName", name);
            return "webLogin";
        }

        if (!user.getVerificationStatus()) {
            // 将验证信息放入request，返回视图页面
            request.setAttribute("nameError", true);
            request.setAttribute("cookieName", name);
            return "webLogin";
        }
        // 定义Md5工具类
        MD5Util m = new MD5Util();
        // 如果获取的页面输入密码为空，或者Md5加密后和从数据库取出的密码不一致，则返回密码错误
        if (StringUtil.isEmpty(pwd) || !m.getMD5ofStr(pwd).equals(user.getPassword())) {
            // 将验证信息放入request，返回视图页面
            request.setAttribute("pwdError", true);
            request.setAttribute("cookieName", name);
            return "webLogin";
        }
        try {
            // 如果输入的是会员
            if (user.getType().equals(UserConst.USER_TYPE_CUSTOMER)) {
                // 定义Cookie工具类
                CookieUtil cookieUtil = new CookieUtil(request, response);

                // 将用户名密码记入Cookie
                if ("true".equals(isCookie)) {
                    cookieUtil.addStoreCookie(user.getId(), name);
                    cookieUtil.addCookie("storeRemeber", "true");
                } else {
                    cookieUtil.removeManagerCookie(user.getId());
                    cookieUtil.removeCookieByKey("storeRemeber");
                }

                // 更改上次登陆时间
                user.setLastLoginTime(DateUtil.getSysDateTimeString());
                // 更改登陆次数
                user.setLoginNum(user.getLoginNum() + 1);
                // 调用Service更新会员
                userService.updateUser(user);
                // 将User对象放入Session中
                session.setAttribute(SystemConst.USER, user);
                String jumpAddressUrl = request.getParameter("jumpAddressUrl");
                if (!StringUtil.isEmpty(jumpAddressUrl)) {
                    return "redirect:" + jumpAddressUrl;
                }
                return "redirect:/user/center/index";

            } else {
                // 调用Service方法从数据库根据用户名查询Store对象
                Object[] obj = storeService.storeLoginByName(name);
                // 得到store对象
                if (obj != null) {
                    Store store = (Store) obj[1];
                    // 定义Cookie工具类
                    CookieUtil cookieUtil = new CookieUtil(request, response);

                    if ("true".equals(isCookie)) {
                        // TODO 将用户名密码记入Cookie
                        cookieUtil.addStoreCookie(store.getId(), name);
                        cookieUtil.addCookie("storeRemeber", "true");
                    } else {
                        cookieUtil.removeManagerCookie(store.getId());
                        cookieUtil.removeCookieByKey("storeRemeber");
                    }

                    // 更改上次登陆时间
                    user.setLastLoginTime(DateUtil.getSysDateTimeString());
                    // 更改登陆次数
                    user.setLoginNum(user.getLoginNum() + 1);
                    userService.updateUser(user);
                    // 将User对象放入Session中
                    session.setAttribute(SystemConst.USER, user);
                    // 将Store对象放入Session中
                    session.setAttribute(SystemConst.STORE, store);
                    String jumpAddressUrl = request.getParameter("jumpAddressUrl");
                    if (!StringUtil.isEmpty(jumpAddressUrl)) {
                        return "redirect:" + jumpAddressUrl;
                    }
                    // 返回到店铺会员中心首页
                    return "redirect:/store/index";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";

    }

    @RequestMapping(value = "/web/login/checkName/{name}")
    @ResponseBody
    public Object loginCheckName(@PathVariable String name, HttpServletRequest request) {
        // 如果用户名存在，返回false，否则返回true
        if (userService.judgmentRepeat(null, name, null)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param productNumber
     * @return Object 返回类型
     * @throws
     * @Title: checkName
     * @Description: TODO(验证用户名是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/regist/checkName")
    @ResponseBody
    public Object checkName(HttpServletRequest request) {
        // 获取用户名
        String name = request.getParameter("name");
        // 如果用户名重复，返回false，否则返回true
        if (userService.judgmentRepeat(null, name, null)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param request
     * @return Object 返回类型
     * @throws
     * @Title: checkStoreName
     * @Description: TODO 验证企业名是否存在 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/regist/checkStoreName")
    @ResponseBody
    public Object checkStoreName(HttpServletRequest request) {
        // 获取企业名
        String name = request.getParameter("name");
        // 如果企业名重复，返回false，否则返回true
        if (storeApplyService.judgmentRepeat(name)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param mobile
     * @return Object 返回类型
     * @throws
     * @Title: checkMobile
     * @Description: TODO(验证手机号是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/regist/checkMobile/{mobile}")
    @ResponseBody
    public Object checkMobile(@PathVariable String mobile) {
        // 如果手机号重复，返回false，否则返回true
        if (userService.judgmentRepeat(null, null, mobile)) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * @param checkEmail
     * @return Object 返回类型
     * @throws
     * @Title: checkEmail
     * @Description: TODO(验证邮箱是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/regist/checkEmail")
    @ResponseBody
    public Object checkEmail(HttpServletRequest request) {
        // 获取email
        String checkEmail = request.getParameter("email");
        // 如果email重复，返回false，否则返回true
        if (userService.judgmentRepeat(checkEmail, null, null)) {
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping(value = "/web/regist/sendCode/{phoneNo}")
    @ResponseBody
    public Object sendCode(@PathVariable String phoneNo, HttpServletRequest request, HttpSession session) {
        // 获取6位随机数,放入session
        MessageUtil mUtil = new MessageUtil();
        int radom = mUtil.randomNo();
        session.setAttribute("mobileCode", String.valueOf(radom));
        // 获取短信内容
        String content = mUtil.getContent(radom);
        // 如果发送成功返回true
        if (MessageUtil.sendPhoneMessage(phoneNo, content)) {
            return true;
        }
        return false;
    }

    /**
     * @param code
     * @return Object 返回类型
     * @throws
     * @Title: checkCode
     * @Description: TODO(手机验证码校验) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/web/regist/checkCode/{code}")
    @ResponseBody
    public Object checkCode(@PathVariable String code, HttpServletRequest request, HttpSession session) {
        // 获取session中的验证码
        String mobileCode = (String) session.getAttribute("mobileCode");
        // 如果匹配返回true
        if (code.equals(mobileCode)) {
            return true;
        }
        return false;
    }

    /**
     * 邮件注册用户，激活账户方法
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/web/regist/accountcheck")
    public String accountActivation(HttpServletRequest request) {
        // 获取用户名
        String userName = request.getParameter("userName");
        // 获取特定识别字符
        String code = request.getParameter("code");
        // 获取客户信息
        User user = userService.getUserByName(userName);
        if (null != user && (new MD5Util().getMD5ofStr(userName + FIELDPWD)).equals(code)) {
            user.setVerificationStatus(true);
            try {
                // 设置用户账户状态为：激活
                userService.updateUser(user);

                // 邮件激活账户成功后自动登录
                request.getSession().setAttribute(SystemConst.USER, user);
                // request.getSession().setAttribute(SystemConst.STORE, store);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("name", user.getName());
        request.setAttribute("moblie", user.getMobile());
        request.setAttribute("email", user.getEmail());
        return "/web/regist/registSuccess";
    }

    /**
     * 重新发送邮件激活账户
     *
     * @param request
     */
    @RequestMapping(value = "/web/regist/resendEmailInfo")
    public void resendEmailInfo(HttpServletRequest request, HttpServletResponse response) {
        String judgment = "false";
        // 获取用户名
        String name = request.getParameter("name");
        // 获取邮件地址
        String email = request.getParameter("email");

        // 邮件标题
        String emailTitle = "账户激活";
        // 邮件内容
        String emailContent = "点击：" + getBasePath() + "web/regist/accountcheck?userName=" + name + "&code=" + new MD5Util().getMD5ofStr(name + FIELDPWD);
        // 发送邮件（这是注册后激活账户的邮件）
        if (MessageUtil.sendMail(email, emailTitle, emailContent)) {
            judgment = "true";
        }
        try {
            PrintWriter writer = response.getWriter();
            writer.print(judgment);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于弹出窗的登陆验证信息校验
     *
     * @return
     */
    @RequestMapping(value = "/web/productDetail/loginCheck", method = RequestMethod.POST)
    @ResponseBody
    public Object alertLogCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 从SESSION中获取系统生成的验证码
        String sessionCode = (String) session.getAttribute("SESSION_SECURITY_CODE");
        // 从页面获取验证码
        String verifyCode = request.getParameter("verifyCode");
        // 获取页面输入的用户名
        String name = request.getParameter("name");
        // 获取页面输入的密码
        String pwd = request.getParameter("password");
        // 获取页面获取的是否保存用户名
        String isCookie = request.getParameter("isCookie");

        // 如果从SESSOIN中获取的验证码为空或者两个验证码不匹配，则返回失败
        if (StringUtil.isEmpty(sessionCode) || !sessionCode.equals(verifyCode)) {
            return false;
        }
        // 调用Service根据用户名获取用户
        User user = userService.getStoreUserByName(name);
        // 如果获取的用户对象为空，则证明用户名不存在
        if (user == null) {
            return false;
        }

        if (!user.getVerificationStatus()) {
            // 将验证信息放入request，返回视图页面

            return "false";
        }

        // 定义Md5工具类
        MD5Util m = new MD5Util();

        // 如果获取的页面输入密码为空，或者Md5加密后和从数据库取出的密码不一致，则返回密码错误
        if (StringUtil.isEmpty(pwd) || !m.getMD5ofStr(pwd).equals(user.getPassword())) {
            // 将验证信息放入request，返回视图页面

            return "false";
        }

        try {
            // 如果输入的是会员
            if (user.getType().equals(UserConst.USER_TYPE_CUSTOMER)) {
                // 定义Cookie工具类
                CookieUtil cookieUtil = new CookieUtil(request, response);

                // 将用户名密码记入Cookie
                if ("true".equals(isCookie)) {
                    cookieUtil.addStoreCookie(user.getId(), name);
                    cookieUtil.addCookie("storeRemeber", "true");
                } else {
                    cookieUtil.removeManagerCookie(user.getId());
                    cookieUtil.removeCookieByKey("storeRemeber");
                }

                // 更改上次登陆时间
                user.setLastLoginTime(DateUtil.getSysDateTimeString());
                // 更改登陆次数
                user.setLoginNum(user.getLoginNum() + 1);
                // 调用Service更新会员
                userService.updateUser(user);
                // 将User对象放入Session中
                session.setAttribute(SystemConst.USER, user);

                return true;

            } else {
                return "storeError";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 更新ccbid
     *
     * @return
     */
    @RequestMapping(value = "/web/user/allupdate")
    @ResponseBody
    public String allUpadte(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Boolean status = userService.updateAllUserForCcb();
        return String.valueOf(status);
    }
}
