package com.huizhuang.zxsq.module.parser;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.huizhuang.zxsq.module.Discuss;
import com.huizhuang.zxsq.module.DiscussGroup;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

public class DiscussGroupParser extends BaseJsonParser<DiscussGroup>{

	@Override
	public DiscussGroup parseIType(String jsonString) throws JSONException {
		DiscussGroup group = new DiscussGroup();
		JSONObject jsonObject = new JSONObject(jsonString);
		parseGroup(jsonObject, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject jsonObject, DiscussGroup group) throws JSONException {
			group.setTotalSize(jsonObject.optInt("totalrecord"));
			group.setPageTotal(jsonObject.optInt("totalpage"));
			if (jsonObject.has("list")) {
				JSONArray array = jsonObject.getJSONArray("list");
				JSONObject jb = null;
				for (int i = 0; i < array.length(); i++) {
					jb = array.getJSONObject(i);
					Discuss mDiscuss = new Discuss();
					mDiscuss.setId(jb.getInt("comment_id"));
					mDiscuss.setContent(jb.getString("content"));
					mDiscuss.setUpNum(jb.getInt("up_num"));
					mDiscuss.SetDownNum(jb.getInt("down_num"));
					mDiscuss.setDatetime(jb.getString("add_time"));
					//float score = (float) jb.optDouble("rank_score_float"); //rank_score_float
					String score = jb.optString("rank_score");
					mDiscuss.setScore(Float.parseFloat(score));
					mDiscuss.setUserId(jb.getJSONObject("author").getInt("user_id"));
					mDiscuss.setUsername(jb.getJSONObject("author").getString("screen_name"));
					mDiscuss.setUserThumb(jb.getJSONObject("author").getJSONObject("user_thumb").getString("img_path"));
					group.add(mDiscuss);
				}
			}
		
		
		if (jsonObject.has("rank")) {
			HashMap<String, Float> scores = new HashMap<String, Float>();
			JSONArray array = jsonObject.getJSONArray("rank");
			JSONObject scorebject = null;
			int length = array.length();
			for (int i = 0; i < length; i++) {
				scorebject = array.getJSONObject(i);
				String name = scorebject.optString("name");
				float score = (float) scorebject.optDouble("rank_score_float");
				scores.put(name, score);
			}
			group.setScores(scores);
		}
		group.setRankScore(jsonObject.optString("rank_score"));
		
		
	}
}
