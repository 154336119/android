package com.huizhuang.zxsq.http.task;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.lgmshare.http.netroid.Request;

public class ComplaintsFeedbackTask extends AbstractHttpTask<Result> {

	public ComplaintsFeedbackTask(Context context, String order_id,
			String reason) {
		super(context);
		// 装修订单
		mRequestParams.add("order_type", "1");
		// 前台用户
		mRequestParams.add("add_operator_type", "2");
		mRequestParams.add("order_id", order_id);
		mRequestParams.add("reason", reason);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL + HttpClientApi.URL_DISPUTE_INDEX;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

	@Override
	public Result parse(String data) {
		return null;
	}

}
