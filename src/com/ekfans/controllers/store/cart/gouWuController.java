package com.ekfans.controllers.store.cart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.service.BargainService;
import com.ekfans.base.order.service.IBargainService;
import com.ekfans.base.order.service.IInquiryService;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.service.IOrderTreatDetailService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.base.product.service.IProductPriceService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.ISupplyProductService;
import com.ekfans.base.product.util.ProductConst;
import com.ekfans.base.product.util.ProductHelper;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.ShopCart;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.service.IShopCartService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.UserAddress;
import com.ekfans.base.user.service.IUserAddressService;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
public class gouWuController extends BasicController {
    // 定义Service
    @Autowired
    private IShopCartService shopCartService;

    @Autowired
    private IUserAddressService userAddressService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderTreatDetailService orderTreatDetailService;

    @Autowired
    private ISystemAreaService systemAreaService;
    
    @Autowired
    private IProductService productService;

    @Autowired
    private IProductPriceService productPriceService;

    @Autowired
    private IStoreService storeService;
    
    @Autowired
    private ISupplyProductService supplyProductService;
    
    @Autowired
    private IBargainService bargainService;
    /**
     * 
     * @Title: cartList
     * @Description: TODO(跳转到购物车页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/purchase/cart/userShopCart")
    public String cartList(HttpSession session, HttpServletRequest request) {
        return "/web/purchase/cart/userShopCart";
    }

    /**
     * 
     * @Title: cartList
     * @Description: TODO(跳转到购物车页面的load) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/purchase/cart/userShopCartload/{productId}/{quantity}/{pInfoId}")
    public String cartListLoad(@PathVariable
    String productId, @PathVariable
    String quantity, @PathVariable
    String pInfoId) {
        // 获取登陆用户的信息,完整的对象
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "/web/purchase/cart/userShopCartLoad";
        }
        List<ProductPrice> priceList = productPriceService.getProductPriceByProductId(productId);
        // 绑定其他商品到页面
        List<Product> products = productService.getDaZhongOtherPro(4, null, ProductConst.PRODUCT_TYPE_DAZONG);
        int quantityNum = Integer.parseInt(quantity);
        // 验证库存
        // int dataNum = productService.getProductListQuantity(productId);
        if (quantityNum <= 0) {
            quantityNum = 1;
            getRequest().setAttribute("errorMsg", "数量最小为1");
        }
        // else if (quantityNum > dataNum) {
        // quantityNum = dataNum;
        // request.setAttribute("errorMsg", "数量超出仓库剩余库存");
        // }
        Store store = storeService.getStore(product.getStoreId());
        ShopCart cart = new ShopCart();
        cart.setChecked(true);
        cart.setProductId(productId);
        cart.setStoreId(store.getId());
        cart.setProductQuantity(quantityNum);
        cart.setProductInfoDetailId(pInfoId);
        ProductPrice price = ProductHelper.getPriceByQuantity(priceList, cart.getProductQuantity());
        if (price != null) {
            cart.setTotalPrice(price.getPrice().multiply(new BigDecimal(cart.getProductQuantity())));
            cart.setUnitPrice(price.getPrice());
        } else {
            cart.setTotalPrice(product.getPfPrice().multiply(new BigDecimal(cart.getProductQuantity())));
            cart.setUnitPrice(product.getPfPrice());
        }
        cart.setListPrice(product.getListPrice());
        cart.setPrice(cart.getListPrice().subtract(cart.getUnitPrice()));
        cart.setTempTotalPrefePrice((product.getUnitPrice().multiply(new BigDecimal(cart.getProductQuantity())))
                .subtract(cart.getTotalPrice()));
        cart.setFare(product.getFare());
        // 绑定总价格和集合到页面上
        getRequest().setAttribute("products", products);
        getRequest().setAttribute("product", product);
        getRequest().setAttribute("cartStore", store);
        getRequest().setAttribute("priceList", priceList);
        getSession().removeAttribute("shopCart");
        getSession().setAttribute("shopCart", cart);
        getRequest().setAttribute("usedPrice", price);
        return "/web/purchase/cart/userShopCartLoad";
    }

    /**
     * 删除
     * 
     * @return
     */
    @RequestMapping(value = "/purchase/shopCart/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable
    String id) {
        // 利用Service的方法根据id删除商品s
        if (shopCartService.deleteShopCartById(id)) {
            // 删除成功，返回true
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证库存
     * 
     * @return
     */
    @RequestMapping(value = "/purchase/shopCart/checkQuantitySub/{productId}/{quantity}/{oldQuantity}")
    public String checkQuantitySub(@PathVariable
    String productId, @PathVariable
    String quantity, @PathVariable
    String oldQuantity,HttpSession session, HttpServletRequest request) {
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
        String userId = user.getId();
        // 获取登陆用户的信息,完整的对象
        Product product = productService.getProDteailById(productId);
        if (product == null) {
            return "/web/purchase/cart/userShopCartLoad";
        }

        int quantityNum = Integer.parseInt(quantity);
        // 验证库存
        int dataNum = product.getQuantity();
        if (quantityNum <= 0) {
            quantityNum = 1;
            getRequest().setAttribute("errorMsg", "数量最小为1");
        }
        else if (quantityNum > dataNum) {
            quantityNum = dataNum;
            request.setAttribute("errorMsg", "数量超出仓库剩余库存");
        }

        // 通过userID得到地址集合
        List<UserAddress> uas = userAddressService.listAddress(userId);
        // 得到默认地址
        UserAddress ua = userAddressService.getDefaultAddess(userId);
        List<SystemArea> systemAreaList = this.systemAreaService.getChildAreasByParentId(Cache.getResource("area.parent.code"));
        // 查询出所有的省
        List<SystemArea> systemAreas = userAddressService.getProvinceList();
        request.setAttribute("systemAreas", systemAreas);       
        // 绑定对象到页面上
        request.setAttribute("uas", uas);
        request.setAttribute("ua", ua);
        request.setAttribute("p", product);
        request.setAttribute("count", quantityNum);
        request.setAttribute("totalPrice", product.getUnitPrice().multiply(new BigDecimal(quantityNum)));
        request.setAttribute("systemAreaList", systemAreaList);
        return "/web/gouwu/loadCart02";
    }
    
    
    /**
     * 用户购买商品登录与身份验证
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/web/gouWu/userShopFiler")
    public@ResponseBody int userShopFiler(HttpSession session, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SystemConst.USER);
        if (user == null) {
            return 0;
        }
//        if(!UserConst.USER_TYPE_BUYER.equals(user.getType())){//判断是否为采购商类型
//        	return 2;
//        }
        return 1;
    }

    /**
     * 
     * @Title: toShopCartAccount
     * @Description: TODO() 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/web/gouWu/order/{productId}")
    public String toShopCartAccount(@PathVariable String productId,HttpSession session, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SystemConst.USER);
//        if (user == null || !UserConst.USER_TYPE_BUYER.equals(user.getType())) {
//            return "redirect:/web/login";
//        }
        request.setAttribute("productId", productId);
        return "/web/gouwu/cart02";
    }
    
    
    /**
     * 
     * @Title: toShopCartAccount
     * @Description: TODO(跳转到确认结算页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/web/gouWu/loadOrder/{productId}/{quantity}")
    public String toShopSupplyCartAccountLoad(@PathVariable String productId,@PathVariable String quantity,HttpSession session, HttpServletRequest request) {
    	String a="^[0-9]*$";
    	 Pattern pp = Pattern.compile(a); 
         Matcher m = pp.matcher(quantity); 
         boolean flg = m.matches(); 
    	if(quantity==" "||!flg){
    		quantity="1";	
    	}
    	int quantityNum = Integer.parseInt(quantity);
    	Product p = productService.getProDteailById(productId);
        //返回商品到页面
        request.setAttribute("p", p);
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
        String userId = user.getId();
        // 通过userID得到地址集合
        List<UserAddress> uas = userAddressService.listAddress(userId);
        // 得到默认地址
        UserAddress ua = userAddressService.getDefaultAddess(userId);
        List<SystemArea> systemAreaList = this.systemAreaService.getChildAreasByParentId(Cache.getResource("area.parent.code"));
        // 查询出所有的省
        List<SystemArea> systemAreas = userAddressService.getProvinceList();
        request.setAttribute("systemAreas", systemAreas);       
        // 绑定对象到页面上
        request.setAttribute("uas", uas);
        request.setAttribute("ua", ua);
        request.setAttribute("count",quantity);
        request.setAttribute("totalPrice", p.getUnitPrice().multiply(new BigDecimal(quantityNum)));
        request.setAttribute("systemAreaList", systemAreaList);
        return "/web/gouwu/loadCart02";
    }
    
    @RequestMapping(value = "/web/gouWu/loadBargainOrder/{bargainId}")
    public String loadBargainOrder(@PathVariable String bargainId,HttpSession session,HttpServletRequest request){
    	
    	Bargain b = bargainService.getDetailById(bargainId);
    	String sourceId = b.getSourceId();
    	BigDecimal price = b.getFinalPrice();
    	BigDecimal quantity = b.getFinalQuantity();
    	BigDecimal totalPrice = price.multiply(quantity);
    	Product p = productService.getProDteailById(sourceId);
        //返回议价到页面
        request.setAttribute("b", b);
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
        String userId = user.getId();
        // 通过userID得到地址集合
        List<UserAddress> uas = userAddressService.listAddress(userId);
        // 得到默认地址
        UserAddress ua = userAddressService.getDefaultAddess(userId);
        List<SystemArea> systemAreaList = this.systemAreaService.getChildAreasByParentId(Cache.getResource("area.parent.code"));
        // 查询出所有的省
        List<SystemArea> systemAreas = userAddressService.getProvinceList();
        request.setAttribute("systemAreas", systemAreas);       
        // 绑定对象到页面上
        request.setAttribute("totalPrice", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        request.setAttribute("p", p);
        request.setAttribute("uas", uas);
        request.setAttribute("ua", ua);
        request.setAttribute("count", "1");
        request.setAttribute("systemAreaList", systemAreaList);
    	
    	return "/web/gouwu/loadBargainCart";
    }
    
    /**
     * 修改
     * 
     * @return
     */
    @RequestMapping(value = "/purchase/shopCart/save/{id}/{cartId}")
    @ResponseBody
    public Object updateShopCart(@PathVariable
    String id, @PathVariable
    String cartId, HttpSession session) {
        ShopCart sc = shopCartService.getByShopCartById(id);
        sc.setProductQuantity(Integer.parseInt(cartId));
        sc.setChecked(true);
        // 利用Service的方法根据id删除商品s
        if (shopCartService.update(sc)) {
            // 删除成功，返回true
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修改数量
     * 
     * @return
     */
    @RequestMapping(value = "/purchase/shopCart/checkUpdateQuantity/{id}/{count}/{cartId}")
    @ResponseBody
    public Object updateShopCartQuantity(@PathVariable
    String id, @PathVariable
    String count, @PathVariable
    String cartId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SystemConst.USER);
        int productNumber = shopCartService.getProductNumber(cartId, user.getId());
        try {
            int tempCount = Integer.valueOf(count);
            if ((productNumber - tempCount) >= 0) {
                ShopCart sc = shopCartService.getByShopCartById(cartId);
                sc.setProductQuantity(Integer.parseInt(count));
                shopCartService.update(sc);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
        /*
         * Product product = productService.getProductById(id); int
         * productQuantity = product.getQuantity() - product.getBuyCount();
         * if(productQuantity - Integer.parseInt(count) > 0){ ShopCart sc =
         * shopCartService.getByShopCartById(cartId);
         * sc.setProductQuantity(Integer.parseInt(count));
         * shopCartService.update(sc); return true; }else{ return false; }
         */
    }

    /**
     * 用于结算时把没有选中的商品状态做改变
     * 
     * @return
     */
    @RequestMapping(value = "/purchase/shopCart/updateChecked/{id}/{cartId}")
    @ResponseBody
    public Object updateShopCartChecked(@PathVariable
    String id, @PathVariable
    String cartId, HttpSession session) {
        ShopCart sc = shopCartService.getByShopCartById(id);
        sc.setProductQuantity(Integer.parseInt(cartId));
        sc.setChecked(false);
        // 利用Service的方法根据id删除商品s
        if (shopCartService.update(sc)) {
            // 删除成功，返回true
            return true;
        } else {
            return false;
        }
    }

    
    /**
     * 议价下单跳支付页面
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/purchase/bargainCart/pay")
    public String bargainCartPay(HttpSession session,HttpServletRequest request){
    	// 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
//        if (user == null || !(UserConst.USER_TYPE_BUYER.equals(user.getType())||UserConst.USER_TYPE_CODE.equals(user.getType()))) {
//            return "redirect:/web/login/two";
//        }
        // 调用方法生成订单
        boolean status = shopCartService.commitBargainCartToOrder(request);
        String totalOrderId = (String) request.getAttribute("totalOrderId");
        BigDecimal sum = (BigDecimal) request.getAttribute("sum");
        int orderType = (Integer)request.getAttribute("orderType");
        // 绑定对象到页面上
        if (status) {
            return "redirect:/purchase/shopCart/comPay?totalOrderId="+totalOrderId+"&sum="+sum+"&orderType="+orderType;
        } else {
            return "error";
        }
    }
    
    /**
     * 保存订单并跳转到订单成功 - 支付 页面
     * 
     * @Title: shopCartPay
     * @Description: TODO(跳转到支付页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/purchase/shopCart/pay")
    public String shopCartPay(HttpSession session, HttpServletRequest request) {
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
//        if (user == null || !(UserConst.USER_TYPE_BUYER.equals(user.getType())||UserConst.USER_TYPE_CODE.equals(user.getType()))) {
//            return "redirect:/web/login/two";
//        }
        // 调用方法生成订单
        boolean status = shopCartService.commitPurchaseCartToOrder(request);
        String totalOrderId = (String) request.getAttribute("totalOrderId");
        BigDecimal sum = (BigDecimal) request.getAttribute("sum");
        int orderType = (Integer)request.getAttribute("orderType");
        // 绑定对象到页面上
        if (status) {
            return "redirect:/purchase/shopCart/comPay?totalOrderId="+totalOrderId+"&sum="+sum+"&orderType="+orderType;
        } else {
            return "error";
        }
    }
    
    @RequestMapping(value = "/purchase/shopCart/comPay")
    public String purchaseShopCartPay(HttpServletRequest request) {
        String PayId = request.getParameter("totalOrderId");
        String sum = request.getParameter("sum");
        String orderType = request.getParameter("orderType");
        // 绑定其他商品到页面
        request.setAttribute("totalOrderId", PayId);
        request.setAttribute("sum", sum);
        request.setAttribute("orderType", Integer.valueOf(orderType));
        return "/web/gouwu/shopCartPay";
    }
    /**
     * 保存订单并跳转到订单成功 - 支付 页面
     * 
     * @Title: shopCartPay
     * @Description: TODO(跳转到支付页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/hexin/shopCart/pay")
    public String hxShopCartPay(HttpSession session, HttpServletRequest request) {
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
//        if (user == null || !UserConst.USER_TYPE_CODE.equals(user.getType())) {
//            return "redirect:/web/login";
//        }
        // 调用方法生成订单
        boolean status = shopCartService.commitPurchaseCartToOrder(request);
        int orderType = (Integer)request.getAttribute("orderType");
        String totalOrderId = (String) request.getAttribute("totalOrderId");
        BigDecimal sum = (BigDecimal) request.getAttribute("sum");
        // 绑定对象到页面上
        if (status) {
            return "redirect:/purchase/shopCart/comPay?totalOrderId="+totalOrderId+"&sum="+sum+"&orderType="+orderType;
        } else {
            return "error";
        }
    }

    /**
     * 支付成功
     */
    @RequestMapping(value = "/purchase/shopCart/paySuccess/{sum}/{payId}")
    public String shopCartPaySuccess(@PathVariable
    String sum, @PathVariable
    String payId, HttpSession session, HttpServletRequest request) {
        // if(session == null) {
        // return "/web/login";
        // }
        // // 获取登陆用户的信息,完整的对象
        // User user = (User)session.getAttribute(SystemConst.USER);
        // String userId = user.getId();
        // //通过userID得到团购车列表
        // List<ShopCart> scs = shopCartService.getShopCartByUserId(userId);
        // double sum = 0.00;
        // for(ShopCart sc:scs){
        // if(sc.isChecked() == true){
        // double totalPrice = sc.getTotalPrice();
        // sum = sum + totalPrice;
        // }
        // }
        // 绑定对象到页面上
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
        if (user == null) {
            return "redirect:/web/login";
        }
        String userId = user.getId();
        List<Order> orders = orderService.orderListByPayId(payId, userId);
        String paymentId = request.getParameter("paymentId");
        for (Order order : orders) {
            OrderTreatDetail otd = new OrderTreatDetail();
            otd.setOrderId(order.getId());
            otd.setType(OrderConst.ORDER_TREAT_PAY);
            otd.setCreateTime(DateUtil.getSysDateTimeString());
            otd.setCreater(user.getName());
            otd.setNote("付款,金额【" + sum + "】");
            orderTreatDetailService.add(otd);

            order.setStatus(OrderConst.ORDER_STATUS_WAIT_SEND);
            order.setPaymentId(paymentId);
            orderService.modifyOrder(order);
        }
        // 绑定其他商品到页面
        List<Product> products = productService.getDaZhongOtherPro(4, null, ProductConst.PRODUCT_TYPE_DAZONG);
        // 绑定总价格和集合到页面上
        request.setAttribute("products", products);
        request.setAttribute("sum", sum);
        request.setAttribute("orderId", payId);
        request.setAttribute("cartProductCount", shopCartService.getShopCartSum(userId));
        return "/web/purchase/cart/shopCartPaySuccess";
    }

    /**
     * 
     * @Title: cartList
     * @Description: TODO(采购商下单跳转) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/purchase/inquiry/cart/{inquiryId}")
    public String carSupplytListLoad(@PathVariable  String inquiryId) {
        // 获取登陆用户的信息,完整的对象
        IInquiryService inquiryService = SpringContextHolder.getBean(IInquiryService.class);
        Inquiry inquiry = inquiryService.getById(inquiryId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (inquiry == null || !"1".equals(inquiry.getStatus()) || (!StringUtil.isEmpty(inquiry.getEndTime()) && (compareTimeString(format.format(new Date()),inquiry.getEndTime())))) {
            return ".....";
        }

        String productId = inquiry.getProductId();
        List<ProductPrice> priceList = productPriceService.getProductPriceByProductId(productId);
        // 绑定其他商品到页面
        List<Product> products = productService.getProductSalesRanking(4, ProductConst.PRODUCT_TYPE_DAZONG);
        ProductPrice price = ProductHelper.getPriceByQuantity(priceList, inquiry.getSellersNumber());
        Product product = productService.getProductById(productId);
        int quantityNum = inquiry.getSellersNumber();
        // 验证库存
        // int dataNum = productService.getProductListQuantity(productId);
        if (quantityNum <= 0) {
            quantityNum = 1;
            getRequest().setAttribute("errorMsg", "数量最小为1");
        }
        // else if (quantityNum > dataNum) {
        // quantityNum = dataNum;
        // request.setAttribute("errorMsg", "数量超出仓库剩余库存");
        // }
        Store store = storeService.getStore(inquiry.getSellersId());
        ShopCart cart = new ShopCart();
        cart.setChecked(true);
        cart.setInquiryId(inquiryId);
        cart.setProductId(productId);
        cart.setStoreId(store.getId());
        cart.setProductQuantity(quantityNum);
        cart.setTotalPrice(inquiry.getFinalPrice().multiply(new BigDecimal(cart.getProductQuantity())));
        cart.setUnitPrice(inquiry.getFinalPrice());
        cart.setListPrice(product.getPfPrice());
        cart.setPrice(cart.getListPrice().subtract(cart.getUnitPrice()));
        if(price != null){
            cart.setTempTotalPrefePrice((price.getPrice().multiply(new BigDecimal(cart.getProductQuantity()))).subtract(cart.getTotalPrice()));  
        }else{
            cart.setTempTotalPrefePrice((product.getPfPrice().multiply(new BigDecimal(cart.getProductQuantity()))).subtract(cart.getTotalPrice()));
        }
        // 绑定总价格和集合到页面上
        getRequest().setAttribute("products", products);
        getRequest().setAttribute("product", product);
        getRequest().setAttribute("cartStore", store);
        getRequest().setAttribute("priceList", priceList);
        getSession().removeAttribute("shopCart");
        getSession().setAttribute("shopCart", cart);
        getRequest().setAttribute("usedPrice", price);
        User user = (User) getSession().getAttribute(SystemConst.USER);
        getRequest().setAttribute("cartProductCountNumber", shopCartService.getShopCartSum(user.getId()));
        getSession().setAttribute("shopCartId", getRequest().getParameter("shopCartId"));
        return "/web/purchase/cart/shopCartAccount";
    }
    
 
    /**
     * 根据议价单下订单
     * @param bargainId
     * @return
     */
    @RequestMapping(value = "/purchase/bargain/cart/{bargainId}")
    public String bargainListLoad(@PathVariable String bargainId){
    	IBargainService bargainService = SpringContextHolder.getBean(IBargainService.class);
    	Bargain bargain = bargainService.getDetailById(bargainId);
    	// 获取商品或者货源id
    	String sourceId = bargain.getSourceId();
    	// 根据sourceId查找商品信息
    	Product product = productService.getProductById(sourceId);
    	// 获取商家信息
    	Store store = storeService.getStore(bargain.getSellerId());
        
        getRequest().setAttribute("bargainId", bargainId);
        
    	
    	
    	return "/web/gouwu/bargainCart";
    }
    
    /**
     * 
     * @Title: cartList
     * @Description: TODO(核心企业对供应商下单跳转) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/purchase/inquirySupply/cart/{inquiryId}")
    public String cartListLoad(@PathVariable String inquiryId) {
        // 获取登陆用户的信息,完整的对象
        IInquiryService inquiryService = SpringContextHolder.getBean(IInquiryService.class);
        Inquiry inquiry = inquiryService.getById(inquiryId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (inquiry == null || !"1".equals(inquiry.getStatus()) || (!StringUtil.isEmpty(inquiry.getEndTime()) && (compareTimeString(format.format(new Date()),inquiry.getEndTime())))) {
            return ".....";
        }
        //根据ID查询供应商发布的商品
        String productId = inquiry.getProductId();
        //根据商品ID查询到商品
        SupplyProduct supplyProduct = supplyProductService.getSupplyProductById(productId);
        //
        
//        List<ProductPrice> priceList = productPriceService.getProductPriceByProductId(productId);
//        // 绑定其他商品到页面
//        List<Product> products = productService.getProductSalesRanking(4, ProductConst.PRODUCT_TYPE_DAZONG);
//        ProductPrice price = ProductHelper.getPriceByQuantity(priceList, inquiry.getSellersNumber());
//        Product product = productService.getProductById(productId);
        int quantityNum = inquiry.getSellersNumber();
        // 验证库存
        // int dataNum = productService.getProductListQuantity(productId);
        if (quantityNum <= 0) {
            quantityNum = 1;
            getRequest().setAttribute("errorMsg", "数量最小为1");
        }
        // else if (quantityNum > dataNum) {
        // quantityNum = dataNum;
        // request.setAttribute("errorMsg", "数量超出仓库剩余库存");
        // }
        Store store = storeService.getStore(inquiry.getSellersId());
        ShopCart cart = new ShopCart();
        cart.setChecked(true);
        cart.setInquiryId(inquiryId);
        cart.setProductId(productId);
        cart.setStoreId(store.getId());
        cart.setProductQuantity(quantityNum);
        cart.setTotalPrice(inquiry.getFinalPrice().multiply(new BigDecimal(cart.getProductQuantity())));
        cart.setUnitPrice(inquiry.getFinalPrice());
        cart.setListPrice(supplyProduct.getPfPrice());
        cart.setPrice(cart.getListPrice().subtract(cart.getUnitPrice()));
//        if(price != null){
//            cart.setTempTotalPrefePrice((price.getPrice().multiply(new BigDecimal(cart.getProductQuantity())))
//                    .subtract(cart.getTotalPrice()));  
//        }else{
//        cart.setTempTotalPrefePrice((product.getPfPrice().multiply(new BigDecimal(cart.getProductQuantity()))).subtract(cart.getTotalPrice()));
//        }
        // 绑定总价格和集合到页面上
     //   request.setAttribute("products", supplyProduct);
        getRequest().setAttribute("product", supplyProduct);
        getRequest().setAttribute("cartStore", store);
        getRequest().setAttribute("priceList", supplyProduct.getPfPrice());
        getSession().removeAttribute("shopCart");
        getSession().setAttribute("shopCart", cart);
        getRequest().setAttribute("usedPrice", supplyProduct.getPfPrice());
        User user = (User) getSession().getAttribute(SystemConst.USER);
        getRequest().setAttribute("cartProductCountNumber", shopCartService.getShopCartSum(user.getId()));
        getSession().setAttribute("shopCartId", getRequest().getParameter("shopCartId"));
        return "/web/purchase/cart/shopSupplyCartAccount";
    }
    
    public static Boolean compareTimeString(String str1, String str2) {
        java.util.Date one;
        java.util.Date two;

        SimpleDateFormat sdfDateTime = new SimpleDateFormat(DateUtil.DATE_FORMAT_DATETIME);
        try {
            one = sdfDateTime.parse(str1);
            two = sdfDateTime.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            if (time1 < time2) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     
     *  订购流程
     */
    @RequestMapping(value = "/web/dingGou/order/{productId}")
    public String toDingGouAccount(@PathVariable String productId,HttpSession session, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SystemConst.USER);
//        if (user == null || !UserConst.USER_TYPE_BUYER.equals(user.getType())) {
//            return "redirect:/web/login";
//        }
        request.setAttribute("productId", productId);
        return "/web/dingGou/cart02";
    }
 
    
    /**
     * 
     * @Title: toShopCartAccount
     * @Description: TODO(跳转到确认结算页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/web/dingGou/loadOrder/{productId}")
    public String toDingGouAccountLoad(@PathVariable String productId,HttpSession session, HttpServletRequest request) {
        Product p = productService.getProDteailById(productId);
        //返回商品到页面
        request.setAttribute("p", p);
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
        String userId = user.getId();
        // 通过userID得到地址集合
        List<UserAddress> uas = userAddressService.listAddress(userId);
        // 得到默认地址
        UserAddress ua = userAddressService.getDefaultAddess(userId);
        List<SystemArea> systemAreaList = this.systemAreaService.getChildAreasByParentId(Cache.getResource("area.parent.code"));
        // 查询出所有的省
        List<SystemArea> systemAreas = userAddressService.getProvinceList();
        request.setAttribute("systemAreas", systemAreas);       
        // 绑定对象到页面上
        request.setAttribute("uas", uas);
        request.setAttribute("ua", ua);
        request.setAttribute("count", p.getAdvanceNum());
        request.setAttribute("totalPrice", p.getAdvancePrice());
        request.setAttribute("systemAreaList", systemAreaList);
        return "/web/dingGou/loadCart02";
    }
    
    /**
     * 验证库存
     * 
     * @return
     */
    @RequestMapping(value = "/dingGou/shopCart/checkQuantitySub/{productId}/{quantity}/{oldQuantity}")
    public String checkDingGouQuantitySub(@PathVariable
    String productId, @PathVariable
    String quantity, @PathVariable
    String oldQuantity,HttpSession session, HttpServletRequest request) {
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
        String userId = user.getId();
        // 获取登陆用户的信息,完整的对象
        Product product = productService.getProDteailById(productId);
        if (product == null) {
            return "/web/purchase/cart/userShopCartLoad";
        }

        int quantityNum = Integer.parseInt(quantity);
        // 验证库存
        int dataNum = product.getQuantity();
        if (quantityNum <= 0) {
            quantityNum = 1;
            getRequest().setAttribute("errorMsg", "数量最小为1");
        }
        else if (quantityNum > dataNum) {
            quantityNum = dataNum;
            request.setAttribute("errorMsg", "数量超出仓库剩余库存");
        }

        // 通过userID得到地址集合
        List<UserAddress> uas = userAddressService.listAddress(userId);
        // 得到默认地址
        UserAddress ua = userAddressService.getDefaultAddess(userId);
        List<SystemArea> systemAreaList = this.systemAreaService.getChildAreasByParentId(Cache.getResource("area.parent.code"));
        // 查询出所有的省
        List<SystemArea> systemAreas = userAddressService.getProvinceList();
        request.setAttribute("systemAreas", systemAreas);       
        // 绑定对象到页面上
        request.setAttribute("uas", uas);
        request.setAttribute("ua", ua);
        request.setAttribute("p", product);
        request.setAttribute("count", quantityNum);
        request.setAttribute("totalPrice", product.getAdvancePrice().multiply(new BigDecimal(quantityNum)));
        request.setAttribute("systemAreaList", systemAreaList);
        return "/web/dingGou/loadCart02";
    }
    
    /**
     * 保存订单并跳转到订单成功 - 支付 页面
     * 
     * @Title: shopCartPay
     * @Description: TODO(跳转到支付页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param session
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/dingGou/shopCart/pay")
    public String dingGouShopCartPay(HttpSession session, HttpServletRequest request) {
        // 获取登陆用户的信息,完整的对象
        User user = (User) session.getAttribute(SystemConst.USER);
//        if (user == null || !UserConst.USER_TYPE_BUYER.equals(user.getType())) {
//            return "redirect:/web/login";
//        }
        // 调用方法生成订单
        boolean status = shopCartService.commitDingGouCartToOrder(request);
        String totalOrderId = (String) request.getAttribute("totalOrderId");
        BigDecimal sum = (BigDecimal) request.getAttribute("sum");
        int orderType = (Integer)request.getAttribute("orderType");
        // 绑定对象到页面上
        if (status) {
            return "redirect:/dingGou/shopCart/comPay?totalOrderId="+totalOrderId+"&sum="+sum+"&orderType="+orderType;
        } else {
            return "error";
        }
    }
    
    @RequestMapping(value = "/dingGou/shopCart/comPay")
    public String dingGouShopCartPay(HttpServletRequest request) {
        String PayId = request.getParameter("totalOrderId");
        String sum = request.getParameter("sum");
        String orderType = request.getParameter("orderType");
        // 绑定其他商品到页面
        request.setAttribute("totalOrderId", PayId);
        request.setAttribute("sum", sum);
        request.setAttribute("orderType", Integer.valueOf(orderType));
        return "/web/dingGou/shopCartPay";
    }
}
