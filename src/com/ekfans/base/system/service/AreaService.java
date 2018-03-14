package com.ekfans.base.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.dao.IAreaDao;
import com.ekfans.base.system.model.Area;
import com.ekfans.pub.util.Pager;

/**
 * 区域Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Service
public class AreaService implements IAreaService {
    
	private Logger log = LoggerFactory.getLogger(ManagerService.class);
    @Resource
    private IAreaDao areaDao;

    @SuppressWarnings("unchecked")
	@Override
    public List<Area> getList(Pager pager) {
     // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 定义查询HQL
        StringBuffer hql = new StringBuffer("from Area area where 1=1");
        
        
        //添加时间倒序排列
        hql.append(" order by area.createTime desc");
        
        try {
            // 调用DAO方法查找管理员
            return areaDao.list(pager, hql.toString(), paramMap);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAllArea() {
		try {
			return areaDao.list("from Area", null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> getStoreByAreaId(String areaId) {
		String hql = "from Store where areaId like '%" + areaId + "%'";
		
		try {
			return areaDao.list(hql, null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	
}
