package com.ekfans.controllers.system.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.user.model.WelfarePurchase;
import com.ekfans.base.user.service.IWelfarePurchaseService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemFlcgController {
    
    @Autowired
    private IWelfarePurchaseService pruchaseService;

    /**
     * 
    * @Title: list
    * @Description: TODO 福利采购列表
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/flcg/list")
    public String list(HttpServletRequest request) {
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
    
        // 获得企业名
        String companyName = request.getParameter("companyName");
        // 获取商品编号
        String productNo = request.getParameter("productNo");
        // 获取开始日期
        String beginDate = request.getParameter("beginDate");
        // 获取结束日期
        String endDate = request.getParameter("endDate");
        
        // 定义开始时间
        String beginTime = "";
        // 定义结束时间
        String endTime = "";
        
        // 开始时间拼接
        if(!StringUtil.isEmpty(beginDate)){
            beginTime = beginDate+" 00:00:00";
        }
        // 结束时间拼接
        if(!StringUtil.isEmpty(endDate)){
            endTime = endDate+" 23:59:59";
        }
       
        // 订单状态
        
        List<WelfarePurchase> purchases = pruchaseService.list(pager, null, productNo, companyName, beginTime, endTime);
        request.setAttribute("purchases",purchases);
        request.setAttribute("beginDate",beginDate);
        request.setAttribute("endDate",endDate);
        request.setAttribute("productNo",productNo);
        request.setAttribute("companyName",companyName);
        request.setAttribute("pager",pager);
        request.setAttribute("currentpageStr",currentPage);
    
        return "/system/order/flcgList";
    }

    /**
     * 
    * @Title: detail
    * @Description: TODO 福利采购详情
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/flcg/detail/{id}")
    public String detail(@PathVariable
    String id, HttpServletRequest request) {
        // 查找福利采购
        WelfarePurchase purchase = pruchaseService.systemGetPurchaseById(id);
        request.setAttribute("purchase",purchase);
        return "/system/order/flcgDetail";
    }

}
