package com.ekfans.plugin.wuliubao.yunshu.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.user.model.User;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;




@Repository
public class WlbAppUserDao extends GenericDao implements IWlbAppUserDao {
	public WlbAppUserDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.User";
	}
	@SuppressWarnings("unchecked")
	@Override
	public User getUser(String mobile) throws Exception {
		StringBuffer sql = new StringBuffer(" from User  where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		sql.append(" and mobile = :mb");
		paramMap.put("mb", mobile);
		List<User> user=null;
	    user = (List<User>) super.list(sql.toString(), paramMap);
		if(user!=null&&user.size()!=0){
			return user.get(0);
		}
		return null;
	}

	@Override
	public void saveUser(User user) {
		try {
			super.addBean(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUser(User user) {
		try {
			super.updateBean(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Aptitude getUserAptitude(String userId) throws Exception {
		StringBuffer sql = new StringBuffer(" from Aptitude  where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		sql.append(" and authenticatorId = :userId");
		paramMap.put("userId", userId);
		List<Aptitude> ap=null;
		ap = (List<Aptitude>) super.list(sql.toString(), paramMap);
		if(ap!=null&&ap.size()!=0){
			return ap.get(0);
		}
		return null;
	}
	@Override
	public List<User> getListUser() throws Exception {
		StringBuffer sql = new StringBuffer(" from User  where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		List<User> user = (List<User>) super.list(sql.toString(), paramMap);
		return user;
	}

}
