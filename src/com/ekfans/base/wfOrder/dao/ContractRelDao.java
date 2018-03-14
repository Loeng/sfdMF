package com.ekfans.base.wfOrder.dao;

import org.springframework.stereotype.Repository;
import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class ContractRelDao extends GenericDao implements IContractRelDao {
	public ContractRelDao() {
		super();
		this.beanClass = "com.ekfans.base.wfOrder.model.ContractRel"; 
	}
}
