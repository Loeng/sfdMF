package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreIntelAppendixDao;
import com.ekfans.base.store.model.StoreIntelAppendix;
import com.ekfans.pub.util.StringUtil;

/**
 * 资质认证附件Service接口实现
 * 
 * @ClassName: StoreIntelAppendixService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class StoreIntelAppendixService implements IStoreIntelAppendixService {

	private Logger log = LoggerFactory.getLogger(StoreIntelAppendixService.class);
	@Resource
	private IStoreIntelAppendixDao storeIntelAppendixDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreIntelAppendix> getStoreIntelAppendixByStoreId(String storeId, String type) {
		if(StringUtil.isEmpty(storeId)){
			return null;
		}
		
		String hql = "select sia from StoreIntelAppendix sia,StoreIntel si where sia.storeIntelId=si.id and si.checkStatus<>'1' and si.storeId='" + storeId + "' and si.type='" + type + "'";
		
		try {
			// 查询资质证明文件
			List<StoreIntelAppendix> list = storeIntelAppendixDao.list(hql, null);
			if (list != null && list.size() > 0) {
				// 把资质文件放入map中文件路径为key,当有相同文件路径，不保存（主要目的去掉重复资质证明文件）
				Map<String, StoreIntelAppendix> map = new HashMap<String, StoreIntelAppendix>();
				for (StoreIntelAppendix sia : list) {
					map.put(sia.getFile(), sia);
				}
				List<StoreIntelAppendix> sialist = new ArrayList<StoreIntelAppendix>();
				for (String str : map.keySet()) {
					sialist.add(map.get(str));
				}
				return sialist;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
}