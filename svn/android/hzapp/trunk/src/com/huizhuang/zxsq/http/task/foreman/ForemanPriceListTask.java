package com.huizhuang.zxsq.http.task.foreman;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ForemanPriceList;
import com.lgmshare.http.netroid.Request;


public class ForemanPriceListTask extends AbstractHttpTask<ForemanPriceList> {

    public ForemanPriceListTask(Context context, String foreman_id){
        super(context);
        mRequestParams.add("foreman_id", foreman_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_QUOTE_GET_PRICE_SHEET;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public ForemanPriceList parse(String data) {
        return JSON.parseObject(data, ForemanPriceList.class);
    }

}