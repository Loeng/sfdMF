package com.ekfans.plugin.wuliubao.yunshu.base.dao;

import java.util.List;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;
/**
 * 议价相关dao接口
 * @author pp
 * @Date 2017-7-19 14:52:44
 *
 */
public interface IWlbAppBargainDao extends IGenericDao{
    /**
     * 获取用户议价信息
     * @param buyerId  买家ID
     * @param sourceId  卖家ID
     * @param sellerId 货源Id(商品id等)
     * @return
     * @throws Exception
     */
	List<Bargain> getBargain(String buyerId,String sellerId,String sourceId,Pager page) throws Exception;
	
}
