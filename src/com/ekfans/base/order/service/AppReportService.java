package com.ekfans.base.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IAppReportDao;
import com.ekfans.base.order.model.AppReport;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

@SuppressWarnings("rawtypes")
@Service
public class AppReportService implements IAppReportService {
	@Autowired
	private IAppReportDao reportDao;

	@Override
	public Boolean checkUserHasReport(String contentType, String contentId, String userId) {
		if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(contentId)) {
			return false;
		}

		Map<String, Object> paraMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select count(report.id) from AppReport as report where 1=1");
		if (!StringUtil.isEmpty(contentId)) {
			sql.append(" and report.contentId = :contentId");
			paraMap.put("contentId", contentId);
		}
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and report.userId = :userId");
			paraMap.put("userId", userId);
		}
		if (!StringUtil.isEmpty(contentType)) {
			sql.append(" and report.contentType = :contentType");
			paraMap.put("contentType", contentType);
		}
		try {
			List list = reportDao.list(sql.toString(), paraMap);
			if (list != null && list.size() > 0) {
				int totalNum = (Integer) list.get(0);
				if (totalNum <= 0) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public Boolean save(AppReport report) {
		if (report == null) {
			return false;
		}
		try {
			report.setUpdateTime(DateUtil.getSysDateTimeString());
			report.setCreateTime(DateUtil.getSysDateTimeString());
			reportDao.addBean(report);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}
