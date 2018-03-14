package com.ekfans.plugin.wuliubao.yunshu.controller.util;

import java.lang.reflect.Field;

import org.json.JSONException;

import com.ekfans.pub.util.JsonObject;

public class JsonToObject {
	public Object getObject(byte[] by) throws IllegalArgumentException, IllegalAccessException, JSONException{
		String s  =  new String(by);
		 s = s.replaceAll("\r|\n", "");
		JsonObject jsonObject = new JsonObject(s);
		Field[] fields = this.getClass().getFields(); 
		for(Field str : fields){
			Object name;
			try {
				name = jsonObject.get(str.getName());
				str.set(this, name);
			} catch (JSONException e) {
				System.out.println(str.getName());
			}
			
		}
		return this;
	}
}
