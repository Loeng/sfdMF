package com.ekfans.base.ccwcc.dao;

import com.ekfans.base.ccwcc.model.CcwccPush;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguoyu on 2017/4/19.
 */
@Repository
public class CcwccPushDao extends GenericDao implements ICcwccPushDao {
    public CcwccPushDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.ccwcc.model.CcwccPush";
    }

    /**
     * 查询推送
     *
     * @param pager
     * @param content
     * @param type
     * @param startTime
     * @param endTime
     * @param pushStartTime
     * @param pushEndTime
     * @return
     */
    @Override
    public List<CcwccPush> getCcwccPushList(Pager pager, String content,String status, String type, String startTime, String endTime, String pushStartTime, String pushEndTime) {
        StringBuffer sql = new StringBuffer(" from CcwccPush as cp where 1=1");
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (!StringUtil.isEmpty(content)) {
            sql.append(" and cp.content like :content");
            paramMap.put("content", "%" + content + "%");
        }
        if(!StringUtil.isEmpty(status)){
            sql.append(" and cp.status = :status");
            paramMap.put("status", status);
        }
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and cp.type = :type");
            paramMap.put("type", type);
        }

        if (!StringUtil.isEmpty(startTime)) {
            sql.append(" and cp.createTime >= :startTime");
            paramMap.put("startTime", startTime);
        }

        if (!StringUtil.isEmpty(endTime)) {
            sql.append(" and cp.createTime <= :endTime");
            paramMap.put("endTime", endTime);
        }

        if (!StringUtil.isEmpty(pushStartTime)) {
            sql.append(" and cp.pushTime >= :pushStartTime");
            paramMap.put("pushStartTime", pushStartTime);
        }
        if (!StringUtil.isEmpty(pushEndTime)) {
            sql.append(" and cp.pushTime <= :pushEndTime");
            paramMap.put("pushEndTime", pushEndTime);
        }

        sql.append(" order by cp.createTime desc");
        List<CcwccPush> ccwccPushList = null;
        try {
            if (pager != null) {
                ccwccPushList = super.list(pager, sql.toString(), paramMap);
            } else {
                ccwccPushList = super.list(sql.toString(), paramMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ccwccPushList;
    }


    /**
     * 根据ID删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean removeByIds(String[] ids) {
        if (ids == null || ids.length <= 0) {
            return false;
        }
        try {
            super.deleteByIds(ids);
            return true;
        } catch (Exception e) {

        }

        return false;
    }
}
