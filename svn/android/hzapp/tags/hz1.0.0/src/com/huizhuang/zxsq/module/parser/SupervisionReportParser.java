package com.huizhuang.zxsq.module.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.huizhuang.zxsq.module.SupervisionReport;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

public class SupervisionReportParser extends BaseJsonParser<SupervisionReport> {

	@Override
	public SupervisionReport parseIType(String jsonString) throws JSONException {
		JSONObject data = new JSONObject(jsonString);
		Log.e("js", jsonString);
		return parseTypeReport(data);
	}

	private SupervisionReport parseTypeReport(JSONObject data) throws JSONException {
		SupervisionReport supervisionReport = new SupervisionReport();
		JSONObject jSONObject = data.optJSONObject("curr_node");
		supervisionReport.setId(jSONObject.optString("id"));
		supervisionReport.setTitle(jSONObject.optString("name"));
		supervisionReport.setResult(jSONObject.optString("remark"));
		supervisionReport.setScore(jSONObject.optInt("score"));
		JSONObject problems = data.optJSONObject("problems");
		JSONArray prev = problems.optJSONArray("prev");
		int lastProblems = 0;
		for (int i = 0; i < prev.length(); i++) {//上节点
			JSONObject jb = prev.optJSONObject(i);
			int status = jb.optInt("statu");
			if(status == -1){
				lastProblems += 1;
			}
		}
		supervisionReport.setLastProblems(lastProblems);
		JSONArray curr = problems.optJSONArray("curr");
		int nowProblems = 0;
		int editProblems = 0;
		for (int i = 0; i < curr.length(); i++) {
			JSONObject jb = curr.optJSONObject(i);
			JSONArray sub = jb.optJSONArray("sub");
			for (int j = 0; j < sub.length(); j++) {
				JSONObject subProblem = sub.optJSONObject(j);
				int status = subProblem.optInt("statu");
				if(status == 1){
					editProblems += 1;
				}else if(status == -1){
					nowProblems += 1;
				}
			}
		}
		supervisionReport.setNowProblems(nowProblems);
		supervisionReport.setEditproblems(editProblems);
		return supervisionReport;
	}
}
