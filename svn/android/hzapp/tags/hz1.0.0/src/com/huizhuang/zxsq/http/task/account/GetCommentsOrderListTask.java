package com.huizhuang.zxsq.http.task.account;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.CommentsWait;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;




/** 
* @ClassName: GetCommentsOrderListTask 
* @Description: 待点评列表task
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-7 下午2:52:27 
*  
*/
public class GetCommentsOrderListTask extends AbstractHttpTask<List<CommentsWait>> {

    public GetCommentsOrderListTask(Context context) {
    	super(context);
        mRequestParams = new RequestParams();
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_GET_COMMENTS_LIST;
    }

	@Override
	public int getMethod() {
		return Method.POST;
	}

	@Override
	public List<CommentsWait> parse(String data) {
		return JSON.parseArray(data, CommentsWait.class);
	}

}
