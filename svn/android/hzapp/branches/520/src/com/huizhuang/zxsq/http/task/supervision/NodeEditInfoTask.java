package com.huizhuang.zxsq.http.task.supervision;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.supervision.NodeEdit;
import com.lgmshare.http.netroid.Request;
/**
 * 各阶段验收信息
 * @author th
 *
 */
public class NodeEditInfoTask extends AbstractHttpTask<List<NodeEdit>> {
    public NodeEditInfoTask(Context context,String order_id) {
        super(context);
        mRequestParams.put("order_id", order_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.CHECK_CHECK_STAGE_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public List<NodeEdit> parse(String data) {
        return JSON.parseArray(data, NodeEdit.class);
    }

}