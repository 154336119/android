package com.huizhuang.zxsq.http.task.supervision;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;
/**
 * 添加投诉
 * @author th
 *
 */
public class ComplaintsAddTask extends AbstractHttpTask<String> {
    public ComplaintsAddTask(Context context,String order_id,String dispute_node_id,String title,String category) {
        super(context);
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("dispute_node_id", dispute_node_id);
        mRequestParams.put("title", title);
        mRequestParams.put("category", category);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.DISPUTE_ADD;
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