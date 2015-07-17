package com.huizhuang.zxsq.http.task.zxbd;


import java.util.ArrayList;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.zxdb.Zxbd;
import com.huizhuang.zxsq.http.bean.zxdb.ZxbdListInfoByStage;
import com.lgmshare.http.netroid.Request;

public class ZxbdListByStageTask extends AbstractHttpTask<ZxbdListInfoByStage>{

	public ZxbdListByStageTask(Context context,String node,String page) {
		super(context);
		mRequestParams.put("node", node);
		mRequestParams.put("huizhuang_debug", "no_auth");
		mRequestParams.put("page", page);
	}
	
	public ZxbdListByStageTask(Context context,String node) {
		super(context);
		mRequestParams.put("node", node);
		mRequestParams.put("huizhuang_debug", "no_auth");
	}

	@Override
	public ZxbdListInfoByStage parse(String data) {
/*		ArrayList<Zxbd> list = new ArrayList<Zxbd>();
		Zxbd zxbd = null;
		for(int i=0;i<15;i++){
			zxbd = new Zxbd();
			zxbd.setTitle("xxxxxx"+i);
			zxbd.setId(i+"");
			zxbd.setUrl("http://.......");
			list.add(zxbd);
		}
		ZxbdListInfoByStage zxbdListInfoByStage = new ZxbdListInfoByStage();
		zxbdListInfoByStage.setList(list);
		zxbdListInfoByStage.setTotalpage(4+"");
		zxbdListInfoByStage.setTotalrecord(15+"");
		return zxbdListInfoByStage;*/
		return JSON.parseObject(data,ZxbdListInfoByStage.class);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL+HttpClientApi.URL_GET_ZXBD_LIST_BY_STAGE;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

}
