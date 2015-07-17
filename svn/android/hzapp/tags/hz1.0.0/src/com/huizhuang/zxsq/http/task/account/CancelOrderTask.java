package com.huizhuang.zxsq.http.task.account;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;

/** 
* @ClassName: CancelOrderTask 
* @Description: 我的订单取消订单 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-11 下午2:25:12 
*  
*/
public class CancelOrderTask extends AbstractHttpTask<String> {

    public CancelOrderTask(Context context, int order_id) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("order_id", order_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_CANCEL_ORDER;
    }

	@Override
	public int getMethod() {
		return Method.POST;
	}

	@Override
	public String parse(String data) {
		return data;
	}

}
