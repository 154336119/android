package com.huizhuang.zxsq.http.task.advertise;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.advertise.Advertise;
import com.lgmshare.http.netroid.Request;

public class AdvertiseTask extends AbstractHttpTask<List<Advertise>>{

	public AdvertiseTask(Context context) {
		super(context);
	}

	@Override
	public List<Advertise> parse(String data) {
		return JSON.parseArray(data, Advertise.class);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL+HttpClientApi.ADVERTISE;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

}
