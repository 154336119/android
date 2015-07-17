package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.bean.order.OrderDetail;
import com.lgmshare.http.netroid.Request;

import java.util.List;
/**
 * 确认量房/预约时间
 * @author admin
 *
 */
public class AppointmentAndMeasureTask extends AbstractHttpTask<String> {
    public AppointmentAndMeasureTask(Context context,String order_id,String allot_id ,String action,String appointment_time) {
        super(context);
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("allot_id", allot_id);
        mRequestParams.put("action", action);
        mRequestParams.put("ske_contract_time", appointment_time);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_APPOINTMENT_OR_MEASURE;
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