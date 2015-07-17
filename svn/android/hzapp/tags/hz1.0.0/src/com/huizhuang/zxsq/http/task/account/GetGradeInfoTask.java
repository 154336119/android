package com.huizhuang.zxsq.http.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.Grade;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;



/** 
* @ClassName: GetGradeInfoTask 
* @Description: 评分页获取监理师、工长信息
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-11 上午10:47:57 
*  
*/
public class GetGradeInfoTask extends AbstractHttpTask<Grade> {

    public GetGradeInfoTask(Context context, String record_id) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("record_id", record_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_GET_STAFF_AND_STORE;
    }

	@Override
	public int getMethod() {
		return Method.POST;
	}

	@Override
	public Grade parse(String data) {
		return JSON.parseObject(data, Grade.class);
	}

}
