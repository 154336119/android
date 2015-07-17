package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.Moblie;
import com.lgmshare.http.netroid.Request;

/**
 * 获取当前手机号
 * @author admin
 *
 */
public class QueryMobileTask extends AbstractHttpTask<Moblie> {

    public QueryMobileTask(Context context){
        super(context);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_ORDER_GET_MOBILE;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Moblie parse(String data) {
        return JSON.parseObject(data, Moblie.class);
    }

}