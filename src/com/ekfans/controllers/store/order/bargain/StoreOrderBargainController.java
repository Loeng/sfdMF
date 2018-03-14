package com.ekfans.controllers.store.order.bargain;

import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.service.IInquiryService;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.base.product.service.IProductPriceService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: StoreOrderListController
* @Description: TODO(跳转到供应借款页面)
* @author ekfans
* @date 2014-4-14 下午02:53:40
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */


@Controller
public class StoreOrderBargainController extends BasicController{
    
    private Logger log = LoggerFactory.getLogger(StoreOrderBargainController.class); 
    //定义service
    @Autowired
    private IInquiryService inquiryService;
    @Autowired
    private IProductPriceService productPriceService;
    /**
     * 
    * @Title: purchaseList
    * @Description: TODO(核心企业采购管理议价列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response
    * @param session
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value ="/store/purchase/pricelist")
    public String purchasePriceList(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        //查询当前登陆用户的店铺ID
        Store userId = (Store) session.getAttribute(SystemConst.STORE);
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取页数
        String currentpageStr = request.getParameter("pageNum");
        //获取页面查询参数
        String proName = request.getParameter("proName");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            try {
                currentPage = Integer.parseInt(currentpageStr);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        // 利用Service的方法查找频道
        List<Inquiry> inquirys = inquiryService.getList(pager, proName, minPrice, maxPrice, userId.getId(),1);
        request.setAttribute("pager", pager);
        request.setAttribute("userId", userId.getId());
        request.setAttribute("minPrice", minPrice);
        request.setAttribute("maxPrice", maxPrice);
        request.setAttribute("inquirys", inquirys);
        request.setAttribute("type", "1");
        request.setAttribute("proName", proName);
        return "/userCenter/store/order/bargain/supplyBargainList";
    }
    
    /**
     * @Title: list
     * @Description: TODO(跳转到供应议价列表页面)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param request
     * @param response
     * @param session
     * @return String 返回类型
     * @throws
      */
     @RequestMapping(value = "/store/supply/list")
     public String list(HttpServletRequest request, HttpServletResponse response, HttpSession session){
         // 获取当前的店铺Id
         Store userId = (Store) session.getAttribute(SystemConst.STORE);
         // 定义分页
         Pager pager = new Pager();
         // 从页面商品名称
         String proName = request.getParameter("proName");
         String minPrice = request.getParameter("minPrice");
         String maxPrice = request.getParameter("maxPrice");
         // 从页面获取页数
         String currentpageStr = request.getParameter("pageNum");
         // 将从页面获取的分页数据转化成int型
         int currentPage = 1;
         if (!StringUtil.isEmpty(currentpageStr)) {
             try {
                 currentPage = Integer.parseInt(currentpageStr);
             } catch (Exception e) {
                log.error(e.getMessage());
             }
         }
         // 设置要查询的页码
         pager.setCurrentPage(currentPage);
         // 利用Service的方法查找频道
         List<Inquiry> inquirys = inquiryService.getList(pager, proName, minPrice, maxPrice, userId.getId(), 0);
         request.setAttribute("proName", proName);
         request.setAttribute("minPrice", minPrice);
         request.setAttribute("maxPrice", maxPrice);
         request.setAttribute("pager", pager);
         request.setAttribute("type", "0");
         request.setAttribute("userId", userId.getId());
         request.setAttribute("inquirys", inquirys);
         return "/userCenter/store/order/bargain/supplyBargainList";
     }
    /**
    * @Title: list
    * @Description: TODO(跳转到供应议价列表页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response
    * @param session
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/supply/supplylist")
    public String listGys(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        // 获取当前的店铺Id
        String userId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
        // 定义分页
        Pager pager = new Pager();
        // 从页面商品名称
        String proName = request.getParameter("proName");
        // 从页面获取页数
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            try {
                currentPage = Integer.parseInt(currentpageStr);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        // 利用Service的方法查找频道
        List<Inquiry> inquirys = inquiryService.getsupplyList(pager, proName, userId);
        request.setAttribute("proName", proName);
        request.setAttribute("pager", pager);
        request.setAttribute("inquirys", inquirys);
        request.setAttribute("userId", userId);
        request.setAttribute("type", "0");
        return "/userCenter/store/coreCompany/supplyBargainList";
    }  
    
    /**
     * 
    * @Title: loadById
    * @Description: TODO(load出议价详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/supple/load/{id}/{type}/{userId}")
    public String loadById(@PathVariable String id,@PathVariable String type,@PathVariable String userId){
        // 得到议价信息
        Inquiry i = inquiryService.getIdByInquiry(id);
        // 得到商品的阶梯价集合
        List<ProductPrice> pps = productPriceService.getProductPriceByProductId(i.getProductId());
        getRequest().setAttribute("i", i);
        getRequest().setAttribute("type", type);
        getRequest().setAttribute("userId", userId);
        getRequest().setAttribute("pps", pps);
        return "/userCenter/store/order/bargain/loadSupply";
    }
    @RequestMapping(value = "supply/loadGys/{id}/{type}")
    public String loadByIdGys(@PathVariable String id,@PathVariable String type){
        // 得到议价信息
        Inquiry i = inquiryService.getById(id);
        //根据议价ID查询议价详情
        getRequest().setAttribute("i", i);
        getRequest().setAttribute("type", type);
        return "/userCenter/store/coreCompany/loadSupplyGys";
    }
    /**
     * 保存
     */
    @RequestMapping(value = "/store/supply/save")
    public String saveSupply(){
        // 从页面得到议价id
        String id = getRequest().getParameter("id");
        // 页面得到的确认数量
        String sellersNumber = getRequest().getParameter("sellersNumber");
        // 页面得到的确认价格
        String FinalPrice = getRequest().getParameter("FinalPrice");
        //得到对应的数据
        Inquiry i = inquiryService.getById(id);
        i.setStatus("1");
        i.setSellersNumber(Integer.valueOf(sellersNumber));
        i.setFinalPrice(new BigDecimal(FinalPrice));
        //修改
        if(inquiryService.updateInquiry(i)){
            return "redirect:/store/supply/supplylist";
        }
        return "redirect:/store/supply/supplylist";
    }
}
