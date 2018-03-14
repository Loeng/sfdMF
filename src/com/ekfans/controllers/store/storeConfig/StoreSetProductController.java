package com.ekfans.controllers.store.storeConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.basic.controller.BasicController;
/**
 * 
* @ClassName: StoreIndexProductController
* @Description: TODO(跳转到商品设置页面)
* @author ekfans
* @date 2014-4-14 下午12:00:26
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreSetProductController extends BasicController{
    @RequestMapping(value = "/store/set/product")
    public String list(HttpServletRequest request,
            HttpServletResponse response, HttpSession session){
        return "store/config/storeSetProduct";
    }
}
