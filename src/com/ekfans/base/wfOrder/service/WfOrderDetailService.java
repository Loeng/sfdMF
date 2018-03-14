package com.ekfans.base.wfOrder.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.wfOrder.dao.IWfOrderDetailDao;
import com.ekfans.base.wfOrder.dao.IWfOrderPriceDao;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderDetail;
import com.ekfans.base.wfOrder.model.WfOrderPrice;
import com.ekfans.pub.util.StringUtil;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(危废品订单明细Service)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class WfOrderDetailService implements IWfOrderDetailService {

	@Autowired
	private IWfOrderDetailDao detailDao;
	@Autowired
	private IWfOrderPriceDao priceDao;

	/**
	 * 根据订单ID获取订单明细集合
	 * 
	 * @param wfOrderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WfOrderDetail> getDetailsByWfOrderId(String wfOrderId) {
		// 如果传过来的危废订单ID为空，则返回NULL
		if (StringUtil.isEmpty(wfOrderId)) {
			return null;
		}

		// 定义SQL查询数据MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" from WfOrderDetail as detail where 1=1");
		// 关联危废订单ID
		sql.append(" and detail.wfOrderId = :wfOrderId");
		paramMap.put("wfOrderId", wfOrderId);
		// 按照位置升序排序
		sql.append(" order by detail.position asc");

		// 定义SQL查询数据MAP
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		// 定义查询SQL,查询品味明细表
		StringBuffer sql2 = new StringBuffer(" from WfOrderPrice as wp where 1=1");
		// 关联危废订单ID
		sql2.append(" and wp.wfOrderId = :wfOrderId");
		paramMap2.put("wfOrderId", wfOrderId);
		// 按照位置升序排序
		sql2.append(" order by wp.position asc");

		try {
			// 查询订单明细
			List<WfOrderDetail> detailList = detailDao.list(sql.toString(), paramMap);
			// 如果查出来的数据为空，则返回
			if (detailList == null || detailList.size() <= 0) {
				return null;
			}

			// 查询品味明细
			List<WfOrderPrice> prices = priceDao.list(sql2.toString(), paramMap2);
			// 归类品味集合
			Map<String, List<WfOrderPrice>> map = new HashMap<String, List<WfOrderPrice>>();
			if (prices != null && prices.size() > 0) {
				for (WfOrderPrice price : prices) {
					if (price == null || StringUtil.isEmpty(price.getOrderDetailId())) {
						continue;
					}
					List<WfOrderPrice> priceList = null;
					if (map.containsKey(price.getOrderDetailId())) {
						priceList = map.get(price.getOrderDetailId());
					}

					if (priceList == null) {
						priceList = new LinkedList<WfOrderPrice>();
					}
					priceList.add(price);
					map.put(price.getOrderDetailId(), priceList);
				}
			} else {
				return detailList;
			}

			List<WfOrderDetail> details = new LinkedList<WfOrderDetail>();
			for (WfOrderDetail detail : detailList) {
				if (detail == null) {
					continue;
				}
				detail.setPrices(map.get(detail.getId()));
				details.add(detail);
			}
			return details;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 新增或修改保存订单明细
	 * 
	 * @param order
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Boolean saveOrUpdateDetail(WfOrder order, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
