package com.ekfans.controllers.user.collect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.ProductCollect;
import com.ekfans.base.product.service.IProductCollectService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 收藏商品Controller
 * 
 * @ClassName: UserProductCollectController
 * @Description: 
 * @author liulin
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class UserProductCollectController extends BasicController{

	private Logger log = LoggerFactory.getLogger(UserProductCollectController.class);
    @Autowired
    private IProductCollectService productCollectService;
    
    /**
     * 
    * @Title: list
    * @Description: TODO(展现商品收藏列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param session
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/user/collect/productCollect")
    public String list(HttpSession session,HttpServletRequest request){
     // 定义分页
        Pager pager = new Pager();
     // 从页面获取分页数据
        String currentpageStr = request.getParameter("pageNum");

        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            try {
                currentPage = Integer.parseInt(currentpageStr);
            } catch (Exception e) {
            	log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
       
        // 获取登陆用户的信息,完整的对象
        User user = (User)session.getAttribute(SystemConst.USER);
        if(user == null) {
            return "redirect:/web/login";
        }
        String userId = user.getId();
        //得到对应的收藏商品
        List<ProductCollect> pcs = productCollectService.getList(pager, userId);
        request.setAttribute("pcs", pcs);
        request.setAttribute("pager", pager);
        request.setAttribute("cur","productCollect");
        return "/userCenter/customer/collect/productCollect";
    }
    
    /**
     * 删除商品
     * 
     * @return
     */
    @RequestMapping(value = "/user/productCollect/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable String id) {
        // 利用Service的方法根据id删除商品s
        productCollectService.delete(id);
        // 删除成功，返回true
        return true;
    }
    
    /**
     * 
    * @Title: prductCollectLoad
    * @Description: TODO(load出的商品收藏页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param session
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/user/productCollect/load")
    public String prductCollectLoad(HttpSession session,HttpServletRequest request){
     // 定义分页
        Pager pager = new Pager();
     // 从页面获取分页数据
        String currentpageStr = request.getParameter("pageNum");

        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            try {
                currentPage = Integer.parseInt(currentpageStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        if(session == null) {
            return "/user/login";
        }
        // 获取登陆用户的信息,完整的对象
        User user = (User)session.getAttribute(SystemConst.USER);
        String userId = user.getId();
        //得到对应的收藏商品
        List<ProductCollect> pcs = productCollectService.getList(pager, userId);
        request.setAttribute("pcs", pcs);
        request.setAttribute("pager", pager);
        return "/userCenter/customer/collect/productCollectLoad";
    }
    
    @RequestMapping(value = "/user/productCollect/add/{id}")
    @ResponseBody
    public Object add(@PathVariable String id,HttpSession session) {
        User user = (User)session.getAttribute(SystemConst.USER);
        if(user == null) {
          return "false";
        }
        //判断根据商品id能否查询出收藏的商品
       ProductCollect pc = productCollectService.getProductCollectByProductId(id,user.getId());
       //如果可以查询到 则返回false
       if(pc == null){
           ProductCollect productCollect = new ProductCollect();
           // 获取登陆用户的信息,完整的对象
           String userId = user.getId();
           productCollect.setUserId(userId);
           productCollect.setProductId(id);
           productCollectService.add(productCollect);
           return true;
       }
    return false;
       
       
    }
    
    /**
     * 
    * @Title: productPay
    * @Description: TODO(用在收藏页面的直接支付页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pId
    * @param session
    * @return String 返回类型
    * @throws
     */
//    @RequestMapping(value = "/user/productCollect/productPay/{pId}/{productQuantity}")
//    public String productPay(@PathVariable String pId,@PathVariable String productQuantity,HttpSession session) {
//        if(session == null) {
//            return "/user/login";
//        }
//        // 获取登陆用户的信息,完整的对象
//        User user = (User)session.getAttribute(SystemConst.USER);
//        String userId = user.getId();
//        if(!shopCartService.getByShopCartByProductId(pId,userId)){
//            ShopCart s = new ShopCart();
//            s.setChecked(false);
//            s.setUserId(userId);
//            s.setProductQuantity(Integer.parseInt(productQuantity));
//            s.setProductId(pId);
//            if(shopCartService.add(s)){
//                return "redirect:/user/cart/userShopCart";
//            }
//        }else{
//            return "redirect:/user/cart/userShopCart";
//        }
//        return "error";
//        
//    }
//    
//    @RequestMapping(value = "/user/shopCart/add/{id}/{productQuantity}")
//    @ResponseBody
//    public Object addShopCart(@PathVariable String id,@PathVariable String productQuantity,HttpSession session) {
//        //判断根据商品id能否查询出收藏的商品
//       ProductCollect pc = productCollectService.getProductCollectById(id);
//       if(session == null) {
//           return "/user/login";
//       }
//       // 获取登陆用户的信息,完整的对象
//       User user = (User)session.getAttribute(SystemConst.USER);
//       String userId = user.getId();
//       //得到商品的productId
//       String productId = pc.getProductId();
//       if(!shopCartService.getByShopCartByProductId(productId,userId)){
//           ShopCart s = new ShopCart();
//           s.setChecked(false);
//           s.setUserId(userId);
//           s.setProductQuantity(Integer.parseInt(productQuantity));
//           s.setProductId(productId);
//           if(shopCartService.add(s)){
//               return true;
//           }
//       }else{
//           ShopCart s = shopCartService.getShopCartByProductId(productId,userId);
//           s.setProductQuantity(s.getProductQuantity()+Integer.parseInt(productQuantity));
//           if(shopCartService.update(s)){
//               return true;
//           }
//       }
//    return false;
//    }
}
