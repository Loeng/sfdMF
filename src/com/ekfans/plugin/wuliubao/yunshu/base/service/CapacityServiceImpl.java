package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wuliubao.yunshu.base.dao.ICapacityDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Capacity;

@Service
@SuppressWarnings("unchecked")
public class CapacityServiceImpl implements ICapacityService{

	@Autowired
	private ICapacityDao dao;
	public List<Capacity> getAllCapacity() {

		StringBuffer sql = new StringBuffer("from Capacity where 1=1");
		try {
			List<Capacity> capacitys = dao.list(sql.toString(),null);
			return capacitys;
		} catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}



}
