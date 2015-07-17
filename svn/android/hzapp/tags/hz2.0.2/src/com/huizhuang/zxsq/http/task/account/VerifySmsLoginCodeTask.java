package com.huizhuang.zxsq.http.task.account;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;

/** 
* @ClassName: VerifySmsLoginCodeTask 
* @Description: 我的订单取消订单 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-11 下午2:25:12 
*  
*/
public class VerifySmsLoginCodeTask extends AbstractHttpTask<String> {

    public VerifySmsLoginCodeTask(Context context, String mobile, String sms_code, String name, String gender,String current_city,String clue_id,String foreman_id) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("mobile", mobile);
        mRequestParams.put("current_city", current_city);
        mRequestParams.put("sms_code", sms_code);
        mRequestParams.put("name", name);
        mRequestParams.put("gender", gender);
        mRequestParams.put("clue_id", clue_id);
        mRequestParams.put("foreman_id", foreman_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_SEND_CHECK_CODE_UNLOGIN;
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
