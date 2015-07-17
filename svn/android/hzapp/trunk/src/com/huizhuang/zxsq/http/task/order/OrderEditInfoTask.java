package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.Result;
import com.lgmshare.http.netroid.Request;

/**
 * 完善订单
 * @author th
 *
 */
public class OrderEditInfoTask extends AbstractHttpTask<Result> {

	public OrderEditInfoTask(Context context, String housing_name,String housing_address,String order_id,String time) {
		super(context);
		mRequestParams.put("housing_id", 0);
		mRequestParams.put("housing_name", housing_name);
		mRequestParams.put("housing_address", housing_address);
		mRequestParams.put("order_id", order_id);
		mRequestParams.put("measuring_time", time);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_ADD_INFO;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

	@Override
	public Result parse(String data) {
		return JSON.parseObject(data, Result.class);
	}

}