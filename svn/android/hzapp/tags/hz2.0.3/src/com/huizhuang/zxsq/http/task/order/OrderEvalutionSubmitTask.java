package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;
/**
 * 量房评价提交
 * @author th
 *
 */
public class OrderEvalutionSubmitTask extends AbstractHttpTask<String> {
    public OrderEvalutionSubmitTask(Context context,String order_id,String allot_id,String store_id,Float measuring_score,String measuring_rank,String measuring_content) {
        super(context);
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("allot_id", allot_id);
        mRequestParams.put("store_id", store_id);
        mRequestParams.put("measuring_score", measuring_score);
        mRequestParams.put("measuring_rank", measuring_rank);
        mRequestParams.put("measuring_content", measuring_content);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.ORDER_MEASURING_PJ;
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