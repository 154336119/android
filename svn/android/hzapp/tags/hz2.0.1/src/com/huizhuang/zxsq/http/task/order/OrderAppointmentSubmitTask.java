package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;
/**
 * 签约工作提交
 * @author th
 *
 */
public class OrderAppointmentSubmitTask extends AbstractHttpTask<String> {
    public OrderAppointmentSubmitTask(Context context,String allot_id) {
        super(context);
        mRequestParams.put("allot_id", allot_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.BOOK_BOOK_STORE;
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