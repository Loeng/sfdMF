package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreApplyDao;
import com.ekfans.base.store.model.StoreApply;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: StoreApplyService
* @Description: TODO(店铺申请的实现类)
* @author ekfans
* @date 2014-5-16 上午11:37:07
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class StoreApplyService implements IStoreApplyService{
    // 定义DAO
    @Resource
    private IStoreApplyDao storeApplyDao;

    // 定义错误处理日志
    private Logger log = LoggerFactory.getLogger(StorageAppraiseService.class);
    /**
     * 店铺申请添加
     */
    public boolean addStoreApply(StoreApply storeApply) {
        try {

            // 将店铺资料保存到数据库
            storeApplyDao.addBean(storeApply);
            // 返回true
            return true;
        } catch (Exception e) {
            // 若出现异常，返回false
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 后台查询出所有的店铺申请信息及分页
     */
    public List<StoreApply> listAll(Pager pager, String operation, String status, String beginDate, String endDate) {
     // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sa.id,sa.contacts,sa.mobile,sa.email,sa.companyName,sa.operation from StoreApply as sa where 1=1");

        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
                
        // 如果查询条件输入了status，添加查询条件
        if (!StringUtil.isEmpty(status)) {
            sql.append(" and sa.status = :status");
            paramMap.put("status", Integer.parseInt(status));
        }

        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(operation)) {
            sql.append(" and sa.operation like :operation");
            paramMap.put("operation", "%"+operation+"%");
        }

        if(!StringUtil.isEmpty(beginDate)&&!StringUtil.isEmpty(endDate)){
            sql.append(" and sa.createTime between  :beginDate  and  :endDate ");
            paramMap.put("beginDate", beginDate);
            paramMap.put("endDate", endDate);
        }
        sql.append(" order by sa.createTime desc");
        try {
            // 调用DAO方法查找商品
            List<Object[]> list = storeApplyDao.list(pager, sql.toString(), paramMap);
            List<StoreApply> storeApplys = new ArrayList<StoreApply>();
            for(Object[] object:list){
                StoreApply sa = new StoreApply();
                sa.setId((String)object[0]);
                sa.setContacts((String)object[1]);
                sa.setMobile((String)object[2]);
                sa.setEmail((String)object[3]);
                sa.setCompanyName((String)object[4]);
                sa.setOperation((String)object[5]);
                storeApplys.add(sa);
            }
            return storeApplys;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据id删除申请店铺的信息
     */
    public boolean deteteStoreApplyByID(String id) {
     // 如果传进来的id为空，则返回false
        if (StringUtil.isEmpty(id)) {
            return false;
        }

        try {
            // 调用SERVICE的方法删除店铺
            storeApplyDao.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据id查询
     */
    public StoreApply getStoreApplyById(String id) {
       try {
           StoreApply storeApply = (StoreApply) storeApplyDao.get(id);
           return storeApply;
    } catch (Exception e) {
        log.error(e.getMessage());
    }
    return null;
    }

    /**
     * 修改店铺信息
     */
    public boolean updateStatus(StoreApply storeApply) {
        try {
            if (storeApply == null) {
                return false;
            }
         // 设置更新时间为当前系统时间
            storeApply.setUpdateTime(DateUtil.getSysDateTimeString());
            // 调用DAO的方法修改店铺
            storeApplyDao.updateBean(storeApply);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean judgmentRepeat(String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer("from StoreApply s where 1=1");

        // 如果用户名不为空，添加查询条件
        if (!StringUtil.isEmpty(name)) {
            hql.append(" and s.companyName=:name");
            map.put("name", name.trim());
        }

        try {
            List<User> list = storeApplyDao.list(hql.toString(), map);
            // 如果查询出的列表为空，即没有重复对象，返回false
            if (null == list || list.size() <= 0) {
                return false;
            }
            // 如果有重复对象，返回true
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    
}
