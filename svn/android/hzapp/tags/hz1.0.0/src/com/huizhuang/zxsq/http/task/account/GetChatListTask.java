package com.huizhuang.zxsq.http.task.account;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.ChatList;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;



/** 
* @ClassName: GetChatListTask 
* @Description: 会话列表
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-6 下午6:28:04 
*  
*/
public class GetChatListTask extends AbstractHttpTask<ChatList> {

    public GetChatListTask(Context context,String minId) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("min_id", minId);
    }
    
    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_GET_CHAT_LIST;
    }

	@Override
	public int getMethod() {
		return Method.POST;
	}

	@Override
	public ChatList parse(String data) {
		return JSON.parseObject(data, ChatList.class);
	}

}
