package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wuliubao.yunshu.base.dao.ICarCKGDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.CarCKG;

@Service
@SuppressWarnings("unchecked")
public class CarCKGServiceImpl implements ICarCKGService {

	@Autowired
	private ICarCKGDao dao;
	@Override
	public List<CarCKG> getAllCarCKG() {

		StringBuffer sql = new StringBuffer("from CarCKG where 1=1");
		try {
			List<CarCKG> carCKGs = dao.list(sql.toString(),null);
			return carCKGs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}


}
