package com.huizhuang.zxsq.http.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.order.CostChange;
import com.huizhuang.zxsq.http.bean.order.CostChangeChild;
import com.huizhuang.zxsq.http.bean.order.Moblie;
import com.lgmshare.http.netroid.Request;

/**
 * 费用变更单
 * @author admin
 *
 */
public class CostChangeTask extends AbstractHttpTask<CostChange> {

    public CostChangeTask(Context context,String stage_id){
        super(context);
        mRequestParams.put("stage_id", stage_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_COST_CHANGE;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public CostChange parse(String data) {
        return JSON.parseObject(data, CostChange.class);
    }

}