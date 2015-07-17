package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.MyMessage;
import com.huizhuang.zxsq.module.MyMessage.Message;
import com.huizhuang.zxsq.module.parser.base.BaseParser;
import com.huizhuang.zxsq.utils.LogUtil;

public class MyMessageParser implements BaseParser<MyMessage> {

	@Override
	public MyMessage parse(String jsonString) throws JSONException {
		JSONObject obj=new JSONObject(jsonString);
		return parseMyMessage(obj);
	}

	private MyMessage parseMyMessage(JSONObject obj) {
		MyMessage myMessage=new MyMessage();
		myMessage.setTotalrecord(obj.optInt("totalpage"));
		myMessage.setTotalpage(obj.optInt("totalrecord"));
		JSONArray array=obj.optJSONArray("list");
		List<Message> list= new ArrayList<MyMessage.Message>();
		for(int i=0;i<array.length();i++){
			Message msg=myMessage.new Message();
			JSONObject objItem=array.optJSONObject(i);
			LogUtil.i(" objItem:"+objItem);
			msg.setAddTime(objItem.optLong("add_time"));
			msg.setContent(objItem.optString("content"));
			msg.setDetailUrl(objItem.optString("detail_url"));
			msg.setId(objItem.optInt("id"));
			msg.setRead(objItem.optInt("read"));
			msg.setUserId(objItem.optInt("user_id"));
			msg.setUserThumb(objItem.optString("user_thumb"));
			msg.setMessageType(objItem.optInt("msg_type"));
			list.add(msg);
		
		}
		myMessage.setMessageList(list);
		return myMessage;
	}

}
