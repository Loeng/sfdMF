package com.ekfans.plugin.cache.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存引擎接口的默认实现
 * 
 * @Title: DefaultCacheEngine.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-17
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DefaultCacheEngine implements CacheEngine {

	private Map cache = new HashMap();

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#add(java.lang.String,
	 *      java.lang.Object)
	 */
	public void add(String key, Object value) {
		this.add(defaultFqn, key, value);
		// log.info(defaultFqn + " | Key = " + key + " success");
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#add(java.lang.String,
	 *      java.lang.String, java.lang.Object)
	 */
	public void add(String fqn, String key, Object value) {
		Map m = (Map) this.cache.get(fqn);
		if (m == null) {
			m = new HashMap();
		}

		m.put(key, value);
		this.cache.put(fqn, m);
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#get(java.lang.String,
	 *      java.lang.String)
	 */
	public Object get(String fqn, String key) {
		Map m = (Map) this.cache.get(fqn);
		if (m == null) {
			return null;
		}

		return m.get(key);
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#get(java.lang.String)
	 */
	public Object get(String key) {
		return this.get(defaultFqn, key);
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#getValues(java.lang.String)
	 */
	public List getValues(String fqn) {
		Map m = (Map) this.cache.get(fqn);
		if (m == null) {
			return new ArrayList();
		}

		return new ArrayList(m.values());
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#init()
	 */
	public void init() {
		this.cache = new HashMap();
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#stop()
	 */
	public void stop() {
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#remove(java.lang.String,
	 *      java.lang.String)
	 */
	public void remove(String fqn, String key) {
		Map m = (Map) this.cache.get(fqn);
		if (m != null) {
			m.remove(key);
		}
	}

	public void remove(String key) {
		Map m = (Map) this.cache.get(defaultFqn);
		if (m != null) {
			m.remove(key);
		}
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#remove(java.lang.String)
	 */
	public void removeAll(String fqn) {
		this.cache.remove(fqn);
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#remove(java.lang.String)
	 */
	public void removeAll() {
		this.cache.remove(defaultFqn);
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#remove(java.lang.String)
	 */
	public int size(String fqn) {
		Map map = (Map) cache.get(fqn);
		if (map == null) {
			return 0;
		}

		return map.size();
	}

	/**
	 * @see com.CacheEngine.pub.util.cache.CacheEngine#remove(java.lang.String)
	 */
	public int size() {
		return this.size(defaultFqn);
	}

	/**
	 * Removes a default cache
	 * 
	 * @param key
	 *            The fqn to remove
	 */
	public String[] getKeys(String fqn) {
		try {
			Map m = (Map) this.cache.get(fqn);
			if (m != null) {
				Set keys = m.keySet();
				if (keys != null) {
					String[] returnKeys = new String[keys.size()];
					Object[] its = keys.toArray();
					for (int i = 0; i < its.length; i++) {
						returnKeys[i] = (String) its[i];
					}

					return returnKeys;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 返回所有默认的Keys
	 */
	public String[] getKeys() {
		try {
			Map m = (Map) this.cache.get(defaultFqn);
			if (m != null) {
				Set keys = m.keySet();

				if (keys != null && keys.size() > 0) {
					String[] returnKeys = new String[keys.size()];
					Object[] its = keys.toArray();
					for (int i = 0; i < its.length; i++) {
						returnKeys[i] = (String) its[i];
					}

					return returnKeys;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public void set(String key, Object value) {
		// TODO Auto-generated method stub
		this.set(key, value);

	}
}
