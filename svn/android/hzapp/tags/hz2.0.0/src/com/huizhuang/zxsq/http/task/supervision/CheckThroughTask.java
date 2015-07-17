package com.huizhuang.zxsq.http.task.supervision;

import android.content.Context;

import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;
/**
 * 各阶段验收通过
 * @author th
 *
 */
public class CheckThroughTask extends AbstractHttpTask<String> {
    public CheckThroughTask(Context context,String stage_id) {
        super(context);
        mRequestParams.put("stage_id", stage_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.CHECK_CHECK_CALIDATION;
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