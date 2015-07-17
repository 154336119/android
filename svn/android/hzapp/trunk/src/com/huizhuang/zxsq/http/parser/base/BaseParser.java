package com.huizhuang.zxsq.http.parser.base;

import org.json.JSONException;


public interface BaseParser<T> {
	
	public static final int SUCCESS_CODE = 200;

	public abstract T parse(String jsonString) throws JSONException;

}
