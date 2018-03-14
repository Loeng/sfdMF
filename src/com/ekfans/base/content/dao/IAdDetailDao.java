package com.ekfans.base.content.dao;

import java.util.List;

import com.ekfans.base.content.model.AdDetail;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 广告明细DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IAdDetailDao extends IGenericDao {
     /**
      * 获取广告明细集合
      * @return
     * @throws Exception 
      */
	 public List<AdDetail> getListAdDetail(String adId) throws Exception;
}
