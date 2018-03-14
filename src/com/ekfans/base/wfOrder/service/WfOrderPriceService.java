package com.ekfans.base.wfOrder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.wfOrder.dao.IWfOrderPriceDao;
import com.ekfans.base.wfOrder.model.WfOrderPrice;
import com.ekfans.pub.util.StringUtil;

@SuppressWarnings("unchecked")
@Service
public class WfOrderPriceService implements IWfOrderPriceService {
	@Autowired
	private IWfOrderPriceDao priceDao;

	@Override
	public List<WfOrderPrice> getWfOrderPrices(String detailId) {
		if (StringUtil.isEmpty(detailId)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from WfOrderPrice as wp where 1=1");
		sql.append(" and wp.orderDetailId = :orderDetailId");
		map.put("orderDetailId", detailId);
		sql.append(" order by wp.position asc");

		try {
			List<WfOrderPrice> prices = priceDao.list(sql.toString(), map);
			return prices;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
