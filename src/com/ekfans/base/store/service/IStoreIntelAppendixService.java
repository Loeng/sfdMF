package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StoreIntelAppendix;

/**
 * 资质认证附件Service接口
 * 
 * @ClassName: IStoreIntelAppendixService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreIntelAppendixService {

	/**
	 * 根据企业Id获取（采购,销售,运输）资质的证明文件
	 * 
	 * @param storeId 企业id
	 * @param type 0:采购,1:销售,2:运输
	 */
	public List<StoreIntelAppendix> getStoreIntelAppendixByStoreId(String storeId, String type);
}