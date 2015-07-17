package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.lgmshare.http.netroid.Request;


/** 
* @ClassName: OrderSubmitTask 
* @Description: 工长下单
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-13 下午6:43:53 
*  
*/
public class OrderSubmitTask extends AbstractHttpTask<Order> {

    public OrderSubmitTask(Context context,String mobile,String add_case_id,int foreman_id, String source_name){
        super(context);
        mRequestParams.put("mobile", mobile);
        if(add_case_id != null){
        	mRequestParams.put("add_case_id", add_case_id);
        }
        if(foreman_id != 0){
        	mRequestParams.put("foreman_id", foreman_id);
        }
        mRequestParams.put("source_name", source_name);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_SUBMIT;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Order parse(String data) {
        return JSON.parseObject(data, Order.class);
    }

}