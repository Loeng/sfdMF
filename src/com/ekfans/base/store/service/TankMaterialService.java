package com.ekfans.base.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.ITankMaterialDao;
import com.ekfans.base.store.model.TankMaterial;



/**
 * 运输企业车辆罐体材质Service接口实现
 * 
 * @ClassName: CreditEstimatesService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class TankMaterialService implements ITankMaterialService {
	@Autowired
	private ITankMaterialDao tankMaterialDao;

	@Override
	public List<TankMaterial> getList() {
		StringBuffer hql = new StringBuffer("select tm from TankMaterial tm where 1=1");
		try {
			List<TankMaterial> tms = this.tankMaterialDao.list(hql.toString(), null);
			return tms;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<TankMaterial> getTankMaterialList(String vehicleTypeId) {
		StringBuffer hql = new StringBuffer("select tm from TankMaterial tm where 1=1");
		hql.append(" and vehicleTypeId ="+vehicleTypeId);
		try {
			@SuppressWarnings("unchecked")
			List<TankMaterial> tms = this.tankMaterialDao.list(hql.toString(), null);
			return tms;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}

}