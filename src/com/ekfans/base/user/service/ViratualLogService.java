package com.ekfans.base.user.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IViratualLogDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.ViratualLog;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 资金账户的接口的实现
 * 
 * @ClassName: IUserIntegralService
 * @author qxh
 * @date 2014-4-24 上午11:26:29
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class ViratualLogService implements IViratualLogService {

	public static Logger logger = LoggerFactory.getLogger(ViratualLogService.class);

	@Resource
	private IViratualLogDao viratualLogDao;

	/**
     * 
     * @Title: getUserNameIntegral 获取用户的庄户
     * @param nameValue
     * @return int 返回类型
     * @throws
     */
    public String getUserNameIntegral(String nameValue,String type) {
        String integration = "noFind";
        try {
            StringBuffer sql = new StringBuffer(" select viratual from User as user where 1=1 ");
            sql.append(" and user.type = :type ");
            sql.append(" and user.name = :name ");

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", nameValue);
            map.put("type", type);
            List<?> list = viratualLogDao.list(sql.toString(), map);

            if (list.size() > 0) {
                integration = list.get(0).toString();
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return integration;
    }
    
    /**
     * 管理员 修改账户资金
     * 
     * @Title: updateIntegral 详细业务流程: 管理员 修改会员积分
     * @param interalLog
     * @return String 返回类型
     * @throws id代表传回来的manager的id
     */
    public String updateIntegral(ViratualLog v, String id) {
        try {
            String userName = v.getUserId();
            StringBuffer sql = new StringBuffer("  from User as user where 1=1 ");
            sql.append(" and user.name = :name ");

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", userName);
            List<?> list = viratualLogDao.list(sql.toString(), map);
            User hibernateUser = null;
            if (list.size() > 0) {
                hibernateUser = (User) list.get(0);
            } else {
                return "noFind";
            }

            // 设置该用户积分
            if ("1".equals(v.getType())) {
                double tmepIntegration = hibernateUser.getViratual().doubleValue() + v.getAmount().doubleValue();
                hibernateUser.setViratual(new BigDecimal(tmepIntegration));
            } else {
                double tmepIntegration = hibernateUser.getViratual().doubleValue() - v.getAmount().doubleValue();
                hibernateUser.setViratual(tmepIntegration < 0 ? new BigDecimal(0.00) : new BigDecimal(tmepIntegration));
            }
            viratualLogDao.updateBean(hibernateUser);

            // 新增一条积分日志
            ViratualLog il = new ViratualLog();
            il.setUserId(hibernateUser.getId());
            il.setAmount(v.getAmount());
            il.setType(v.getType());
            il.setOperator(id);
            if ("1".equals(il.getType())) {
                il.setSource(UserConst.USER_INTEGRAL_SOURCE_ADD);
            } else {
                il.setSource(UserConst.USER_INTEGRAL_SOURCE_REDUCE);
            }

            il.setNote(v.getNote());
            il.setCreateTime(DateUtil.getSysDateTimeString());
            viratualLogDao.addBean(il);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return "failure";
        }

        return "success";

    }

    /**
     * 
     * @Title: integralLogList 详细业务流程: 查询出　账户资金日志
     * @param pager
     * @param name
     * @param beginDate
     * @param endDate
     * @return List<IntegralLog> 返回类型
     * @throws
     */
    public List<ViratualLog> integralLogList(Pager pager, String name, String status, String beginDate, String endDate,String type) {
        List<ViratualLog> list = new ArrayList<ViratualLog>();
        try {

            // 定义查询SQL
            StringBuffer sql = new StringBuffer(
                    " select v,u.name,m.name from ViratualLog as v,User as u,Manager as m where 1=1");

            sql.append(" and m.id = v.operator");
            sql.append(" and v.userId=u.id");
            // 定义参数MAP
            Map<String, Object> paramMap = new HashMap<String, Object>();
            
            // 如果查询条件输入了type，添加查询条件
            if (!StringUtil.isEmpty(type)) {
                sql.append(" and u.type = :type");
                paramMap.put("type", type);
            }
            // 如果查询条件输入了name，添加查询条件
            if (!StringUtil.isEmpty(name)) {
                sql.append(" and u.name like :name");
                paramMap.put("name", "%" + name + "%");
            }

            if (!StringUtil.isEmpty(status)) {
                sql.append(" and v.type = :status");
                paramMap.put("status", status);
            }

            if (!StringUtil.isEmpty(beginDate) && !StringUtil.isEmpty(endDate)) {
                sql.append(" and v.createTime between  :beginDate  and  :endDate ");
                paramMap.put("beginDate", beginDate);
                paramMap.put("endDate", endDate);
            }
            sql.append(" order by v.createTime desc");
            List<?> listTemp = viratualLogDao.list(sql.toString(), paramMap);
            for (int i = 0; i < listTemp.size(); i++) {
                Object[] tempObj = (Object[]) listTemp.get(i);
                ViratualLog il = (ViratualLog) tempObj[0];

                String userName = tempObj[1] != null ? tempObj[1].toString() : "";
                String managerName = tempObj[2] != null ? tempObj[2].toString() : "";
                // 临时存值
                il.setUserId(userName);
                il.setOperator(managerName);
                // 替代当序号的临时值
                il.setOrderId(String.valueOf(i + 1));

                list.add(il);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return list;
    }
}
