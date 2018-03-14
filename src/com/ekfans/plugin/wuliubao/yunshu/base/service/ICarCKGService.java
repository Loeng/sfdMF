package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import com.ekfans.plugin.wuliubao.yunshu.base.model.CarCKG;


/**
 * 车辆长宽高Service接口
 * @author Administrator
 *
 */
public interface ICarCKGService {

	/**
	 * 得到车辆长度信息
	 * @return
	 */
	public List<CarCKG> getAllCarCKG();
}
