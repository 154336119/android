package com.huizhuang.zxsq.http.task;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.CityOpen;
import com.lgmshare.http.netroid.Request;

public class CityOpenTask extends AbstractHttpTask<List<CityOpen>> {

	public CityOpenTask(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL + HttpClientApi.URL_CONSTANTS_AREAS_OPEN;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

	@Override
	public List<CityOpen> parse(String data) {
		return JSON.parseArray(data, CityOpen.class);
	}

}
