package com.huizhuang.zxsq.http.task.solution;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.solution.ConstructionSiteList;
import com.lgmshare.http.netroid.Request;

/**
 * 施工现场列表task
 * @author admin
 *
 */
public class ConstructionSiteTask extends AbstractHttpTask<ConstructionSiteList> {

    public ConstructionSiteTask(Context context,String showcase_id,int page,int num) {
        super(context);
        mRequestParams.put("showcase_id", showcase_id);
        mRequestParams.put("page", page);
        mRequestParams.put("num", num);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.SOLUTION_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public ConstructionSiteList parse(String data) {
        return JSON.parseObject(data, ConstructionSiteList.class);
    }
    
}
