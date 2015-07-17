package com.huizhuang.zxsq.http.task.supervision;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsData;
import com.lgmshare.http.netroid.Request;
/**
 * 当前节点是否有投诉
 * @author th
 *
 */
public class ComplaintsInfoTask extends AbstractHttpTask<ComplaintsData> {
    public ComplaintsInfoTask(Context context,String order_id,String dispute_node_id) {
        super(context);
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("dispute_node_id", dispute_node_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.DISPUTE_INFO;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public ComplaintsData parse(String data) {
        return JSON.parseObject(data, ComplaintsData.class);
    }

}