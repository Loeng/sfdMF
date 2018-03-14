package com.ekfans.base.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IAppForwardingDao;
import com.ekfans.base.order.model.AppForwarding;
import com.ekfans.base.order.model.AppForwarding;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
public class AppForwardingService implements IAppForwardingService {
	@Autowired
	private IAppForwardingDao forwardingDao;

	@Override
	public Boolean save(AppForwarding supplyForwarding) {
		if (supplyForwarding == null) {
			return false;
		}
		try {
			supplyForwarding.setUpdateTime(DateUtil.getSysDateTimeString());
			supplyForwarding.setCreateTime(DateUtil.getSysDateTimeString());
			forwardingDao.addBean(supplyForwarding);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<AppForwarding> getMyForwardingList(Pager pager, String contentType, String userId) {
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql语句
		StringBuffer hql = new StringBuffer("from AppForwarding where 1=1");
		if (!StringUtil.isEmpty(userId)) {
			hql.append(" and userId = :userId");
			params.put("userId", userId);
		}
		if (!StringUtil.isEmpty(contentType)) {
			hql.append(" and contentType = :contentType");
			params.put("contentType", contentType);
		}
		// 排序
		hql.append(" order by createTime desc");

		try {
			return forwardingDao.list(pager, hql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
