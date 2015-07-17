package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.CostChange;
import com.huizhuang.zxsq.http.bean.order.CostChangeChild;
import com.huizhuang.zxsq.http.bean.order.Moblie;
import com.huizhuang.zxsq.http.bean.order.PayInfo;
import com.lgmshare.http.netroid.Request;

/**
 * 支付信息
 * @author admin
 *
 */
public class GetPayInfoTask extends AbstractHttpTask<PayInfo> {

    public GetPayInfoTask(Context context,String order_id,String node){
        super(context);
        mRequestParams.put("order_id", order_id);
        mRequestParams.put("node", node);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.CHECK_GET_PAY_INFO;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public PayInfo parse(String data) {
        return JSON.parseObject(data, PayInfo.class);
    }

}