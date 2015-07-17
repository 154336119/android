package com.huizhuang.zxsq.http.task.supervision;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsNode;
import com.lgmshare.http.netroid.Request;
/**
 * 投诉节点信息
 * @author th
 *
 */
public class ComplaintsNodeTask extends AbstractHttpTask<List<ComplaintsNode>> {
    public ComplaintsNodeTask(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.DISPUTE_NODE;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<ComplaintsNode> parse(String data) {
        return JSON.parseArray(data, ComplaintsNode.class);
    }

}