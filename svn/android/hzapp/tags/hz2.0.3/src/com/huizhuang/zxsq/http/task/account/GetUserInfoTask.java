package com.huizhuang.zxsq.http.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.User;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;


/** 
* @ClassName: GetUserInfoTask 
* @Description: 个人中心主页task 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-10 上午11:13:39 
*  
*/
public class GetUserInfoTask extends AbstractHttpTask<User> {

    public GetUserInfoTask(Context context) {
    	super(context);
        mRequestParams = new RequestParams();
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.USER_LOGIN_BY_TOKEN_AND_USER_ID;
    }

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public User parse(String data) {
		return JSON.parseObject(data, User.class);
	}
}
