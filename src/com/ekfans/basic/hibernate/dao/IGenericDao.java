/** 
 *
 * @author <a href="mailto:flustar2008@163.com">flustar</a>
 * @version 1.0 
 * Creation date: Dec 23, 2007 6:19:21 PM
 */
package com.ekfans.basic.hibernate.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.ekfans.pub.util.Pager;

@SuppressWarnings("rawtypes")
public interface IGenericDao {

	/**
	 * 创建Session
	 * 
	 * @Title: createSession
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return Session 返回类型
	 * @throws
	 */
	public Session createSession();

	/**
	 * 新增
	 * 
	 * @param po
	 *            需要持续化的临时对象
	 * @throws HibernateException
	 */
	public void addBean(Object po) throws Exception;

	public void addBean(Object po, Session session) throws Exception;

	/**
	 * 根据主键获取持久化对象
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object get(java.io.Serializable id) throws Exception;

	public Object get(java.io.Serializable id, Session session)
			throws Exception;

	/**
	 * 更新操作
	 * 
	 * @param po
	 *            持续化值对象
	 * @throws HibernateException
	 */
	public void updateBean(Object po) throws Exception;

	public void updateBean(Object po, Session session) throws Exception;

	public void saveOrUpdateBean(Object po) throws Exception;

	public void saveOrUpdateBean(Object po, Session session) throws Exception;

	public void updateBean(String hql, Map<String, Object> map)
			throws Exception;

	public void updateBean(String hql, Map<String, Object> map, Session session)
			throws Exception;

	// public void updateBean(Object po, Session sess) throws Exception;

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void deleteById(java.io.Serializable id) throws Exception;

	public void deleteById(java.io.Serializable id, Session session)
			throws Exception;

	public void deleteByIds(String[] ids) throws Exception;

	public void deleteByIds(String[] ids, Session session) throws Exception;

	/**
	 * 删除持续化对象
	 * 
	 * @param po
	 *            持续化对象
	 * @param sess
	 *            session会话
	 * @throws java.lang.Exception
	 */
	public void delete(Object po) throws Exception;

	public void delete(Object po, Session session) throws Exception;

	/**
	 * 删除所有HSQL查询结果
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void delete(String hql, Map<String, Object> map) throws Exception;

	public void delete(String hql, Map<String, Object> map, Session session)
			throws Exception;

	/**
	 * 根据查询条件得到查询结果
	 * 
	 * @param querySQL
	 *            查询条件
	 * @return 查询结果
	 * @throws HibernateException
	 */
	public List list(String querySQL, Map<String, Object> map) throws Exception;

	public List list(String querySQL, Map<String, Object> map, Session session)
			throws Exception;

	/**
	 * 带分页参数的查询
	 * 
	 * @param page
	 *            分页对象
	 * @param querySql
	 *            查询条件
	 * @return 查询结果
	 * @throws HibernateException
	 */
	public List list(Pager page, String querySql, Map<String, Object> map)
			throws Exception;

}