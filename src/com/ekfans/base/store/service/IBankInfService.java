package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.BankInf;
import com.ekfans.pub.util.Pager;

/**
 * 运输企业车辆信息Service接口
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IBankInfService {

	/**
	 * 导入银行（仅执行一次）
	 * 
	 * @param urlPath
	 * @return
	 */
	public boolean setBankInfs(String filePath);

	/**
	 * 根据ID获取银行信息对象
	 * 
	 * @param id
	 * @return
	 */
	public BankInf getBankInfById(String id);

	/**
	 * 根据KEY搜索银行对象
	 * 
	 * @param key
	 * @return
	 */
	public List<BankInf> getBankInfos(String key, Pager pager);

}