package com.huizhuang.zxsq.http.task.common;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.common.UploadImageResult;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;

public class UploadImageTask extends AbstractHttpTask<UploadImageResult> {

    public UploadImageTask(Context context, String type, File file) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("business_type", type);
        try {
            mRequestParams.put("image", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_COMMON_UPLOAD_IMAGE;
    }

	@Override
	public UploadImageResult parse(String data) {
		return JSON.parseObject(data, UploadImageResult.class);
	}

	@Override
	public int getMethod() {
		return Method.POST;
	}

}
