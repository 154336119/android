package com.huizhuang.zxsq.http.task.zxbd;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.zxdb.ZxbdDetail;
import com.lgmshare.http.netroid.Request;

public class ZxbdDetailTask extends AbstractHttpTask<ZxbdDetail>{
	public ZxbdDetailTask(Context context,String id) {
		super(context);
		mRequestParams.put("huizhuang_debug", "no_auth");
		mRequestParams.put("id", id);
	}

	@Override
	public ZxbdDetail parse(String data) {
		return JSON.parseObject(data, ZxbdDetail.class);
	}

	@Override
	public String getUrl() {  
		return HttpClientApi.BASE_URL+HttpClientApi.URL_GET_ARTICLE_BY_ID;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

}
