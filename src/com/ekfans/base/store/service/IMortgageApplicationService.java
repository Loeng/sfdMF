package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.MortgageApplication;
import com.ekfans.pub.util.Pager;

/**
 * 贷款申请Service接口
 * 
 * @ClassName: IMortgageApplicationService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IMortgageApplicationService {

	/**
	 * 获取分页（订单，信用）借贷列表
	 * 
	 * @param pager 分页
	 * @param type 类型（1:订单申请，2:信用申请，3:合同借贷，4:危废品借贷）
	 * @param storeId 申请企业id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param orderId 订单号
	 * @param minMoney 借款金额（小）
	 * @param maxMoney 借款金额（大）
	 * @param contractNo 合同编号
	 * @param status 状态（1:提交中，2:审核中，3:审核成功）
	 */
	public List<MortgageApplication> getMAList(Pager pager, Integer type, String storeId, String startTime, String endTime, String orderId, String minMoney, String maxMoney, String contractNo, String status);
	
	/**
	 * 保存（订单，信用）借贷申请
	 * 
	 * @return 1:成功，2:失败，3:订单号不能空，4:订单合同或抵押物文件不能为空
	 */
	public int saveMa(MortgageApplication ma);
	
	/**
	 * 根据id获取（订单，信用）借贷详情
	 * 
	 * @param id
	 * @return
	 */
	public MortgageApplication getMaById(String id);
	
	/**
	 * 更新（订单，信用）借贷申请
	 * 
	 * @return 1:成功，2:失败，3:订单号不能空，4:订单合同或抵押物文件不能为空
	 */
	public int updateMa(MortgageApplication ma);
	
	/**
	 * 得到借贷总数
	 * 
	 * @param storeId
	 * @return String
	 */
	public String getMASum(String storeId);
	
	/**
	 * 核心企业首页的借款申请
	 * 
	 * @param storeId
	 * @return List<MortgageApplication>
	 */
	public List<MortgageApplication> getList(String storeId);
	
	/// =============  华丽的分割线嘿嘿（下面后台的方法）  =============
	/**
	 * 后台分页获取信用借贷和订单借贷数据
	 * 
	 * @param pager 分页
	 * @param type 会员类型（0：个人会员，1：供应商，2：采购商，3：核心企业）
	 * @param matype 借贷类型（1:订单申请，2:信用申请）
	 * @param orderId 订单号
	 * @param storeName 企业名称
	 * @param status 状态
	 * @return List<MortgageApplication>
	 */
	public List<MortgageApplication> getSysMaList(Pager pager, String type, int matype, String orderId, String storeName, String status);
}