package com.ekfans.base.store.dao;

import java.util.List;

import com.ekfans.base.store.model.CarUser;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 运输企业车辆人员信息DAO接口
 * 
 * @ClassName: IIntelDao
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ICarUserDao extends IGenericDao {
	/**
	 * 获取接口CarUser数据
	 * @return
	 */
	public List<CarUser> getCarUsersInterface();
}