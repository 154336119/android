package com.huizhuang.zxsq.http.task.common;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.common.HouseType;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;

public class SearchHouseTypeListTask extends AbstractHttpTask<List<HouseType>> {

    private String url = "";

    public SearchHouseTypeListTask(Context context,String housing_id) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("housing_id", housing_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + url;
    }

	@Override
	public List<HouseType> parse(String data) {
		return JSON.parseArray(data, HouseType.class);
	}

	@Override
	public int getMethod() {
		return Method.GET;
	}

}
