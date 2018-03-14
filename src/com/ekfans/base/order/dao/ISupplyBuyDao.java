package com.ekfans.base.order.dao;

import java.util.List;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

/**
 * 供需关系DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: ekfans
 * @author lh
 * @date 2016年3月3日17:25:09
 * @version 1.0
 */
public interface ISupplyBuyDao extends IGenericDao {
	/**
	 * 公共list方法
	 * 
	 * @param pager
	 * @param title 供求的标题
	 * @param type 类型0供应 1求购
	 * @param status 状态0关闭 1正常 2已完成
	 * @param productType 商品类型0成品 1原材料 2危废品
	 * @param checkStatus 审核状态(0:未审核 1：通过 -1不通过)
	 * @return 供需实例集合
	 */
	public List<SupplyBuy> listCommon(Pager pager, String title, String type, String status, String productType, Integer checkStatus);

}