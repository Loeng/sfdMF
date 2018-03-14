package com.ekfans.base.user.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IUserLevelDao;
import com.ekfans.base.user.model.UserLevel;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 会员等级业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class UserLevelService implements IUserLevelService {
    // 定义DAO
    @Autowired
    private IUserLevelDao userLevelDao;

    /**
     * 查询等级列表
     * 
     * @param page
     * @return
     */
    public List<UserLevel> getLevels(Pager page) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select userLevel from UserLevel as userLevel where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            List<UserLevel> list = userLevelDao.list(page, sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增会员等级
     * 
     * @param level
     * @param uploadFile
     * @param uploadFileContentType
     * @param uploadFileName
     * @return
     */
    public boolean addLevel(UserLevel level, String uploadFileName) {
        // 如果传进来的会员等级对象为空，则返回false
        if (level == null) {
            return false;
        }
        try {

            // 调用SERVICE的方法添加会员等级
            userLevelDao.addBean(level);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据id删除会员等级
     * 
     * @param id
     * @return
     */
    public boolean deleteLevel(String id) {
        // 如果传进来的id为空，则返回false
        if (StringUtil.isEmpty(id)) {
            return false;
        }

        try {
            // 删除旧图标
            UserLevel level = (UserLevel) userLevelDao.get(id);
            if (!StringUtil.isEmpty(level.getIcon())) {
                File file = new File(level.getIcon());
                if (file.exists()) {
                    file.delete();
                }
            }
            // 调用SERVICE的方法删除会员等级
            userLevelDao.deleteById(id);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改会员等级
     * 
     * @param level
     * @param uploadFile
     * @param uploadFileContentType
     * @param uploadFileName
     * @return
     */
    public boolean modifyLevel(UserLevel level, String uploadFileName) {
        // 如果传进来的会员等级对象为空，则返回false
        if (level == null) {
            return false;
        }
        try {

            // 删除旧图标
            if (!StringUtil.isEmpty(level.getIcon())) {
                File file = new File(level.getIcon());
                if (file.exists()) {
                    file.delete();
                }
            }

            // 调用DAO的方法修改会员等级
            userLevelDao.updateBean(level);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public UserLevel getLevel(String id) {
        // 如果传进来的id为空，则返回null
        if (StringUtil.isEmpty(id)) {
            return null;
        }

        try {
            // 调用SERVICE的方法查找会员等级
            return (UserLevel) userLevelDao.get(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据会员等级名取得会员等级
     */
    public UserLevel getUserLevelByName(String name) {
        StringBuffer hql = new StringBuffer("from UserLevel as ul where 1=1 and ul.name =:name");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        try {
            List<UserLevel> list = userLevelDao.list(hql.toString(), map);
            return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}