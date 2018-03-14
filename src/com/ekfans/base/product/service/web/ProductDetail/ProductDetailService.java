package com.ekfans.base.product.service.web.ProductDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.model.Appraise;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.product.dao.web.ProductDetail.IProductDetailDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.service.web.ProductDetail.vo.StoreServiceVo;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.dao.IShopCartDao;
import com.ekfans.base.system.model.ShopCart;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: ProductDetailService
 * @Description: TODO(前台-商品详情)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-21 下午03:46:52
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProductDetailService implements IProductDetailService {
	
	private Logger log = LoggerFactory.getLogger(ProductDetailService.class);
	@Autowired
	private IProductDetailDao productDetailDao;
	@Autowired
	private IShopCartDao shopCartDao;

	/**
	 * 根据商品的id获得商品的详情 商品所需详情的分析:
	 * one.商品表(Product)、商品所属店铺(Store)点击商品时自动跳转到该商品所在的店铺、
	 * 该商品的模板信息(TemplateFields)
	 * two.商品的一些详情信息(ProductInfo)、商品评价记录(Appraise)、商品成交记录(OrderDetail)
	 * three.该店铺的一些促销信息
	 */
	@Override
	public Object[] getProDetailByProId(String proId) {
		if (StringUtil.isEmpty(proId)) {
			return null;
		}
		// 定义返回的Object数组
		Object[] returnObjArray = new Object[2];
		Product product = Cache.getProductById(proId);
		Store store = getProductDetailPartOneOne(product.getStoreId());
		returnObjArray[0] = store;// Store
		// 查询出所有该商品所属扩展字段信息
		List<ProductInfo> pi = getProductInfo(proId);
		// 放入Product的List<ProductInfo>
		product.setInfoList(pi);
		// 查询出该商品所属扩展字段的详情信息
		List<ProductInfoDetail> pid = getProductInfoDetail(proId);
		// 放入Product的List<ProductInfoDetail>
		product.setInfoDetailList(pid);
		returnObjArray[1] = product;
		return returnObjArray;
	}

	/**
	 * 
	 * @Title: getProductDetailPartOne
	 * @Description: TODO(查询出某件商品的商品、店铺的基本信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productDetailDao
	 * @param @param log
	 * @param @param proId
	 * @param @return 设定文件
	 * @return Object[] 返回类型
	 * @throws
	 */
	private Store getProductDetailPartOneOne(String storeId) {
		if (StringUtil.isEmpty(storeId)) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from Store s where s.id =:storeId");
		params.put("storeId", storeId);
		try {
			List<Store> list = this.productDetailDao.list(hql.toString(), params);
			if (list != null && list.size() == 1) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
		
		/*
		try {
			// 如果商品id为null,直接返回null,避免对象的创建浪费有限的性能
			if (StringUtil.isEmpty(proId)) {
				return null;
			}

			// 查询出店铺和商品的基本信息
			StringBuffer hql = new StringBuffer("select p.id,p.name,p.sortName,p.storeId,p.unitPrice,p.listPrice,p.quantity,p.brand,");
			hql.append(" p.picture,p.bigPicture,p.centerPicture,p.smallPicture,p.description,p.note,p.templateId,p.status,");
			hql.append(" p.checkStatus,p.checkMan,p.checkTime,p.checkNote,p.searchKey,p.visitCount,p.buyCount,p.mainCategory,");
			hql.append(" p.productNumber,");
			hql.append(" s.id,s.storeName,s.storeLogo,s.levelId,s.province,s.city,s.area,s.address,s.status,s.checkStatus,s.checkMan,s.checkTime,");
			hql.append(" s.checkNote,s.domain,s.orgId,s.createTime,s.updateTime,s.notes,s.roleId,s.coordinateX,s.coordinateY,s.warningMobile,s.warningEmail,");
			hql.append(" p.fare");
			hql.append(" from Product as p,Store as s where 1=1");
			// 关联条件
			hql.append(" and p.storeId = s.id");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and p.id = :proId");
			params.put("proId", proId);

			List list = productDetailDao.list(hql.toString(), params);
			if (list != null && list.size() == 1) {
				// 定义返回数组
				Object[] objArray = new Object[2];
				Product product = new Product();
				Store store = new Store();

				Object[] objects = (Object[]) list.get(0);
				// product部分
				product.setId((String) objects[0]);
				product.setName((String) objects[1]);
				product.setSortName((String) objects[2]);
				product.setStoreId((String) objects[3]);

				product.setUnitPrice((BigDecimal) objects[4]);
				product.setListPrice((BigDecimal) objects[5]);

				product.setQuantity((Integer) objects[6]);
				product.setBrand((String) objects[7]);
				product.setPicture((String) objects[8]);
				product.setBigPicture((String) objects[9]);
				product.setCenterPicture((String) objects[10]);
				product.setSmallPicture((String) objects[11]);
				product.setDescription((String) objects[12]);
				product.setNote((String) objects[13]);
				product.setTemplateId((String) objects[14]);
				product.setStatus((Boolean) objects[15]);
				product.setCheckStatus((Integer) objects[16]);
				product.setCheckMan((String) objects[17]);
				product.setCheckTime((String) objects[18]);
				product.setCheckNote((String) objects[19]);
				product.setSearchKey((String) objects[20]);
				product.setVisitCount((Integer) objects[21]);
				product.setBuyCount((Integer) objects[22]);
				product.setMainCategory((String) objects[23]);
				product.setProductNumber((String) objects[24]);
				product.setFare((BigDecimal) objects[48]);
				// store部分
				store.setId((String) objects[25]);
				store.setStoreName((String) objects[26]);
				store.setStoreLogo((String) objects[27]);
				//store.setLevelId((String) objects[28]);
				store.setProvince((String) objects[29]);
				store.setCity((String) objects[30]);
				store.setArea((String) objects[31]);
				store.setAddress((String) objects[32]);
				//store.setStatus((Boolean) objects[33]);
				//store.setCheckStatus((Integer) objects[34]);
				//store.setCheckMan((String) objects[35]);
				//store.setCheckTime((String) objects[36]);
				//store.setCheckNote((String) objects[37]);
				store.setDomain((String) objects[38]);
				store.setOrgId((String) objects[39]);
				store.setCreateTime((String) objects[40]);
				store.setUpdateTime((String) objects[41]);
				store.setNotes((String) objects[42]);
				store.setRoleId((String) objects[43]);
				store.setCoordinateX((String) objects[44]);
				store.setCoordinateY((String) objects[45]);
				store.setWarningMobile((String) objects[46]);
				store.setWarningEmail((String) objects[47]);

				objArray[0] = product;
				objArray[1] = store;

				// 返回基本商品详情基本信息数组
				return objArray;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
		*/
	}

	/**
	 * 
	 * @Title: getProductDetailPartOneTwo
	 * @Description: TODO(查询出商品扩展字段信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productDetailDao2
	 * @param @param log2
	 * @param @param proId
	 * @param @return 设定文件
	 * @return List<ProductInfo> 返回类型
	 * @throws
	 */
	private List<ProductInfo> getProductInfo(String proId) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select pi");
			hql.append(" from ProductInfo as pi where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and pi.productId = :proId");
			params.put("proId", proId);
			// 按position(显示位置)升序排序
			hql.append(" order by pi.position ASC");

			List list = productDetailDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<ProductInfo> productInfos = new ArrayList<ProductInfo>();
				for (int i = 0; i < list.size(); i++) {
					ProductInfo pInfo = (ProductInfo) list.get(i);

					// 如果为可选字段,还需对其字段值进行拆分
					String infoType = pInfo.getInfoType();
					if ("1".equals(infoType)) {
						// 拆分得到infoValue的值
						String[] infoValues = pInfo.getInfoValue().split(";");
						// 定义infoValueList
						List<String> infoValueList = new ArrayList<String>();
						// 定义图片List
						List<String> picList = new ArrayList<String>();
						if (infoValues != null && infoValues.length > 0) {
							for (int j = 0; j < infoValues.length; j++) {
								// 根据infoValue查询对应的图片
								String pic = getinfoPicbyInfoValue(infoValues[j], String.valueOf(pInfo.getPosition()), pInfo.getProductId());
								// 添加value到List
								infoValueList.add(infoValues[j]);
								// 添加图片到List
								picList.add(pic);
							}
						}
						// 将拆分好的value集合放入pInfo
						pInfo.setInfoValueList(infoValueList);
						// 将图片放入pInfo
						pInfo.setPicList(picList);
					}

					productInfos.add(pInfo);
				}

				return productInfos;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: getinfoPicbyInfoValue
	 * @Description: 得到商品模板图片 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param value
	 *            productInfo值
	 * @param position
	 *            productInfo排序
	 * @param id
	 *            商品id
	 * @return String 返回类型
	 * @throws
	 */
	private String getinfoPicbyInfoValue(String value, String position, String id) {
		// 如果value或者position为空，返回null
		if (StringUtil.isEmpty(position) || StringUtil.isEmpty(value)) {
			return null;
		}
		// 定义hql语句
		StringBuffer hql = new StringBuffer("select");
		// 定义参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// 根据排序位置确定查询字段和条件
		String hql1 = " pid.infoPic" + position;
		String hql2 = " and pid.infoValue" + position;
		hql.append(hql1);
		hql.append(" from ProductInfoDetail as pid where 1=1");
		hql.append(hql2);
		hql.append(" = :value");
		hql.append(" and pid.productId = :id");
		params.put("value", value);
		params.put("id", id);
		try {
			// 调用dao查询
			List<String> pic = productDetailDao.list(hql.toString(), params);
			// 若有查询结果则返回
			if (pic != null && pic.size() > 0) {
				return pic.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: getProductInfoDetail
	 * @Description: TODO(查询出可选扩展字段商品的详情信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productDetailDao2
	 * @param @param log2
	 * @param @param proInfoId
	 * @param @param position
	 * @param @return 设定文件
	 * @return List<ProductInfoDetail> 返回类型
	 * @throws
	 */
	private List<ProductInfoDetail> getProductInfoDetail(String proId) {
		try {
			// 如果需要的条件不存在,直接返回null,避免创建对象影响性能
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select pid");
			hql.append(" from ProductInfoDetail as pid where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and pid.productId = :proId");
			params.put("proId", proId);
			// 查询出结果、并将结果集返回
			List<ProductInfoDetail> details = productDetailDao.list(hql.toString(), params);
			if (details != null && details.size() > 0) {
				return details;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 加入购物车
	 */
	@Override
	public boolean addShoppingCart(String proId, String userId) {
		try {
			if (StringUtil.isEmpty(proId) || StringUtil.isEmpty(userId)) {
				return false;
			}
			// 为购物车新增一条记录
			ShopCart shopCart = new ShopCart();
			shopCart.setProductId(proId);
			shopCart.setUserId(userId);
			shopCart.setProductQuantity(1);
			shopCartDao.addBean(shopCart);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 立即结算单间商品
	 */
	@Override
	public boolean buyNow(String userId, String proId) {
		try {
			if (StringUtil.isEmpty(proId) || StringUtil.isEmpty(userId)) {
				return false;
			}
			// 为购物车新增一条记录
			ShopCart shopCart = new ShopCart();
			shopCart.setProductId(proId);
			shopCart.setUserId(userId);
			shopCart.setProductQuantity(1);
			// 查询出其他为选中状态的商品,设置其他商品为非选中状态
			List<ShopCart> shopCarts = getCheckedShopCart();
			if (shopCarts != null && shopCarts.size() > 0) {
				for (int i = 0; i < shopCarts.size(); i++) {
					ShopCart sCart = shopCarts.get(i);
					sCart.setChecked(false);
					shopCartDao.updateBean(sCart);
				}
			}
			// 设置当前商品为立即结算状态(即选中)
			shopCart.setChecked(true);

			shopCartDao.addBean(shopCart);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @Title: getCheckedShopCart
	 * @Description: TODO(查询出选中结算的商品) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param myShopCartDao
	 * @param @param log
	 * @param @return 设定文件
	 * @return List<ShopCart> 返回类型
	 * @throws
	 */
	private List<ShopCart> getCheckedShopCart() {
		try {
			StringBuffer hql = new StringBuffer("select sc from ShopCart as sc where 1=1");
			hql.append(" and sc.checked=true");

			return shopCartDao.list(hql.toString(), null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询出该件商品的所有评价信息
	 */
	@Override
	public List<Appraise> getAppraiseByProId(String proId, String apType, Pager pager) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select a from Appraise as a where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and a.productId = :proId");
			params.put("proId", proId);
			// 添加状态 好评、中评、差评
			if (!StringUtil.isEmpty(apType)) {
				hql.append(" and a.type = :apType");
				params.put("apType", apType);
			}
			// 添加排序
			hql.append(" order by a.createTime DESC");

			List<Appraise> appraises = productDetailDao.list(pager, hql.toString(), params);
			if (appraises != null && appraises.size() > 0) {
				return appraises;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 商品的成交记录
	 */
	@Override
	public List<OrderDetail> getProductDealRecord(String proId, Pager pager) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select u.nickName,p.name,od.totalPrice,od.quantity,o.createTime,u.headPortrait ");
			hql.append(" from OrderDetail as od,Order as o,User as u,Product as p where 1=1");
			hql.append(" and od.orderId = o.id");
			hql.append(" and od.userId = u.id");
			hql.append(" and od.productId = p.id");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and od.productId = :proId");
			params.put("proId", proId);
			// 只查询该商品所属订单为完成状态的商品
			hql.append(" and o.status=5");

			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			String newMonth = month + "";
			if (month < 10) {
				newMonth = "0" + newMonth;
			}
			String beginDate = year + "-" + newMonth + "-" + "01" + " " + "00:00:00";
			String endDate = year + "-" + newMonth + "-" + maxDate + " " + "24:00:00";
			// 只查询当前一个月成交的
			hql.append(" and o.createTime >= :beginDate");
			params.put("beginDate", beginDate);
			hql.append(" and o.createTime <= :endDate");
			params.put("endDate", endDate);
			// 添加排序
			hql.append(" order by o.createTime DESC");
			List list = productDetailDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
				for (int i = 0; i < list.size(); i++) {
					OrderDetail oDetail = new OrderDetail();
					Object[] objects = (Object[]) list.get(i);
					oDetail.setUserName((String) objects[0]);
					oDetail.setProductName((String) objects[1]);
					oDetail.setTotalPrice((BigDecimal) objects[2]);
					oDetail.setQuantity((Integer) objects[3]);
					oDetail.setDealTime((String) objects[4]);
					oDetail.setHeadPhoto((String) objects[5]);
					orderDetails.add(oDetail);
				}
				// 返回结果集
				return orderDetails;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 当前商品总评价数统计
	 */
	@Override
	public String getAppraiseSum(String proId) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select count(a.id) as psum from Appraise as a where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and a.productId = :proId");
			params.put("proId", proId);

			List list = productDetailDao.list(hql.toString(), params);

			if (list != null && list.size() == 1) {
				Object object = list.get(0);
				return object.toString();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 当前商品当前月的总成交数统计
	 */
	@Override
	public String getProductDealRecordSum(String proId) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select count(od.id)");
			hql.append(" from OrderDetail as od,Order as o,User as u,Product as p where 1=1");
			hql.append(" and od.orderId = o.id");
			hql.append(" and od.userId = u.id");
			hql.append(" and od.productId = p.id");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and od.productId = :proId");
			params.put("proId", proId);
			// 只查询该商品所属订单为完成状态的商品
			hql.append(" and o.status=5");

			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			String newMonth = month + "";
			if (month < 10) {
				newMonth = "0" + newMonth;
			}
			String beginDate = year + "-" + newMonth + "-" + "01" + " " + "00:00:00";
			String endDate = year + "-" + newMonth + "-" + maxDate + " " + "24:00:00";
			// 只查询当前一个月成交的
			hql.append(" and o.createTime >= :beginDate");
			params.put("beginDate", beginDate);
			hql.append(" and o.createTime <= :endDate");
			params.put("endDate", endDate);

			List list = productDetailDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				Object object = list.get(0);
				return object.toString();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询库存
	 */
	@Override
	public String getRepertory(String proId) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer();
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append("select p.quantity");
			hql.append(" from Product as p where 1=1");
			hql.append(" and p.id = :proId");
			params.put("proId", proId);

			List list = productDetailDao.list(hql.toString(), params);
			if (list != null && list.size() == 1) {
				Object object = list.get(0);
				return object.toString();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获得infoDetail
	 */
	@Override
	public ProductInfoDetail getProInfoDetailByCondition(String proId, String info1NameAndValue, String info2NameAndValue, String info3NameAndValue, String info4NameAndValue) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer();
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append("select pid");
			hql.append(" from ProductInfoDetail as pid where 1=1");
			hql.append(" and pid.productId = :proId");
			params.put("proId", proId);

			// 添加模板条件(如:尺码,颜色等) 模板条件这块,如果选择了模板条件,那么本商品应有模板都必须有对应的选择值
			if (!StringUtil.isEmpty(info1NameAndValue)) {
				String[] info1Array = info1NameAndValue.split("/");
				String info1Name = info1Array[0];
				String info1Value = info1Array[1];
				hql.append(" and pid.infoName1 = :info1Name");
				params.put("info1Name", info1Name);
				hql.append(" and pid.infoValue1 = :info1Value");
				params.put("info1Value", info1Value);
			}
			if (!StringUtil.isEmpty(info2NameAndValue)) {
				String[] info2Array = info2NameAndValue.split("/");
				String info2Name = info2Array[0];
				String info2Value = info2Array[1];
				hql.append(" and pid.infoName2 = :info2Name");
				params.put("info2Name", info2Name);
				hql.append(" and pid.infoValue2 = :info2Value");
				params.put("info2Value", info2Value);
			}
			if (!StringUtil.isEmpty(info3NameAndValue)) {
				String[] info3Array = info3NameAndValue.split("/");
				String info3Name = info3Array[0];
				String info3Value = info3Array[1];
				hql.append(" and pid.infoName3 = :info3Name");
				params.put("info3Name", info3Name);
				hql.append(" and pid.infoValue3 = :info3Value");
				params.put("info3Value", info3Value);
			}
			if (!StringUtil.isEmpty(info4NameAndValue)) {
				String[] info4Array = info4NameAndValue.split("/");
				String info4Name = info4Array[0];
				String info4Value = info4Array[1];
				hql.append(" and pid.infoName4 = :info4Name");
				params.put("info4Name", info4Name);
				hql.append(" and pid.infoValue4 = :info4Value");
				params.put("info4Value", info4Value);
			}

			List<ProductInfoDetail> list = productDetailDao.list(hql.toString(), params);

			if (list != null && list.size() == 1) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取商品的多角度视图
	 * 
	 * @param proId 商品id
	 * @return List<ProductPicture>
	 */
	@Override
	public List<ProductPicture> getProPicture(String proId) {
		if (StringUtil.isEmpty(proId)) {
			return null;
		}
		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		//String hql = "select p,pp from Product p,ProductPicture pp where p.id=pp.productId and p.id=:proId";
		String hql = "from ProductPicture pp where pp.productId=:proId";
		// setting data
		params.put("proId", proId);
		
		try {
			/*
			List<Object[]> list = this.productDetailDao.list(hql.toString(), params);
			if(list != null && list.size() > 0){
				List<ProductPicture> pplist = new ArrayList<ProductPicture>();
				for (Object[] obj : list) {
					ProductPicture pp = (ProductPicture)obj[1];
					pp.setProduct((Product)obj[0]);
					
					pplist.add(pp);
				}
				
				return pplist;
			}
			*/
			return this.productDetailDao.list(hql.toString(), params);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
		
		/*
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			// 定义存储图片路径的集合
			List<ProductPicture> pictures = new ArrayList<ProductPicture>();

			// 第一部分查询出,Product的图片 不且为第一个展示
			StringBuffer hql = new StringBuffer("select p from Product as p where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and p.id = :proId");
			params.put("proId", proId);
			List<Product> ps = productDetailDao.list(hql.toString(), params);
			if (ps != null && ps.size() == 1) {
				ProductPicture pp = new ProductPicture();
				Product p = ps.get(0);
				pp.setProductId(p.getId());
				pp.setPicture(p.getPicture());
				pp.setBigPicture(p.getBigPicture());
				pp.setMidPicture(p.getCenterPicture());
				pp.setSmallPicture(p.getSmallPicture());
				pp.setPosition("0");// 设置显示第一个, ProductPicture的商品图片显示位置记得从"1"开始
				pictures.add(pp);
			}

			// 第二部分,查询出多角度视图
			StringBuffer ppHql = new StringBuffer("select pp from ProductPicture as pp where 1=1");
			Map<String, Object> ppParams = new HashMap<String, Object>();
			ppHql.append(" and pp.productId = :proId");
			ppParams.put("proId", proId);
			hql.append(" order by pp.position ASC");
			List<ProductPicture> pps = productDetailDao.list(ppHql.toString(), ppParams);
			if (pps != null && pps.size() > 0) {
				for (int i = 0; i < pps.size(); i++) {
					pictures.add(pps.get(i));
				}
			}
			// 最后返回多角度视图
			return pictures;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
		*/
	}

	/**
	 * 获取店铺的服务集合
	 */
	public List<StoreServiceVo> getServiceByServiceName(String storeId) {
		/*
		try {
			Store store = (Store) storeDao.get(storeId);
			if (store != null) {
				String serviceName = store.getServiceName();
				if (!StringUtil.isEmpty(serviceName)) {
					String[] serviceArray = serviceName.split(";");
					List<StoreServiceVo> ssvs = new ArrayList<StoreServiceVo>();
					ResourceBundleUtil r = new ResourceBundleUtil();
					for (int i = 0; i < serviceArray.length; i++) {
						String pkey = serviceArray[i];
						String pvalue = r.getProperty(pkey);
						if (pvalue != null) {
							StoreServiceVo ssv = new StoreServiceVo();
							String[] valueArray = pvalue.split(";");
							ssv.setTitleName(valueArray[0]);
							ssv.setTitleImg(valueArray[1]);
							ssvs.add(ssv);
						}
					}
					return ssvs;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}*/
		return null;
	}

}
