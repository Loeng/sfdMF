package com.ekfans.base.tender.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.tender.dao.ITenderLogDao;
import com.ekfans.base.tender.model.TenderLog;
import com.ekfans.pub.util.StringUtil;

/**
 * 登陆日志Service接口实现
 * 
 * @ClassName: LoginLogService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("rawtypes")
public class TenderLogService implements ITenderLogService {

	private Logger log = LoggerFactory.getLogger(TenderLogService.class);
	@Resource
	private ITenderLogDao tenderLogDao;

	@Override
	public Boolean addTenderLog(TenderLog tenderLog) {
		if (tenderLog == null) {
			return false;
		}
		try {
			tenderLogDao.addBean(tenderLog);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean checkHasLog(String storeId, String tenderId) {
		if (StringUtil.isEmpty(storeId) || StringUtil.isEmpty(tenderId)) {
			return false;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select tl.id from TenderLog as tl where 1=1");
		sql.append(" and tl.tenderId = :tenderId");
		sql.append(" and tl.creator = :storeId");
		paramMap.put("tenderId", tenderId);
		paramMap.put("storeId", storeId);
		try {
			List list = tenderLogDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

}
