package com.ekfans.base.store.dao;

import java.io.Serializable;
import java.util.List;

import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 企业基础信息DAO接口
 * 
 * @ClassName: IStoreDao
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreDao extends IGenericDao {

	/**
	 * 根据主键获取持久化对象
	 */
	public <T> T get(Class<T> clazz, Serializable id) throws Exception;

	/**
	 * 获取需要同步的企业对象，包含企业本身信息store数据，和账号信息user表数据
	 * @return
	 */
	public List<Object[]> getStoresInterface();
	
}