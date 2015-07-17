package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.lgmshare.http.netroid.Request;

import java.util.List;

public class QueryTagListTask extends AbstractHttpTask<List<KeyValue>> {
    public QueryTagListTask(Context context) {
        super(context);
        mRequestParams.add("cate_key", "order_special");
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_COMMON_TAG_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<KeyValue> parse(String data) {
        return JSON.parseArray(data, KeyValue.class);
    }

}