package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;
/**
 * 订单取消提交
 * @author th
 *
 */
public class OrderCancelSubmitTask extends AbstractHttpTask<String> {
    public OrderCancelSubmitTask(Context context,String order_id,int reason_id,String content) {
        super(context);
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("cancel_invalidate_reason", reason_id);
        mRequestParams.put("cancel_invalidate_reason_des", content);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.ORDER_CANCEL_ORDER;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String parse(String data) {
        return data;
    }

}