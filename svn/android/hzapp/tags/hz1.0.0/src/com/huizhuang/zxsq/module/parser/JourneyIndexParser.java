package com.huizhuang.zxsq.module.parser;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.JourneyIndex;
import com.huizhuang.zxsq.module.parser.base.BaseParser;
import com.huizhuang.zxsq.utils.LogUtil;

public class JourneyIndexParser implements BaseParser<JourneyIndex> {

	@Override
	public JourneyIndex parse(String jsonString) throws JSONException {
		JSONObject obj=new JSONObject(jsonString);
		
		
		return paresJourneyIndex(obj);
	}

	private JourneyIndex paresJourneyIndex(JSONObject obj) {
		JourneyIndex index=new JourneyIndex();
		index.setAddress(obj.optString("address"));
		index.setRank(obj.optString("rank"));
		index.setJourneyTotal(obj.optDouble("journey_total"));
		String min=obj.optString("min_date");
		if(min!=null){
			min=min.replaceAll("-", ".");
		}
		index.setMinDate(min);
		String max=obj.optString("max_date");
		if(max!=null){
			max=max.replaceAll("-", ".");
		}
		index.setMaxDate(max);
		JSONArray journeyObj=obj.optJSONArray("journey_list");
		if(journeyObj!=null){
			Map<Integer,Double> journeyList=new HashMap<Integer,Double>();
			for(int i=1;i<7;i++){
				journeyList.put(i, 0D);
			}
			double cost = 0;
			LogUtil.i(" journeyObj:"+journeyObj);
			for(int i=0;i<journeyObj.length();i++){
				JSONObject object = journeyObj.optJSONObject(i);
				int num=object.optInt("t_type");
				cost = object.optDouble("total");
				if(Double.isNaN(cost)){
					cost=0;
				}
				journeyList.put(num, cost);
			}
			index.setJourneyMap(journeyList);
		}
		return index;
	}

}
