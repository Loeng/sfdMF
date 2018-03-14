package com.ekfans.basic.hibernate.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ekfans.pub.util.Pager;

/**
 * DAO核心基类
 * 
 * @Title: GenericDao.java
 * @Description:易科B2B2C平台
 * @author 成都易科远见科技有限公司
 * @Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company:成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年1月30日 上午11:28:43
 */
@SuppressWarnings("rawtypes")
public class GenericDao implements IGenericDao {
	public static Logger logger = LoggerFactory.getLogger(GenericDao.class);
	@Resource
	private SessionFactory sessionFactory;

	protected String beanClass;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Session createSession() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
		} catch (Exception e) {

		}
		return session;
	}

	public void commitTransaction(Session session, Transaction transaction) throws Exception {
		try {
			session.flush();
			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * 新增
	 * 
	 * @param po
	 *            需要持续化的临时对象
	 * @throws HibernateException
	 */
	public void addBean(Object po) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(po);
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 新增
	 * 
	 * @param po
	 *            需要持续化的临时对象
	 * @throws HibernateException
	 */
	public void addBean(Object po, Session session) throws Exception {
		try {
			if (session == null || !session.isOpen()) {
				throw new Exception("Session error");
			}
			session.save(po);
		} catch (Exception e) {
			logger.error("事物处理失败：" + po.getClass().getName() + ";GenericDao-addBean");
			throw e;
		}
	}

	/**
	 * 根据主键获取持久化对象
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object get(Serializable id) throws Exception {
		Object tempBean = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tempBean = session.get(Class.forName(beanClass), id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		// if (tempBean != null) {
		// tempBean = covertNullToSpace(tempBean);
		// }
		return tempBean;
	}

	public Object get(Serializable id, Session session) throws Exception {
		Object tempBean = null;
		try {
			tempBean = session.get(Class.forName(beanClass), id);
		} catch (Exception e) {
			logger.error("事物处理失败：GenericDao-getBean");
		}
		return tempBean;
	}

	/**
	 * 更新操作
	 * 
	 * @param po
	 *            持续化值对象
	 * @throws HibernateException
	 */
	public void updateBean(Object po) throws Exception {
		Session session = null;
		Transaction transaction = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.update(po);
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新操作
	 * 
	 * @param po
	 *            持续化值对象
	 * @throws HibernateException
	 */
	public void updateBean(Object po, Session session) throws Exception {

		try {
			if (session == null || !session.isOpen()) {
				throw new Exception("Session error");
			}
			session.update(po);
		} catch (Exception e) {
			logger.error("事物处理失败：" + po.getClass().getName() + ";GenericDao-updateBean");
			throw e;
		}
	}

	/**
	 * 保存或更新操作
	 * 
	 * @param po
	 *            持续化值对象
	 * @throws HibernateException
	 */
	public void saveOrUpdateBean(Object po) throws Exception {
		Session session = null;
		Transaction transaction = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(po);
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 保存或更新操作
	 * 
	 * @param po
	 *            持续化值对象
	 * @throws HibernateException
	 */
	public void saveOrUpdateBean(Object po, Session session) throws Exception {

		try {
			if (session == null || !session.isOpen()) {
				throw new Exception("Session error");
			}
			session.saveOrUpdate(po);
		} catch (Exception e) {
			logger.error("事物处理失败：" + po.getClass().getName() + ";GenericDao-updateBean");
			throw e;
		}
	}

	public void updateBean(String hql, Map<String, Object> map) throws Exception {
		Session session = null;
		Transaction transaction = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			coverValuesFromMap(query, map, hql);

			query.executeUpdate();
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	public void updateBean(String hql, Map<String, Object> map, Session session) throws Exception {
		try {
			Query query = session.createQuery(hql);
			coverValuesFromMap(query, map, hql);

			query.executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void deleteById(Serializable id) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Object tempBean = session.load(Class.forName(beanClass), id);
			session.delete(tempBean);
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void deleteById(Serializable id, Session session) throws Exception {
		try {
			if (session == null || !session.isOpen()) {
				throw new Exception("Session error");
			}
			Object tempBean = session.load(Class.forName(beanClass), id);
			session.delete(tempBean);
		} catch (Exception e) {
			logger.error("事物处理失败：GenericDao-deleteById;Id=" + id);
			throw e;
		}
	}

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void deleteByIds(String[] ids) throws Exception {
		if (ids == null || ids.length <= 0) {
			return;
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sql = "from " + beanClass + " entity where entity.id in (";
			for (int i = 0; i < ids.length; i++) {
				sql += "'" + ids[i] + "'";
				if (i + 1 < ids.length) {
					sql += ",";
				}
			}
			sql += ")";
			String sss = getDeleteSql(sql);
			Query query = session.createQuery(sss);

			query.executeUpdate();
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void deleteByIds(String[] ids, Session session) throws Exception {
		try {
			if (session == null || !session.isOpen()) {
				throw new Exception("Session error");
			}
			String sql = "from " + beanClass + " entity where entity.id in (";
			for (int i = 0; i < ids.length; i++) {
				sql += "'" + ids[i] + "'";
				if (i + 1 < ids.length) {
					sql += ",";
				}
			}
			sql += ")";

			Query query = session.createQuery(getDeleteSql(sql));

			query.executeUpdate();
		} catch (Exception e) {
			logger.error("事物处理失败：GenericDao-deleteByIds;Ids=" + ids);
			throw e;
		}
	}

	/**
	 * 删除持续化对象
	 * 
	 * @param po
	 *            持续化对象
	 * @param sess
	 *            session会话
	 * @throws java.lang.Exception
	 */
	public void delete(Object po) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.delete(po);
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除持续化对象
	 * 
	 * @param po
	 *            持续化对象
	 * @param sess
	 *            session会话
	 * @throws java.lang.Exception
	 */
	public void delete(Object po, Session session) throws Exception {
		try {
			if (session == null || !session.isOpen()) {
				throw new Exception("Session error");
			}
			session.delete(po);
		} catch (Exception e) {
			logger.error("事物处理失败：GenericDao-delete;PoName=" + po.getClass().getName());
			throw e;
		}
	}

	/**
	 * 删除所有HSQL查询结果
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void delete(String hql, Map<String, Object> map) throws Exception {
		Session session = null;
		Transaction transaction = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(getDeleteSql(hql));
			coverValuesFromMap(query, map, hql);

			query.executeUpdate();
			commitTransaction(session, transaction);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除所有HSQL查询结果
	 * 
	 * @param id
	 * @throws HibernateException
	 */
	public void delete(String hql, Map<String, Object> map, Session session) throws Exception {
		try {
			if (session == null || !session.isOpen()) {
				throw new Exception("Session error");
			}
			Query query = session.createQuery(getDeleteSql(hql));
			coverValuesFromMap(query, map, hql);

			query.executeUpdate();
		} catch (Exception e) {
			logger.error("事物处理失败：GenericDao-deleteBySQL;SQL=" + hql);
			throw e;
		}
	}

	public List list(String querySQL, Map<String, Object> map) throws Exception {
		List list = null;
		Session session = null;

		try {
			if (session == null || !session.isOpen()) {
				session = sessionFactory.openSession();
			}

			Query query = session.createQuery(querySQL);
			coverValuesFromMap(query, map, querySQL);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return list;
	}

	public List list(String querySQL, Map<String, Object> map, Session session) throws Exception {
		List list = null;

		try {
			if (session == null || !session.isOpen()) {
				session = sessionFactory.openSession();
			}

			Query query = session.createQuery(querySQL);
			coverValuesFromMap(query, map, querySQL);
			list = query.list();
		} catch (Exception e) {
			logger.error("事物处理失败：GenericDao-list;hql=" + querySQL);
			throw e;
		}

		return list;
	}

	public List list(Pager page, String querySql, Map<String, Object> map) throws Exception {
		List list = null;
		Session session = null;

		try {
			if (session == null || !session.isOpen()) {
				session = sessionFactory.openSession();
			}

			Query query = session.createQuery(querySql);
			coverValuesFromMap(query, map, querySql);
			if (page != null) {
				query.setFirstResult(page.getFromRow());
				query.setMaxResults(page.getRowsPerPage());
			}

			list = query.list();

			if (page != null) {
				String countSQL = countSql(querySql);
				Query countQuery = session.createQuery(countSQL);
				coverValuesFromMap(countQuery, map, countSQL);
				List its = countQuery.list();
				int totalRow = 0;
				if (its != null && its.size() > 0) {
					totalRow = ((Number) its.get(0)).intValue();
				}

				page.setTotalRow(totalRow);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	/**
	 * 获取删除对象的sql
	 * 
	 * @param sql
	 * @return
	 */
	private String getDeleteSql(String sql) {
		String endSql = sql.substring(sql.indexOf("from"));

		return "delete " + endSql;

	}

	private Query coverValuesFromMap(Query query, Map<String, Object> paramMap, String sql) {
		if (paramMap != null) {
			for (String key : paramMap.keySet()) {
				if (sql.indexOf(":" + key) != -1) {
					if (paramMap.get(key) instanceof Object[]) {
						query.setParameterList(key, (Object[]) paramMap.get(key));
					} else if (paramMap.get(key) instanceof Collection) {
						query.setParameterList(key, (Collection) paramMap.get(key));
					} else {
						query.setParameter(key, paramMap.get(key));
					}
				}
			}
		}
		return query;
	}

	/**
	 * 根据查询SQL获取查询总记录数的SQL
	 * 
	 * @param querySql
	 * @return
	 */
	private String countSql(String querySql) {
		// 获取count字段
		String countStr = "*";
		int countIndex = querySql.toLowerCase().indexOf("distinct");
		if (countIndex > 0) {
			String s = querySql.substring(countIndex);
			int commaIndex = s.indexOf(",");

			int fromIndex = s.indexOf("from");

			int endIndex = commaIndex;

			if ((fromIndex >= 1) && (fromIndex < endIndex)) {
				endIndex = fromIndex;
			}

			if (endIndex > 0) {
				countStr = s.substring(0, endIndex);

				if (countStr.indexOf(".") == -1) {
					countStr = countStr.trim() + ".id";
				}
			}
		} else {
			countIndex = querySql.toLowerCase().indexOf("group by");
			if (countIndex > 0) {
				int commaIndex = querySql.indexOf("having");

				if (commaIndex == -1) {
					commaIndex = querySql.indexOf("order by");
				}

				if (commaIndex == -1) {
					commaIndex = querySql.length();
				}

				countStr = " " + querySql.substring(countIndex + "group by".length(), commaIndex);

				if (countStr.indexOf(",") != -1) {
					countStr = countStr.substring(0, countStr.indexOf(","));
				}

				countStr = "distinct " + countStr;
			}
		}

		int index = querySql.toLowerCase().indexOf(" from ");
		String counthql;

		if (index <= 0) {
			counthql = "select count(" + countStr + ") " + querySql;
		} else {
			counthql = querySql.substring(index);
			counthql = "select count(" + countStr + ") " + counthql;
		}

		int groupByIndex = counthql.indexOf("group by");
		if (groupByIndex > 0) {
			counthql = counthql.substring(0, groupByIndex);
		}

		int orderByIndex = counthql.indexOf("order by");
		if (orderByIndex > 0) {
			counthql = counthql.substring(0, orderByIndex);
		}
		return counthql;
	}
}
