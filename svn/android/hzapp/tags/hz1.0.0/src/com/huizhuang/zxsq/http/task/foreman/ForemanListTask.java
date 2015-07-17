package com.huizhuang.zxsq.http.task.foreman;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ForemanList;
import com.huizhuang.zxsq.module.Result;
import com.lgmshare.http.netroid.Request;


public class ForemanListTask extends AbstractHttpTask<ForemanList> {

    public ForemanListTask(Context context, String area_id,  String longitude, String latitude, String work_age, String renovation_range, String sort, String page){
        super(context);
        mRequestParams.add("area_id", area_id);
        mRequestParams.add("longitude", longitude);
        mRequestParams.add("latitude", latitude);
        mRequestParams.add("work_age", work_age);
        mRequestParams.add("renovation_range", renovation_range);
        mRequestParams.add("sort", sort);
        mRequestParams.add("page", page);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_FOREMAN_GET_FOREMEN;
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