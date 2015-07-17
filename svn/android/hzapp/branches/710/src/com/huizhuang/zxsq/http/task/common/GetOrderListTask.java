package com.huizhuang.zxsq.http.task.common;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;



/** 
* @ClassName: GetOrderListTask 
* @Description: 订单列表
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-6 下午6:28:04 
*  
*/
public class GetOrderListTask extends AbstractHttpTask<List<Order>> {

    public GetOrderListTask(Context context, int order_type,String minId) {
    	super(context);
        mRequestParams = new RequestParams();
        if(order_type != 0){
        	mRequestParams.put("order_type", order_type);
        }
        mRequestParams.put("min_id", minId);
    }

    public GetOrderListTask(Context context,String minId) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("min_id", minId);
    }
    
    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_GET_ORDER_ORDER_OLIST;
    }

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public List<Order> parse(String data) {
		return JSON.parseArray(data, Order.class);
	}

}
