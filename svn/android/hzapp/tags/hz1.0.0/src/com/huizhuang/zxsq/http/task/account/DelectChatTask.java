package com.huizhuang.zxsq.http.task.account;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.ChatList;
import com.huizhuang.zxsq.http.bean.account.MyMessage;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;



/** 
* @ClassName: DelectChatTask 
* @Description:删除会话 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-6 下午6:28:04 
*  
*/
public class DelectChatTask extends AbstractHttpTask<String> {

    public DelectChatTask(Context context,String id) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("id", id);
    }
    
    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_DELECT_CHAR;
    }

	@Override
	public int getMethod() {
		return Method.POST;
	}

	@Override
	public String parse(String data) {
		return JSON.parseObject(data, String.class);
	}

}
