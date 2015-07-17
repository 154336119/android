package com.huizhuang.zxsq.http;

public interface ResponseParser<T> {
	
	T parse(String data);
	
}
