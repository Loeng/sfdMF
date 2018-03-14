package com.ekfans.base.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.user.dao.IUserAddressDao;
import com.ekfans.base.user.model.UserAddress;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 用户地址Service接口实现
 * 
 * @ClassName: UserAddressService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class UserAddressService implements IUserAddressService {

	public static Logger log = LoggerFactory.getLogger(UserAddressService.class);
	@Resource
	private IUserAddressDao userAddressDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAddress> listAddress(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return null;
		}

		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		String hql = "from UserAddress where userId=:userId order by status desc";
		// setting data
		params.put("userId", userId);

		try {
			return this.userAddressDao.list(hql, params);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addAdddress(UserAddress ud) {
		if (ud == null) {
			return false;
		}

		try {
			ud.setCreateTime(DateUtil.getSysDateTimeString());
			this.userAddressDao.addBean(ud);

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean setDefaultAddress(String id, String trueId) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		Session session = null;
		Transaction tran = null;
		try {
			session = this.userAddressDao.createSession();
			tran = session.beginTransaction();

			// params data
			Map<String, Object> params = new HashMap<String, Object>();
			// hql
			String hql = "update UserAddress set status=:status";
			// setting data
			params.put("status", false);
			this.userAddressDao.updateBean(hql, params, session);
			params.clear();

			// hql
			String hql1 = "update UserAddress set status=:status where id=:id";
			// setting data
			params.put("status", true);
			params.put("id", id);

			this.userAddressDao.updateBean(hql1, params, session);

			tran.commit();
			session.flush();

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			if (tran != null) {
				tran.rollback();
			}
			return false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	@Override
	public boolean deleteAdddress(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			this.userAddressDao.deleteById(id);

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean modifyAddress(UserAddress ud) {
		if (ud == null) {
			return false;
		}

		try {
			this.userAddressDao.updateBean(ud);

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public UserAddress getUserAddressById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}

		try {
			return (UserAddress) userAddressDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object[] storeLoginByName(String address) {
		if (StringUtil.isEmpty(address)) {
			return null;
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select ua.id,ua.name,ua.city,ua.address,ua.zipcode,ua.mobile from UserAddress as ua,User as user where 1=1");
		sql.append("and ua.id = user.id");
		sql.append("and username = :name");
		paraMap.put("name", address);
		try {
			List list = userAddressDao.list(sql.toString(), paraMap);
			if (list != null && list.size() > 0) {
				Object[] obj = (Object[]) list.get(0);
				if (obj != null && obj.length > 0) {
					// 定义返回的Object数组
					Object[] returnObj = new Object[2];
					UserAddress userAddress = new UserAddress();
					userAddress.setId((String) obj[1]);
					userAddress.setName((String) obj[2]);
					// userAddress.setCity((String)obj[3]);
					userAddress.setAddress((String) obj[4]);
					userAddress.setZipcode((String) obj[5]);
					userAddress.setMobile((String) obj[6]);
					returnObj[1] = address;
					return returnObj;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询出所有的省
	 */
	public List<SystemArea> getProvinceList() {
		// / 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select distinct(sa) from SystemArea as sa where 1=1");
		// 根据父ID查询
		String rootCode = Cache.getResource("area.parent.code");
		sql.append(" and sa.parentCode =:rootCode");
		paramMap.put("rootCode", rootCode);
		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List<SystemArea> List = userAddressDao.list(sql.toString(), paramMap);
			return List;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据父地区ID获取下级地区集合
	 * 
	 * @param parentId
	 *            父地区ID
	 * @return
	 */
	public List<SystemArea> getChildAreasByParentId(String parentId) {
		// 如果传进来的地区ID为空，则返回空串
		if (StringUtil.isEmpty(parentId)) {
			return null;
		}

		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select distinct(sa) from SystemArea as sa where 1=1");
		// 根据父ID查询
		sql.append(" and sa.parentCode = :parentId");
		paramMap.put("parentId", parentId);
		// 排序：按照首拼字母升序，ID升序
		sql.append(" order by sa.id asc");

		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List<SystemArea> areaList = userAddressDao.list(sql.toString(), paramMap);
			return areaList;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据用户id查询出默认地址
	 */
	public UserAddress getDefaultAddess(String userId) {
		StringBuffer sql = new StringBuffer(" select ua from UserAddress as ua where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and ua.userId = :userId");
			paramMap.put("userId", userId);
		}

		sql.append(" and ua.status = true");
		try {
			List<UserAddress> list = userAddressDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void cancelDefaultAddress(UserAddress userAddress) {
		try {
			userAddressDao.updateBean(userAddress);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}