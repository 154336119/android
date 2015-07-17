package com.huizhuang.zxsq.http.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.module.User;
import com.lgmshare.http.netroid.Request.Method;

public class UserLoginTask extends AbstractHttpTask<User>{

	public UserLoginTask(Context context, String username, String passwrod){
		super(context);
		mRequestParams.add("mobile", username);
		mRequestParams.add("passwrod", passwrod);
	}
	
	@Override
	public String getUrl() {
		return "http://192.168.1.11:8093/designer/login.json";
	}

	@Override
	public int getMethod() {
		return Method.POST;
	}

	@Override
	public User parse(String data) {
		return JSON.parseObject(data, User.class);
	}

}
