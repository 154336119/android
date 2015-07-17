package com.huizhuang.zxsq.http.task.supervision;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;
/**
 * 各阶段评价提交
 * @author th
 *
 */
public class EvalutionSubmitTask extends AbstractHttpTask<String> {
    public EvalutionSubmitTask(Context context,String comment_key,String store_id,String order_id,String content,Float score,String item_ids) {
        super(context);
        mRequestParams.put("comment_key", comment_key);
        mRequestParams.put("store_id", store_id);
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("content", content);
        mRequestParams.put("score", score);
        mRequestParams.put("item_ids", item_ids);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.COMMENT_COMMENT_ADD;
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