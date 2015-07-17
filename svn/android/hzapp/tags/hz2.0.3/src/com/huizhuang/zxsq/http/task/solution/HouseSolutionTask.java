package com.huizhuang.zxsq.http.task.solution;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.solution.HouseSolution;
import com.lgmshare.http.netroid.Request;

/**
 * @ClassName: HouseSolutionTask
 * @Description:搜索小区
 * @author jean
 * @mail 342592622@qq.com
 * @date 2014-12-10 上午11:26:32
 * 
 */
public class HouseSolutionTask extends AbstractHttpTask<List<HouseSolution>> {

    public HouseSolutionTask(Context context,String keyword,String lon,String lat) {
        super(context);
        mRequestParams.put("keyword", keyword);
        mRequestParams.put("lon", lon);
        mRequestParams.put("lat", lat);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.HOUSE_SOLUTION;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<HouseSolution> parse(String data) {
        return JSON.parseArray(data, HouseSolution.class);
    }
    
}
