package com.ekfans.base.system.service;

import java.util.List;

import com.ekfans.base.system.model.MessageConfigDetail;
import com.ekfans.pub.util.Pager;

/**
 * 邮箱配置模板实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IMessageConfigDetailService {
    /**
     * 
    * @Title: listDetail
    * @Description: TODO(查询信息配置模板列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pager 分页
    * @param name 模板名
    * @param id 模板id
    * @return List<Channel> 返回类型
    * @throws
     */
    public List<MessageConfigDetail> listDetail(Pager pager, String name, String id);
    
    /**
     * 
    * @Title: getDetailById
    * @Description: TODO(根据id查找信息配置模板)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return MessageConfigDetail 返回类型
    * @throws
     */
    public MessageConfigDetail getDetailById(String id);
    
    /**
     * 
    * @Title: modifyDetail
    * @Description: TODO(修改信息配置模板)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param detail 信息模板实体类
    * @return boolean 返回类型
    * @throws
     */
    public boolean modifyDetail(MessageConfigDetail detail);
}
