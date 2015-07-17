package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;
/**
 * 取消预约签约
 * @author th
 *
 */
public class OrderCancelAppointmentSubmitTask extends AbstractHttpTask<String> {
    public OrderCancelAppointmentSubmitTask(Context context,String allot_id,String reason,String content) {
        super(context);
        mRequestParams.put("allot_id", allot_id);
        mRequestParams.put("reason", reason);
        mRequestParams.put("other_result", content);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.BOOK_CANCEL_BOOK_RESON;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String parse(String data) {
        return data;
    }

}