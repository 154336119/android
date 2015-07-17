package com.huizhuang.zxsq.http.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.home.HomePageData;
import com.lgmshare.http.netroid.Request;

public class HomePageTask  extends AbstractHttpTask<HomePageData> {

    public HomePageTask(Context context){
        super(context);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_HOME_PAGE_lIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public HomePageData parse(String data) {
        return JSON.parseObject(data, HomePageData.class);
    }

}
