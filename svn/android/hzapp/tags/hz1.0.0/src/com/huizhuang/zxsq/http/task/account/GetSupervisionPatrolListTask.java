package com.huizhuang.zxsq.http.task.account;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.SupervisionPatrol;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;

/** 
* @ClassName: GetSupervisionPatrolListTask 
* @Description: 监理师巡查task
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-10 下午2:29:22 
*  
*/
public class GetSupervisionPatrolListTask extends AbstractHttpTask<List<SupervisionPatrol>> {

    public GetSupervisionPatrolListTask(Context context, int order_id,String minId) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("min_id", minId);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_GET_RECORD_LIST;
    }

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public List<SupervisionPatrol> parse(String data) {
		return JSON.parseArray(data, SupervisionPatrol.class);
	}

}
