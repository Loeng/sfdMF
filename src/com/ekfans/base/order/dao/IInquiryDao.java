package com.ekfans.base.order.dao;

import java.util.List;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

/**
 * 询价DAO接口
 * 
 * @ClassName: IInquiryDao
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IInquiryDao extends IGenericDao {
	/**
     * 获取用户议价信息
     * @param buyersId  买家ID
     * @param sellersId  卖家ID
     * @param productId 货源Id(商品id等)
     * @param page 分页
     * @return
     * @throws Exception
     */
	List<Inquiry> getBargain(String buyersId,String sellersId,String productId,Pager page) throws Exception;
}