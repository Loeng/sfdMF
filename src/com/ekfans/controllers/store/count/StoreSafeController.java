package com.ekfans.controllers.store.count;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;

/**
 * 
 * 店铺商品Controller
 * 
 * @Title: StoreSafeController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liulin
 * @date 2014-3-26 下午14:41:47 
 * @version V1.0
 */
@Controller
public class StoreSafeController extends BasicController {
    @Autowired
    IUserService userService;

    /**
     * 跳转到安全设置页面
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/store/safe/list")
    public String toUpdateUserPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 从Session中获取user对象
        Store store = (Store) session.getAttribute(SystemConst.STORE);
        User user = userService.getUser(store.getId());
        request.setAttribute("user", user);
        return "/store/count/safeList";
    }
}
