package com.huizhuang.zxsq.http.task.foreman;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ForemanDetails;
import com.lgmshare.http.netroid.Request;


public class ForemanDetailsTask extends AbstractHttpTask<ForemanDetails> {

    public ForemanDetailsTask(Context context, String store_id){
        super(context);
        mRequestParams.add("store_id", store_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_FOREMAN_GET_DETAILS;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public ForemanDetails parse(String data) {
        return JSON.parseObject(data, ForemanDetails.class);
    }

}