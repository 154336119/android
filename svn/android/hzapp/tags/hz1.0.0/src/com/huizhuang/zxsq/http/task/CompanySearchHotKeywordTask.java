package com.huizhuang.zxsq.http.task;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request.Method;

public class CompanySearchHotKeywordTask extends AbstractHttpTask<List<String>>{

	public CompanySearchHotKeywordTask(Context context){
		super(context);
	}
	
	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL + HttpClientApi.REQ_COMPANY_LSIT;
	}

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public List<String> parse(String data) {
		return JSON.parseArray(data, String.class);
	}

}
