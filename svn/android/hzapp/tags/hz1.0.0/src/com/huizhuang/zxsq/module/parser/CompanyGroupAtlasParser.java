package com.huizhuang.zxsq.module.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.CompanyGroup;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

public class CompanyGroupAtlasParser extends BaseJsonParser<CompanyGroup> {

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
		if (jsonObject.has("albums")) {
			JSONObject dataJb = jsonObject.getJSONObject("albums");
			group.setTotalSize(dataJb.optInt("totalrecord"));
			group.setPageTotal(dataJb.optInt("totalpage"));
			if (dataJb.has("list")) {
				JSONArray array = dataJb.getJSONArray("list");
				JSONObject companyJb = null;
				for (int i = 0; i < array.length(); i++) {
					companyJb = array.getJSONObject(i);
					Company company = new Company();
					
					Atlas ra = new Atlas();
					ra.setId(companyJb.optString("album_id"));//套图ID
					ra.setDescription(companyJb.optString("design_idea"));//套图简介
					company.setRecommendAtlas(ra);
					
					//公司信息
					if (companyJb.has("store")) {
						JSONObject storeJb = companyJb.getJSONObject("store");
						company.setId(storeJb.optInt("id"));
						company.setFullName(storeJb.optString("name"));
						company.setLogoImage(storeJb.optString("logo"));
					}
					
					//封面图
					if (companyJb.has("thumb")) {
						JSONObject thumbJb = companyJb.getJSONObject("thumb");
						Atlas coverAtlas = new Atlas();
						coverAtlas.setId(thumbJb.optString("id"));
						coverAtlas.setImage(thumbJb.optString("img"));
						coverAtlas.setFavorited(thumbJb.optInt("favored") == 0?false:true);
						company.setCoverAtlas(coverAtlas);
					}
					
					group.add(company);
				}
			}
		}
	}
}
