package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IIntelDao;
import com.ekfans.base.store.model.Intel;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 资质Service接口实现
 * 
 * @ClassName: IntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class IntelService implements IIntelService {
	
	private Logger log = LoggerFactory.getLogger(IntelService.class);
	@Resource
	private IIntelDao intelDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Intel> getIntelAll() {
		try {
			return intelDao.list("from Intel where status=true", null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Intel> getIntelByStoreId(String storeId, String type) {
		if(StringUtil.isEmpty(storeId)){
			return null;
		}
		
		String hql = "select i from Intel i,StoreIntel si where i.id=si.intelId and si.checkStatus<>'1' and si.storeId='" + storeId + "'";
		if(!StringUtil.isEmpty(type)){
			hql += " and si.type='" + type + "'";
		}
		
		try {
			return intelDao.list(hql, null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Intel> getIntelByStoreIdTong(String storeId, String type) {
		if(StringUtil.isEmpty(storeId)){
			return null;
		}
		
		String hql = "select i from Intel i,StoreIntel si where i.id=si.intelId and si.checkStatus='1' and si.storeId='" + storeId + "'";
		if(!StringUtil.isEmpty(type)){
			hql += " and si.type='" + type + "'";
		}
		
		try {
			return intelDao.list(hql, null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public int saveIntel(Intel intel) {
		if(intel == null){
			return 1;
		}
		
		try {
			if(StringUtil.isEmpty(intel.getName())){
				return 2;
			}
			
			intel.setCreateTime(DateUtil.getSysDateTimeString());
			intelDao.addBean(intel);
			
			return 3;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Intel> getIntelList(Pager page, String name) {
		// 定义参数MAP
		Map<String, Object> params = new HashMap<String, Object>();
		// 定义查询HQL
		StringBuffer hql = new StringBuffer("from Intel");
		
		if(!StringUtil.isEmpty(name)){
			hql.append(" where name like :name");
			params.put("name", "%" + name + "%");
		}
		
		try {
			return intelDao.list(page, hql.toString(), params);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean deleteIntel(String ids) {
		if(StringUtil.isEmpty(ids)){
			return true;
		}
		
		StringBuffer hql = new StringBuffer("delete from Intel where id in (");
		if(ids.indexOf(",") < 0){
			hql.append("'" + ids + "'");
		}else{
			String[] ida = ids.split(",");
			for (int i = 0; i < ida.length; i++) {
				hql.append((i == 0 ? "'" + ida[i] : ",'" + ida[i]) + "'");
			}
		}
		hql.append(")");
		
		try {
			intelDao.delete(hql.toString(), new HashMap<String, Object>());
			
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Intel getIntelById(String id) {
		if(StringUtil.isEmpty(id)){
			return null;
		}
		
		try {
			return (Intel)intelDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public int updateIntel(Intel intel) {
		if(intel == null){
			return 1;
		}
		
		try {
			if(StringUtil.isEmpty(intel.getName())){
				return 2;
			}
			
			intel.setUpdateTime(DateUtil.getSysDateTimeString());
			intelDao.updateBean(intel);
			
			return 3;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return 1;
	}
	
}