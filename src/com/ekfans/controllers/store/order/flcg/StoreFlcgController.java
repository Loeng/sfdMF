package com.ekfans.controllers.store.order.flcg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.WelfarePurchase;
import com.ekfans.base.user.service.IWelfarePurchaseService;
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
public class StoreFlcgController extends BasicController{
    
  //定义service
    @Autowired
    private IWelfarePurchaseService pruchaseService;
    
    /**
     * 
    * @Title: list
    * @Description: TODO(跳转到storeList页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param response
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/flcg/list")
    public String list(){
        //  获取当前的店铺Id
        String storeId = ((Store) getSession().getAttribute(SystemConst.STORE)).getId();
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = getRequest().getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
                currentPage = Integer.parseInt(currentpageStr);
           }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
    
        // 获得企业名
        String companyName = getRequest().getParameter("companyName");
        // 获取商品编号
        String productNo = getRequest().getParameter("productNo");
        // 获取开始日期
        String beginDate = getRequest().getParameter("beginDate");
        // 获取结束日期
        String endDate = getRequest().getParameter("endDate");
        
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
        
        List<WelfarePurchase> purchases = pruchaseService.list(pager, storeId, productNo, companyName, beginTime, endTime);
        getRequest().setAttribute("purchases",purchases);
        getRequest().setAttribute("beginDate",beginDate);
        getRequest().setAttribute("endDate",endDate);
        getRequest().setAttribute("productNo",productNo);
        getRequest().setAttribute("companyName",companyName);
        getRequest().setAttribute("pager",pager);
        getRequest().setAttribute("currentpageStr",currentPage);
    
        return "/userCenter/store/flcg/flcgList";
    }
    
    /**
     * 
    * @Title: query
    * @Description: TODO 福利采购详情
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/flcg/query/{id}")
    public String query(@PathVariable String id){
        // 查找福利采购
        WelfarePurchase purchase = pruchaseService.getPurchaseById(id);
        getRequest().setAttribute("purchase",purchase);
        return "/userCenter/store/flcg/flDetail";
    } 
    
    /**
     * 
    * @Title: close
    * @Description: TODO 福利采购关闭
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/flcg/close/{id}")
    public String close(@PathVariable String id){
        // 查找福利采购
        WelfarePurchase purchase = pruchaseService.getPurchaseById(id);
        // 设置状态为关闭
        purchase.setStatus("2");
        if(pruchaseService.update(purchase)){
        	getRequest().setAttribute("ok",true);
        }else{
        	getRequest().setAttribute("ok",false);
        }
        getRequest().setAttribute("purchase",purchase);
        return "/userCenter/store/flcg/flDetail";
    } 
    
    /**
     * 
    * @Title: handle
    * @Description: TODO 福利采购处理
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/flcg/handle/{id}")
    public String handle(@PathVariable String id){
        // 查找福利采购
        WelfarePurchase purchase = pruchaseService.getPurchaseById(id);
        // 设置状态为处理
        purchase.setStatus("1");
        if(pruchaseService.update(purchase)){
        	getRequest().setAttribute("ok",true);
        }else{
        	getRequest().setAttribute("ok",false);
        }
        getRequest().setAttribute("purchase",purchase);
        return "/userCenter/store/flcg/flDetail";
    } 
}
