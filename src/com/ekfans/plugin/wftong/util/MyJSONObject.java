package com.ekfans.plugin.wftong.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * org.json.JSONObject包装类
 * @author 成都易科远见有限公司
 *
 */
public class MyJSONObject extends JSONObject {

	public MyJSONObject() {
		super();
	}

	public MyJSONObject(String jsonString) throws JSONException {
		super(jsonString);
	}

	public MyJSONObject(Object object) {
		super(object);
	}

	/**
	 * 重写getString,避免过多has判断，默认值为""
	 */
	@Override
	public String getString(String key) {
		String value = "";

		try {
			if (super.has(key)) {
				value = super.getString(key);
			}
		} catch (JSONException e) {
			System.out.println("Warning : getString error in MyJSONObject.");
		}
		return value;
	}

	public void putBean(java.lang.Object bean) {
		if (bean == null) {
			return;
		}
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = (String) property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = (Object) getter.invoke(bean);

					super.put(key, value);
				}
			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}
	}

	/**
	 * 重写getJSONArray,避免过多has判断，默认值为空对象
	 */
	@Override
	public JSONArray getJSONArray(String key) {
		JSONArray jsonArray = new JSONArray();
		try {
			if (super.has(key)) {
				jsonArray = super.getJSONArray(key);
			}
		} catch (JSONException e) {
			System.out.println("Warning : getJSONArray error in MyJSONObject.");
		}
		return jsonArray;
	}

}
