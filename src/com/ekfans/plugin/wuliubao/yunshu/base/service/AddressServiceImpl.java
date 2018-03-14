package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IAddressDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Address;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class AddressServiceImpl implements IAddressService{

	@Autowired
	private IAddressDao dao;

	@Override
	public List<Address> getAllAddress(Pager page,String userId) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from Address a where 1=1");
		// 类型
		if(!StringUtil.isEmpty(userId)){
			sql.append(" and a.userId = :userId");
			map.put("userId", userId);
		}
		try {
			List<Address> list = dao.list(page, sql.toString(), map);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean addAddress(Address address) {
		try {
			dao.addBean(address);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean deleteAddress(String ids) {
		// TODO Auto-generated method stub
		try {
			if(ids.indexOf(",") < 0){
				dao.deleteById(ids);
				return true;
			}else{
				String [] id = ids.split(",");
				for (int i = 0; i < id.length; i++) {
					dao.deleteById(id[i]);
				}
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean modifyAddress(Address address) {
		try {
			dao.updateBean(address);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Address getAddress(String id) {
		// TODO Auto-generated method stub
		try {
			Address address = (Address) dao.get(id);
			return address;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
