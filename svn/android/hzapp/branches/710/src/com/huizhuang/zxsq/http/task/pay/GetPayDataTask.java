package com.huizhuang.zxsq.http.task.pay;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.DeylayChangeOrder;
import com.lgmshare.http.netroid.Request;

/**
 *  支付参数
 * @author admin
 *
 */
public class GetPayDataTask extends AbstractHttpTask<String> {

    public GetPayDataTask(Context context,String finance_id,String pay_type,String promo){
        super(context);
        mRequestParams.put("finance_id", finance_id);
        mRequestParams.put("pay_type", pay_type);
        mRequestParams.put("promo", promo);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.CHECK_GET_PAY_DATA;
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