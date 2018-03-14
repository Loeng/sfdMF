package com.ekfans.base.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.IMessageConfigDetailDao;
import com.ekfans.base.system.model.MessageConfigDetail;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class MessageConfigDetailService implements IMessageConfigDetailService {
    // 定义Log
    public static Logger log = LoggerFactory.getLogger(MessageConfigDetailService.class);
    // 注入Dao
    @Autowired
    private IMessageConfigDetailDao messageConfigDetailDao;
    
	public List<MessageConfigDetail> listDetail(Pager pager, String name, String id) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select m from MessageConfigDetail as m where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(name)) {
            sql.append(" and m.name  like :name");
            paramMap.put("name","%" + name +"%");
        }
        
        // 如果查询条件输入了id，添加查询条件
        if (!StringUtil.isEmpty(id)) {
            sql.append(" and m.id  like :id");
            paramMap.put("id","%" + id +"%");
        }

        // 按index排序
        sql.append(" order by m.index asc");
        
        try {
            // 调用DAO方法查找信息配置模板
            List<MessageConfigDetail> list = messageConfigDetailDao.list(pager, sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            // 将报错的信息打印到日志表
            log.error(e.getMessage());
        }
        return null;
    }

    public MessageConfigDetail getDetailById(String id) {
        if (id == null) {
            return null;
        }
        try {
            // 调用DAO方法查找信息配置模板
            return (MessageConfigDetail) messageConfigDetailDao.get(id);
        } catch (Exception e) {
            // 将报错的信息打印到日志表
            log.error(e.getMessage());
        }
        return null;
    }

    public boolean modifyDetail(MessageConfigDetail detail) {
        if(detail==null){
            return false;
        }
        try {
            // 调用DAO方法修改信息配置模板
            messageConfigDetailDao.updateBean(detail);
            return true;
        } catch (Exception e) {
            // 将报错的信息打印到日志表
            log.error(e.getMessage());
        }
        return false;
    }
}
