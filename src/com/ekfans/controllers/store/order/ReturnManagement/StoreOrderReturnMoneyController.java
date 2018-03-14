package com.ekfans.controllers.store.order.ReturnManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.basic.controller.BasicController;
/**
 * 
* @ClassName: StoreOrderListController
* @Description: TODO(跳转到待订单退款页面)
* @author ekfans
* @date 2014-4-14 下午02:53:40
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreOrderReturnMoneyController extends BasicController{
    @RequestMapping(value = "/store/order/returnMoney")
    public String list(HttpServletRequest request,
            HttpServletResponse response, HttpSession session){
        return "/userCenter/store/order/ReturnManagement/orderReturnMoney";
    }  
}
