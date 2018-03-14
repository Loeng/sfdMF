package com.ekfans.base.user.service;

import java.util.List;

import com.ekfans.base.user.model.IntegralLog;
import com.ekfans.pub.util.Pager;

/**
 * 会员积分的接口
* @ClassName: IUserIntegralService
* @author qxh
* @date 2014-4-24 上午11:26:29
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IIntegralService {

    /**
     * 
    * @Title: getUserNameIntegral
    * 获取用户的积分
    * @param nameValue
    * @return String 返回类型
    * @throws
     */
    public String getUserNameIntegral(String nameValue);
    
    
    /**
     * 管理员 修改会员积分
    * @Title: updateIntegral
    * 详细业务流程:
    * 管理员 修改会员积分
    * @param interalLog
    * @return String 返回类型
    * @throws
     */
    public String updateIntegral(IntegralLog interalLog,String id);
//    public boolean updateIntegral(IntegralLog interalLog);
    
    /**
     * 
    * @Title: integralLogList
    * 详细业务流程:
    *  查询出　积分日志　
    * @param pager
    * @param name
    * @param beginDate
    * @param endDate
    * @return List<IntegralLog> 返回类型
    * @throws
     */
    public List<IntegralLog> integralLogList(Pager pager, String name, String status,String beginDate,String endDate);
    
    /**
     * 
    * @Title: deleteLevelIntegral
    * @Description: TODO(获取用户因管理员修改等级而记录的日志)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param userName
    * @return IntegralLog 返回类型
    * @throws
     */
    public IntegralLog getLevelIntegral(String userId);
    /**
     * 根据id删除积分日志
     */
    public boolean deleteUserIngegral(String id);
    
    /**
     * 
    * @Title: deleteAllIngegralByUserId
    * @Description: TODO(删除会员对应的积分日志)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return boolean 返回类型
    * @throws
     */
    public boolean deleteAllIngegralByUserId(String id);
    
    /**
     * 获取积分记录的明细
    * @param id
    * @return IntegralLog 返回类型
    * @throws
     */
    public IntegralLog getIntegralLog(String id);
    /**
     * 
    * @Title: saveIntegralLog
    * @Description: TODO  保存操作
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param integralLog
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean saveIntegralLog(IntegralLog integralLog);
}
