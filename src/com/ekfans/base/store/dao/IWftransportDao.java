package com.ekfans.base.store.dao;

import java.util.List;

import com.ekfans.base.store.model.Wftransport;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

/**
 * @ClassName: IWftransportDao  
 * @Description: TODO(危废运输dao接口) 
 * @Copyright: Copyright (c) 2016年3月15日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年3月15日上午11:57:33
 * @version 1.0
 */
public interface IWftransportDao extends IGenericDao{
   /**
    * 获取用户车辆丶 车源丶 货源信息
    * @param userid
    * @param type 
    * @return
    * @throws Exception
    */
	List<Wftransport> getUserWftransport(String userid,int type,Pager page,String status) throws Exception;
	/**
	 * 获取未审核商品的数量
	 * @param type 商品类型(0:车源,1:货源)
	 * @return
	 */
	public int getWftransportCheckNum(int type);
}
