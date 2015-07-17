package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.lgmshare.http.netroid.Request;

import java.util.List;

public class GetOrderTask extends AbstractHttpTask<List<Order>> {
    public GetOrderTask(Context context,String min_id,String max_id) {
        super(context);
        mRequestParams.put("min_id", min_id);
        mRequestParams.put("max_id", max_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_GET_ORDER;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public List<Order> parse(String data) {
        return JSON.parseArray(data, Order.class);
    }

}