
package com.huizhuang.zxsq.module.parser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.CompanyArticles;
import com.huizhuang.zxsq.module.CompanyArticlesGroup;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

public class CompanyArticlesGroupParser extends BaseJsonParser<CompanyArticlesGroup> {

	@Override
	public CompanyArticlesGroup parseIType(String jsonString) throws JSONException {
		CompanyArticlesGroup group = new CompanyArticlesGroup();
		JSONObject jsonObject = new JSONObject(jsonString);
		parseGroup(jsonObject, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject jsonObject, CompanyArticlesGroup group) throws JSONException {
		group.setTotalSize(jsonObject.getJSONObject("diaries").optInt("totalrecord"));
		group.setPageTotal(jsonObject.getJSONObject("diaries").optInt("totalpage"));
		if (jsonObject.getJSONObject("diaries").has("list")) {
			JSONArray array = jsonObject.getJSONObject("diaries").getJSONArray("list");
			JSONObject companyArticlesJb = null;
			for (int i = 0; i < array.length(); i++) {
				companyArticlesJb = array.getJSONObject(i);
				CompanyArticles companyArticle = new CompanyArticles();
				companyArticle.setId(companyArticlesJb.optString("id"));
				companyArticle.setTitle(companyArticlesJb.optString("title"));
				companyArticle.setSummary(companyArticlesJb.optString("summary"));
				companyArticle.setImg(companyArticlesJb.optString("img"));
				companyArticle.setAddTime(companyArticlesJb.optString("add_time"));
				companyArticle.setShareCount(companyArticlesJb.optInt("share_count"));
				companyArticle.setFfCount(companyArticlesJb.optInt("ff_count"));
				companyArticle.setCommentCount(companyArticlesJb.optInt("comment_count"));
				companyArticle.setIsFavored(companyArticlesJb.optInt("is_favored")==0?false:true);
				companyArticle.setDetailUrl(companyArticlesJb.optString("detail_url"));
				
				group.add(companyArticle);
	}
}
		
	}
}
