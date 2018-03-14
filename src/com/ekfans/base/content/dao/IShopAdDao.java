package com.ekfans.base.content.dao;

import java.util.List;

import com.ekfans.base.content.model.ShopAd;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 广告DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IShopAdDao extends IGenericDao {

	List<ShopAd> getConfiguredList() throws Exception;
	
}
