package com.ekfans.base.wfOrder.dao;

import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class ContractDetailsDao extends GenericDao implements IContractDetailsDao {
	public ContractDetailsDao() {
		super();
		this.beanClass = "com.ekfans.base.wfOrder.model.ContractDetails";
	}
}
