package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.FinancialData;

/**
 * 企业财务信息Service接口
 * 
 * @ClassName: IFinancialDataService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IFinancialDataService {

	public List<FinancialData> getFinancialDataByStoreId(String storeId);
}