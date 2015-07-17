package com.huizhuang.zxsq.http.task.account;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.CommentHistory;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;

/** 
* @ClassName: GetCommentHistoryListTask 
* @Description: 获取已点评列表task 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-12 上午11:36:07 
*  
*/
public class GetCommentHistoryListTask extends AbstractHttpTask<List<CommentHistory>> {

    public GetCommentHistoryListTask(Context context) {
    	super(context);
        mRequestParams = new RequestParams();
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_GET_COMMENTS_HISTORY_LIST;
    }

	@Override
	public int getMethod() {
		return Method.POST;
	}

	@Override
	public List<CommentHistory> parse(String data) {
		return JSON.parseArray(data, CommentHistory.class);
	}

}
