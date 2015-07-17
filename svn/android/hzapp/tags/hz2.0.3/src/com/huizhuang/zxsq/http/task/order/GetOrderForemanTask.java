package com.huizhuang.zxsq.http.task.order;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.OrderSignForeman;
import com.lgmshare.http.netroid.Request;
/**
 * 签约工作列表
 * @author admin
 *
 */
public class GetOrderForemanTask extends AbstractHttpTask<List<OrderSignForeman>> {
    public GetOrderForemanTask(Context context,String order_id) {
        super(context);
        mRequestParams.put("order_id", order_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.BOOK_BOOK_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<OrderSignForeman> parse(String data) {
        return JSON.parseArray(data, OrderSignForeman.class);
    }

}