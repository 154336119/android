package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.DiaryNode;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

public class DiaryNodeParser extends BaseJsonParser<ArrayList<DiaryNode>> {

	@Override
	public ArrayList<DiaryNode> parseIType(String jsonString) throws JSONException {
		ArrayList<DiaryNode> group = new ArrayList<DiaryNode>();
		JSONObject jsonObject = new JSONObject(jsonString);
		parseGroup(jsonObject, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject jsonObject, ArrayList<DiaryNode> group) throws JSONException {
		if (jsonObject.has("nodes")) {
			JSONArray nodeArray = jsonObject.getJSONArray("nodes");
			int length = nodeArray.length();
			for (int i = 0; i < length; i++) {
				DiaryNode node = new DiaryNode();
				JSONObject nodeJson = nodeArray.getJSONObject(i);
				node.setId(nodeJson.getString("id"));
				node.setName(nodeJson.getString("name"));
				node.setDays(nodeJson.getInt("days"));
				node.setDiaryCount(nodeJson.getInt("diary_count"));
				group.add(node);
			}
		}
	}

}