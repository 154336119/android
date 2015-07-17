package com.huizhuang.zxsq.http.task.solution;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.solution.Solution;
import com.lgmshare.http.netroid.Request;

/**
 * 施工现场列表task
 * @author admin
 *
 */
public class ConstructionSiteListTask extends AbstractHttpTask<List<Solution>> {

    public ConstructionSiteListTask(Context context,String housing_id) {
        super(context);
        mRequestParams.put("housing_id", housing_id);
    }
    public ConstructionSiteListTask(Context context,String latitude,String longitude) {
        super(context);
        mRequestParams.put("lat", latitude);
        mRequestParams.put("lon", longitude);
    }
    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.SOLUTION_CASE_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<Solution> parse(String data) {
        return JSON.parseArray(data, Solution.class);
    }
    
}
