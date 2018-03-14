package com.ekfans.base.wfOrder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.wfOrder.dao.ContractDetailsDao;
import com.ekfans.base.wfOrder.model.ContractDetails;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class ContractDetailsService implements IContractDetailsService {
	@Autowired
	private ContractDetailsDao cdd;

	@Override
	public List<ContractDetails> findContractDetails(String contractContendId,
			String name) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 根据合同含量id得到合同含量明细集合
			StringBuffer hql = new StringBuffer(
					" from ContractDetails cc where cc.contractContentId= :contractContentId");
			map.put("contractContentId", contractContendId);
			if (!StringUtil.isEmpty(name)) {
				hql.append(" and cc.contentName ='" + name + "'");
			}
			// 得到合同明细含量集合
			List<ContractDetails> list = (List<ContractDetails>) cdd.list(
					hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据明细ID和数量获取对象
	 * 
	 * @param contengId
	 * @param num
	 * @return
	 */
	public ContractDetails getDetailPriceByNumAndContentId(String contentId,
			String num) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(
				" from ContractDetails cc where cc.contractContentId= :contractContentId");
		map.put("contractContentId", contentId);
		hql.append(" and cc.startRegion <= :startNum");
		map.put("startNum", Double.parseDouble(num));
		hql.append(" and cc.endRegion > :endNum");
		map.put("endNum", Double.parseDouble(num));
		hql.append(" order by cc.price desc");
		try {
			List<ContractDetails> details = cdd.list(hql.toString(), map);
			if (details != null && details.size() > 0) {
				return details.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}
