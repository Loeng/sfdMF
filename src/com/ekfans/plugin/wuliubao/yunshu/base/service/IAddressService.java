package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import com.ekfans.plugin.wuliubao.yunshu.base.model.Address;
import com.ekfans.pub.util.Pager;


/**
 * 用户地址Service接口
 * @author Administrator
 *
 */
public interface IAddressService {

	/**
	 * 得到用户所有的常用地址信息
	 * @return
	 */
	public List<Address> getAllAddress(Pager page,String userId);
	
	/**
	 * 添加常用地址
	 * @param address
	 * @return
	 */
	public boolean addAddress(Address address) ;
	
	/**
	 * 删除常用地址
	 * @param id
	 * @return
	 */
	public boolean deleteAddress(String id);
	
	/**
	 * 修改常用地址
	 * @param address
	 * @return
	 */
	public boolean modifyAddress(Address address);
	
	/**
	 * 通过地址id得到地址信息
	 * @param id
	 * @return
	 */
	public Address getAddress(String id);
}

















