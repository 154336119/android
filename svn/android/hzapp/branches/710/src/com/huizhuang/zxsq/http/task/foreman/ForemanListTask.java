package com.huizhuang.zxsq.http.task.foreman;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ForemanList;
import com.lgmshare.http.netroid.Request;


public class ForemanListTask extends AbstractHttpTask<ForemanList> {

    public ForemanListTask(Context context, String city_id,String latitude,String longitude,int page){
        super(context);
        mRequestParams.put("longitude", longitude);
        mRequestParams.put("latitude", latitude);
        mRequestParams.put("page", page);
        mRequestParams.put("current_city", city_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.FOREMEN_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public ForemanList parse(String data) {
        return JSON.parseObject(data, ForemanList.class);
    }

}