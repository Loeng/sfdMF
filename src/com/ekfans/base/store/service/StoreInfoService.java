package com.ekfans.base.store.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreInfoDao;
import com.ekfans.base.store.model.StoreInfo;
import com.ekfans.pub.util.StringUtil;

/**
 * 企业详细信息Service接口实现
 * 
 * @ClassName: StoreInfoService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class StoreInfoService implements IStoreInfoService {
	
	private Logger log = LoggerFactory.getLogger(StoreInfoService.class);
	@Resource
	private IStoreInfoDao storeInfoDao;
	
	@Override
	public StoreInfo getStoreInfoById(String id) {
		if(StringUtil.isEmpty(id)){
			return null;
		}
		
		try {
			return (StoreInfo)storeInfoDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}

}