package com.ekfans.base.user.service;

import java.util.List;

import com.ekfans.base.user.model.ViratualLog;
import com.ekfans.pub.util.Pager;

/**
 * 账户资金的接口
* @ClassName: IUserIntegralService
* @author qxh
* @date 2014-4-24 上午11:26:29
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IViratualLogService {

    /**
     * 
    * @Title: getUserNameIntegral
    * 获取用户的账户
    * @param nameValue
    * @return String 返回类型
    * @throws
     */
    public String getUserNameIntegral(String nameValue,String type);
    
    /**
     * 管理员 修改账户积分
    * @Title: updateIntegral
    * 详细业务流程:
    * 管理员 修改会员积分
    * @param interalLog
    * @return String 返回类型
    * @throws
     */
    public String updateIntegral(ViratualLog v,String id);
    
    /**
     * 
    * @Title: integralLogList
    * 详细业务流程:
    *  查询出　账户日志　
    * @param pager
    * @param name
    * @param beginDate
    * @param endDate
    * @return List<IntegralLog> 返回类型
    * @throws
     */
    public List<ViratualLog> integralLogList(Pager pager, String name, String status,String beginDate,String endDate,String type);
}
