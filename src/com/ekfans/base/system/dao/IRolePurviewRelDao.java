package com.ekfans.base.system.dao;

import java.util.List;

import com.ekfans.base.system.model.RolePurviewRel;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 角色与权限关系表DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IRolePurviewRelDao extends IGenericDao {

    
    public void batchSaveEntity(List<RolePurviewRel> rolePurviewRelList) throws Exception;
}
