package com.huizhuang.zxsq.http.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.Supervisioner;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;

/** 
* @ClassName: GetSupervisionerDetailTask 
* @Description: 获取监理师详情task
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-9 下午5:24:39 
*  
*/
public class GetSupervisionerDetailTask extends AbstractHttpTask<Supervisioner> {

    public GetSupervisionerDetailTask(Context context, String id) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("jl_staff_id", id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_ACCOUNT_SUPERVISION_DETAIL;
    }

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public Supervisioner parse(String data) {
		return JSON.parseObject(data, Supervisioner.class);
	}

}
