package com.ekfans.controllers.user.refund;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.model.Refund;
import com.ekfans.base.order.service.IOrderAddressService;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.service.IOrderTreatDetailService;
import com.ekfans.base.order.service.StoreOrder.ReturnManagement.IRefundOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IRefundService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 退/换货申请理由的保存
 * 
 * @ClassName: UserRefundProductController
 * @Description:
 * @author 成都易科远见科技有限公司
 * @date May 20, 2014 3:04:24 PM
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class UserRefundProductController extends BasicController {

	private Logger log = LoggerFactory.getLogger(UserRefundProductController.class);
	@Autowired
	private IRefundService refundService;
	@Autowired
	private IRefundOrderService refundOrderService;
	@Autowired
	private IOrderTreatDetailService orderTreatDetailService;
	@Autowired
	private IOrderDetailService orderDetailService;
	@Autowired
	private IOrderAddressService orderAddressService;
	@Autowired
	private IOrderService orderService;

	@RequestMapping(value = "/user/order/returnProduct")
	public String list(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String orderId = request.getParameter("orderId"); // 订单id
		String beginDate = request.getParameter("beginDate"); // 开始时间
		String endDate = request.getParameter("endDate"); // 结束时间
		String pageNum = request.getParameter("pageNum"); // 从页面获取页码
		
		// 分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPage = Integer.parseInt(pageNum);
			} catch (Exception e) {
				log.error(e.getMessage());
				currentPage = 1;
			}
		}
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(10);

		List<Refund> rlist = this.refundOrderService.getRefundOrder(pager, user.getId(), orderId, beginDate, endDate);
		
		request.setAttribute("rlist", rlist);
		request.setAttribute("pager", pager);
		request.setAttribute("orderId", orderId);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("cur", "returnProduct");
		
		return "/userCenter/customer/refund/userRefundProduct";

	}

	/**
	 * 退/换货列表(点击查看详情)
	 */
	@RequestMapping(value = "/user/order/returnProductDetail/{refundId}")
	public String returnProductDetail(@PathVariable String refundId, HttpServletRequest request) {
		Refund refund = this.refundOrderService.getRefundById(refundId);
		
		OrderDetail orderDetail = this.orderDetailService.getOrderDetailByProductId(refund.getProductId(), refund.getOrderId());
		
		Order order = orderService.getOrderId(refund.getOrderId());
		
		OrderTreatDetail orderTreatDetail = orderTreatDetailService.getOrderTreatDetailByOrderId(refund.getOrderId());
		
		OrderAddress orderAddress = orderAddressService.getOrderAddressByOrderId(refund.getOrderId());
		
		request.setAttribute("order", order);
		request.setAttribute("orderDetail", orderDetail);
		request.setAttribute("orderTreatDetail", orderTreatDetail);
		request.setAttribute("orderAddress", orderAddress);
		request.setAttribute("refund", refund);
		request.setAttribute("cur", "returnProduct");
		
		return "/userCenter/customer/refund/refundProductDetail";
	}

	/**
	 * 申请退/换货的理由保存，以及图片的保存
	 */
	@RequestMapping(value = "/user/refund/save")
	@ResponseBody
	public Object applyRefund(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String orderId = request.getParameter("orderId");
		String productId = request.getParameter("productId");
		String refundType = request.getParameter("refundType");
		String note = request.getParameter("note");
		
		// 第一张图
		String imageOne = "/customerfiles/product/" + DateUtil.getNoSpSysDateString() + "/imageOne/";
		imageOne = FileUploadHelper.uploadFile("imageOne", imageOne, request, response);
		// 第二张图
		String imageTwo = "/customerfiles/product/" + DateUtil.getNoSpSysDateString() + "/imageTwo/";
		imageTwo = FileUploadHelper.uploadFile("imageTwo", imageTwo, request, response);
		// 第三张图
		String imageThree = "/customerfiles/product/" + DateUtil.getNoSpSysDateString() + "/imageThree/";
		imageThree = FileUploadHelper.uploadFile("imageThree", imageThree, request, response);
		// 第四张图
		String imageFour = "/customerfiles/product/" + DateUtil.getNoSpSysDateString() + "/imageFour/";
		imageFour = FileUploadHelper.uploadFile("imageFour", imageFour, request, response);
		
		// 如果理由和图片为空则不能保存
		if (StringUtil.isEmpty(note)) {
			return false;
		}
		
		Refund refund = new Refund();
		refund.setUserId(user.getId());
		refund.setOrderId(orderId);
		refund.setProductId(productId);
		refund.setType("0");
		refund.setRefundType(refundType);
		refund.setStatus("0");
		refund.setCreateTime(DateUtil.getSysDateTimeString());
		refund.setNote(note);
		refund.setImageOne(imageOne);
		refund.setImageTwo(imageTwo);
		refund.setImageThree(imageThree);
		refund.setImageFour(imageFour);
		
		if (this.refundService.addRefund(refund)) {
		    
		    OrderTreatDetail otd = new OrderTreatDetail();
            otd.setCreateTime(DateUtil.getSysDateTimeString());
            otd.setCreater(user.getNickName() == null ?user.getName():user.getNickName());
            otd.setOrderId(orderId);
            otd.setNote("【" + user.getNickName() == null ?user.getName():user.getNickName()+ "】申请退换货处理");
            otd.setType(OrderConst.ORDER_TREAT_APPLY);
            orderTreatDetailService.add(otd);
            Order order = orderService.getOrderByOrderId(orderId);
            order.setServiceStatus("0");
            orderService.modifyOrder(order);
			return true;
		}
		return null;
	}
	
	// ==================================
	//             企业中心
	// ==================================
	@RequestMapping(value = "/store/thorder/returnProduct")
	public String storeTHOrderList(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String orderId = request.getParameter("orderId"); // 订单id
		String pageNum = request.getParameter("pageNum"); // 从页面获取页码
		
		// 分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPage = Integer.parseInt(pageNum);
			} catch (Exception e) {
				log.error(e.getMessage());
				currentPage = 1;
			}
		}
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(10);

		List<Refund> rlist = this.refundOrderService.getHXQRefundOrder(pager, user.getId(), orderId);
		
		request.setAttribute("rlist", rlist);
		request.setAttribute("pager", pager);
		request.setAttribute("orderId", orderId);
		
		return "/userCenter/store/refund/refund_list";

	}

	/**
	 * 退/换货列表(点击查看详情)
	 */
	@RequestMapping(value = "/store/thorder/returnProductDetail/{refundId}")
	public String storeTHOrderReturnProductDetail(@PathVariable String refundId, HttpServletRequest request) {
		Refund refund = this.refundOrderService.getRefundById(refundId);
		
		OrderDetail orderDetail = this.orderDetailService.getOrderDetailByProductId(refund.getProductId(), refund.getOrderId());
		
		Order order = orderService.getOrderId(refund.getOrderId());
		
		OrderTreatDetail orderTreatDetail = orderTreatDetailService.getOrderTreatDetailByOrderId(refund.getOrderId());
		
		OrderAddress orderAddress = orderAddressService.getOrderAddressByOrderId(refund.getOrderId());
		
		request.setAttribute("order", order);
		request.setAttribute("orderDetail", orderDetail);
		request.setAttribute("orderTreatDetail", orderTreatDetail);
		request.setAttribute("orderAddress", orderAddress);
		request.setAttribute("refund", refund);
		request.setAttribute("cur", "returnProduct");
		
		return "/userCenter/store/refund/refund_detail";
	}
}
