package com.ekfans.base.wfOrder.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.wfOrder.dao.IContractDao;
import com.ekfans.base.wfOrder.dao.IContractDetailsDao;
import com.ekfans.base.wfOrder.dao.IWfOrderDetailDao;
import com.ekfans.base.wfOrder.dao.IWfOrderPriceDao;
import com.ekfans.base.wfOrder.dao.IWfOrderTransDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ContractDetails;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderDetail;
import com.ekfans.base.wfOrder.model.WfOrderPrice;
import com.ekfans.base.wfOrder.model.WfOrderTrans;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

public class WfOrderHelper {
	// 待确认
	public static final String WFORDER_STATUS_WAIT_SURE = "00";
	// 待物流企业确认
	public static final String WFORDER_STATUS_WAIT_TRANS_SURE = "01";
	// 待支付预付金
	public static final String WFORDER_STATUS_WAIT_PAY_YF = "02";
	// 预付金已支付待确定收款预付金
	public static final String WFORDER_STATUS_WAIT_PAYYF_SURE = "11";
	// 待发货
	public static final String WFORDER_STATUS_WAIT_FH = "03";
	// 待收货
	public static final String WFORDER_STATUS_WAIT_SH = "04";
	// 待上传品味
	public static final String WFORDER_STATUS_WAIT_PW = "05";
	// 待确认最终品味
	public static final String WFORDER_STATUS_WAIT_SURE_PW = "06";
	// 待确认最终价格
	public static final String WFORDER_STATUS_WAIT_SURE_PRICE = "07";
	// 待最终结算
	public static final String WFORDER_STATUS_WAIT_PAY = "08";
	// 结算已完成,待确认结算完成
	public static final String WFORDER_STATUS_WAIT_PAY_SURE = "12";
	// 订单完成
	public static final String WFORDER_STATUS_WAIT_FINISH = "09";
	// 订单无效
	public static final String WFORDER_STATUS_FAILD = "10";

	public static Map<String, String> orderStatusMap = new HashMap<String, String>();
	static {
		orderStatusMap.put(WFORDER_STATUS_WAIT_SURE, "待产生方确认订单信息");
		orderStatusMap.put(WFORDER_STATUS_WAIT_TRANS_SURE, "产生方已确认,待物流企业确认订单信息");
		orderStatusMap.put(WFORDER_STATUS_WAIT_PAY_YF, "承运方已确认,待支付预付金");
		orderStatusMap.put(WFORDER_STATUS_WAIT_PAYYF_SURE, "预付金已支付,待收款确认");
		orderStatusMap.put(WFORDER_STATUS_WAIT_FH, "待产生方确认发货");
		orderStatusMap.put(WFORDER_STATUS_WAIT_SH, "产生方已发货,待处置方确认收货");
		orderStatusMap.put(WFORDER_STATUS_WAIT_PW, "待处置方上传最终品位信息");
		orderStatusMap.put(WFORDER_STATUS_WAIT_SURE_PW, "待产生方确认最终品位信息");
		orderStatusMap.put(WFORDER_STATUS_WAIT_SURE_PRICE, "待双方确认最终订单价格");
		orderStatusMap.put(WFORDER_STATUS_WAIT_PAY, "待最终结算");
		orderStatusMap.put(WFORDER_STATUS_WAIT_PAY_SURE, "已结算,待确定结算款项到账");
		orderStatusMap.put(WFORDER_STATUS_WAIT_FINISH, "订单已完成");
		orderStatusMap.put(WFORDER_STATUS_FAILD, "订单已取消");

	}

	// 订单余额类型 - 申请退款
	public static final String WFORDER_LIST_PRICE_STATUS_SHENQ = "1";
	// 订单余额类型 - 申请退款-拒绝
	public static final String WFORDER_LIST_PRICE_STATUS_SHENQ_JJ = "2";
	// 订单余额类型 - 申请退款-待退款
	public static final String WFORDER_LIST_PRICE_STATUS_SHENQ_WAIT = "3";

	public static boolean checkOrderView(String orderStatus) {
		if (StringUtil.isEmpty(orderStatus)) {
			return false;
		}
		if (WFORDER_STATUS_WAIT_PW.equals(orderStatus) || WFORDER_STATUS_WAIT_SURE_PW.equals(orderStatus) || WFORDER_STATUS_WAIT_SURE_PRICE.equals(orderStatus) || WFORDER_STATUS_WAIT_PAY.equals(orderStatus) || WFORDER_STATUS_WAIT_FINISH.equals(orderStatus)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 计算订单品味价格等数据
	 * 
	 * @param order
	 * @param request
	 * @param response
	 * @return
	 */
	public static List<WfOrderDetail> computation(WfOrder order, HttpServletRequest request, HttpServletResponse response) {
		if (order == null) {
			return null;
		}
		List<WfOrderDetail> detailList = new LinkedList<WfOrderDetail>();
		try {

			IContractDao contractDao = SpringContextHolder.getBean(IContractDao.class);
			Contract contract = (Contract) contractDao.get(order.getContractId());
			if (contract == null) {
				return null;
			}

			String priceIds = request.getParameter("priceIds");
			String contentIds = request.getParameter("contentIds");
			if (StringUtil.isEmpty(priceIds)) {
				return null;
			}

			String[] priceIdArray = priceIds.split(";");
			if (priceIdArray == null || priceIdArray.length <= 0) {
				return null;
			}

			for (int i = 0; i < priceIdArray.length; i++) {
				String priceId = priceIdArray[i];
				if (StringUtil.isEmpty(priceId)) {
					continue;
				}

				WfOrderDetail detail = new WfOrderDetail();

				// 获取录入的重量
				String weightStr = request.getParameter(priceId + "Weight");
				String moistureContentStr = request.getParameter(priceId + "MoistureContent");
				String testReport = FileUploadHelper.getUploadFileUrl(priceId + "TestReport", request);
				BigDecimal weight = new BigDecimal(weightStr);
				BigDecimal moistureContent = new BigDecimal(moistureContentStr);

				detail.setWeight(weight);
				detail.setMoistureContent(moistureContent);
				detail.setTestReport(testReport);
				BigDecimal dryWeight = weight.multiply(new BigDecimal(1).subtract(moistureContent.divide(new BigDecimal(100))));
				detail.setDryWeight(dryWeight);

				List<WfOrderPrice> prices = new LinkedList<WfOrderPrice>();
				if (!order.isCountType()) {
					String[] contentArray = contentIds.split(";");
					// 含量品味计价
					for (int j = 0; j < contentArray.length; j++) {
						// 元素ID
						String contentId = contentArray[j];
						// 元素名称
						String contentName = request.getParameter(priceId + contentId + "ContentName");
						// 元素品味
						String contentStr = request.getParameter(priceId + contentId + "Content");
						// 元素网上均价
						String contentPriceStr = request.getParameter(priceId + contentId + "ContentPrice");
						// 计价类型
						String chargingType = request.getParameter(priceId + contentId + "ChargingType");
						// 计价单位
						String chargingUnit = request.getParameter(priceId + contentId + "ChargingUnit");

						WfOrderPrice price = new WfOrderPrice();
						BigDecimal content = new BigDecimal(contentStr);
						BigDecimal contentPrice = new BigDecimal(contentPriceStr);
						ContractDetails cd = getDetailByContent(contentId, content.doubleValue(), null);

						// 计算出金属量
						Double contentQuantity = 0.00;
						if ("0".equals(chargingType)) {
							contentQuantity = dryWeight.multiply(content.divide(new BigDecimal(100))).doubleValue();
						} else {
							contentQuantity = dryWeight.multiply(content).doubleValue();
						}

						if (cd != null) {
							// 计价系数
							price.setCoefficient(cd.getPrice().doubleValue());
							BigDecimal finalPrice = contentPrice.multiply(cd.getPrice().divide(new BigDecimal(100)));

							if (contract.getCreatorId().equals(order.getBuyId())) {
								if ("0".equals(cd.getType())) {
									// 对卖家来说就是付费
									finalPrice = new BigDecimal(0).subtract(finalPrice);
								}
							} else {
								if ("1".equals(cd.getType())) {
									// 对卖家来说就是付费
									finalPrice = new BigDecimal(0).subtract(finalPrice);
								}
							}

							BigDecimal conTotal = new BigDecimal(contentQuantity).multiply(finalPrice);
							// 单价
							price.setFinalPrice(finalPrice);
							// 总价
							price.setContentTotalPrice(conTotal);

						}

						// 元素名称
						price.setContentName(contentName);
						// 合同品味ID
						price.setContractContentId(contentId);
						// 含量
						price.setContent(content.doubleValue());
						// 金属量
						price.setContentQuantity(contentQuantity);
						// 均价
						price.setContentPrice(contentPrice);
						// 计价类型
						price.setChargingType(chargingType);
						// 计价单位
						price.setChargingUnit(chargingUnit);
						prices.add(price);
					}
					detail.setPrices(prices);
				} else {
					// 一口价计价
					detail.setPrice(new BigDecimal(contract.getYkjPrice()));
					detail.setTotalPrice(new BigDecimal(contract.getYkjPrice()).multiply(detail.getDryWeight()));
				}

				detailList.add(detail);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return detailList;
	}

	/**
	 * 根据合同含量ID和含量获取含量对象
	 * 
	 * @param contentId
	 * @param content
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ContractDetails getDetailByContent(String contentId, Double content, Session session) {
		if (StringUtil.isEmpty(contentId)) {
			return null;
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer("select cd from ContractDetails as cd where 1=1");
			sql.append(" and cd.contractContentId = :contractContentId");
			paramMap.put("contractContentId", contentId);
			sql.append(" and ((cd.startRegion <= ").append(content).append(" and cd.endRegion > ").append(content).append(")");
			sql.append(" or (cd.startRegion <= 0.00 and cd.endRegion > ").append(content).append(")");
			sql.append(" or (cd.startRegion <= ").append(content).append(" and cd.endRegion <= 0.00)");
			sql.append("))");
			sql.append(" order by cd.price desc");
			IContractDetailsDao contractDetailsDao = SpringContextHolder.getBean(IContractDetailsDao.class);

			List<ContractDetails> details = null;
			if (session != null) {
				details = contractDetailsDao.list(sql.toString(), paramMap, session);
			} else {
				details = contractDetailsDao.list(sql.toString(), paramMap);
			}
			if (details != null && details.size() > 0) {
				return details.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean saveOrUpdateOrderContracts(Boolean isUpdate, WfOrder wfOrder, Session session, IWfOrderPriceDao priceDao, IWfOrderDetailDao detailDao, HttpServletRequest request, HttpServletResponse response) {
		if (wfOrder == null) {
			return false;
		}

		try {
			if (priceDao == null) {
				priceDao = SpringContextHolder.getBean(IWfOrderPriceDao.class);
			}
			if (detailDao == null) {
				detailDao = SpringContextHolder.getBean(IWfOrderDetailDao.class);
			}
			if (isUpdate) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer delSql = new StringBuffer(" from WfOrderPrice as wop where 1=1");
				delSql.append(" and wop.wfOrderId = :wfOrderId");
				paramMap.put("wfOrderId", wfOrder.getId());
				if (session != null) {
					priceDao.delete(delSql.toString(), paramMap, session);
				} else {
					priceDao.delete(delSql.toString(), paramMap);
				}

				if (!wfOrder.isCountType()) {
					Map<String, Object> paramMap2 = new HashMap<String, Object>();
					StringBuffer delSql2 = new StringBuffer(" from WfOrderDetail as detail where 1=1");
					delSql2.append(" and detail.wfOrderId = :wfOrderId");
					paramMap2.put("wfOrderId", wfOrder.getId());

					if (session != null) {
						detailDao.delete(delSql2.toString(), paramMap2, session);
					} else {
						detailDao.delete(delSql2.toString(), paramMap2);
					}
				}
			}

			BigDecimal totalPrice = new BigDecimal(0);

			String priceIds = request.getParameter("priceIds");
			String contentIds = request.getParameter("contentIds");
			if (StringUtil.isEmpty(priceIds)) {
				return false;
			}

			String[] priceIdArray = priceIds.split(";");
			if (priceIdArray == null || priceIdArray.length <= 0) {
				return false;
			}

			IContractDao contractDao = SpringContextHolder.getBean(IContractDao.class);
			Contract contract = null;
			if (session != null) {
				contract = (Contract) contractDao.get(wfOrder.getContractId(), session);
			} else {
				contract = (Contract) contractDao.get(wfOrder.getContractId());
			}

			if (contract == null) {
				return false;
			}

			for (int i = 0; i < priceIdArray.length; i++) {
				String priceId = priceIdArray[i];
				if (StringUtil.isEmpty(priceId)) {
					continue;
				}

				WfOrderDetail detail = new WfOrderDetail();

				// 获取录入的重量
				String weightStr = request.getParameter(priceId + "Weight");
				// 获取水分
				String moistureContentStr = request.getParameter(priceId + "MoistureContent");
				// 获取上传的检测报告
				String currentPath = "/customerfiles/store/" + wfOrder.getBuyId() + "/wfp/" + DateUtil.getNoSpSysDateString();
				String testReport = FileUploadHelper.uploadFile(priceId + "TestReport", currentPath, request, response);

				BigDecimal weight = new BigDecimal(weightStr);
				BigDecimal moistureContent = new BigDecimal(moistureContentStr);

				detail.setWfOrderId(wfOrder.getId());
				detail.setWeight(new BigDecimal(String.format("%.4f", weight)));
				detail.setMoistureContent(new BigDecimal(String.format("%.4f", moistureContent)));
				detail.setTestReport(testReport);
				BigDecimal dryWeight = weight.multiply(new BigDecimal(1).subtract(moistureContent.divide(new BigDecimal(100))));
				detail.setDryWeight(new BigDecimal(String.format("%.4f", dryWeight)));
				detail.setPosition(i + 1);

				if (!wfOrder.isCountType()) {
					String[] contentArray = contentIds.split(";");
					if (session != null) {
						detailDao.addBean(detail, session);
					} else {
						detailDao.addBean(detail);
					}

					// 含量品味计价
					for (int j = 0; j < contentArray.length; j++) {
						// 元素ID
						String contentId = contentArray[j];
						// 元素名称
						String contentName = request.getParameter(priceId + contentId + "ContentName");
						// 元素品味
						String contentStr = request.getParameter(priceId + contentId + "Content");
						// 元素网上均价
						String contentPriceStr = request.getParameter(priceId + contentId + "ContentPrice");
						// 计价类型
						String chargingType = request.getParameter(priceId + contentId + "ChargingType");
						// 计价单位
						String chargingUnit = request.getParameter(priceId + contentId + "ChargingUnit");

						WfOrderPrice price = new WfOrderPrice();
						BigDecimal content = new BigDecimal(contentStr);
						BigDecimal contentPrice = new BigDecimal(contentPriceStr);
						ContractDetails cd = getDetailByContent(contentId, content.doubleValue(), session);

						// 计算出金属量
						Double contentQuantity = 0.00;
						if ("0".equals(chargingType)) {
							contentQuantity = dryWeight.multiply(content.divide(new BigDecimal(100))).doubleValue();
						} else {
							contentQuantity = dryWeight.multiply(content).doubleValue();
						}

						if (cd != null) {
							// 计价系数
							price.setCoefficient(cd.getPrice().doubleValue());
							BigDecimal finalPrice = new BigDecimal(String.format("%.4f", contentPrice.multiply(cd.getPrice().divide(new BigDecimal(100)))));

							if (contract.getCreatorId().equals(wfOrder.getBuyId())) {
								if ("0".equals(cd.getType())) {
									// 对卖家来说就是付费
									finalPrice = new BigDecimal(0).subtract(finalPrice);
								}
							} else {
								if ("1".equals(cd.getType())) {
									// 对卖家来说就是付费
									finalPrice = new BigDecimal(0).subtract(finalPrice);
								}
							}

							BigDecimal conTotal = new BigDecimal(String.format("%.4f", new BigDecimal(contentQuantity).multiply(finalPrice)));
							// 单价
							price.setFinalPrice(finalPrice);
							// 总价
							price.setContentTotalPrice(conTotal);
							// 计算订单总价
							totalPrice = totalPrice.add(price.getContentTotalPrice());
						}

						// 设置危废订单ID
						price.setWfOrderId(wfOrder.getId());
						// 合同品味ID
						price.setContractContentId(contentId);
						// 元素名称
						price.setContentName(contentName);
						// 含量
						price.setContent(Double.parseDouble(String.format("%.4f", content)));
						// 金属量
						price.setContentQuantity(Double.parseDouble(String.format("%.4f", contentQuantity)));
						// 均价
						price.setContentPrice(new BigDecimal(String.format("%.4f", contentPrice)));
						// 计价类型
						price.setChargingType(chargingType);
						// 计价单位
						price.setChargingUnit(chargingUnit);
						// 设置明细ID
						price.setOrderDetailId(detail.getId());
						// 创建时间
						price.setCreateTime(DateUtil.getSysDateTimeString());
						price.setPosition(j + 1);
						if (session != null) {
							priceDao.addBean(price, session);
						} else {
							priceDao.addBean(price);
						}
					}
				} else {
					// 一口价计价
					detail.setPrice(new BigDecimal(contract.getYkjPrice()));
					detail.setTotalPrice(new BigDecimal(String.format("%.4f", detail.getPrice().multiply(detail.getDryWeight()))));
					totalPrice = totalPrice.add(detail.getTotalPrice());
					if (session != null) {
						detailDao.addBean(detail, session);
					} else {
						detailDao.addBean(detail);
					}
				}

			}
			wfOrder.setTotalPrice(new BigDecimal(String.format("%.4f", totalPrice)));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public static List<WfOrderPrice> checkContracts(Boolean update, WfOrder wfOrder, List<WfOrderPrice> prices, IWfOrderPriceDao priceDao, Session session, HttpServletRequest request) {

		try {

			List<WfOrderPrice> returnPrices = new ArrayList<WfOrderPrice>();
			if (priceDao == null) {
				priceDao = SpringContextHolder.getBean(IWfOrderPriceDao.class);
			}
			Double maozhong = wfOrder.getWfpTotal();
			Double ganzhong = maozhong;
			Double totalPrice = 0.00;

			if (wfOrder.isCountType()) {
				totalPrice = totalPrice + wfOrder.getContractOriginal().doubleValue() * maozhong;
			}

			if (prices != null && prices.size() > 0) {
				for (int i = 0; i < prices.size(); i++) {
					WfOrderPrice price = prices.get(i);
					if (price == null) {
						continue;
					}
					String relContent = request.getParameter(price.getId() + "relContent");
					Double quan = price.getContent();
					if (!StringUtil.isEmpty(relContent)) {
						quan = Double.parseDouble(relContent);
					}
					ganzhong = Double.parseDouble(String.format("%.5f", maozhong - maozhong * (quan / 100)));
				}

				IContractDetailsDao contractDetailsDao = SpringContextHolder.getBean(IContractDetailsDao.class);

				for (int i = 0; i < prices.size(); i++) {
					WfOrderPrice price = prices.get(i);
					if (price == null) {
						continue;
					}
					String relContent = request.getParameter(price.getId() + "relContent");

					Double quan = price.getContent();
					if (!StringUtil.isEmpty(relContent)) {
						quan = Double.parseDouble(relContent);
					}
					Map<String, Object> paramMap = new HashMap<String, Object>();
					StringBuffer sql = new StringBuffer("select cd.price,cd.type from ContractDetails as cd,ContractContent as cc where 1=1");
					sql.append(" and cd.contractId = :contractId");
					paramMap.put("contractId", wfOrder.getContractId());
					sql.append(" and cd.contractContentId = cc.id");
					sql.append(" and cd.contractContentId = '").append(price.getContractContentId()).append("'");
					sql.append(" and ((cd.startRegion <= ").append(quan).append(" and cd.endRegion > ").append(quan).append(")");
					sql.append(" or (cd.startRegion <= 0.00 and cd.endRegion > ").append(quan).append(")");
					sql.append(" or (cd.startRegion <= ").append(quan).append(" and cd.endRegion <= 0.00))");
					sql.append(" order by cd.price desc");
					List<Object[]> details = contractDetailsDao.list(sql.toString(), paramMap);
					if (details != null && details.size() > 0) {
						Object[] obj = details.get(0);
						BigDecimal conPrice = (BigDecimal) obj[0];
						price.setContentPrice(conPrice);
						String type = obj[1].toString();
						if ("1".equals(type)) {
							conPrice = new BigDecimal(0 - conPrice.doubleValue());
						}
						Double contentQuantity = 0.00;
						if ("0".equals(price.getChargingType())) {
							contentQuantity = (new BigDecimal(ganzhong).multiply(new BigDecimal(quan)).divide(new BigDecimal(100))).doubleValue();
						} else {
							contentQuantity = (new BigDecimal(ganzhong).multiply(new BigDecimal(quan))).doubleValue();
						}
						price.setContentQuantity(Double.parseDouble(String.format("%.5f", contentQuantity)));
						totalPrice = totalPrice + contentQuantity * conPrice.doubleValue();
						price.setContentTotalPrice(new BigDecimal(String.format("%.2f", contentQuantity * conPrice.doubleValue())));
						returnPrices.add(price);
						if (update) {
							priceDao.updateBean(price, session);
						}
					}
				}
			}
			wfOrder.setRealTotalPrice(new BigDecimal(String.format("%.2f", totalPrice)));
			return returnPrices;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static boolean saveOrUpdateOrderTrans(Boolean isUpdate, String[] storeIds, WfOrder wfOrder, Session session, IWfOrderTransDao transDao, HttpServletRequest request) {
		if (wfOrder == null) {
			return false;
		}
		try {

			if (transDao == null) {
				transDao = SpringContextHolder.getBean(IWfOrderTransDao.class);
			}
			if (isUpdate) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer delSql = new StringBuffer(" from WfOrderTrans as wot where 1=1");
				delSql.append(" and wot.wfOrderId = :wfOrderId");
				paramMap.put("wfOrderId", wfOrder.getId());

				if (session != null) {
					transDao.delete(delSql.toString(), paramMap, session);
				} else {
					transDao.delete(delSql.toString(), paramMap);
				}
			}

			if (storeIds == null || storeIds.length <= 0) {
				return true;
			}

			IStoreDao storeDao = SpringContextHolder.getBean(IStoreDao.class);
			for (String storeId : storeIds) {
				if (StringUtil.isEmpty(storeId)) {
					continue;
				}
				Store s = (Store) storeDao.get(storeId, session);
				if (s == null) {
					continue;
				}
				WfOrderTrans trans = new WfOrderTrans();
				trans.setTransStoreId(storeId);
				trans.setTransStoreName(s.getStoreName());
				trans.setWfOrderId(wfOrder.getId());
				if (session != null) {
					transDao.addBean(trans, session);
					wfOrder.setStatus(WFORDER_STATUS_WAIT_SURE);
				} else {
					transDao.addBean(trans);
					wfOrder.setStatus(WFORDER_STATUS_WAIT_SURE);
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
