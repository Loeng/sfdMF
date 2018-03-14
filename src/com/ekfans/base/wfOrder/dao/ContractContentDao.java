package com.ekfans.base.wfOrder.dao;

import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class ContractContentDao extends GenericDao implements IContractContentDao{
    public ContractContentDao(){
        super();
        this.beanClass="com.ekfans.base.wfOrder.model.ContractContent";
    }
}
