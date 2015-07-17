package com.huizhuang.zxsq.http.task.foreman;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionSiteDetails;
import com.huizhuang.zxsq.http.bean.solution.SolutionDetail;
import com.lgmshare.http.netroid.Request;


public class ConstructionSiteDetailsTask extends AbstractHttpTask<ConstructionSiteDetails> {

    public ConstructionSiteDetailsTask(Context context, String showcase_id){
        super(context);
        mRequestParams.add("showcase_id", showcase_id);
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
    public ConstructionSiteDetails parse(String data) {
        SolutionDetail solutionDetail = JSON.parseObject(data, SolutionDetail.class);
        ConstructionSiteDetails constructionSiteDetails = new ConstructionSiteDetails();
        constructionSiteDetails.setId(solutionDetail.getId());
        constructionSiteDetails.setHousing_name(solutionDetail.getHousing_name());
        constructionSiteDetails.setName(solutionDetail.getName());
        constructionSiteDetails.setCost(solutionDetail.getBudget());
        constructionSiteDetails.setRenovation_way(solutionDetail.getRenovation_way());
        constructionSiteDetails.setDoor_model(solutionDetail.getDoor_model());
        constructionSiteDetails.setSize(solutionDetail.getArea());
        constructionSiteDetails.setRoom_style(solutionDetail.getRoom_style());
        constructionSiteDetails.setPhrase(solutionDetail.getZx_node());
        constructionSiteDetails.setOrder_count(solutionDetail.getOrder_count());
        constructionSiteDetails.setImage(solutionDetail.getImage());
        constructionSiteDetails.setData(solutionDetail.getData());
        constructionSiteDetails.setStore_id(solutionDetail.getStore_id());
        return constructionSiteDetails;
    }

}