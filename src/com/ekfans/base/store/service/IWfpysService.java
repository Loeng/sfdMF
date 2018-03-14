package com.ekfans.base.store.service;

import java.util.List;
import java.util.Set;

import com.ekfans.base.order.model.Wfpml;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.pub.util.Pager;

public interface IWfpysService {

	public Wfpys getWfpys(String id);
	
	/**
	 * 获取Wfpys集合
	 * @param first 是否查询第一级 true 只查询第一级 false 根据parentId查询子集
	 * @param parentId
	 * @return
	 */
	public List<Wfpys> getList(boolean first,String parentId,String name);
	
	public List<Wfpys> getList(Set<String> set);
	
	public String getChildIdsByAccId(String accId);
}
