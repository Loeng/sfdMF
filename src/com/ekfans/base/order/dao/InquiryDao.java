package com.ekfans.base.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 询价DAO接口实现
 * 
 * @ClassName: InquiryDao
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class InquiryDao extends GenericDao implements IInquiryDao {
	
	public InquiryDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.order.model.Inquiry";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inquiry> getBargain(String buyersId, String sellersId, String productId, Pager page) throws Exception {
		StringBuffer sql = new StringBuffer(" from Inquiry  where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(buyersId)){
			sql.append(" and buyersId = :buyersId");
			paramMap.put("buyersId", buyersId);
		}
		if(!StringUtil.isEmpty(productId)){
		sql.append(" and productId = :productId");
		paramMap.put("productId", productId);
		}
		if(!StringUtil.isEmpty(sellersId)){
			sql.append(" and sellersId = :sellersId");
			paramMap.put("sellersId", sellersId);
		}
		// 根据需求排序
		sql.append(" order by updateTime desc");
		List<Inquiry> inquiry=null;
		if(page!=null){
			inquiry = (List<Inquiry>) super.list(page,sql.toString(), paramMap);
			return inquiry;
		}
		inquiry = (List<Inquiry>) super.list(sql.toString(), paramMap);
		return inquiry;
	}
}
