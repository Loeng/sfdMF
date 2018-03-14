package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.ICooperationDao;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Cooperation;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 合作机构service接口实现类
 * @ClassName CooperationService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月15日
 */
@Service
@SuppressWarnings("unchecked")
public class CooperationService implements ICooperationService {
 
	private Logger log = LoggerFactory.getLogger(CooperationService.class);
	
	@Autowired
	private ICooperationDao cooperationDao;

	@Override
	public boolean addCooperation(Cooperation cp) {
		// TODO Auto-generated method stub
		try{
			cp.setCreateTime(DateUtil.getSysDateTimeString());
			cooperationDao.addBean(cp);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean updateCooperation(Cooperation cp) {
		// TODO Auto-generated method stub
		try{
			cp.setUpdateTime(DateUtil.getSysDateTimeString());
			cooperationDao.updateBean(cp);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean delCooperationById(String id) {
		// TODO Auto-generated method stub
		if(StringUtil.isEmpty(id)){
			return false;
		}
		
		try{
			cooperationDao.deleteById(id);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Cooperation> listCooperation(Pager pager, String orgName,
			String type, String contactMan, String contactPhone) {
		// 定义参数MAP
		Map<String, Object> params = new HashMap<String, Object>();
		// 定义hql
		StringBuffer hql = new StringBuffer("from Cooperation as c where 1=1");
		
		// 添加查询条件
		if(!StringUtil.isEmpty(orgName)){
			hql.append(" and c.orgName like :orgName");
			params.put("orgName", "%" + orgName + "%");
		}
		
		if(!StringUtil.isEmpty(type)){
			hql.append(" and c.type =:type");
			params.put("type", type);
		}
		
		if(!StringUtil.isEmpty(contactMan)){
			hql.append(" and c.contactMan like :contactMan");
			params.put("contactMan", "%" + contactMan + "%");
		}
		
		if(!StringUtil.isEmpty(contactPhone)){
			hql.append(" and c.contactPhone like :contactPhone");
			params.put("contactPhone", "%" + contactPhone + "%");
		}
		hql.append(" order by c.createTime desc");
		
		try{
			// 调用DAO查询列表
			List<Cooperation> cps = cooperationDao.list(pager, hql.toString(), params);
			return cps;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean delCooperation(String ids) {
		if(StringUtil.isEmpty(ids)){
			return true;
		}
		
		StringBuffer hql = new StringBuffer("delete from Cooperation where id in (");
		// 单个删除
		if(ids.indexOf(",") < 0){
			hql.append("'" + ids + "'");
		}else{
			// 批量删除
			String[] ida = ids.split(",");
			for (int i = 0; i < ida.length; i++) {
				hql.append((i == 0 ? "'" + ida[i] : ",'" + ida[i]) + "'");
			}
		}
		hql.append(")");
		
		try {
			cooperationDao.delete(hql.toString(), new HashMap<String, Object>());
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Cooperation findById(String id) {
		try {
			if (!StringUtil.isEmpty(id)) {
				return (Cooperation) cooperationDao.get(id);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
