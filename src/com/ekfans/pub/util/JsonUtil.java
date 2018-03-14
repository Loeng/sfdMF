package com.ekfans.pub.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;

import com.ekfans.base.store.model.Intel;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.regexp.internal.recompile;

/**
 * 将对象或对象数组转换为json字符串
 * 
 * @author yikelizi
 * 
 */
public class JsonUtil {
	/**
	 * 任意对象转换为json字符串
	 */
	public static String objectToJson(Object object) {
		StringBuilder json = new StringBuilder();
		if (object == null) {
			json.append("\"\"");
		} else if (object instanceof String) {
			json.append("\"").append((String) object).append("\"");
		} else if (object instanceof Float) {
			json.append("\"").append(Float.toString((Float) object)).append("\"");
		} else if (object instanceof Integer) {
			json.append("\"").append(Integer.toString((Integer) object)).append("\"");
		} else if (object instanceof Boolean) {
			if ((Boolean) object == true) {
				json.append("\"").append("1").append("\"");
			} else {
				json.append("\"").append("0").append("\"");
			}

		} else if (object instanceof Double) { // 这里添加double类型
			json.append("\"").append(Double.toString((Double) object)).append("\"");
		} else if (object instanceof BigDecimal) {// 添加BigDecimal
			json.append("\"").append(object.toString()).append("\"");
		} else if (object instanceof Map) {
			Map<Object, Object> map = (Map<Object, Object>) object;
			json.append("{");

			Iterator<Map.Entry<Object, Object>> entries = map.entrySet().iterator();

			while (entries.hasNext()) {

				Map.Entry<Object, Object> entry = entries.next();
				json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
				if (entries.hasNext()) {
					json.append(",");
				}
			}
			json.append("}");
		} else {
			json.append(beanToJson(object));
		}
		return json.toString();
	}

	/**
	 * 传入任意一个 javabean 对象生成一个指定规格的字符串
	 */
	public static String beanToJson(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = objectToJson(props[i].getName());
					String value = objectToJson(props[i].getReadMethod().invoke(bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * 通过传入一个列表对象,调用指定方法将列表中的数据生成一个JSON规格指定字符串
	 */
	public static String listToJson(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 转换Map为JSON字符串
	 * 
	 * @param map
	 *            待转换的Map集合
	 * @return JSON字符串
	 */
	public static String convertToJsonString(Map<? extends Object, ? extends Object> map) {
		String jsonString = null;
		try {
			jsonString = new ObjectMapper().writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	public static JSONArray convertToJsonArray(List<?> list) {
		JSONArray array = new JSONArray();
		try {
			if (list == null || list.size() == 0) {
				return array;
			}

			// new GsonBuilder().serializeNulls().create() gson默认忽略空的对象，这样写转换时将不会忽略
			array = new JSONArray(new GsonBuilder().serializeNulls().create().toJson(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * 验证APP参数非空验证
	 * @return 验证失败的字段错误信息（如：参数错误： userId userName）
	 */
	public static String verifyParamNotNullApp(MyJSONObject jsonResquest, Object... obj) {
		String errorHeader = Cache.getAppResource("app.system.paramError") + ":";
		StringBuffer errorMsg = new StringBuffer();
		if (obj != null && obj.length > 0) {
			for (int i = 0; i < obj.length; i++) {
				// 取出要验证的参数
				String param = (String) obj[i];
				if (StringUtil.isEmpty(param)) {
					continue;
				}

				// 取出要验证的参数值
				String value = jsonResquest.getString(param);
				// 为空拼接错误消息
				if (StringUtil.isEmpty(value)) {
					errorMsg.append(" " + param);
				}
			}
		}

		// 错误消息为空时返回""，否则返回"头+错误消息"
		return StringUtil.isEmpty(errorMsg.toString()) ? "" : (errorHeader += errorMsg.toString());
	}
}
