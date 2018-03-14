package com.ekfans.base.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.ICategoryGoodsDao;
import com.ekfans.base.store.model.CategoryGoods;

/**
 * 运输企业车辆人员信息Service接口实现
 * 
 * @ClassName: CreditEstimatesService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class CategoryGoodsService implements ICategoryGoodsService {
	@Autowired
	private ICategoryGoodsDao dao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryGoods> getCategoryGoodsList() {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("from CategoryGoods where 1=1");
		try {
			List<CategoryGoods> categoryGoods = dao.list(sql.toString(),null);
			return categoryGoods;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}