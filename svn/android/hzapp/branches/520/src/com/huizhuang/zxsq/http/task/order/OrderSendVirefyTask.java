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
public class OrderSendVirefyTask extends AbstractHttpTask<String> {

    public OrderSendVirefyTask(Context context,String mobile,String housing_id,String housing_name,String housing_address
    		,String measuring_time,String house_type_area,String city_id,String source,String source_name
    		,String px,String py,String foreman_id,String area_name,String name,String gender,String province_name,String city_name){
        super(context);
        mRequestParams.put("mobile", mobile);
        mRequestParams.put("housing_name", housing_name);
        mRequestParams.put("housing_address", housing_address);
        mRequestParams.put("measuring_time", measuring_time);
        mRequestParams.put("house_type_area", house_type_area);
        mRequestParams.put("current_city", city_id);
        mRequestParams.put("province_name", province_name);
        mRequestParams.put("city_name", city_name);
        mRequestParams.put("area_name", area_name);
        mRequestParams.put("source", "android");
        mRequestParams.put("px", py);
        mRequestParams.put("py", px);
        mRequestParams.put("foreman_id", foreman_id);
        mRequestParams.put("add_entrance", "");
        mRequestParams.put("source_name", source_name);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_SEND_VIREFY_CODE;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String parse(String data) {
        return JSON.parseObject(data, String.class);
    }

}