package com.huizhuang.zxsq.http.task.supervision;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsType;
import com.lgmshare.http.netroid.Request;
/**
 * 投诉分类
 * @author th
 *
 */
public class ComplaintsTypeTask extends AbstractHttpTask<List<ComplaintsType>> {
    public ComplaintsTypeTask(Context context,String parent_id) {
        super(context);
        mRequestParams.put("parent_id", parent_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.DISPUTE_CATEGORY;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<ComplaintsType> parse(String data) {
        return JSON.parseArray(data, ComplaintsType.class);
    }

}