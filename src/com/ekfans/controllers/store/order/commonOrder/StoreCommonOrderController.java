package com.ekfans.controllers.store.order.commonOrder;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: StoreOrderListController
* @Description: TODO(大宗订单)
* @author ekfans
* @date 2014-4-14 下午02:53:40
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */


@Controller
public class StoreCommonOrderController extends BasicController{
    
  //定义service
    @Autowired
    private IStoreOrderService storeOrderService;
    
    /**
     * 跳转到storeList页面
	 * @param actType 查询类型（1 销售订单 2 采购订单）
	 * @param orderType 订单类型 （0成品 2绿色）
	 * @param request
	 * @param session
	 * @return
    */
    @RequestMapping(value = "/store/order/gylList/{actType}/{orderType}")
    public String list(@PathVariable String actType,@PathVariable String orderType,HttpServletRequest request,HttpSession session){
        //  获取当前的店铺Id
        String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
                currentPage = Integer.parseInt(currentpageStr);
           }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        pager.setRowsPerPage(10);
        // 获得相关参数
        String orderId = request.getParameter("orderId");
        String userName = request.getParameter("userName");
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        // 订单状态
        
        List<Order> orders = storeOrderService.getStoreOrderByConditions(storeId,orderId,userName,beginDate,endDate,null,orderType,actType,pager);
        request.setAttribute("orders",orders);
        request.setAttribute("orderId",orderId);
        request.setAttribute("userName",userName);
        request.setAttribute("beginDate",beginDate);
        request.setAttribute("endDate",endDate);
        request.setAttribute("actType",actType);
        request.setAttribute("orderType",orderType);
        request.setAttribute("pageNum",currentpageStr);
        request.setAttribute("pager",pager);
        request.setAttribute("currentpageStr",currentPage);
        return "/userCenter/store/order/gyl/orderList";
    } 
}
