package com.ekfans.base.store.service;

import com.ekfans.base.store.model.StoreInfo;

/**
 * 企业详细信息Service接口
 * 
 * @ClassName: IStoreInfoService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreInfoService {
	
	/**
	 * 根据id企业详细信息
	 */
	public StoreInfo getStoreInfoById(String id);

}