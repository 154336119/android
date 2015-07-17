package com.huizhuang.zxsq.http;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.utils.LogUtil;
import com.lgmshare.http.netroid.NetworkResponse;
import com.lgmshare.http.netroid.Request;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.Response;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.lgmshare.http.netroid.exception.ParseError;

public class BeanParserRequest<T> extends Request<T>{

	/**
     * 调试TAG
     */
    private static final String TAG = BeanParserRequest.class.getSimpleName();
	
	private ResponseParser<T> mParser = null;
	
	public BeanParserRequest(int method, String url, RequestParams params, ResponseParser<T> parse, RequestCallBack<T> callBack) {
		super(method, url, params, callBack);
		mParser = parse;
	}
	
	public BeanParserRequest(int method, String url, ResponseParser<T> parser, RequestCallBack<T> callBack) {
		super(method, url, null, callBack);
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
            LogUtil.e(TAG, data);
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
