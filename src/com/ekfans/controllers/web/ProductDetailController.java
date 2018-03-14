package com.ekfans.controllers.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Appraise;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.base.product.service.IProductBrandService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductCollectService;
import com.ekfans.base.product.service.IProductInfoDetailService;
import com.ekfans.base.product.service.IProductPictureService;
import com.ekfans.base.product.service.IProductPriceService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.web.ProductConsult.IProductConsultService;
import com.ekfans.base.product.service.web.ProductDetail.IProductDetailService;
import com.ekfans.base.product.util.ProductConst;
import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.model.ShopCart;
import com.ekfans.base.system.service.IShopCartService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品分类Controller
 * 
 * @ClassName: ProductDetailController
 * @Description:
 * @author lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class ProductDetailController extends BasicController {

	// private Logger log =
	// LoggerFactory.getLogger(ProductDetailController.class);
	@Autowired
	private IProductDetailService productDetailService;
	@Autowired
	private IShopCartService shopCartService;
	@Autowired
	private IProductConsultService productConsultService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProductCollectService productCollectService;
	@Autowired
	private IProductBrandService productBrandService;
	@Autowired
	private IProductPriceService priceService;
	@Autowired
    private IProductInfoDetailService productInfoDetailService;
	@Autowired
	private IProductPictureService productPictureService;
	/**
	 * 跳转到商品详细页
	 * 
	 * @param proId
	 *            商品id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/proDetail/{proId}")
	public String goToProductDetail(@PathVariable String proId, HttpServletRequest request) {
		List<ProductPicture> pictures = productPictureService.getList(proId);
		Product p = productService.getProductById(proId);
		p.setVisitCount(p.getVisitCount()+1);
		productService.modifyProduct(p);
		request.setAttribute("p", p);
		// 多角度视图
		request.setAttribute("pictures", pictures);
	    return "/web/product/productDetail";
		
	}

	/**
	 * 
	 * @Title: getProSumByCondition
	 * @Description: TODO(根据条件查询库存、价格、以及infoDetailId) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/proDetail/proSum")
	@ResponseBody
	public Object getProSumByCondition(HttpServletRequest request) {
		// 获取查询库存所需条件
		String proId = request.getParameter("proId");
		String info1NameAndValue = request.getParameter("info1NameAndValue");
		String info2NameAndValue = request.getParameter("info2NameAndValue");
		String info3NameAndValue = request.getParameter("info3NameAndValue");
		String info4NameAndValue = request.getParameter("info4NameAndValue");
		// 这些条件其中一个不满足,直接返回null
		if (StringUtil.isEmpty(proId)) {
			return null;
		}
		ProductInfoDetail pid = productDetailService.getProInfoDetailByCondition(proId, info1NameAndValue, info2NameAndValue, info3NameAndValue, info4NameAndValue);

		// 把对象转为JSON字符串
		String str = JsonUtil.objectToJson(pid);
		if (null == pid) {
			return "false";
		}
		return str;
	}

	/**
	 * 
	 * @Title: loadAppraise
	 * @Description: TODO(根据分页查询去当前商品的评价) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/proDetail/appraise")
	public String loadAppraise(HttpServletRequest request) {
		// 获得proId
		String proId = request.getParameter("proId");
		// 获得评价类型
		String apType = request.getParameter("evaluate");
		// 定义分页
		Pager pager = new Pager();
		// 获得当前页数
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null || pageNum == "") {
			pageNum = 1 + "";
		}
		pager.setCurrentPage(Integer.parseInt(pageNum));
		// pager.setRowsPerPage(1);

		List<Appraise> appraises = productDetailService.getAppraiseByProId(proId, apType, pager);
		if (appraises != null) {
			for (Appraise a : appraises) {
				User user = userService.getUser(a.getSource());
				a.setHeadPhoto(user.getHeadPortrait());
				a.setSourceName(user.getNickName());
			}
		}
		request.setAttribute("proId", proId);
		request.setAttribute("apType", apType);
		request.setAttribute("pager", pager);
		request.setAttribute("appraises", appraises);
		request.setAttribute("pageNum", pageNum);
		return "/web/shop/product/proDetailAppraise";
	}

	/**
	 * 
	 * @Title: loadDealRecord
	 * @Description: TODO(根据分页查询出当前商品的月成交记录) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/proDetail/dealRecord")
	public String loadDealRecord(HttpServletRequest request) {
		// 获得proId
		String proId = request.getParameter("proId");
		// 定义分页
		Pager pager = new Pager();
		// 获得当前页数
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null || pageNum == "") {
			pageNum = 1 + "";
		}
		pager.setCurrentPage(Integer.parseInt(pageNum));
		// pager.setRowsPerPage(1);

		List<OrderDetail> orderDetails = productDetailService.getProductDealRecord(proId, pager);

		request.setAttribute("proId", proId);
		request.setAttribute("pager", pager);
		request.setAttribute("orderDetails", orderDetails);
		request.setAttribute("pageNum", pageNum);
		return "/web/shop/product/proDetailDealRecord";
	}

	/**
	 * 
	 * @Title: addShoppingCart
	 * @Description: TODO(加入购物车--提示加入购物车成功) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param proId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/proDetail/addshopCart/{pId}/{productQuantity}/{pInfoId}")
	@ResponseBody
	public Object addShopCart(@PathVariable String pId, @PathVariable String productQuantity, @PathVariable String pInfoId, HttpSession session) {
		// 获取登陆用户的信息,完整的对象
		User user = (User) session.getAttribute(SystemConst.USER);

		// 判断根据商品id能否查询出收藏的商品
		if (user == null) {
			// return "redirect:/user/login";
			return false;
		}

		String userId = user.getId();
		// 得到商品的productId
		if (!shopCartService.getByShopCartByProductId(pId, userId, pInfoId)) {
			ShopCart s = new ShopCart();
			s.setChecked(false);
			s.setUserId(userId);
			s.setProductQuantity(Integer.parseInt(productQuantity));
			s.setProductId(pId);
			s.setProductInfoDetailId(pInfoId);
			if (shopCartService.add(s)) {
				return true;
			}
		} else {
			ShopCart s = shopCartService.getShopCartByProductId(pId, userId, pInfoId);
			s.setProductQuantity(s.getProductQuantity() + Integer.parseInt(productQuantity));
			if (shopCartService.update(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: buyNow
	 * @Description: TODO(立即购买该商品) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/proDetail/buyNow/{pId}/{productQuantity}/{pInfoId}")
	public String productPay(@PathVariable String pId, @PathVariable String productQuantity, @PathVariable String pInfoId, HttpServletRequest request) {
		// 获取登陆用户的信息,完整的对象
		User user = (User) request.getSession().getAttribute(SystemConst.USER);

		if (user == null) {
			// return "redirect:/user/login";
			System.out.println(request.getParameter("jumpAddressUrl"));
			return "redirect:/web/login/zero?jumpAddressUrl=" + request.getParameter("jumpAddressUrl");
		}
		Product product = productService.getProductById(pId);
		String userId = user.getId();
		if (!shopCartService.getByShopCartByProductId(pId, userId, pInfoId)) {
			ShopCart s = new ShopCart();
			s.setChecked(false);
			s.setUserId(userId);
			s.setProductQuantity(Integer.parseInt(productQuantity));
			s.setProductId(pId);
			s.setProductInfoDetailId(pInfoId);
			if (shopCartService.add(s)) {
				return "redirect:/user/cart/userShopCart";
			}
		} else {
		    ShopCart s = shopCartService.getShopCartByProductId(pId, userId, pInfoId);
		    if(ProductConst.PRODUCT_TYPE_XIAOZONG.equals(product.getType())){
		        int newQuantity = s.getProductQuantity() + Integer.parseInt(productQuantity);
		        if(!StringUtil.isEmpty(pInfoId)){
		            ProductInfoDetail pid = productInfoDetailService.getProductInfoDetailById(pInfoId);
		            if(pid!=null){
		                if(pid.getQuantity() < newQuantity){
		                    s.setProductQuantity(pid.getQuantity());
		                }else{
		                    s.setProductQuantity(newQuantity);
		                }
		            }else{
		                s.setProductQuantity(newQuantity);
		            }
		        }else{
		            if(product.getQuantity() < newQuantity){
		                s.setProductQuantity(product.getQuantity());
		            }else{
		                s.setProductQuantity(newQuantity);
		            }
		        }
		    }else{
    			s.setProductQuantity(s.getProductQuantity() + Integer.parseInt(productQuantity));
		    }
			if (shopCartService.update(s)) {
				return "redirect:/user/cart/userShopCart";
			}
		}
		return "error";
	}

	/**
	 * 
	 * @Title: buyNow
	 * @Description: TODO(立即购买该商品) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/purchase/proDetail/{pId}/{productQuantity}/{pInfoId}")
	public String purchaseAddOrder(@PathVariable String pId, @PathVariable String productQuantity, @PathVariable String pInfoId, HttpServletRequest request) {
		// 获取登陆用户的信息,完整的对象
		User user = (User) request.getSession().getAttribute(SystemConst.USER);

		if (user == null || !UserConst.USER_TYPE_BUYER.equals(user.getType())) {
			// return "redirect:/user/login";
			System.out.println(request.getParameter("jumpAddressUrl"));
			return "redirect:/web/login/two?jumpAddressUrl=" + request.getParameter("jumpAddressUrl");
		}

		request.setAttribute("productId", pId);
		request.setAttribute("productQuantity", productQuantity);
		request.setAttribute("pInfoId", pInfoId);
		return "/web/purchase/cart/userShopCart";
	}

}
