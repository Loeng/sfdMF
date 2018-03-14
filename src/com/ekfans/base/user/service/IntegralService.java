package com.ekfans.base.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IIntegralDao;
import com.ekfans.base.user.model.IntegralLog;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 会员积分的接口的实现
 * 
 * @ClassName: IUserIntegralService
 * @author qxh
 * @date 2014-4-24 上午11:26:29
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class IntegralService implements IIntegralService {

	public static Logger logger = LoggerFactory.getLogger(IntegralService.class);

	@Resource
	private IIntegralDao integralDao;

	/**
	 * 
	 * @Title: getUserNameIntegral 获取用户的积分
	 * @param nameValue
	 * @return int 返回类型
	 * @throws
	 */
	public String getUserNameIntegral(String nameValue) {
		String integration = "noFind";
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "select integration from User where name=:name";
		map.put("name", nameValue.trim());
		
		try {
			List<?> list = integralDao.list(hql, map);
			if (list != null && list.size() > 0) {
				integration = list.get(0).toString();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return integration;
	}

	/**
	 * 管理员 修改会员积分
	 * 
	 * @Title: updateIntegral 详细业务流程: 管理员 修改会员积分
	 * @param interalLog
	 * @return String 返回类型
	 * @throws id代表传回来的manager的id
	 */
	public String updateIntegral(IntegralLog interalLog, String id) {
		try {
			String userName = interalLog.getOrderId();
			StringBuffer sql = new StringBuffer("  from User as user where 1=1 ");
			sql.append(" and user.name = :name ");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", userName);
			List<?> list = integralDao.list(sql.toString(), map);
			User hibernateUser = null;
			if (list.size() > 0) {
				hibernateUser = (User) list.get(0);
			} else {
				return "noFind";
			}

			// 设置该用户积分
			if ("1".equals(interalLog.getType())) {
				double tmepIntegration = hibernateUser.getIntegration() + interalLog.getIntegral();
				hibernateUser.setIntegration(tmepIntegration);
			} else {
				double tmepIntegration = hibernateUser.getIntegration() - interalLog.getIntegral();
				hibernateUser.setIntegration(tmepIntegration < 0 ? 0 : tmepIntegration);
			}
			integralDao.updateBean(hibernateUser);

			// 新增一条积分日志
			IntegralLog il = new IntegralLog();
			il.setUserId(hibernateUser.getId());
			il.setIntegral(interalLog.getIntegral());
			il.setOrderId("");
			il.setType(interalLog.getType());
			il.setOperation(id);

			if ("1".equals(il.getType())) {
				il.setSource(UserConst.USER_INTEGRAL_SOURCE_ADD);
			} else {
				il.setSource(UserConst.USER_INTEGRAL_SOURCE_REDUCE);
			}

			il.setNote(interalLog.getNote());
			il.setCreateTime(DateUtil.sdfDateTime.format(new Date()));
			il.setStatus(true);
			il.setInvalidTime(interalLog.getInvalidTime());
			il.setOperation(id);
			integralDao.addBean(il);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return "failure";
		}

		return "success";

	}

	/**
	 * 
	 * @Title: integralLogList 详细业务流程: 查询出　积分日志　
	 * @param pager
	 * @param name
	 * @param beginDate
	 * @param endDate
	 * @return List<IntegralLog> 返回类型
	 * @throws
	 */
	public List<IntegralLog> integralLogList(Pager pager, String name, String status, String beginDate, String endDate) {
		List<IntegralLog> list = new ArrayList<IntegralLog>();
		try {

			// 定义查询SQL
			StringBuffer sql = new StringBuffer(" select il,u.name,m.name from IntegralLog as il,User as u,Manager as m where 1=1");

			sql.append(" and m.id = il.operation");
			sql.append(" and il.userId=u.id");
			// 定义参数MAP
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// 如果查询条件输入了name，添加查询条件
			if (!StringUtil.isEmpty(name)) {
				sql.append(" and u.name like :name");
				paramMap.put("name", "%" + name + "%");
			}

			if (!StringUtil.isEmpty(status)) {
				sql.append(" and il.status = :status");
				paramMap.put("status", Boolean.parseBoolean(status));
			}

			if (!StringUtil.isEmpty(beginDate) && !StringUtil.isEmpty(endDate)) {
				sql.append(" and il.invalidTime between  :beginDate  and  :endDate ");
				paramMap.put("beginDate", beginDate);
				paramMap.put("endDate", endDate);
			}
			sql.append(" order by il.createTime desc");
			List<?> listTemp = integralDao.list(sql.toString(), paramMap);
			for (int i = 0; i < listTemp.size(); i++) {
				Object[] tempObj = (Object[]) listTemp.get(i);
				IntegralLog il = (IntegralLog) tempObj[0];

				String userName = tempObj[1] != null ? tempObj[1].toString() : "";
				String managerName = tempObj[2] != null ? tempObj[2].toString() : "";
				// 临时存值
				il.setUserId(userName);
				il.setOperation(managerName);
				// 替代当序号的临时值
				il.setOrderId(String.valueOf(i + 1));

				list.add(il);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 根据id删除用户
	 */
	public boolean deleteUserIngegral(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			integralDao.deleteById(id);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 获取积分记录的明细
	 * 
	 * @param id
	 * @return IntegralLog 返回类型
	 * @throws
	 */
	public IntegralLog getIntegralLog(String id) {
		IntegralLog integralLog = null;
		try {
			integralLog = (IntegralLog) integralDao.get(id);
			User user = (User) integralDao.get(integralLog.getUserId());
			integralLog.setUserId(user.getName());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return integralLog;
	}

	/**
	 * 保存操作
	 */
	@Override
	public boolean saveIntegralLog(IntegralLog integralLog) {
		if (integralLog == null) {
			return false;
		}
		try {
			integralDao.addBean(integralLog);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public IntegralLog getLevelIntegral(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return null;
		}
		try {
			StringBuffer sql = new StringBuffer(" select inter from IntegralLog as inter where 1=1 ");
			// 关联用户
			sql.append(" and inter.userId = :userId ");

			// 来源为修改等级
			sql.append(" and inter.source = '7' ");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);

			List<IntegralLog> list = integralDao.list(sql.toString(), map);

			if (list.size() > 0) {
				IntegralLog integration = list.get(0);
				return integration;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean deleteAllIngegralByUserId(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			StringBuffer sql = new StringBuffer(" select inter from IntegralLog as inter where 1=1 ");
			// 关联用户
			sql.append(" and inter.userId = :userId ");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", id);

			integralDao.delete(sql.toString(), map);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
}
