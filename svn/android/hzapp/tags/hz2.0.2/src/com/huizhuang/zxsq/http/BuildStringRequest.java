package com.huizhuang.zxsq.http;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.utils.LogUtil;
import com.lgmshare.http.netroid.NetworkResponse;
import com.lgmshare.http.netroid.exception.*;
import com.lgmshare.http.netroid.Request;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.Response;
import com.lgmshare.http.netroid.exception.NetroidError;

public class BuildStringRequest extends Request<String>{

	public BuildStringRequest(int method, String url, RequestCallBack<String> listener) {
		super(method, url, listener);
	}
	
	public BuildStringRequest(int method, String url, RequestParams params, RequestCallBack<String> listener) {
		super(method, url, params, listener);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
            String data = new String(response.data, response.charset);
            LogUtil.i("data:"+ data);
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("status")) {
				int code = jsonObject.getInt("status");
				if (code == 200) {
					return Response.success(data, response);
				}else{
					return Response.error(new NetroidError(code, jsonObject.getString("msg")));
				}
			}
            return Response.error(new ParseError(405, "请求错误："+data));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(506,"不支持的编码"));
        } catch (JSONException e) {
        	return Response.error(new ParseError(505,"格式化错误"));
		}
	}
	
	
}
