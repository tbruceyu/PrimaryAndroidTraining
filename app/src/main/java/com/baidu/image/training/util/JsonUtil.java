/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	/**
	 *
	 * 根据Json字符串解析JsonObject
	 *
	 * @param class1
	 *            类
	 * @param jsonstr
	 *            json字符串
	 * @return 对象
	 */
	public static <T> T ParseJsonObject(Class<T> class1, String json) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(json);
			jsonObject = jsonObject.getJSONObject("data");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fromJsonObject(class1, jsonObject);
	}

	/**
	 * 解析JsonArray数据
	 * 
	 * @param class1
	 *            类
	 * @param json
	 *            json字符串
	 * @return
	 */
	public static <T> List<T> ParseJsonArray(Class<T> class1, String json) {
		JSONArray jsonArray = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			jsonArray = jsonObject.getJSONArray("data");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		List<T> list = null;
		try {
			if (jsonArray != null) {
				list = new ArrayList<T>();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject;
					jsonObject = jsonArray.getJSONObject(i);
					T object = fromJsonObject(class1, jsonObject);
					list.add(object);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 解析JsonObject数据
	 * 
	 * @param class1
	 *            类
	 * @param jsonObject
	 *            JsonObject对象
	 * @return
	 */
	public static <T> T fromJsonObject(Class<T> class1, JSONObject jsonObject) {
		T obj = null;
		try {
			obj = class1.newInstance();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String FieldName = field.getName();
				String typeName = field.getType().toString();
				boolean isNull = jsonObject.isNull(FieldName);
				if (!isNull) {
					Object value = jsonObject.get(field.getName());
					if (value != null && !value.toString().equals("")) {
						if (typeName.endsWith("int")
								|| typeName.endsWith("java.lang.Integer")) { // 数据类型为int或Integer
							field.set(obj, jsonObject.getInt(FieldName));
						} else if (typeName.endsWith("long")
								|| typeName.endsWith("java.lang.Long")) { // 数据类型为String
							field.set(obj, jsonObject.getLong(FieldName));
						} else if (typeName.endsWith("String")
								|| typeName.endsWith("java.lang.String")) { // 数据类型为String
							field.set(obj, jsonObject.getString(FieldName));
						} else if (typeName.endsWith("double")
								|| typeName.endsWith("java.lang.Double")) {// 数据类型为double
							field.set(obj, jsonObject.getDouble(FieldName));
						} else if (typeName.endsWith("float")
								|| typeName.endsWith("java.lang.Float")) { // 数据类型为float
							field.set(obj, Float.parseFloat(jsonObject
									.getString(FieldName)));
						} else if (typeName.endsWith("boolean")
						        || typeName.endsWith("java.lang.Boolean")) {   //数据类型为boolean
						    field.set(obj, jsonObject.getBoolean(FieldName));
						} else if (typeName.endsWith("List")
								|| typeName.endsWith("java.util.List")) {// 数据类型为List
							JSONArray jsonArray = jsonObject
									.getJSONArray(FieldName);
							Class<?> cls = field.getType().getComponentType();
							Type genericFieldType = field.getGenericType();
							if (genericFieldType instanceof ParameterizedType) {
								ParameterizedType aType = (ParameterizedType) genericFieldType;
								Type[] fieldArgTypes = aType
										.getActualTypeArguments();
								for (Type fieldArgType : fieldArgTypes) {
									Class<?> fieldArgClass = (Class<?>) fieldArgType;
									cls = fieldArgClass;
								}
							}
							List<Object> t = new ArrayList<Object>();
							for (int i = 0; i < jsonArray.length(); i++) {
								if (cls.isAssignableFrom(String.class)) {
									String string = (String) jsonArray.get(i);
									t.add(string);

								} else {
									JSONObject jobj = jsonArray
											.getJSONObject(i);
									Object object = fromJsonObject(cls, jobj);

									t.add(object);

									if (jsonArray.length() == 20) {
									}

								}
							}
							field.set(obj, t);
						} else if (Object.class.isAssignableFrom(field.getType())) {
							JSONObject jObject = jsonObject
									.getJSONObject(FieldName);
							Class<?> objcls = field.getType();
							field.set(obj, fromJsonObject(objcls, jObject));
						} else { // 其他类型
							System.out.println(typeName + "暂不支持该转换类型！");
						}
					}
				}
			}
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 解析JsonArray数据
	 * 
	 * @param class1
	 *            类
	 * @param jsonArray
	 *            JsonArray
	 * @return
	 */
	public static <T> List<T> fromJsonArray(Class<T> class1, JSONArray jsonArray) {
		List<T> list = new ArrayList<T>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject;
				jsonObject = jsonArray.getJSONObject(i);
				T object = fromJsonObject(class1, jsonObject);
				list.add(object);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(list.size());
		return list;
	}

}