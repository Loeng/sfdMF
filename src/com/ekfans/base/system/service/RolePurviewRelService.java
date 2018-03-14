package com.ekfans.base.system.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.IRolePurviewRelDao;
import com.ekfans.base.system.model.RolePurviewRel;
import com.ekfans.pub.util.StringUtil;

/**
 * 角色权限关系表业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Service
public class RolePurviewRelService implements IRolePurviewRelService {
	// 定義DAO
	@Autowired
	private IRolePurviewRelDao rolePurviewRelDao;

    /**
     * 添加关联表
     */
    public boolean addRolePurviewRel(RolePurviewRel rpr) {
        if (rpr == null) {
            return false;
        }
        try {
            // 调用DAO方法添加
            rolePurviewRelDao.addBean(rpr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据id删除
     */
    public boolean delete(String id) {
     // 如果传进来的id为空，则返回false
        if (StringUtil.isEmpty(id)) {
            return false;
        }

        try {
            // 调用SERVICE的方法删除店铺
            rolePurviewRelDao.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  修改
     */
    public boolean modify(RolePurviewRel rpr) {
     // 如果传进来的店铺对象为空，则返回false
        if (rpr == null) {
            return false;
        }
        try {
            // 调用DAO的方法修改店铺
            rolePurviewRelDao.updateBean(rpr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    
    }
    /**
     * 根据roleId删除对应数据
     */
    public boolean getRoleIdDelete(String roleId) {
     // 定义查询SQL
        StringBuffer sql = new StringBuffer(" delete rpr from RolePurviewRel as rpr where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //如果roleId不为空
        if (!StringUtil.isEmpty(roleId)) {
            // 添加查询条件
            sql.append(" and rpr.roleId = :roleId");
            paramMap.put("roleId", roleId);
        }
        try {
            rolePurviewRelDao.delete(sql.toString(), paramMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
