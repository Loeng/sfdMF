package com.ekfans.pub.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie的工具类  
 * <p>
 * Title: CookieUtil.java
 * </p>
 * <p>
 * Description:易科B2B2C平台
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:EKFANS
 * </p>
 * 
 * @author yikelizi   
 * @date 2014-2-28 下午11:01:30   
 * @version V1.0 
 */
@SuppressWarnings( { "rawtypes", "unchecked" })
public class CookieUtil {
	// Cookie key - 管理员
	private static final String MANAGER_KEY = "manager/";

	// Cookie Key - 店铺
	private static final String STORE_KEY = "store/";

	// Cookie Key - 会员
	private static final String USER_KEY = "user/";

	public static String getManagerKey() {
		return MANAGER_KEY;
	}

	// request对象
	private HttpServletRequest request = null;
	// 获取Response
	private HttpServletResponse response = null;
	// Cookie集合
	private Map cookieMap = new HashMap();

	public CookieUtil(HttpServletRequest request, HttpServletResponse response) {
		// 将传进来的值赋给对应对象
		this.request = request;
		this.response = response;

		Cookie ck[] = this.request.getCookies();
		if (ck != null && ck.length > 0) {
			for (int i = 0; i < ck.length; i++) {
				this.cookieMap.put(ck[i].getName(), ck[i]);
			}
		}
	}

	/**
	 * 根据Key获取Cookie
	 * 
	 * @param key
	 * @return
	 */
	public Cookie getCookieByKey(String key) {
		Cookie cookie = (Cookie) cookieMap.get(key);
		return cookie;
	}

	/**
	 * 从Cookie中移除Cookie
	 * 
	 * @param key
	 * @return
	 */
	public boolean removeCookieByKey(String key) {
		try {
			Cookie cookie = this.getCookieByKey(key);
			cookie.setMaxAge(0);
			cookie.setValue(null);
			response.addCookie(cookie);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 从Cookie中移除Cookie
	 * 
	 * @param key
	 * @return
	 */
	public boolean addCookie(String key, String value) {
		try {
			Cookie oldCookie = getCookieByKey(key);
			if (oldCookie != null) {
				return true;
			}
			Cookie cookie = new Cookie(key, value);
			cookie.setMaxAge(10000 * 60 * 60 * 60 * 24);
			response.addCookie(cookie);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 添加管理员用户Cookie
	 * 
	 * @param managerId
	 * @return
	 */
	public boolean addManagerCookie(String name, String password) {
		try {
			String key = MANAGER_KEY + name;
			addCookie(key, password);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 删除管理员用户Cookie
	 * 
	 * @param managerId
	 * @return
	 */
	public boolean removeManagerCookie(String managerId) {
		try {
			String key = MANAGER_KEY + managerId;
			removeCookieByKey(key);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 获取管理员所保存的Cookie集合
	 * 
	 * @return
	 */
	public String[] getManagerLoginCookie() {
		String[] returnStr = new String[2];
		if (this.cookieMap != null && this.cookieMap.size() > 0) {
			Set<String> key = this.cookieMap.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				String keyName = (String) it.next();
				if (!StringUtil.isEmpty(keyName) && keyName.startsWith(MANAGER_KEY)) {
					Cookie cookie = (Cookie) this.cookieMap.get(keyName);
					String name = keyName.substring(keyName.indexOf("/") + 1, keyName.length());
					returnStr[0] = name;
					returnStr[1] = cookie.getValue();
					break;
				}
			}

		}
		return returnStr;
	}

	/**
	 * 添加店铺用户Cookie
	 * 
	 * @param managerId
	 * @return
	 */
	public boolean addStoreCookie(String id, String userName) {
		try {
			String key = STORE_KEY + id;
			addCookie(key, userName);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 删除店铺用户Cookie
	 * 
	 * @param managerId
	 * @return
	 */
	public boolean removeStoreCookie(String id) {
		try {
			String key = STORE_KEY + id;
			removeCookieByKey(key);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 获取店铺用户所保存的Cookie集合
	 * 
	 * @return
	 */
	public String getStoreLoginNameCookie() {
		if (this.cookieMap != null && this.cookieMap.size() > 0) {
			Set<String> key = this.cookieMap.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				String keyName = (String) it.next();
				if (!StringUtil.isEmpty(keyName) && keyName.startsWith(STORE_KEY)) {
					Cookie cookie = (Cookie) this.cookieMap.get(keyName);
					return cookie.getValue();
				}
			}

		}
		return "";
	}

	/**
	 * 获取会员所保存的Cookie集合
	 * 
	 * @return
	 */
	public String getUserLoginNameCookie() {
		if (this.cookieMap != null && this.cookieMap.size() > 0) {
			Set<String> key = this.cookieMap.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				String keyName = (String) it.next();
				if (!StringUtil.isEmpty(keyName) && keyName.startsWith(USER_KEY)) {
					Cookie cookie = (Cookie) this.cookieMap.get(keyName);
					return cookie.getValue();
				}
			}

		}
		return "";
	}

	/**
	 * 根据Key获取Cookie的值
	 * 
	 * @param key
	 * @return
	 */
	public String getValueByKey(String key) {
		try {
			Cookie cookie = getCookieByKey(key);
			if (cookie != null) {
				return cookie.getValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}
