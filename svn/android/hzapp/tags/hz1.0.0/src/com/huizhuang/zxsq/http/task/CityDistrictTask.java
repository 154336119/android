package com.huizhuang.zxsq.http.task;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.CityDistrict;
import com.lgmshare.http.netroid.Request;

public class CityDistrictTask extends AbstractHttpTask<List<CityDistrict>> {

	public CityDistrictTask(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL + HttpClientApi.URL_CONSTANTS_AREAS;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

	@Override
	public List<CityDistrict> parse(String data) {
		CityDistrictData dat = JSON.parseObject("{\"list\":" + data + "}",
				CityDistrictData.class);
		return dat.list;
	}

}

class CityDistrictData {
	public List<CityDistrict> list;
}
