package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.model.Store;
import com.ekfans.pub.util.Pager;

/**
 * 
* @ClassName: IStoreConsult
* @Description: TODO(咨询管理、留言管理)
* @author HJC
* @date 2014-5-4 上午10:38:14
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreConsultService {
    
    /**
     * 
    * @Title: getConsultByCondition
    * @Description: TODO(根据条件查询出  咨询或留言　列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param  userName    咨询人
    * @param  beginDate 
    * @param  endDate
    * @param  replyStatus 是否已经回复
    * @param  pager       页码
    * @return List<StoreConsult>    返回类型
    * @throws
     */
    public List<Consult> getConsultByCondition(Store store,String userName,
                                                    String beginDate,
                                                    String endDate, 
                                                    String replyStatus,
                                                    Pager pager);
    
    /**
     * 
    * @Title: getLeaveByCondition
    * @Description: TODO(根据条件查询留言列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param productName
    * @param @param userName
    * @param @param beginDate
    * @param @param endDate
    * @param @param status
    * @param @param pager
    * @param @return    设定文件
    * @return List<StoreConsult>    返回类型
    * @throws
     */
    
  
    public List<Consult> getLeaveByCondition(String userName,
                                                  String beginDate,
                                                  String endDate,
                                                  String status,
                                                  Pager pager);
    
    /**
    * @Title: saveConsult
    * @Description: TODO(保存 咨询和留言的回复)
    * @param content
    * @param consultID
    * @param store
    * @return String 返回类型
    * @throws
     */
    public String saveConsult(String content,String consultID,Store store);
    
    /**
     * @Title: saveConsult
     * @Description: TODO(批量 保存 咨询和留言的回复)
     * @param content
     * @param consultID
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveConsults(String content,String consultIDS,Store store);
    
    /**
    * @Title: getConsultByCondition
    * @Description: TODO 根据创建者取得咨询
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param creator
    * @param replyStatus 
    * @param endDate 
    * @param beginDate 
    * @param productName 
    * 
    * @param @return    设定文件
    * @return List<Consult>    返回类型
    * @throws
     */
    public List<Consult> getConsultByCondition(Pager pager,String creator, String productName, String beginDate, String endDate, String replyStatus);
    
    /**
     * 
    * @Title: getConsultByUserId
    * @Description: TODO 根据userId取得该用户下的留言
    * 详细业务流程:wsj
    * (详细描述此方法相关的业务处理流程)
    * @param @param creator
    * @param @param storeName
    * @param @param beginDate
    * @param @param endDate
    * @param @param replyStatus
    * @param @param pager
    * @param @return    设定文件
    * @return List<Consult>    返回类型
    * @throws
     */
    public List<Consult> getConsultByUserId(String creator,String storeName,
                                            String beginDate,String endDate,
                                            String replyStatus,Pager pager);
    /**
     * 
    * @Title: getByParentId
    * @Description: TODO 根据父类Id取得留言的回复
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param parentId
    * @param @return    设定文件
    * @return Consult    返回类型
    * @throws
     */
    public List<Consult> getByParentId(String parentId);
    
    /**
     * 
    * @Title: getConsultListByCondition
    * @Description: TODO(后台-内容审核(这块暂时都放在资讯下面)-得到咨询列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param pager
    * @param @return    设定文件
    * @return List<Consult>    返回类型
    * @throws
     */
    public List<Consult> getConsultListByCondition(String content,String person,
                                                   String beginDate,String endDate,
                                                   String consultType,Pager pager);
    
    /**
     * 
    * @Title: getConsultDetailByConsultId
    * @Description: TODO(后台)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param consultId
    * @param @return    设定文件
    * @return Consult    返回类型
    * @throws
     */
    public Consult getConsultDetailByConsultId(String consultId);
    
    /**
     * 
    * @Title: checkConsult
    * @Description: TODO(内容审核(咨询、留言))
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param checkMan
    * @param @param checkStatus
    * @param @param checkNote
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public void checkConsult(String consultId,String checkMan,Integer checkStatus,String checkNote);
    
   
}


