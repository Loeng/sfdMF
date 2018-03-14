package com.ekfans.base.wfOrder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.wfOrder.dao.ContractContentDao;
import com.ekfans.base.wfOrder.dao.ContractDao;
import com.ekfans.base.wfOrder.dao.IContractDetailsDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ContractContent;
import com.ekfans.base.wfOrder.model.ContractDetails;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class ContractContentService implements IContractContentService {
	@Autowired
	private ContractContentDao ccDao;
	@Autowired
	private ContractDao contractDao;
	@Autowired
	private IContractDetailsDao contractDetailsDao;

	@Override
	public List<ContractContent> findContractContentWithChilds(String contractId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 根据合同id查找合同含量hql
			StringBuffer hql = new StringBuffer(" from ContractContent cc where cc.contractId= :contractId order by cc.type desc");
			map.put("contractId", contractId);
			// 得到合同含量集合
			List<ContractContent> list = (List<ContractContent>) ccDao.list(hql.toString(), map);
			// 如果list里面有内容就根据合同含量id查询合同含量明细集合
			if (list != null && list.size() > 0) {
				for (ContractContent cc : list) {
					StringBuffer hql2 = new StringBuffer(" from ContractDetails cd where cd.contractContentId='" + cc.getId() + "' order by cd.startRegion asc");
					// 得到合同含量明细集合
					List<ContractDetails> cds = contractDetailsDao.list(hql2.toString(), null);
					cc.setChildList(cds);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ContractContent> findContractContent(String contractId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 根据合同id查找合同含量hql
			StringBuffer hql = new StringBuffer(" from ContractContent cc where cc.contractId= :contractId order by cc.type desc");
			map.put("contractId", contractId);
			// 得到合同含量集合
			List<ContractContent> list = (List<ContractContent>) ccDao.list(hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据合同ID，数量和品味计算单价
	 * 
	 * @param contractId
	 * @param quantity
	 * @param contents
	 * @return
	 */
	public Double sumTotalPrice(String contractId, String quantity, Map<String, Double> contents) {
		if (StringUtil.isEmpty(contractId) || StringUtil.isEmpty(quantity) || contents == null || contents.size() <= 0) {
			return 0.00;
		}

		Contract contract = null;
		// 干重 ＝ 总重量 - (总重量×水分比例)
		// 品味总重 = 干重 × 品味含量
		// 品味价格 = 品味总重 × 品味单价
		// 订单价格 = 品味价格总和

		Double quanNum = 0.00;
		Double ganzhong = 0.00;
		try {
			contract = (Contract) contractDao.get(contractId);
			quanNum = Double.parseDouble(quantity);
			ganzhong = quanNum;
		} catch (Exception e) {
			return 0.00;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select cd,cc.chargingUnit,cc.type from ContractDetails as cd,ContractContent as cc where 1=1");
		sql.append(" and cd.contractId = :contractId");
		paramMap.put("contractId", contractId);
		sql.append(" and cd.contractContentId = cc.id");

		sql.append(" and (");
		int a = 0;
		for (Map.Entry<String, Double> entry : contents.entrySet()) {
			String detailId = entry.getKey();
			Double quan = entry.getValue();
			if (!StringUtil.isEmpty(detailId)) {
				if (detailId.indexOf("|") != -1) {
					ganzhong = Double.parseDouble(String.format("%.5f", quanNum - (quanNum * quan / 100)));
					String[] sfs = detailId.split("\\|");
					detailId = sfs[1];
				}

				if (a > 0) {
					sql.append(" or");
				}
				a++;
				sql.append("(");
				sql.append(" cd.contractContentId = '").append(detailId).append("'");
				sql.append(" and ((cd.startRegion <= ").append(quan).append(" and cd.endRegion > ").append(quan).append(")");
				sql.append(" or (cd.startRegion <= 0.00 and cd.endRegion > ").append(quan).append(")");
				sql.append(" or (cd.startRegion <= ").append(quan).append(" and cd.endRegion <= 0.00)");
				sql.append("))");

			}

		}

		sql.append(" )");
		sql.append(" order by cd.price desc");
		if (a <= 0) {
			return 0.00;
		}
		try {
			List<Object[]> details = contractDetailsDao.list(sql.toString(), paramMap);
			if (details == null || details.size() <= 0) {
				return 0.00;
			}
			double totalPrice = 0.00;
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			for (Object[] obj : details) {
				ContractDetails detail = (ContractDetails) obj[0];
				String chargingUnit = obj[1].toString();
				Boolean type = Boolean.valueOf(obj[2].toString());
				if (detail == null || map.containsKey(detail.getContractContentId())) {
					continue;
				}
				map.put(detail.getContractContentId(), true);
				Double conUnit = 0.00;
				if (!StringUtil.isEmpty(chargingUnit)) {
					if ("g".equalsIgnoreCase(chargingUnit)) {
						conUnit = Double.parseDouble(String.format("%.4f", ganzhong * contents.get(detail.getContractContentId())));
					} else {
						conUnit = Double.parseDouble(String.format("%.5f", ganzhong * contents.get(detail.getContractContentId())));
					}
				} else if (!type) {
					conUnit = Double.parseDouble(String.format("%.5f", ganzhong * (contents.get(detail.getContractContentId())) / 100));
				}
				if (!type) {
					if ("0".equals(detail.getType())) {
						totalPrice = totalPrice + (conUnit * detail.getPrice().doubleValue());
					} else {
						totalPrice = totalPrice - (conUnit * detail.getPrice().doubleValue());
					}
				} else {
					if ("0".equals(detail.getType())) {
						totalPrice = totalPrice + (quanNum * detail.getPrice().doubleValue());
					} else {
						totalPrice = totalPrice - (quanNum * detail.getPrice().doubleValue());
					}
				}
			}

			if (contract != null && "0".equals(contract.getPayType())) {
				totalPrice = totalPrice + (contract.getYkjPrice() * quanNum);
			}

			return Double.parseDouble(String.format("%.2f", totalPrice));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0.00;
	}

	public static void main(String[] args) {
		Double quanNum = 5.1415;
		Double quan = 21.51;

		Double ganzhong = quanNum - (quanNum * quan / 100);
		System.out.println(ganzhong);
		System.out.println();

		// BigDecimal maozhong = new BigDecimal(5);
		// BigDecimal bizhong = new BigDecimal(21.51);
		// BigDecimal b = new BigDecimal(1).subtract(bizhong.divide(new
		// BigDecimal(100)));
		// System.out.println(bizhong.divide(new BigDecimal(100)));
		// BigDecimal a = maozhong.multiply(b).round(4);
		// System.out.println(a);

	}
}
