package com.huizhuang.zxsq.http.parser.base;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: BaseJsonParser
 * @Description: 解析器基础类(JSON格式)
 * @author lim
 * @param <T>
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:20:19
 */
public abstract class BaseJsonParser<T> implements BaseParser<T> {

	public abstract T parseIType(String jsonString) throws JSONException;

	@Override
	public T parse(String jsonString) throws JSONException {
		String newJsonString = replaceNullValue(jsonString);
		return parseIType(newJsonString);
	}

	/**
	 * 过滤掉value为null的字段
	 * 
	 * @param jsonString
	 * @return
	 */
	protected String replaceNullValue(String jsonString) {
		// 把形如：【"a":null,】 给替换掉
		jsonString = jsonString.replaceAll("\"[^\"]+\":null,", "");
		// 把形如：【,"a":null】 把前面有个逗号的给替换掉
		jsonString = jsonString.replaceAll(",\"[^\"]+\":null", "");
		return jsonString;
	}

	/**
	 * @param key
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	protected String getString(String key, JSONObject jsonObject)
			throws JSONException {
		String res = "";
		if (jsonObject.has(key)) {
			res = jsonObject.getString(key);
		}
		return res;
	}

	/**
	 * 去掉null
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public String getStringNotNull(JSONObject jsonObject, String key) {
		String res = "";
		try {
			if (jsonObject.has(key)) {
				if (jsonObject.get(key) == JSONObject.NULL
						|| jsonObject.isNull(key)) {
					res = "";
				} else {
					res = jsonObject.getString(key);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param key
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	protected int getInt(String key, JSONObject jsonObject)
			throws JSONException {
		int res = -1;
		if (jsonObject.has(key)) {
			res = jsonObject.getInt(key);
		}
		return res;
	}

	/**
	 * @param key
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	protected double getDouble(String key, JSONObject jsonObject)
			throws JSONException {
		double res = 0l;
		if (jsonObject.has(key)) {
			res = jsonObject.getDouble(key);
		}
		return res;
	}

	/**
	 * @param key
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	protected long getLong(String key, JSONObject jsonObject)
			throws JSONException {
		long res = 0l;
		if (jsonObject.has(key)) {
			res = jsonObject.getLong(key);
		}
		return res;
	}
}
