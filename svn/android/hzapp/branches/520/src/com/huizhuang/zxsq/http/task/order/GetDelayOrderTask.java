package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.DeylayChangeOrder;
import com.lgmshare.http.netroid.Request;

/**
 * 延期变更单
 * @author admin
 *
 */
public class GetDelayOrderTask extends AbstractHttpTask<DeylayChangeOrder> {

    public GetDelayOrderTask(Context context,String stage_id){
        super(context);
        mRequestParams.put("stage_id", stage_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.CHECK_GET_DELAY_ORDER;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public DeylayChangeOrder parse(String data) {
        return JSON.parseObject(data, DeylayChangeOrder.class);
    }

}