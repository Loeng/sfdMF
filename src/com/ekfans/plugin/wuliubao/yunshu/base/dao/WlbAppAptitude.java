package com.ekfans.plugin.wuliubao.yunshu.base.dao;


import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

import javax.annotation.Resource;

@Repository
public class WlbAppAptitude extends GenericDao implements IWlbAppAptitude{
	@Resource
	private SessionFactory sessionFactory;
	public WlbAppAptitude() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude";
	}
	@SuppressWarnings("unchecked")
	@Override
	public Aptitude getAptitude(String authenticatorId) throws Exception {
		StringBuffer sql = new StringBuffer(" from Aptitude where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		sql.append(" and authenticatorId = :mb");
		paramMap.put("mb", authenticatorId);
		List<Aptitude> user=null;
	    user = (List<Aptitude>) super.list(sql.toString(), paramMap);
		if(user!=null&&user.size()!=0){
			return user.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Aptitude> getCheckAptitude(Pager pager,String nickName,String name) throws Exception {
		StringBuffer sql = new StringBuffer(" from Aptitude where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(nickName)){
			sql.append(" and nickName = :nickName");
			paramMap.put("nickName", nickName);
		}
		if(!StringUtil.isEmpty(name)){
			sql.append(" and name = :name");
			paramMap.put("name",name);
		}
		sql.append(" and state = :state");
		paramMap.put("state", "1");
		//按更新时间倒序
		sql.append(" order by updateTime desc");
		List<Aptitude> user=null;
	    user = (List<Aptitude>) super.list(pager,sql.toString(), paramMap);
	    return user;
	}
	@Override
	public int getAptitudeNum() throws Exception {
		String sql= "select count(1) from Aptitude where state = '1'";
		@SuppressWarnings("unchecked")
		List<BigInteger> num = super.createSession().createSQLQuery(sql).list();
		return num.get(0).intValue();
	}

}
