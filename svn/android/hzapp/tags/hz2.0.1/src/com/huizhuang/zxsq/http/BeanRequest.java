package com.huizhuang.zxsq.http;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.http.parser.base.BaseParser;
import com.huizhuang.zxsq.utils.LogUtil;
import com.lgmshare.http.netroid.NetworkResponse;
import com.lgmshare.http.netroid.exception.*;
import com.lgmshare.http.netroid.Request;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.Response;

public class BeanRequest<T> extends Request<T>{

	private BaseParser<T> mParser = null;
	
	public BeanRequest(int method, String url, RequestParams params, BaseParser<T> parser, RequestCallBack<T> callBack) {
		super(method, url, params, callBack);
		addHeader("User-Agent", "huizhuang;shengqi;"+"1.2.0"+";android-phone");
		mParser = parser;
	}
	
	public BeanRequest(int method, String url, BaseParser<T> parser, RequestCallBack<T> callBack) {
		super(method, url, null, callBack);
		addHeader("User-Agent", "huizhuang;shengqi;"+"1.2.0"+";android-phone");
		mParser = parser;
	}

	@Override
	protected NetroidError parseNetworkError(NetroidError netroidError) {
		return super.parseNetworkError(netroidError);
	}
	
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
            String data = new String(response.data, response.charset);
            LogUtil.e(data);
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("status")) {
				int code = jsonObject.getInt("status");
				if (code == 200) {
					return Response.success(mParser.parse(jsonObject.getString("data")), response);
				}else{
					return Response.error(new ParseError(code, jsonObject.getString("msg")));
				}
			}
            return Response.error(new ParseError(405, "请求错误："+data));
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
        	e.printStackTrace();
        	return Response.error(new ParseError(505,"数据格式化错误"));
		}
	}

}
