package com.ekfans.base.store.dao;

import java.util.List;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

/**
 * 运输企业车辆信息DAO接口
 * 
 * @ClassName: IIntelDao
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ICarInfoDao extends IGenericDao {
	/**
	 * 获取用户车辆信息
	 * @param userId 用户id
	 * @param page 分页
	 * @return
	 * @throws Exception 
	 */
     List<CarInfo> getUserCarInfo(String userId,Pager page) throws Exception;
     
}