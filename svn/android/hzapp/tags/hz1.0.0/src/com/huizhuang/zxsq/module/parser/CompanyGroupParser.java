
package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.CompanyGroup;
import com.huizhuang.zxsq.module.Discuss;
import com.huizhuang.zxsq.module.Service;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

public class CompanyGroupParser extends BaseJsonParser<CompanyGroup> {

	@Override
	public CompanyGroup parseIType(String jsonString) throws JSONException {
		CompanyGroup group = new CompanyGroup();
		JSONObject jsonObject = new JSONObject(jsonString);
		parseGroup(jsonObject, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject jsonObject, CompanyGroup group) throws JSONException {
		group.setTotalSize(jsonObject.optInt("totalrecord"));
		group.setPageTotal(jsonObject.optInt("totalpage"));
		if (jsonObject.has("list")) {
			JSONArray array = jsonObject.getJSONArray("list");
			JSONObject companyJb = null;
			for (int i = 0; i < array.length(); i++) {
				companyJb = array.getJSONObject(i);
				Company company = new Company();
				company.setIsFavored(companyJb.optInt("is_favored")==0?false:true);
				company.setId(companyJb.optInt("store_id"));
				company.setFullName(companyJb.optString("full_name"));
				company.setShortName(companyJb.optString("short_name"));
				company.setAddress(companyJb.optString("company_address"));
				
				company.setScore(companyJb.optInt("rank"));
				company.setPx(companyJb.optString("px"));
				company.setPy(companyJb.optString("py"));
				company.setOrderNum(companyJb.optString("order_count"));
				company.setdiaryCount(companyJb.optString("diary_count"));
				company.setDiscussNum(companyJb.optString("comment_count"));
				
				company.setLogoImage(companyJb.optJSONObject("logo").optString("thumb_path"));
				if(!TextUtils.isEmpty(companyJb.optString("contract_price"))&&companyJb.optString("contract_price").contains(".")){
					String cp = companyJb.optString("contract_price");
					cp = cp.substring(0, cp.indexOf("."));
					company.setContractPrice(cp);
				}else{
				company.setContractPrice("0");
				}
				if(companyJb.optJSONObject("hot_comment")!=null&&!companyJb.optJSONObject("hot_comment").toString().equals("{}")){
					Discuss mDiscuss = new Discuss();
					JSONObject hotCommentJB = companyJb.optJSONObject("hot_comment");
					mDiscuss.setUsername(hotCommentJB.optJSONObject("author").optString("screen_name"));
					mDiscuss.setContent(hotCommentJB.optString("content"));
					company.setHotComment(mDiscuss);
				}
				if (companyJb.has("services")) {
					JSONArray serviceArray = companyJb.optJSONArray("services");
					ArrayList<Service> serviceList = new ArrayList<Service>();
					JSONObject serviceJb = null;
					for (int j = 0; j < serviceArray.length(); j++) {
						serviceJb = serviceArray.optJSONObject(j);
						Service service = new Service();
						service.setId(serviceJb.optInt("id"));
						service.setValue("value");
						serviceList.add(service);
				    }
					company.setServices(serviceList);
				}
					JSONObject sketchJb = companyJb.optJSONObject("sketch");
					if(!sketchJb.toString().equals("{}")){
 					Atlas atlas = new Atlas();
					atlas.setId(companyJb.optJSONObject("sketch").optString("id"));
					atlas.setAlbumId(companyJb.optJSONObject("sketch").optString("album_id"));
					atlas.setStyle(companyJb.optJSONObject("sketch").optString("room_style"));
					atlas.setImage(companyJb.optJSONObject("sketch").optJSONObject("img").optString("img_path"));
					company.setCoverAtlas(atlas);
					}
				
				group.add(company);
	}
}
		
	}
}
