package com.huizhuang.zxsq.http.task.order;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.OrderDetailChild;
import com.lgmshare.http.netroid.Request;
/**
 * 量房评价列表
 * @author admin
 *
 */
public class GetOrderEvalutionTask extends AbstractHttpTask<List<OrderDetailChild>> {
    public GetOrderEvalutionTask(Context context,String order_id) {
        super(context);
        mRequestParams.put("order_id", order_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.ORDER_MEASURING_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public List<OrderDetailChild> parse(String data) {
        return JSON.parseArray(data, OrderDetailChild.class);
    }

}