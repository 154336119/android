package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.TypeSummary;
import com.huizhuang.zxsq.module.parser.base.BaseParser;

public class BillDetailTypeParser implements BaseParser<List<TypeSummary>> {

	@Override
	public List<TypeSummary> parse(String jsonString) throws JSONException {
		JSONArray typeSumArray=new JSONArray(jsonString);
		return parseTypeSummary(typeSumArray);
	}

	private List<TypeSummary> parseTypeSummary(JSONArray typeSumArray) {
		List<TypeSummary> typeSumList=new ArrayList<TypeSummary>();
		for(int i=0;i<typeSumArray.length();i++){
			JSONObject objItem=typeSumArray.optJSONObject(i);
			TypeSummary typeSumary=new TypeSummary();
			typeSumary.setcType(objItem.optInt("c_type"));
			typeSumary.setcTypeName(objItem.optString("c_type_name"));
			typeSumary.setTotal(objItem.optDouble("total"));
			typeSumary.settType(objItem.optInt("t_type"));
			typeSumary.settTypeName(objItem.optString("t_type_name"));
			typeSumList.add(typeSumary);
		}
		return null;
	}

}
