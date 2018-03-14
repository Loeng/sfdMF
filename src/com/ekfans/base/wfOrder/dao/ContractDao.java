package com.ekfans.base.wfOrder.dao;

import org.springframework.stereotype.Repository;
import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class ContractDao extends GenericDao implements IContractDao{
    public ContractDao(){
        super();
        this.beanClass="com.ekfans.base.wfOrder.model.Contract";
    }
}
