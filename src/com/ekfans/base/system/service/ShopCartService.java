package com.ekfans.base.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tools.ant.taskdefs.optional.Cab;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IBargainDao;
import com.ekfans.base.order.dao.IOrderAddressDao;
import com.ekfans.base.order.dao.IOrderDao;
import com.ekfans.base.order.dao.IOrderDetailDao;
import com.ekfans.base.order.dao.IOrderTreatDetailDao;
import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.service.IBargainService;
import com.ekfans.base.order.service.IOrderPhotoService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.base.product.dao.IProductInfoDetailDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.service.IProductCategoryRelService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductInfoDetailService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.ISupplyProductService;
import com.ekfans.base.product.service.ProductService;
import com.ekfans.base.product.util.ProductConst;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.dao.IShopCartDao;
import com.ekfans.base.system.model.ShopCart;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.number.NoManager;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: ShopCartService
 * @Description: TODO(购物车的service实现)
 * @author ekfans
 * @date 2014-5-19 上午11:38:04
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ShopCartService implements IShopCartService {
	// 定义DAO
	@Autowired
	private IShopCartDao shopCartDao;
	@Autowired
	private ISupplyProductService supplyProductService;
	@Autowired
	private IOrderDao orderDao;
	@Autowired
	private IBargainService bargainService;
	@Autowired
	private IProductCategoryService categoryService;
	@Autowired
	private IProductCategoryRelService categoryRelService;
	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(ProductService.class);

	/**
	 * 根据id删除
	 */
	public boolean deleteShopCartById(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			// 调用SERVICE的方法删除店铺
			shopCartDao.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据id删除
	 */
	public boolean deleteShopCartById(String id, Session session) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			// 调用SERVICE的方法删除店铺
			shopCartDao.deleteById(id, session);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据userID得到集合
	 */
	public List<ShopCart> getShopCartByUserId(String userId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select s.storeName,s.id from Product as p ,ShopCart as sc,Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.id = sc.productId");
		// 当品牌的对应店铺id和店铺id对应
		sql.append(" and p.storeId = s.id");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and sc.userId = :userId");
			paramMap.put("userId", userId);
		}

		sql.append(" group by s.id ");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = shopCartDao.list(sql.toString(), paramMap);
			List<ShopCart> ShopCarts = new ArrayList<ShopCart>();
			for (Object[] object : list) {
				ShopCart shopCart = new ShopCart();
				shopCart.setName((String) object[0]);
				shopCart.setStoreId((String) object[1]);
				ShopCarts.add(shopCart);
			}
			return ShopCarts;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据id得到对象
	 */
	public ShopCart getByShopCartById(String id) {
		if (id == null) {
			return null;
		}
		try {
			// 调用DAO方法查找广告
			ShopCart shopCart = (ShopCart) shopCartDao.get(id);
			if (shopCart != null) {
				return shopCart;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 修改
	 */
	public Boolean update(ShopCart shopCart) {
		try {
			// 调用DAO方法修改广告
			shopCartDao.updateBean(shopCart);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 添加
	 */
	public Boolean add(ShopCart shopCart) {
		try {
			// 调用DAO方法修改广告
			shopCartDao.addBean(shopCart);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据商品id返回boolean
	 */
	public Boolean getByShopCartByProductId(String pId, String userId,
			String pInfoId) {
		StringBuffer sql = new StringBuffer(
				"select sc from ShopCart as sc where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(pId)) {
			sql.append(" and sc.productId = :productId");
			map.put("productId", pId);
		}
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and sc.userId = :userId");
			map.put("userId", userId);
		}
		if (!StringUtil.isEmpty(pInfoId)) {
			sql.append(" and sc.productInfoDetailId = :productInfoDetailId");
			map.put("productInfoDetailId", pInfoId);
		}
		try {
			List<ShopCart> list = shopCartDao.list(sql.toString(), map);
			if (list.size() == 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据商品id得到购物车数据
	 */
	public ShopCart getShopCartByProductId(String pId, String userId,
			String pInfoId) {
		StringBuffer sql = new StringBuffer(
				"select sc from ShopCart as sc where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(pId)) {
			sql.append(" and sc.productId = :productId");
			map.put("productId", pId);
		}
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and sc.userId = :userId");
			map.put("userId", userId);
		}
		if (!StringUtil.isEmpty(pInfoId)) {
			sql.append(" and sc.productInfoDetailId = :productInfoDetailId");
			map.put("productInfoDetailId", pInfoId);
		}
		try {
			List<ShopCart> list = shopCartDao.list(sql.toString(), map);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ShopCart> getShopCartByUserIdAndStoreId(String userId,
			String storeId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select sc.id,p.id,p.recommendPicture1,p.name,p.unitPrice,s.storeName,p.quantity,"
						+ "p.buyCount,p.listPrice,sc.productQuantity,sc.checked,s.id,sc.productInfoDetailId,p.fare,sc.productQuantity "
						+ "from Product as p ,ShopCart as sc,Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.id = sc.productId");
		// 当品牌的对应店铺id和店铺id对应
		sql.append(" and p.storeId = s.id");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and sc.userId = :userId");
			paramMap.put("userId", userId);
		}
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and s.id = :storeId");
			paramMap.put("storeId", storeId);
		}
		sql.append(" order by s.id desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = shopCartDao.list(sql.toString(), paramMap);
			List<ShopCart> ShopCarts = new ArrayList<ShopCart>();
			for (Object[] object : list) {

				ShopCart shopCart = new ShopCart();
				shopCart.setId((String) object[0]);
				shopCart.setProductId((String) object[1]);
				shopCart.setPicture((String) object[2]);
				shopCart.setProductName((String) object[3]);

				shopCart.setUnitPrice((BigDecimal) object[4]);

				shopCart.setName((String) object[5]);

				shopCart.setQuantity((Integer) object[6]);

				shopCart.setListPrice((BigDecimal) object[8]);

				shopCart.setProductQuantity((Integer) object[9]);
				BigDecimal price = (BigDecimal) object[4];// 商城价
				BigDecimal num = new BigDecimal((Integer) object[9]);// 数量
				shopCart.setTotalPrice(price.multiply(num));// 总价

				shopCart.setChecked((Boolean) object[10]);
				shopCart.setStoreId((String) object[11]);
				shopCart.setFare((BigDecimal) object[13]);// 获取商品运费金额
				if (!StringUtil.isEmpty((String) object[12])) {
					shopCart.setProductInfoDetailId((String) object[12]);
					StringBuffer sql2 = new StringBuffer(
							"from ProductInfoDetail as pid where 1 = 1 ");
					// 定义参数MAP
					Map<String, Object> paramMap2 = new HashMap<String, Object>();
					// 当品牌的id和product品跑对应
					sql2.append(" and pid.id = :pId");
					paramMap2.put("pId", (String) object[12]);
					List<ProductInfoDetail> pids = shopCartDao.list(
							sql2.toString(), paramMap2);
					if (pids != null && pids.size() > 0) {
						shopCart.setUnitPrice(pids.get(0).getPrice());
						shopCart.setQuantity(pids.get(0).getQuantity());
						shopCart.setTotalPrice(pids.get(0).getPrice()
								.multiply(num));// 总价
					}

				}
				BigDecimal prefe = shopCart.getListPrice().subtract(
						shopCart.getUnitPrice());// 优惠
				shopCart.setPrice(shopCart.getListPrice().subtract(
						shopCart.getUnitPrice()));
				shopCart.setTempTotalPrefePrice(prefe.multiply(num));// 总优惠
				ShopCarts.add(shopCart);
			}
			return ShopCarts;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<ShopCart> getShopCartByUserIdCheck(String userId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select s.storeName,s.id,p.unitPrice,sc.productQuantity,sc.checked,p.fare from Product as p ,ShopCart as sc,Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.id = sc.productId");
		// 当品牌的对应店铺id和店铺id对应
		sql.append(" and p.storeId = s.id");
		sql.append(" and sc.checked = true");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and sc.userId = :userId");
			paramMap.put("userId", userId);
		}

		sql.append(" group by s.id ");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = shopCartDao.list(sql.toString(), paramMap);
			List<ShopCart> ShopCarts = new ArrayList<ShopCart>();
			for (Object[] object : list) {
				ShopCart shopCart = new ShopCart();
				shopCart.setName((String) object[0]);
				shopCart.setStoreId((String) object[1]);
				BigDecimal price = (BigDecimal) object[2];
				BigDecimal num = new BigDecimal((Integer) object[3]);
				;
				shopCart.setTotalPrice(price.multiply(num));
				shopCart.setChecked((Boolean) object[4]);

				shopCart.setFare((BigDecimal) object[5]);// 获取商品邮费
				// shopCart.setTempFare(FormatDigitalUtil.formatPriceObject(object[5]));//格式化邮费【方便前台显示】
				ShopCarts.add(shopCart);
			}
			return ShopCarts;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<ShopCart> getListByUserId(String userId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select sc.id,p.id,p.picture,p.name,p.unitPrice,s.storeName,p.quantity,p.buyCount,p.listPrice,sc.productQuantity,sc.checked,s.id from Product as p ,ShopCart as sc,Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.id = sc.productId");
		// 当品牌的对应店铺id和店铺id对应
		sql.append(" and p.storeId = s.id");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and sc.userId = :userId");
			paramMap.put("userId", userId);
		}

		sql.append(" order by s.id desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = shopCartDao.list(sql.toString(), paramMap);
			List<ShopCart> ShopCarts = new ArrayList<ShopCart>();
			for (Object[] object : list) {
				ShopCart shopCart = new ShopCart();
				shopCart.setId((String) object[0]);
				shopCart.setProductId((String) object[1]);
				shopCart.setPicture((String) object[2]);
				shopCart.setProductName((String) object[3]);
				shopCart.setUnitPrice((BigDecimal) object[4]);
				shopCart.setName((String) object[5]);
				shopCart.setQuantity((Integer) object[6] - (Integer) object[7]);
				shopCart.setListPrice((BigDecimal) object[8]);
				shopCart.setPrice(shopCart.getListPrice().subtract(
						shopCart.getUnitPrice()));
				shopCart.setProductQuantity((Integer) object[9]);
				BigDecimal price = (BigDecimal) object[4];
				BigDecimal num = new BigDecimal((Integer) object[9]);
				;
				shopCart.setTotalPrice(price.multiply(num));
				shopCart.setChecked((Boolean) object[10]);
				shopCart.setStoreId((String) object[11]);
				ShopCarts.add(shopCart);
			}
			return ShopCarts;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Integer getProductNumber(String cartId, String userId) {
		String hql = "select pid.quantity from ShopCart t,ProductInfoDetail pid where"
				+ " pid.id=t.productInfoDetailId and t.userId=:userId and t.id=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", cartId);
		map.put("userId", userId);
		try {
			List<Object> list = shopCartDao.list(hql, map);
			if (null == list || list.size() <= 0) {
				String hql2 = "select p.quantity from ShopCart t,Product p where"
						+ " p.id=t.productId and t.userId=:userId and t.id=:id";
				List<Object> list2 = shopCartDao.list(hql2, map);
				return Integer.parseInt(list2.get(0).toString());
			}
			String productNumber = list.get(0).toString();
			return Integer.valueOf(productNumber);
		} catch (Exception e) {
			log.error(e.getMessage());
			return 0;
		}

	}

	/**
	 * 得到购物车总的商品数量
	 */
	public String getShopCartSum(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return "0";
		}

		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "select count(sc.id) from ShopCart sc where sc.userId=:userId";
		param.put("userId", userId);
		try {
			List<Object> list = shopCartDao.list(hql, param);
			return list.get(0).toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			return "0";
		}
	}

	@Override
	public List<ShopCart> getShopCartByUserIdCheckNew(String userId,
			String cartId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select s.storeName,s.id,p.unitPrice,sc.productQuantity,sc.checked,p.fare from Product as p ,ShopCart as sc,Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.id = sc.productId");
		// 当品牌的对应店铺id和店铺id对应
		sql.append(" and p.storeId = s.id");
		// 如果查询条件输入了name，添加查询条件

		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and sc.userId = :userId");
			paramMap.put("userId", userId);
		}

		String tempStr = "";
		if (cartId.indexOf(",") == -1) {
			tempStr = "'" + cartId + "'";
		} else {
			String[] carArray = cartId.split(",");
			for (int i = 0; i < carArray.length; i++) {
				tempStr += ",'" + carArray[i] + "'";
			}
		}

		if (tempStr.indexOf(",") >= 0) {
			tempStr = tempStr.substring(1);
		}
		sql.append(" and sc.id in (" + tempStr + ")");
		sql.append(" group by s.id ");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = shopCartDao.list(sql.toString(), paramMap);
			List<ShopCart> ShopCarts = new ArrayList<ShopCart>();
			for (Object[] object : list) {
				ShopCart shopCart = new ShopCart();
				shopCart.setName((String) object[0]);
				shopCart.setStoreId((String) object[1]);
				BigDecimal price = (BigDecimal) object[2];
				BigDecimal num = new BigDecimal((Integer) object[3]);
				;
				shopCart.setTotalPrice(price.multiply(num));
				shopCart.setChecked((Boolean) object[4]);

				shopCart.setFare((BigDecimal) object[5]);// 获取商品邮费
				// shopCart.setTempFare(FormatDigitalUtil.formatPriceObject(object[5]));//格式化邮费【方便前台显示】
				ShopCarts.add(shopCart);
			}
			return ShopCarts;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean commitShopCartToOrder(HttpServletRequest request) {
		Session session = null;
		Transaction transaction = null;
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		Store s = (Store) request.getSession().getAttribute(SystemConst.STORE);
		int orderType = 0;
		String productType = "";

		try {
			session = shopCartDao.createSession();
			transaction = session.beginTransaction();

			// 统计用户购物车商品总数
			request.setAttribute("cartProductCountNumber",
					getShopCartSum(user.getId()));

			String userId = user.getId();
			// 通过userID得到团购车列表
			List<ShopCart> scs = getShopCartByUserIdCheck(userId);
			// 订单备注
			String[] notes = request.getParameterValues("note");
			// 获取配送方式名称
			String[] shipment = request.getParameterValues("shipment");
			// 发票类型
			String[] invoiceType = request.getParameterValues("invoiceType");
			// 发票内容
			String[] invoiceContent = request
					.getParameterValues("invoiceContent");
			// 发票抬头
			String[] invoiceTitle = request.getParameterValues("invoiceTitle");
			// 获取商品运费
			String maxFare = request.getParameter("fare");

			for (ShopCart sc : scs) {
				sc.setChildList(getShopCartByUserIdAndStoreId(userId,
						sc.getStoreId()));
			}
			NoManager noManager = new NoManager();
			String PayId = noManager.createTotalOrderId();
			BigDecimal orderTotal = new BigDecimal("0.00");
			BigDecimal sum = new BigDecimal("0.00");
			// 设置oreder信息和订单详情信息
			for (int i = 0; i < scs.size(); i++) {
				if (scs.get(i).isChecked() == true) {
					// 定义一个自动生成的订单id
					NoManager no = new NoManager();
					String orderId = no.createOrderId();
					// 得到用户确认的收货地址收货人等信息
					String uaProvincial = request.getParameter("uaProvincial");
					String uaCity = request.getParameter("uaCity");
					String uaArea = request.getParameter("uaArea");
					String uaAddress = request.getParameter("uaAddress");
					String uaZipcode = request.getParameter("uaZipcode");
					String uaMobile = request.getParameter("uaMobile");
					String uaName = request.getParameter("uaName");
					OrderAddress orderAddress = new OrderAddress();
					orderAddress.setOrderId(orderId);
					orderAddress.setProvincial(uaProvincial);
					orderAddress.setCity(uaCity);
					orderAddress.setArea(uaArea);
					orderAddress.setAddress(uaAddress);
					orderAddress.setZipCode(uaZipcode);
					orderAddress.setMobile(uaMobile);
					orderAddress.setName(uaName);
					Order order = new Order();

					order.setId(orderId);
					order.setStoreId(scs.get(i).getStoreId());
					order.setUserId(userId);
					if (null != notes && !StringUtil.isEmpty(notes[i])) {
						order.setNote(notes[i]);
					}

					order.setShippingStatus(false);
					order.setStatus(OrderConst.ORDER_STATUS_WAIT_PAY);
					order.setCreateTime(DateUtil.getSysDateTimeString());
					// 判断是否需要发票
					if (null != invoiceType
							&& !StringUtil.isEmpty(invoiceType[i])) {
						order.setInvoice(true);
						order.setInvoiceType(invoiceType[i].equals("false") ? false
								: true);
					}
					if (null != invoiceTitle
							&& !StringUtil.isEmpty(invoiceTitle[i])) {
						order.setInvoice(true);
						order.setInvoiceTitle(invoiceTitle[i]);
					}
					if (null != invoiceContent
							&& !StringUtil.isEmpty(invoiceContent[i])) {
						order.setInvoice(true);
						order.setInvoiceContent(invoiceContent[i]);
					}
					order.setShipment(shipment[i]);
					order.setPayId(PayId);
					OrderTreatDetail orderTreatDetail = new OrderTreatDetail();
					orderTreatDetail.setOrderId(orderId);
					orderTreatDetail.setCreateTime(DateUtil
							.getSysDateTimeString());
					if (StringUtil.isEmpty(user.getNickName())) {
						orderTreatDetail.setCreater(user.getName());
					} else {
						orderTreatDetail.setCreater(user.getNickName());
					}
					orderTreatDetail.setType(OrderConst.ORDER_TREAT_NEW_ORDER);
					orderTreatDetail.setNote("下单");

					IOrderAddressDao orderAddressDao = SpringContextHolder
							.getBean(IOrderAddressDao.class);
					IOrderTreatDetailDao orderTreatDetailDao = SpringContextHolder
							.getBean(IOrderTreatDetailDao.class);
					IOrderDetailDao orderDetailDao = SpringContextHolder
							.getBean(IOrderDetailDao.class);
					IOrderDao orderDao = SpringContextHolder
							.getBean(IOrderDao.class);
					IOrderPhotoService orderPhotoService = SpringContextHolder
							.getBean(IOrderPhotoService.class);
					IProductDao productDao = SpringContextHolder
							.getBean(IProductDao.class);
					IProductInfoDetailDao productInfoDetailDao = SpringContextHolder
							.getBean(IProductInfoDetailDao.class);
					// 反射定义商品Service
					IProductService productService = SpringContextHolder
							.getBean(IProductService.class);
					IProductInfoDetailService infoDetailService = SpringContextHolder
							.getBean(IProductInfoDetailService.class);
					orderAddressDao.addBean(orderAddress, session);
					orderTreatDetailDao.addBean(orderTreatDetail, session);
					List<ShopCart> scList = scs.get(i).getChildList();
					for (int j = 0; j < scList.size(); j++) {

						if (scList.get(j).isChecked() == true) {
							OrderDetail od = new OrderDetail();
							// 获取商品
							Product product = productService.getProductById(
									scList.get(j).getProductId(), session);
							if (product != null) {
								productType = product.getType();
								// if
								// (ProductConst.PRODUCT_TYPE_CAIGOU.equals(product.getType()))
								// {
								// orderType = OrderConst.ORDER_TYPE_CAIGOU;
								// } else if
								// (ProductConst.PRODUCT_TYPE_XIAOZONG.equals(product.getType()))
								// {
								// orderType = OrderConst.ORDER_TYPE_XIAOZONG;
								// } else if
								// (ProductConst.PRODUCT_TYPE_DAZONG.equals(product.getType()))
								// {
								// orderType = OrderConst.ORDER_TYPE_DAZONG;
								// }
								od.setOrderId(orderId);
								od.setStoreId(scList.get(j).getStoreId());
								od.setUserId(userId);
								od.setProductId(scList.get(j).getProductId());
								od.setProductName(scList.get(j)
										.getProductName());
								od.setQuantity(scList.get(j)
										.getProductQuantity());
								od.setProductInfoDetailId(scList.get(j)
										.getProductInfoDetailId());

								ProductInfoDetail infoDetail = infoDetailService
										.getProductInfoDetailById(scList.get(j)
												.getProductInfoDetailId());

								if (infoDetail != null) {
									od.setInfoName1(infoDetail.getInfoName1());
									od.setInfoName2(infoDetail.getInfoName2());
									od.setInfoName3(infoDetail.getInfoName3());
									od.setInfoName4(infoDetail.getInfoName4());
									od.setInfoValue1(infoDetail.getInfoValue1());
									od.setInfoValue2(infoDetail.getInfoValue2());
									od.setInfoValue3(infoDetail.getInfoValue3());
									od.setInfoValue4(infoDetail.getInfoValue4());
								}
								ServletContext servletContext = request
										.getSession().getServletContext();
								String oldPic = product.getSmallPicture();
								String picture = "/customerfiles/orderDetail/"
										+ od.getId()
										+ "/productCopy/Small"
										+ java.util.UUID.randomUUID()
												.toString()
										+ oldPic.substring(oldPic
												.lastIndexOf("."));
								FileUtil.copyFile(
										servletContext.getRealPath(oldPic),
										servletContext.getRealPath(picture));
								od.setProductImage(picture);
								od.setProductNo(product.getProductNumber());
								od.setPrice(scList.get(j).getUnitPrice());
								od.setTotalPrice(scList.get(j).getTotalPrice());
								od.setIntegral(0);
								// 完成订单过后，如果是优选商城的商品则减少库存
								if (ProductConst.PRODUCT_TYPE_XIAOZONG
										.equals(product.getType())) {
									// 数量
									int quantity = scList.get(j)
											.getProductQuantity();
									if (!StringUtil.isEmpty(scList.get(j)
											.getProductInfoDetailId())) {
										ProductInfoDetail pid = infoDetailService
												.getProductInfoDetailById(scList
														.get(j)
														.getProductInfoDetailId());
										pid.setQuantity(pid.getQuantity()
												- quantity);
										productInfoDetailDao.updateBean(pid,
												session);
									} else {
										product.setQuantity(product
												.getQuantity() - quantity);
										productDao.updateBean(product, session);
									}
								}
								// 完成订单后执行删除选择的购物车操作
								deleteShopCartById(scList.get(j).getId(),
										session);
								orderTotal = orderTotal.add(od.getTotalPrice());
								orderDetailDao.addBean(od, session);
								// 生成订单商品快照
								orderPhotoService.createPhoto(od, product,
										request, session);
							}
						}
					}
					// 运费
					BigDecimal fare = StringUtil.isEmpty(maxFare) ? new BigDecimal(
							"0.00") : new BigDecimal(maxFare);
					order.setType(orderType);
					order.setProductPrice(orderTotal);
					order.setTotalPrice(orderTotal.add(fare));
					order.setPaid(orderTotal.add(fare));
					order.setFare(fare);
					orderDao.addBean(order, session);
					orderTotal = new BigDecimal("0.00");
					sum = sum.add(order.getPaid());
				}
			}
			IProductService productService = SpringContextHolder
					.getBean(IProductService.class);

			// for (ShopCart s : scs) {
			// for (ShopCart sc : s.getChildList()) {
			// if (sc.isChecked() == true) {
			// BigDecimal totalPrice = sc.getTotalPrice();
			// sum = sum.add(totalPrice);
			// }
			// }
			// }
			session.flush();
			transaction.commit();
			session.close();
			// 绑定对象到页面上
			request.setAttribute("totalOrderId", PayId);
			// 绑定其他商品到页面
			List<Product> products = productService.getProductSalesRanking(4,
					productType);
			// 绑定总价格和集合到页面上
			request.setAttribute("products", products);
			request.setAttribute("sum", sum);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	@Override
	public boolean commitBargainCartToOrder(HttpServletRequest request){
		Session session = null;
		Transaction transaction = null;
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		Store s = (Store) request.getSession().getAttribute(SystemConst.STORE);
		try {
			session = orderDao.createSession();
			transaction = session.beginTransaction();
			String userId = user.getId();
			// 得到bargainId
			String bargainId = request.getParameter("bargainId");
			Bargain b = bargainService.getDetailById(bargainId);
			// 得到总价
			String t_price = request.getParameter("totalPrice");
			// 得到商品id
			String productId = request.getParameter("productId");
			// 得到购买数量
			BigDecimal t_count = b.getFinalQuantity();
			Integer count = t_count.intValue();
			// 得到购买价格
			String price = request.getParameter("finalPrice");
			// 得到备注信息
			String note = request.getParameter("note");
			NoManager noManager = new NoManager();
			String PayId = noManager.createTotalOrderId();
			BigDecimal orderTotal = new BigDecimal("0.00");
			// 设置oreder信息和订单详情信息
			// 定义一个自动生成的订单id
			NoManager no = new NoManager();
			String orderId = no.createOrderId();
			// 得到用户确认的收货地址收货人等信息
			String uaProvincial = request.getParameter("uaProvincial");
			String uaCity = request.getParameter("uaCity");
			String uaArea = request.getParameter("uaArea");
			String uaAddress = request.getParameter("uaAddress");
			String uaZipcode = request.getParameter("uaZipcode");
			String uaMobile = request.getParameter("uaMobile");
			String uaName = request.getParameter("uaName");
			OrderAddress orderAddress = new OrderAddress();
			orderAddress.setOrderId(orderId);
			orderAddress.setProvincial(uaProvincial);
			orderAddress.setCity(uaCity);
			orderAddress.setArea(uaArea);
			orderAddress.setAddress(uaAddress);
			orderAddress.setZipCode(uaZipcode);
			orderAddress.setMobile(uaMobile);
			orderAddress.setName(uaName);
			Order order = new Order();
			// 反射定义商品Service
			IProductService productService = SpringContextHolder
					.getBean(IProductService.class);
			IProductDao productDao = SpringContextHolder
					.getBean(IProductDao.class);
			// 获取商品
			Product product = productService.getProductById(productId, session);
			product.setQuantity(product.getQuantity() - Integer.valueOf(count));
			product.setCjl(product.getCjl() + Integer.valueOf(count));
			productDao.updateBean(product, session);
			order.setId(orderId);
			order.setStoreId(product.getStoreId());
			
			order.setUserId(userId);
			order.setNote(note);
			order.setShippingStatus(false);
			order.setStatus(OrderConst.ORDER_STATUS_WAIT_PAY);
			order.setCreateTime(DateUtil.getSysDateTimeString());

			order.setPayId(orderId);
			OrderTreatDetail orderTreatDetail = new OrderTreatDetail();
			orderTreatDetail.setOrderId(orderId);
			orderTreatDetail.setCreateTime(DateUtil.getSysDateTimeString());
			orderTreatDetail.setCreater(s.getStoreName());
			orderTreatDetail.setType(OrderConst.ORDER_TREAT_NEW_ORDER);
			orderTreatDetail.setNote("下单");

			IOrderAddressDao orderAddressDao = SpringContextHolder
					.getBean(IOrderAddressDao.class);
			IOrderTreatDetailDao orderTreatDetailDao = SpringContextHolder
					.getBean(IOrderTreatDetailDao.class);
			IOrderDetailDao orderDetailDao = SpringContextHolder
					.getBean(IOrderDetailDao.class);
			IOrderDao orderDao = SpringContextHolder.getBean(IOrderDao.class);
			IBargainDao bargainDao = SpringContextHolder.getBean(IBargainDao.class);
			// 修改议价单订单状态为已下单
			b.setOrderStatus("1");
			bargainDao.updateBean(b);
			IOrderPhotoService orderPhotoService = SpringContextHolder
					.getBean(IOrderPhotoService.class);

			orderAddressDao.addBean(orderAddress, session);
			orderTreatDetailDao.addBean(orderTreatDetail, session);
			OrderDetail od = new OrderDetail();

			if (product != null) {
				od.setOrderId(orderId);
				od.setStoreId(product.getStoreId());
				od.setUserId(userId);
				od.setProductId(productId);
				od.setProductName(product.getName());
				od.setQuantity(Integer.valueOf(count));
				BigDecimal finalPrice = new BigDecimal(price);
				od.setPrice(finalPrice);
				BigDecimal totalPrice = new BigDecimal(t_price);
				od.setTotalPrice(totalPrice);
				od.setIntegral(0);
				orderDetailDao.addBean(od, session);
				ServletContext servletContext = request.getSession()
						.getServletContext();
				String oldPic = product.getSmallPicture();
				String picture = "/customerfiles/orderDetail/" + od.getId()
						+ "/productCopy/Small"
						+ java.util.UUID.randomUUID().toString();
				if (!StringUtil.isEmpty(oldPic)) {
					picture += oldPic.substring(oldPic.lastIndexOf("."));
					FileUtil.copyFile(servletContext.getRealPath(oldPic),
							servletContext.getRealPath(picture));
					od.setProductImage(picture);
				}
				od.setProductNo(product.getProductNumber());
				// 完成订单后执行删除选择的购物车操作
				orderTotal = orderTotal.add(od.getTotalPrice());
				orderDetailDao.updateBean(od, session);
				// 生成订单商品快照
				orderPhotoService.createPhoto(od, product, request, session);
			}

			//查出商品分类
			ProductCategory category=categoryService.getCategoryById(categoryRelService.getPcRlBy(product.getId()).getCategoryId());
			//判断商品来源设置订单类型
			if(category.getFullPath().contains(Cache.getResource("lvseshangcheng"))){// 2:绿色商城订单
				order.setType(2);
			}else{// 1:普通订单
				order.setType(0);
			}
			order.setProductPrice(orderTotal);
			order.setTotalPrice(orderTotal);
			order.setPaid(orderTotal);

			orderDao.addBean(order, session);
			session.flush();
			transaction.commit();
			session.close();

			request.getSession().removeAttribute("shopCart");
			// 绑定对象到页面上
			request.setAttribute("totalOrderId", orderId);
			// 绑定总价格和集合到页面上
			request.setAttribute("sum", order.getPaid());
			request.setAttribute("orderType", 0);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}
	
	@Override
	public boolean commitPurchaseCartToOrder(HttpServletRequest request) {
		Session session = null;
		Transaction transaction = null;
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		Store s = (Store) request.getSession().getAttribute(SystemConst.STORE);
		String productType = "";

		try {
			session = shopCartDao.createSession();
			transaction = session.beginTransaction();
			String userId = user.getId();

			// // 获取配送方式名称
			// String[] shipment = request.getParameterValues("shipment");
			// // 发票类型
			// String[] invoiceType = request.getParameterValues("invoiceType");
			// // 发票内容
			// String[] invoiceContent =
			// request.getParameterValues("invoiceContent");
			// // 发票抬头
			// String[] invoiceTitle =
			// request.getParameterValues("invoiceTitle");
			// // 获取商品运费
			// String maxFare = request.getParameter("fare");
			// 得到商品id
			String productId = request.getParameter("productId");
			// 得到购买数量
			String count = request.getParameter("count");
			// 得到备注信息
			String note = request.getParameter("note");
			NoManager noManager = new NoManager();
			BigDecimal orderTotal = new BigDecimal("0.00");
			// 设置oreder信息和订单详情信息
			// 定义一个自动生成的订单id
			NoManager no = new NoManager();
			String orderId = no.createOrderId();
			String PayId = orderId;
			// 得到用户确认的收货地址收货人等信息
			String uaProvincial = request.getParameter("uaProvincial");
			String uaCity = request.getParameter("uaCity");
			String uaArea = request.getParameter("uaArea");
			String uaAddress = request.getParameter("uaAddress");
			String uaZipcode = request.getParameter("uaZipcode");
			String uaMobile = request.getParameter("uaMobile");
			String uaName = request.getParameter("uaName");
			OrderAddress orderAddress = new OrderAddress();
			orderAddress.setOrderId(orderId);
			orderAddress.setProvincial(uaProvincial);
			orderAddress.setCity(uaCity);
			orderAddress.setArea(uaArea);
			orderAddress.setAddress(uaAddress);
			orderAddress.setZipCode(uaZipcode);
			orderAddress.setMobile(uaMobile);
			orderAddress.setName(uaName);
			Order order = new Order();
			// 反射定义商品Service
			IProductService productService = SpringContextHolder
					.getBean(IProductService.class);
			IProductDao productDao = SpringContextHolder
					.getBean(IProductDao.class);
			// 获取商品
			Product product = productService.getProductById(productId, session);
			product.setQuantity(product.getQuantity() - Integer.valueOf(count));
			product.setCjl(product.getCjl() + Integer.valueOf(count));
			productDao.updateBean(product, session);
			order.setId(orderId);
			order.setStoreId(product.getStoreId());
			order.setUserId(userId);
			order.setNote(note);
			// if (null != notes && !StringUtil.isEmpty(notes[0])) {
			// order.setNote(notes[0]);
			// }
			order.setShippingStatus(false);
			order.setStatus(OrderConst.ORDER_STATUS_WAIT_PAY);
			order.setCreateTime(DateUtil.getSysDateTimeString());
			// 判断是否需要发票
			// if (null != invoiceType && !StringUtil.isEmpty(invoiceType[0])) {
			// order.setInvoice(true);
			// order.setInvoiceType(invoiceType[0].equals("false") ? false :
			// true);
			// }
			// if (null != invoiceTitle && !StringUtil.isEmpty(invoiceTitle[0]))
			// {
			// order.setInvoice(true);
			// order.setInvoiceTitle(invoiceTitle[0]);
			// }
			// if (null != invoiceContent &&
			// !StringUtil.isEmpty(invoiceContent[0])) {
			// order.setInvoice(true);
			// order.setInvoiceContent(invoiceContent[0]);
			// }
			// order.setShipment((shipment != null && shipment.length > 0) ?
			// shipment[0] : "");
			order.setPayId(PayId);
			OrderTreatDetail orderTreatDetail = new OrderTreatDetail();
			orderTreatDetail.setOrderId(orderId);
			orderTreatDetail.setCreateTime(DateUtil.getSysDateTimeString());
			orderTreatDetail.setCreater(s.getStoreName());
			orderTreatDetail.setType(OrderConst.ORDER_TREAT_NEW_ORDER);
			orderTreatDetail.setNote("下单");

			IOrderAddressDao orderAddressDao = SpringContextHolder
					.getBean(IOrderAddressDao.class);
			IOrderTreatDetailDao orderTreatDetailDao = SpringContextHolder
					.getBean(IOrderTreatDetailDao.class);
			IOrderDetailDao orderDetailDao = SpringContextHolder
					.getBean(IOrderDetailDao.class);
			IOrderDao orderDao = SpringContextHolder.getBean(IOrderDao.class);
			IOrderPhotoService orderPhotoService = SpringContextHolder
					.getBean(IOrderPhotoService.class);

			orderAddressDao.addBean(orderAddress, session);
			orderTreatDetailDao.addBean(orderTreatDetail, session);
			OrderDetail od = new OrderDetail();

			if (product != null) {
				od.setOrderId(orderId);
				od.setStoreId(product.getStoreId());
				od.setUserId(userId);
				od.setProductId(productId);
				od.setProductName(product.getName());
				od.setQuantity(Integer.valueOf(count));
				od.setPrice(product.getUnitPrice());
				od.setTotalPrice(product.getUnitPrice().multiply(
						new BigDecimal(count)));
				od.setIntegral(0);
				orderDetailDao.addBean(od, session);
				ServletContext servletContext = request.getSession()
						.getServletContext();
				String oldPic = product.getSmallPicture();
				String picture = "/customerfiles/orderDetail/" + od.getId()
						+ "/productCopy/Small"
						+ java.util.UUID.randomUUID().toString();
				if (!StringUtil.isEmpty(oldPic)) {
					picture += oldPic.substring(oldPic.lastIndexOf("."));
					FileUtil.copyFile(servletContext.getRealPath(oldPic),
							servletContext.getRealPath(picture));
					od.setProductImage(picture);
				}
				od.setProductNo(product.getProductNumber());
				// 完成订单后执行删除选择的购物车操作
				orderTotal = orderTotal.add(od.getTotalPrice());
				orderDetailDao.updateBean(od, session);
				// 生成订单商品快照
				orderPhotoService.createPhoto(od, product, request, session);
			}

			//查出商品分类
			ProductCategory category=categoryService.getCategoryById(categoryRelService.getPcRlBy(product.getId()).getCategoryId());
			//判断商品来源设置订单类型
			if(category.getFullPath().contains(Cache.getResource("lvseshangcheng"))){// 2:绿色商城订单
				order.setType(2);
			}else{// 1:普通订单
				order.setType(0);
			}
			order.setProductPrice(orderTotal);
			order.setTotalPrice(orderTotal);
			order.setPaid(orderTotal);

			orderDao.addBean(order, session);
			session.flush();
			transaction.commit();
			session.close();

			request.getSession().removeAttribute("shopCart");
			// 绑定对象到页面上
			request.setAttribute("totalOrderId", PayId);
			// 绑定总价格和集合到页面上
			request.setAttribute("sum", order.getPaid());
			request.setAttribute("orderType", 0);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	@Override
	public boolean commitDingGouCartToOrder(HttpServletRequest request) {
		Session session = null;
		Transaction transaction = null;
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		Store s = (Store) request.getSession().getAttribute(SystemConst.STORE);
		String productType = "";

		try {
			session = shopCartDao.createSession();
			transaction = session.beginTransaction();
			String userId = user.getId();

			// // 获取配送方式名称
			// String[] shipment = request.getParameterValues("shipment");
			// // 发票类型
			// String[] invoiceType = request.getParameterValues("invoiceType");
			// // 发票内容
			// String[] invoiceContent =
			// request.getParameterValues("invoiceContent");
			// // 发票抬头
			// String[] invoiceTitle =
			// request.getParameterValues("invoiceTitle");
			// // 获取商品运费
			// String maxFare = request.getParameter("fare");
			// 得到商品id
			String productId = request.getParameter("productId");
			// 得到购买数量
			String count = request.getParameter("count");
			// 得到备注信息
			String note = request.getParameter("note");
			NoManager noManager = new NoManager();
			BigDecimal orderTotal = new BigDecimal("0.00");
			// 设置oreder信息和订单详情信息
			// 定义一个自动生成的订单id
			NoManager no = new NoManager();
			String orderId = no.createOrderId();
			String PayId = orderId;
			// 得到用户确认的收货地址收货人等信息
			String uaProvincial = request.getParameter("uaProvincial");
			String uaCity = request.getParameter("uaCity");
			String uaArea = request.getParameter("uaArea");
			String uaAddress = request.getParameter("uaAddress");
			String uaZipcode = request.getParameter("uaZipcode");
			String uaMobile = request.getParameter("uaMobile");
			String uaName = request.getParameter("uaName");
			OrderAddress orderAddress = new OrderAddress();
			orderAddress.setOrderId(orderId);
			orderAddress.setProvincial(uaProvincial);
			orderAddress.setCity(uaCity);
			orderAddress.setArea(uaArea);
			orderAddress.setAddress(uaAddress);
			orderAddress.setZipCode(uaZipcode);
			orderAddress.setMobile(uaMobile);
			orderAddress.setName(uaName);
			Order order = new Order();
			// 反射定义商品Service
			IProductService productService = SpringContextHolder
					.getBean(IProductService.class);
			// IProductDao productDao =
			// SpringContextHolder.getBean(IProductDao.class);
			// 获取商品
			Product product = productService.getProductById(productId, session);
			// product.setQuantity(product.getQuantity() -
			// Integer.valueOf(count));
			// productDao.updateBean(product, session);
			order.setId(orderId);
			order.setStoreId(product.getStoreId());
			order.setUserId(userId);
			order.setNote(note);
			// if (null != notes && !StringUtil.isEmpty(notes[0])) {
			// order.setNote(notes[0]);
			// }
			order.setShippingStatus(false);
			order.setStatus(OrderConst.ORDER_STATUS_WAIT_PAY);
			order.setCreateTime(DateUtil.getSysDateTimeString());
			// 判断是否需要发票
			// if (null != invoiceType && !StringUtil.isEmpty(invoiceType[0])) {
			// order.setInvoice(true);
			// order.setInvoiceType(invoiceType[0].equals("false") ? false :
			// true);
			// }
			// if (null != invoiceTitle && !StringUtil.isEmpty(invoiceTitle[0]))
			// {
			// order.setInvoice(true);
			// order.setInvoiceTitle(invoiceTitle[0]);
			// }
			// if (null != invoiceContent &&
			// !StringUtil.isEmpty(invoiceContent[0])) {
			// order.setInvoice(true);
			// order.setInvoiceContent(invoiceContent[0]);
			// }
			// order.setShipment((shipment != null && shipment.length > 0) ?
			// shipment[0] : "");
			order.setPayId(PayId);
			OrderTreatDetail orderTreatDetail = new OrderTreatDetail();
			orderTreatDetail.setOrderId(orderId);
			orderTreatDetail.setCreateTime(DateUtil.getSysDateTimeString());
			orderTreatDetail.setCreater(s.getStoreName());
			orderTreatDetail.setType(OrderConst.ORDER_TREAT_NEW_ORDER);
			orderTreatDetail.setNote("预订单下单");

			IOrderAddressDao orderAddressDao = SpringContextHolder
					.getBean(IOrderAddressDao.class);
			IOrderTreatDetailDao orderTreatDetailDao = SpringContextHolder
					.getBean(IOrderTreatDetailDao.class);
			IOrderDetailDao orderDetailDao = SpringContextHolder
					.getBean(IOrderDetailDao.class);
			IOrderDao orderDao = SpringContextHolder.getBean(IOrderDao.class);
			IOrderPhotoService orderPhotoService = SpringContextHolder
					.getBean(IOrderPhotoService.class);

			orderAddressDao.addBean(orderAddress, session);
			orderTreatDetailDao.addBean(orderTreatDetail, session);
			OrderDetail od = new OrderDetail();

			if (product != null) {
				od.setOrderId(orderId);
				od.setStoreId(product.getStoreId());
				od.setUserId(userId);
				od.setProductId(productId);
				od.setProductName(product.getName());
				od.setQuantity(Integer.valueOf(count));
				od.setPrice(product.getAdvancePrice());
				od.setTotalPrice(product.getAdvancePrice().multiply(
						new BigDecimal(count)));
				od.setIntegral(0);
				orderDetailDao.addBean(od, session);
				ServletContext servletContext = request.getSession()
						.getServletContext();
				String oldPic = product.getSmallPicture();
				String picture = "/customerfiles/orderDetail/" + od.getId()
						+ "/productCopy/Small"
						+ java.util.UUID.randomUUID().toString();
				if (!StringUtil.isEmpty(oldPic)) {
					picture += oldPic.substring(oldPic.lastIndexOf("."));
					FileUtil.copyFile(servletContext.getRealPath(oldPic),
							servletContext.getRealPath(picture));
					od.setProductImage(picture);
				}

				od.setProductNo(product.getProductNumber());
				// 完成订单后执行删除选择的购物车操作
				orderTotal = orderTotal.add(od.getTotalPrice());
				orderDetailDao.updateBean(od, session);
				// 生成订单商品快照
				orderPhotoService.createPhoto(od, product, request, session);
			}

			order.setType(0);
			order.setProductPrice(orderTotal);
			order.setTotalPrice(orderTotal);
			order.setPaid(orderTotal);

			orderDao.addBean(order, session);
			session.flush();
			transaction.commit();
			session.close();

			request.getSession().removeAttribute("shopCart");
			// 绑定对象到页面上
			request.setAttribute("totalOrderId", PayId);
			// 绑定总价格和集合到页面上
			request.setAttribute("sum", order.getPaid());
			request.setAttribute("orderType", 1);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}
}
