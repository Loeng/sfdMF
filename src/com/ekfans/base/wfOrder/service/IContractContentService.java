package com.ekfans.base.wfOrder.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.wfOrder.model.ContractContent;

public interface IContractContentService {
	public List<ContractContent> findContractContentWithChilds(String contractId);

	public List<ContractContent> findContractContent(String contractId);

	/**
	 * 根据合同ID，数量和品味计算单价
	 * 
	 * @param contractId
	 * @param quantity
	 * @param contents
	 * @return
	 */
	public Double sumTotalPrice(String contractId, String quantity, Map<String, Double> contents);
}
