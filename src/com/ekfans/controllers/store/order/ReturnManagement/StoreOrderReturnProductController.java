package com.ekfans.controllers.store.order.ReturnManagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.model.Refund;
import com.ekfans.base.order.service.StoreOrder.ReturnManagement.IRefundOrderService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: StoreOrderListController
 * @Description: TODO(跳转到退换货处理页面)
 * @author 成都易科远见科技有限公司
 * @date 2014-4-14 下午02:53:40
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreOrderReturnProductController extends BasicController {
	// 注入Service
	@Autowired
	private IRefundOrderService refundOrderService;

	@RequestMapping(value = "/store/order/returnProduct")
	public String list(HttpServletRequest request, HttpSession session) {
		// 得到orderId
		String orderId = request.getParameter("orderId");
		// 得到refundId
		String refundId = request.getParameter("refundId");

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

		// List<RefundVO> refundVOs =
		// refundOrderService.getRefundOrder(orderId,refundId,pager,null,null,null,null);

		// request.setAttribute("refundVOs",refundVOs);
		request.setAttribute("orderId", orderId);
		request.setAttribute("refundId", refundId);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pager", pager);

		return "/userCenter/store/order/ReturnManagement/orderReturnProduct";
	}

	/**
	 * 
	 * @Title: list2
	 * @Description: TODO(方便首页的退换货记录的统计) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/returnProduct/{refundStatus}")
	public String list2(@PathVariable String refundStatus, HttpServletRequest request, HttpSession session) {
		// 得到orderId
		String orderId = request.getParameter("orderId");
		// 得到refundId
		String refundId = request.getParameter("refundId");

		Store store = (Store) session.getAttribute(SystemConst.STORE);
		// 调用缓存 获取店铺会员对应的权限集合 这里获取的是交易管理页面的权限集合
		String purviewId = "ORDER";
		StorePurview purview = Cache.getStorePurview(store.getRoleId(), purviewId, true);

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

		// List<RefundVO> refundVOs =
		// refundOrderService.getRefundOrder(orderId,refundId,pager,null,null,null,refundStatus);

		// request.setAttribute("refundVOs",refundVOs);
		request.setAttribute("orderId", orderId);
		request.setAttribute("refundId", refundId);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pager", pager);

		// 把权限对应的权限放入session
		session.setAttribute("storePurview", purview);
		session.setAttribute("parentChose", purviewId); // 商户中心顶部的默认选中
		session.setAttribute("storeChose", "ORDER_RETURN"); // 商户中心左侧菜单的默认选中

		return "store/order/ReturnManagement/orderReturnProduct";
	}

	/**
	 * 
	 * @Title: returnProductDetail
	 * @Description: TODO(查看退换货记录的详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param refundId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/returnProductDetail/{refundId}")
	public String returnProductDetail(@PathVariable String refundId, HttpServletRequest request) {
		Refund refund = refundOrderService.getRefundById(refundId);
		// 得到orderId
		String orderId = refund.getOrderId();
		// 得到productId
		String productId = refund.getProductId();

		Object[] objects = refundOrderService.getOrderDetailByOrderId(orderId);

		// 地址详情
		OrderAddress address = (OrderAddress) objects[0];

		// 订单详情
		Order order = (Order) objects[1];

		// 订单日志详情(主要是取出付款时间)
		OrderTreatDetail treatDetail = (OrderTreatDetail) objects[2];

		// 得到商品详情
		Product product = refundOrderService.getProdyctById(productId);

		request.setAttribute("refund", refund);
		request.setAttribute("address", address);
		request.setAttribute("order", order);
		request.setAttribute("product", product);
		request.setAttribute("treatDetail", treatDetail);
		request.setAttribute("refundId", refundId);
		return "/store/order/ReturnManagement/ReturnHandle/orderReturnProductHandle";
	}

	/**
	 * 
	 * @Title: refundHandle
	 * @Description: TODO(退换货处理-处理单条请求) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param refundId
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/refundHandle/{refundId}/{sellerStatus}")
	@ResponseBody
	public Object refundHandle(@PathVariable String refundId, @PathVariable String sellerStatus, HttpServletRequest request) {
		String reason = request.getParameter("reason");
		boolean handleOk = refundOrderService.refundOrExChengeProduct(refundId, sellerStatus, reason);
		return handleOk;
	}

	/**
	 * 
	 * @Title: agreeRefunds
	 * @Description: TODO(批量同意退货/换货) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/aggreeRefunds")
	@ResponseBody
	public Object agreeRefunds(HttpServletRequest request) {
		String refundSize = request.getParameter("size");
		int rSize = Integer.parseInt(refundSize);
		if (rSize > 0) {
			List<String> refunds = new ArrayList<String>();
			for (int i = 0; i < rSize; i++) {
				String r = "refundId";
				int m = i + 1;
				r = r + m;
				String refundId = request.getParameter(r);
				r = "refundId";
				refunds.add(refundId);
			}
			boolean agree = refundOrderService.aggreeRefuns(refunds);
			return agree;
		}
		return false;
	}

}
