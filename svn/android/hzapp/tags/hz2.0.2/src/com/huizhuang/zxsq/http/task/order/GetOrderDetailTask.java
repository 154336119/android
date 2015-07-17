package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.OrderDetail;
import com.lgmshare.http.netroid.Request;
/**
 * 订单详细
 * @author th
 *
 */
public class GetOrderDetailTask extends AbstractHttpTask<OrderDetail> {
    public GetOrderDetailTask(Context context,String order_id) {
        super(context);
        mRequestParams.put("order_id", order_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_DETAIL;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public OrderDetail parse(String data) {
        return JSON.parseObject(data, OrderDetail.class);
    }

}