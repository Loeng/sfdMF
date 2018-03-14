package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import com.ekfans.plugin.wuliubao.yunshu.base.model.Capacity;


/**
 * 车辆载重范围Service接口
 * @author Administrator
 *
 */
public interface ICapacityService {

	/**
	 * 得到车辆载重范围信息
	 * @return
	 */
	public List<Capacity> getAllCapacity();
}
