package com.ekfans.base.content.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.content.dao.IAdDetailDao;
import com.ekfans.base.content.model.AdDetail;
import com.ekfans.pub.util.StringUtil;

/**
 * 广告明细业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Service("adDetailServiceImpl")
public class AdDetailService implements IAdDetailService {
    //定义错误处理日志
    public static Logger log = LoggerFactory.getLogger(AdDetailService.class);
    // 定義DAO
    @Autowired
    private IAdDetailDao adDetailDao;
 
    @Override
    public List<AdDetail> getDeailsByAdvertId(String id) {
        // 如果id为空返回null
        if(StringUtil.isEmpty(id)){
            return null;
        }
        try {
                // 定义查询语句
                StringBuffer hql = new StringBuffer("select ad from AdDetail as ad where 1=1");
                Map<String,Object> params = new HashMap<String,Object>();
                // 关联广告id
                hql.append(" and ad.adId=:id");
                params.put("id",id);
                
                // 调用dao查询广告明细列表
                List<AdDetail> details = adDetailDao.list(hql.toString(),params);
                return details;
            }
            catch(Exception e) {
                log.error(e.getMessage());
            }
        return null;
    }

    @Override
    public boolean addAdvertDetail(AdDetail detail) {
        if(detail==null){
            return false;
        }
        try {
            adDetailDao.addBean(detail);
            return true;
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteDetailByAdvertId(String id) {
        if (StringUtil.isEmpty(id)) {
            return false;
        }
        // 定义sql语句
        StringBuffer sql = new StringBuffer(" select tf from AdDetail as ad where 1=1 ");
        // 定义Map函数
        Map<String, Object> hashMap = new HashMap<String, Object>();
        sql.append(" and ad.adId = :adId");
        hashMap.put("adId", id);
        try {
            adDetailDao.delete(sql.toString(), hashMap);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean modifyAdvertDetail(AdDetail detail) {
        try {
            // 调用DAO方法修改广告
            adDetailDao.updateBean(detail);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
