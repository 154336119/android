package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.Company;
import com.lgmshare.http.netroid.Request;

/**
 * Created by php on 2015/1/26.
 */
public class QueryRecommendCompanyTask extends AbstractHttpTask<Company> {

    public QueryRecommendCompanyTask(Context context){
        super(context);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_RECOMMEND_COMPANY;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Company parse(String data) {
        return JSON.parseObject(data, Company.class);
    }

}