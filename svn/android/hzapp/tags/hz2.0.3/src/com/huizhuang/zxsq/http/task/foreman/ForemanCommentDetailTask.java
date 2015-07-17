package com.huizhuang.zxsq.http.task.foreman;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.lgmshare.http.netroid.Request;

public class ForemanCommentDetailTask extends AbstractHttpTask<List<ForemanComment>> {

	public ForemanCommentDetailTask(Context context,String store_id,String operator_id,String id) {
		super(context);
		mRequestParams.add("id", id);
		mRequestParams.add("store_id", store_id);
		mRequestParams.add("operator_id", operator_id);
	}

	@Override
	public List<ForemanComment> parse(String data) {
		return JSON.parseArray(data, ForemanComment.class);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL+HttpClientApi.FOREMAN_COMMENT_DETAIL;
	}

	@Override
	public int getMethod() {
		return Request.Method.POST;
	}

}
