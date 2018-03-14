package com.ekfans.base.content.service;

import java.util.List;

import com.ekfans.base.content.model.AdDetail;

/**
 * 广告明细实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IAdDetailService {
    /**
     * 
    * @Title: getDeailsByAdvertId
    * @Description: TODO(根据广告id获取广告明细的集合)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return List<AdDetail> 返回类型
    * @throws
     */
    public List<AdDetail> getDeailsByAdvertId(String id);
    
    /**
     * 
    * @Title: addAdvertDetail
    * @Description: TODO(新增广告明细)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param detail
    * @return boolean 返回类型
    * @throws
     */
    public boolean addAdvertDetail(AdDetail detail);
    
    /**
     * 
    * @Title: deleteDetailByAdvertId
    * @Description: TODO(根据广告id删除广告明细)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return boolean 返回类型
    * @throws
     */
    public boolean deleteDetailByAdvertId(String id);
    
    /**
     * 
    * @Title: modifyAdvert
    * @Description: TODO(修改广告明细)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param ad
    * @return boolean 返回类型
    * @throws
     */
    public boolean modifyAdvertDetail(AdDetail detail);
}
