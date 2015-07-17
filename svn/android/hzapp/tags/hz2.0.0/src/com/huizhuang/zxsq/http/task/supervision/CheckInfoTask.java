package com.huizhuang.zxsq.http.task.supervision;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.supervision.CheckInfo;
import com.lgmshare.http.netroid.Request;
/**
 * 各阶段验收信息
 * @author th
 *
 */
public class CheckInfoTask extends AbstractHttpTask<CheckInfo> {
    public CheckInfoTask(Context context,String stage_id) {
        super(context);
        mRequestParams.put("stage_id", stage_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.CHECK_CHECK_STAGE_INFO;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public CheckInfo parse(String data) {
        return JSON.parseObject(data, CheckInfo.class);
    }

}