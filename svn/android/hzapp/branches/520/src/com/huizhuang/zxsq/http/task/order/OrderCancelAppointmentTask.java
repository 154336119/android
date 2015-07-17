package com.huizhuang.zxsq.http.task.order;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.OrderCancelRason;
import com.lgmshare.http.netroid.Request;
/**
 * 取消预约签约原因列表
 * @author admin
 *
 */
public class OrderCancelAppointmentTask extends AbstractHttpTask<List<OrderCancelRason>> {
    public OrderCancelAppointmentTask(Context context,String order_id) {
        super(context);
        mRequestParams.put("order_id", order_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.BOOK_CANCEL_BOOK;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<OrderCancelRason> parse(String data) {
        return JSON.parseArray(data, OrderCancelRason.class);
    }

}