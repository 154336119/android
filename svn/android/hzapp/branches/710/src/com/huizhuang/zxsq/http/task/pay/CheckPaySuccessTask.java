package com.huizhuang.zxsq.http.task.pay;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.lgmshare.http.netroid.Request;

/**
 *  POS机支付
 * @author admin
 *
 */
public class CheckPaySuccessTask extends AbstractHttpTask<String> {

    public CheckPaySuccessTask(Context context,String finance_id){
        super(context);
        mRequestParams.put("finance_id", finance_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.CHECK_PAY_SUCCESS;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String parse(String data) {
        return JSON.parseObject(data, String.class);
    }

}