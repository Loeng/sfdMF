package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.CreditEstimates;
import com.ekfans.pub.util.Pager;

/**
 * 信用测算信息Service接口
 * 
 * @ClassName: ICreditEstimatesService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ICreditEstimatesService {

	/**
	 * 分页查询信用测算信息
	 * 
	 * @param pager
	 * @param status 测算状态
	 * @param storeName 企业名称
	 * @return
	 */
	public List<CreditEstimates> getCeList(Pager pager, Integer status, String storeName, String type);
	
	/**
	 * 根据id获取企业信用测算信息
	 * 
	 * @param id
	 * @return
	 */
	public CreditEstimates getCreditEstimates(String id);
	
	/**
	 * 填写信用测算的测算结果
	 * 
	 * @param id 
	 * @param status 状态（0:未测评,1:测评中,2:测评完成）
	 * @param note 测算等级
	 * @return true:成功,false:失败
	 */
	public Boolean auCreditInfo(String id, String note);
}