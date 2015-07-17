package com.huizhuang.zxsq.http.task.account;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.ChatList;
import com.huizhuang.zxsq.http.bean.account.EnsureTrade;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;



/** 
* @ClassName: GetEnSureTradeTask 
* @Description: 应付款确认
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-6 下午6:28:04 
*  
*/
public class GetEnSureTradeTask extends AbstractHttpTask<EnsureTrade> {

    public GetEnSureTradeTask(Context context,String order_id) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("order_id", order_id);
    }
    
    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_GET_ENSURE_TRADE;
    }

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public EnsureTrade parse(String data) {
		return JSON.parseObject(data, EnsureTrade.class);
	}

}
