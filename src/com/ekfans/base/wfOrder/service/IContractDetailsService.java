package com.ekfans.base.wfOrder.service;

import java.util.List;

import com.ekfans.base.wfOrder.model.ContractDetails;

public interface IContractDetailsService {
	public List<ContractDetails> findContractDetails(String contractContendId, String name);

	/**
	 * 根据明细ID和数量获取对象
	 * 
	 * @param contengId
	 * @param num
	 * @return
	 */
	public ContractDetails getDetailPriceByNumAndContentId(String contentId, String num);
}
