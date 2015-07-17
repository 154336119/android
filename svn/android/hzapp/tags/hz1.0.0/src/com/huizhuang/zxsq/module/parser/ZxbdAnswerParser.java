package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.ZxbdAnswer;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;
import com.huizhuang.zxsq.utils.LogUtil;

public class ZxbdAnswerParser extends BaseJsonParser<ArrayList<ZxbdAnswer>> {

	@Override
	public ArrayList<ZxbdAnswer> parseIType(String jsonString)
			throws JSONException {
		ArrayList<ZxbdAnswer> group = new ArrayList<ZxbdAnswer>();
		JSONObject data = new JSONObject(jsonString);
		parseGroup(data, group);
		return group;
	}
	
	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject dataJson, ArrayList<ZxbdAnswer> group) throws JSONException {
		String title = dataJson.getString("title");
		JSONArray ansJa = dataJson.getJSONArray("list");
		int length2 = ansJa.length();
		for (int j = 0; j < length2; j++) {
			JSONObject ansJson = ansJa.getJSONObject(j);
			ZxbdAnswer answer = new ZxbdAnswer();
			JSONArray quesJa = ansJson.getJSONArray("list");
			ArrayList<ZxbdAnswer> queslist = new ArrayList<ZxbdAnswer>();
			int quesLength = quesJa.length();
			for (int k = 0; k < quesLength; k++) {
				JSONObject quesJson = quesJa.getJSONObject(k);
				ZxbdAnswer ques = new ZxbdAnswer();
				ques.type = quesJson.getInt("type");
				ques.content = quesJson.getString("content");
				
				LogUtil.i("ques.needSubmit:"+ques.needSubmit+" ques.content: "+ques.content);
				queslist.add(ques);
			}
			answer.title = ansJson.getString("title");
			answer.needSubmit=ansJson.optInt("needSubmit");
			answer.childs = queslist;
			group.add(answer);
		}
	}
}
