package com.ekfans.base.metal.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 
 * @ClassName: AppraiseDao
 * @Description: TODO(商户中心-评价操作的Dao)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-14 上午09:27:46
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class PreciousMetalDetailDao extends GenericDao implements IPreciousMetalDetailDao {
	public PreciousMetalDetailDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.metal.model.PreciousMetalDetail";
	}
}
