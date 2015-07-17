package com.huizhuang.zxsq.http.task.solution;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionSiteDetails;
import com.huizhuang.zxsq.http.bean.solution.SolutionDetail;
import com.lgmshare.http.netroid.Request;


public class SolutionDetailTask extends AbstractHttpTask<SolutionDetail> {

    public SolutionDetailTask(Context context, String showcase_id){
        super(context);
        mRequestParams.add("showcase_id", showcase_id);
    }
    public SolutionDetailTask(Context context, String showcase_id,String latitude,String longitude){
        super(context);
        mRequestParams.add("showcase_id", showcase_id);
        mRequestParams.add("lat", latitude);
        mRequestParams.add("lon", longitude);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.SOLUTION_DETAIL_HEAD;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public SolutionDetail parse(String data) {
    	SolutionDetail solutionDetail = JSON.parseObject(data, SolutionDetail.class);
        return solutionDetail;
    }

}