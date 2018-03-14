package com.ekfans.base.wfOrder.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.wfOrder.model.ScrapWfp;
import com.ekfans.base.wfOrder.model.ScrapWfpTrans;
import com.ekfans.pub.util.Pager;

public interface IScrapWfpService {

	/**
	 * 根据ID获取对象
	 * 
	 * @param scrapId
	 * @return
	 */
	public ScrapWfp getScrapWfpById(String scrapId);

	/**
	 * 保存危废申报
	 * 
	 * @param wfp
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean SaveOrUpdate(ScrapWfp wfp, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 变更运输合同
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean changeTrans(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 审核危废申报
	 * 
	 * @param wfp
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean Check(String scrapId, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 获取危废报废集合
	 * 
	 * @param contractId
	 * @param contractName
	 * @param wfName
	 * @param checkStatus
	 * @param pager
	 * @return
	 */
	public List<ScrapWfp> getWfpList(String contractId, String contractName, String contractNo, String wfName, String checkStatus, String storeId, String viewType, Pager pager);

	/**
	 * 获取危废报废集合
	 * 
	 * @param contractId
	 * @param contractName
	 * @param wfName
	 * @param checkStatus
	 * @param pager
	 * @return
	 */
	public List<ScrapWfp> getWfpListLoad(String contractId, String contractName, String contractNo, String wfName, String checkStatus, String storeId, String viewType, Pager pager);

	/**
	 * 删除在线申报
	 * 
	 * @param scrapWfpId
	 * @return
	 */
	public boolean deleteScrapWfp(String scrapWfpId, HttpServletRequest request);

	/**
	 * 根据申报ID获取在线申报所关联的运输企业
	 * 
	 * @param scrapWfpId
	 * @return
	 */
	public List<ScrapWfpTrans> getTransByWfpId(String scrapWfpId);

	public ScrapWfp getChildWfp(String parentId);

	/**
	 * 根据申报单ID获取该申报单关联的有效运输企业
	 * 
	 * @param scrapWfpId
	 * @return
	 */
	public List<Store> getTransStoreByWfpId(String scrapWfpId);
}
