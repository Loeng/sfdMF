package com.ekfans.base.store.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreLevelDao;
import com.ekfans.base.store.model.StoreLevel;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 店铺等级业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class StoreLevelService implements IStoreLevelService {
	// 定义DAO
	@Autowired
	private IStoreLevelDao storeLevelDao;

	/**
	 * 查询等级列表
	 * 
	 * @param page
	 * @return
	 */
	public List<StoreLevel> getLevels(Pager page) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select storeLevel from StoreLevel as storeLevel where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//添加时间倒序排列
		sql.append(" order by storeLevel.createTime desc");
		try {
			List<StoreLevel> list = storeLevelDao.list(page, sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		return null;
	}

	/**
	 * 新增店铺等级
	 * 
	 * @param level
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addLevel(StoreLevel level, File uploadFile, String uploadFileContentType) {
		// 如果传进来的店铺等级对象为空，则返回false
		if (level == null) {
			return false;
		}
		try {
			
			// 设置创建时间为当前系统时间
			level.setCreateTime(DateUtil.getSysDateTimeString());
			// 调用SERVICE的方法添加店铺等级
			storeLevelDao.addBean(level);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id删除店铺等级
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteLevel(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			// 删除旧图标
			StoreLevel level = (StoreLevel) storeLevelDao.get(id);
			if (!StringUtil.isEmpty(level.getIcon())) {
				File file = new File(level.getIcon());
				if (file.exists()) {
					file.delete();
				}
			}
			// 调用SERVICE的方法删除店铺等级
			storeLevelDao.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改店铺等级
	 * 
	 * @param level
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyLevel(StoreLevel level, File uploadFile, String uploadFileContentType) {
		// 如果传进来的店铺等级对象为空，则返回false
		if (level == null) {
			return false;
		}
		try {
			
			// 调用SERVICE的方法修改店铺等级
			storeLevelDao.updateBean(level);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public StoreLevel getLevel(String id) {
		// 如果传进来的id为空，则返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}

		try {
			// 调用SERVICE的方法查找店铺等级
			return (StoreLevel) storeLevelDao.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}