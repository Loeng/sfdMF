package com.ekfans.base.wfOrder.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.ekfans.base.wfOrder.dao.IContractCarsDao;
import com.ekfans.base.wfOrder.dao.IContractContentDao;
import com.ekfans.base.wfOrder.dao.IContractDetailsDao;
import com.ekfans.base.wfOrder.dao.IContractRelDao;
import com.ekfans.base.wfOrder.model.ContractCars;
import com.ekfans.base.wfOrder.model.ContractContent;
import com.ekfans.base.wfOrder.model.ContractDetails;
import com.ekfans.base.wfOrder.model.ContractRel;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 合同帮助类
 * 
 * @author liuguoyu
 * 
 */
public class ContractHelper {
	/**
	 * 新增或编辑合同的含量信息
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	public static boolean saveOrUpdateContractContents(boolean isUpdate, String contractId, Session session, HttpServletRequest request) {
		// 从页面获取合同的含量信息集合
		String[] contentValues = request.getParameterValues("contentValues");
		// 如果获取的含量集合为空或者长度小于1，则表示不需要操作，直接返回成功
		if (contentValues == null || contentValues.length <= 0) {
			return true;
		}

		try {
			IContractContentDao contentDao = SpringContextHolder.getBean(IContractContentDao.class);
			IContractDetailsDao detailsDao = SpringContextHolder.getBean(IContractDetailsDao.class);

			if (isUpdate) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer contentSql = new StringBuffer("from ContractContent as cc where 1=1");
				contentSql.append(" and cc.contractId = :contractId");

				StringBuffer detailsSql = new StringBuffer("from ContractDetails as cd where 1=1");
				detailsSql.append(" and cd.contractId = :contractId");
				paramMap.put("contractId", contractId);

				if (session != null) {
					contentDao.delete(contentSql.toString(), paramMap, session);
					detailsDao.delete(detailsSql.toString(), paramMap, session);
				} else {
					contentDao.delete(contentSql.toString(), paramMap);
					detailsDao.delete(detailsSql.toString(), paramMap);
				}
			}

			// 循环获取的集合
			for (String contentValue : contentValues) {
				// 如果循环出来的值为空，则跳过
				if (StringUtil.isEmpty(contentValue)) {
					continue;
				}

				// 以分号分割字符串
				String[] values = contentValue.split(";");
				// 获取分割后的第一组，含量名称
				String name = values[0];
				// 如果获取的名称为空，则跳过该条循环
				if (StringUtil.isEmpty(name)) {
					continue;
				}
				// 计价类型
				String chargingType = values[1];
				// 计价单位
				String chargingUnit = values[2];
				// 获取价格区间串
				String priceTotalStr = values[3];
				// 获取类型
				String type = values[4];

				// 定义一个合同对象
				ContractContent content = new ContractContent();
				// 设置合同ID
				content.setContractId(contractId);
				// 设置含量元素名称
				content.setName(name);
				// 设置含量计价类型
				content.setChargingType(chargingType);
				// 设置含量计价单位
				content.setChargingUnit("1".equals(chargingType) ? chargingUnit : "");
				// 设置含量元素类型
				content.setType("1".equals(type) ? true : false);
				// 设置创建时间
				content.setCreateTime(DateUtil.getSysDateTimeString());
				// 设置更新时间
				content.setUpdateTime(DateUtil.getSysDateTimeString());

				// 如果获取的价格区间串为空，则跳过
				if (StringUtil.isEmpty(priceTotalStr)) {
					continue;
				}
				priceTotalStr = priceTotalStr.replaceAll("\\{", "");
				priceTotalStr = priceTotalStr.replaceAll("\\}", "");
				// 以竖线分割价格字符串
				String[] prices = priceTotalStr.split("\\|");
				// 如果分割出来的数组为空，或者长度为0，则跳过
				if (prices == null || prices.length <= 0) {
					continue;
				}

				boolean insertStatus = false;

				// 循环数组
				for (String priceStr : prices) {
					// 如果字符串为空，则跳过
					if (StringUtil.isEmpty(priceStr)) {
						continue;
					}
					// 以逗号分割字符串
					String[] priceValues = priceStr.split(",");
					// 获取分割后的 含量系数开始
					String startRegion = priceValues[0];
					// 获取分割后的 含量系数结束
					String endRegion = priceValues[1];
					// 获取分割后的 付费价格
					String price = priceValues[2];
					if (StringUtil.isEmpty(startRegion) && StringUtil.isEmpty(endRegion) && StringUtil.isEmpty(price) || "0".equals(startRegion) && "0".equals(endRegion) && "0".equals(price)) {
						continue;
					} else {
						insertStatus = true;
						break;
					}
				}
				if (insertStatus) {
					// 调用事物保持对象
					if (session != null) {
						contentDao.addBean(content, session);
					} else {
						contentDao.addBean(content);
					}
				}

				// 循环数组
				for (String priceStr : prices) {
					// 如果字符串为空，则跳过
					if (StringUtil.isEmpty(priceStr)) {
						continue;
					}
					// 以逗号分割字符串
					String[] priceValues = priceStr.split(",");
					// 获取分割后的 含量系数开始
					String startRegion = priceValues[0];
					// 获取分割后的 含量系数结束
					String endRegion = priceValues[1];
					// 获取分割后的 付费价格
					String price = priceValues[2];
					// 获取分割后的 付费类型
					String ffType = priceValues[3];
					// 如果开始和结束的含量系数以及价格都为空，或者都为0，则不保存数据库
					if (StringUtil.isEmpty(startRegion) && StringUtil.isEmpty(endRegion) && StringUtil.isEmpty(price) || "0".equals(startRegion) && "0".equals(endRegion) && "0".equals(price)) {
						continue;
					}
					// 定义一个新的明细对象
					ContractDetails detail = new ContractDetails();
					// 设置微量元素名称
					detail.setContentName(name);
					// 设置含量ID
					detail.setContractContentId(content.getId());
					// 设置合同ID
					detail.setContractId(contractId);
					// 设置开始系数
					Double sr = 0.00;
					Double er = 0.00;
					BigDecimal pr = new BigDecimal(0);
					try {
						if (StringUtil.isEmpty(startRegion)) {
							sr = 0.00;
						} else {
							sr = Double.parseDouble(startRegion);
						}
						if (StringUtil.isEmpty(endRegion)) {
							er = 0.00;
						} else {
							er = Double.parseDouble(endRegion);
							if (er <= 0.00) {
								er = 0.00;
							}
						}
						pr = new BigDecimal(price);
					} catch (Exception e) {
						// TODO: handle exception
					}
					detail.setStartRegion(sr);
					// 设置结束系数

					detail.setEndRegion(er);
					// 设置价格
					detail.setPrice(pr);
					// 设置创建时间
					detail.setCreateTime(DateUtil.getSysDateTimeString());
					// 设置付费类型
					detail.setType("付费".equals(ffType) ? "1" : "0");
					if (session != null) {
						detailsDao.addBean(detail, session);
					} else {
						detailsDao.addBean(detail);
					}
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 新增或编辑合同的车辆计费信息
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	public static boolean saveOrUpdateContractCars(boolean isUpdate, String contractId, Session session, HttpServletRequest request) {
		String[] selectIds = request.getParameterValues("checkbox1");
		if (selectIds == null) {
			return false;
		}
		IContractCarsDao contractCarsDao = SpringContextHolder.getBean(IContractCarsDao.class);

		try {
			if (isUpdate) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer("from ContractCars as cc where 1=1");
				sql.append(" and cc.contractId = :contractId");
				paramMap.put("contractId", contractId);
				if (session != null) {
					contractCarsDao.delete(sql.toString(), paramMap, session);
				} else {
					contractCarsDao.delete(sql.toString(), paramMap);
				}
			}

			for (String carId : selectIds) {
				if (StringUtil.isEmpty(carId) || "true".equals(carId)) {
					continue;
				}
				String priceStr = request.getParameter(carId + "weight");
				ContractCars cars = new ContractCars();
				cars.setCarInfoId(carId);
				cars.setContractId(contractId);
				try {
					cars.setPrice(new BigDecimal(priceStr));
				} catch (Exception e) {
					// TODO: handle exception
				}
				cars.setCreateTime(DateUtil.getSysDateTimeString());
				cars.setUpdateTime(DateUtil.getSysDateTimeString());
				if (session != null) {
					contractCarsDao.addBean(cars, session);
				} else {
					contractCarsDao.addBean(cars);
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	/**
	 * 新增或编辑合同的车辆计费信息
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	public static boolean saveOrUpdateContractRels(boolean isUpdate, String contractId, Session session, HttpServletRequest request, IContractRelDao relDao) {
		String ysContracts = request.getParameter("ysContracts");
		if (StringUtil.isEmpty(ysContracts)) {
			return true;
		}
		if (relDao == null) {
			relDao = SpringContextHolder.getBean(IContractRelDao.class);
		}
		try {
			if (isUpdate) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer("from ContractRel as cr where 1=1");
				sql.append(" and cr.contractId = :contractId");
				paramMap.put("contractId", contractId);
				if (session != null) {
					relDao.delete(sql.toString(), paramMap, session);
				} else {
					relDao.delete(sql.toString(), paramMap);
				}
			}

			String[] cons = ysContracts.split(";");
			for (String con : cons) {
				if (StringUtil.isEmpty(con) || con.indexOf("_") == -1) {
					continue;
				}

				String[] conVals = con.split("\\_");
				if (conVals != null && conVals.length > 0) {
					ContractRel rel = new ContractRel();
					rel.setContractId(contractId);
					rel.setYsContractId(conVals[0]);
					rel.setYsStoreId(conVals[2]);
					if (session != null) {
						relDao.addBean(rel, session);
					} else {
						relDao.addBean(rel);
					}
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	public static void main(String[] args) {
		String aa = "aa_bb_cc;ab_bc_cd;ad_bd_ce;";
		String[] cons = aa.split(";");
		for (String con : cons) {
			if (StringUtil.isEmpty(con) || con.indexOf("_") == -1) {
				continue;
			}

			String[] conVals = con.split("\\_");
			System.out.println(conVals[0]);
		}

	}
}
