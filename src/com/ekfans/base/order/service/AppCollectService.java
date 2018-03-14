package com.ekfans.base.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IAppCollectDao;
import com.ekfans.base.order.model.AppCollect;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@SuppressWarnings("rawtypes")
@Service
public class AppCollectService implements IAppCollectService {
	@Autowired
	private IAppCollectDao collectDao;

	/**
	 * 判断用户是否收藏该咨询，true 已收藏，false 未收藏
	 */
	@Override
	public Boolean checkUserHasCollect(String contentType, String contentId, String userId) {
		if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(contentId)) {
			return false;
		}

		Map<String, Object> paraMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select count(collect.id) from AppCollect as collect where 1=1");
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and collect.userId = :userId");
			paraMap.put("userId", userId);
		}
		if (!StringUtil.isEmpty(contentType)) {
			sql.append(" and collect.contentType = :contentType");
			paraMap.put("contentType", contentType);
		}
		if (!StringUtil.isEmpty(contentId)) {
			sql.append(" and collect.contentId = :contentId");
			paraMap.put("contentId", contentId);
		}
		try {
			List list = collectDao.list(sql.toString(), paraMap);
			if (list != null && list.size() > 0) {
				Long totalNum = (Long) list.get(0);
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

	/**
	 * 收藏供需
	 */
	@Override
	public Boolean save(AppCollect collect) {
		if (collect == null) {
			return false;
		}
		try {
			collect.setUpdateTime(DateUtil.getSysDateTimeString());
			collect.setCreateTime(DateUtil.getSysDateTimeString());
			collectDao.addBean(collect);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 取消收藏
	 */
	@Override
	public Boolean delByUser(String userId, String contentType, String contentId) {
		if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(contentId)) {
			return false;
		}

		Map<String, Object> paraMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from AppCollect as collect where 1=1");
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and collect.userId = :userId");
			paraMap.put("userId", userId);
		}
		if (!StringUtil.isEmpty(contentType)) {
			sql.append(" and collect.contentType = :contentType");
			paraMap.put("contentType", contentType);
		}
		if (!StringUtil.isEmpty(contentId)) {
			sql.append(" and collect.contentId = :contentId");
			paraMap.put("contentId", contentId);
		}
		try {
			collectDao.delete(sql.toString(), paraMap);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public List<AppCollect> getMyCollectList(Pager pager, String contentType, String userId) {
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql语句
		StringBuffer hql = new StringBuffer("from AppCollect where 1=1");
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
			return collectDao.list(pager, hql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
