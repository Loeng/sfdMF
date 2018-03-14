package com.ekfans.controllers.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.WelfarePurchase;
import com.ekfans.base.user.service.IWelfarePurchaseService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;

/**
 * 
 * @ClassName: hjcProductListController
 * @Description: TODO(前台-搜索店铺、搜索商品)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-19 下午02:32:49
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class FlShopController extends BasicController {
    // 注入service
    @Autowired
    private IWelfarePurchaseService purchaseService;
    @Autowired
    private IProductService productService;
    
    /**
     * 
    * @Title: getProduct
    * @Description: TODO 根据商品编号查询商品数量单位
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程) void 返回类型
    * @throws
     */
    @RequestMapping(value = "/web/flShop/product")
    @ResponseBody
    public void getProduct(HttpServletResponse resp) {
        // 获取商品编号
        String productNo = getRequest().getParameter("productNo");
        // 查询商品
        Product p = productService.getProductByProductNo(productNo);
        // 返回单位
        if (p != null) {
        	backWriteStr(resp, p.getUnit()+","+p.getId()+","+p.getStoreId());
        } else {
        	backWriteStr(resp, "false");
        }

    }
    
    /**
     * 
    * @Title: apply
    * @Description: TODO 申请福利采购
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param purchase
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/web/flShop/apply")
    @ResponseBody
    public String apply(WelfarePurchase purchase,HttpServletRequest request) {
        // 设置创建时间
        purchase.setCreateTime(DateUtil.getSysDateTimeString());
        
        User user = (User)getSession().getAttribute(SystemConst.USER);
        
        // 登录的话,记录用户id
        if(user != null){
            purchase.setUserId(user.getId());
        }
        
        // 获取性别
        String sex = request.getParameter("sex");
        purchase.setLinkMan(purchase.getLinkMan()+sex);
        
        // 保存申请
        if(purchaseService.save(purchase)){
            return "true";
        }
        return "false";
    }

}
