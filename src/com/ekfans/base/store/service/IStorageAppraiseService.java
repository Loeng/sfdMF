package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.dto.AppraiseContDto;
import com.ekfans.base.store.dto.AppraiseDto;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;

public interface IStorageAppraiseService {
  
    
    /**
     * 
    * @Title: appraiseCount
    * @Description: TODO(获取评价的类型的统计)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程) void 返回类型
    * @throws
     */
    public List<AppraiseContDto> appraiseCount(Store store);
    
    /**
     * 
    * @Title: appraiseCount
    * @Description: TODO(获取评价的类型的统计)
    * 详细业务流程:---------------------------------------------
    * (详细描述此方法相关的业务处理流程) void 返回类型
    * @throws
     */
    public List<AppraiseContDto> appraiseCount(User user);
    /**
     * 获取评价管理的信息
    * @Title: getMyStoreAppraises
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param store
    * @param pager
    * @param productName
    * @param appraiser
    * @param beginDate
    * @param endDate
    * @param status
    * @return List<Appraise> 返回类型
    * @throws
     */
    public List<AppraiseDto> getMyStoreAppraises(Store store, Pager pager, String productName, String appraiser,
            String beginDate, String endDate, String replyStatus);
    
    /**
     * 获取评价管理的信息
    * @Title: getMyStoreAppraises
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param store
    * @param pager
    * @param productName
    * @param appraiser
    * @param beginDate
    * @param endDate
    * @param status
    * @return List<Appraise> 返回类型
    * @throws
     */
    public List<AppraiseDto> getMyStoreAppraises(String source, Pager pager, String productName, String appraiser,
            String beginDate, String endDate, String replyStatus);
    
    /**
     * 获取评价管理的信息
    * @Title: getMyStoreAppraises
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param user--------------------------------------
    * @param pager
    * @param productName
    * @param appraiser
    * @param beginDate
    * @param endDate
    * @param status
    * @return List<Appraise> 返回类型
    * @throws
     */
    public List<AppraiseDto> getUserAppraises(User user, Pager pager, String productName, String appraiser,
            String beginDate, String endDate, String replyStatus);
    
    /**
     * @Title: saveAppraise
     * @Description: TODO(保存 咨询和留言的回复)
     * @param content
     * @param consultID
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveAppraise(String content,String appraiseID,Store store);
    
    /**
     * @Title: saveAppraise
     * @Description: TODO(保存 咨询和留言的回复)
     * @param content----------------------------------
     * @param consultID
     * @param user
     * @return String 返回类型
     * @throws
      */
    public String saveAppraise(String content,String appraiseID,User user);
    
    
    /**
     * @Title: saveAppraises
     * @Description: TODO(批量 保存 评价的回复)
     * @param content
     * @param appraiseIDS
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveAppraises(String content,String appraiseIDS,Store store);
    
    /**
     * @Title: saveUserAppraises
     * @Description: TODO(批量 保存 评价的回复)
     * @param content-------------------------------------------------------------
     * @param appraiseIDS
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveAppraises(String content,String appraiseIDS,User user);
    
    /**
     * 
    * @Title: getAppraiseBySource
    * @Description: TODO 根据登录的用户查的属于该用户的评价
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param source
    * @param @return    设定文件
    * @return List<Appraise>    返回类型
    * @throws
     */
    public List<AppraiseDto> getAppraiseBySource(String source, Pager pager);
    
    /**
     * 
    * @Title: getAppraiseByTarget
    * @Description: TODO 根据登录的用户查的属于该用户的评价
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param source
    * @param @return    设定文件
    * @return List<Appraise>    返回类型
    * @throws
     */
    public List<AppraiseDto> getAppraiseByTarget(String target);
}
