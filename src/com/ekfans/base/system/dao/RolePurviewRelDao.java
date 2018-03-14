package com.ekfans.base.system.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.ekfans.base.system.model.RolePurviewRel;
import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 角色与权限关系DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
public class RolePurviewRelDao extends GenericDao implements IRolePurviewRelDao {
	
	@Override
	public void batchSaveEntity(List<RolePurviewRel> rolePurviewRelList) throws Exception {
	    //在session工厂中获取当前session
	    Session session = this.getSessionFactory().getCurrentSession();
	    //在session获取事务
	    Transaction tran = session.beginTransaction();
	    if(null != rolePurviewRelList){
		int i = 0;
		for(;i < rolePurviewRelList.size();i++){
		    session.save(rolePurviewRelList.get(i));
		    if(i%25==0){
			session.flush();
			session.clear();
		    }
		}
		if(i%25==0){
		    session.flush();
		    session.clear();
		}
	    }
	    //提交事务，保存到数据库
	    tran.commit();
	    //关闭事务
	    session.close();
	}
    	
    	
}
