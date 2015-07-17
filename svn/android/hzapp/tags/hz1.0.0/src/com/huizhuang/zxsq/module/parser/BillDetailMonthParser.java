package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.module.BillDetailMonth;
import com.huizhuang.zxsq.module.BillDetailMonth.BillDetailItem;
import com.huizhuang.zxsq.module.parser.base.BaseParser;

public class BillDetailMonthParser implements BaseParser<BillDetailMonth> {

	@Override
	public BillDetailMonth parse(String jsonString) throws JSONException {
		JSONObject obj=new JSONObject(jsonString);
		return parseBillDetail(obj);
	}

	private BillDetailMonth parseBillDetail(JSONObject obj) {
		BillDetailMonth detail=new BillDetailMonth();
		detail.setTotal(obj.optDouble("total"));
		List<BillDetailItem> itemList=new ArrayList<BillDetailMonth.BillDetailItem>();
		JSONArray recordRrray=obj.optJSONArray("record_list");
		for(int i=0;i<recordRrray.length();i++){
			JSONObject objItem=recordRrray.optJSONObject(i);
			BillDetailItem item=detail.new BillDetailItem();
			item.setTotal(objItem.optDouble("total"));
			item.setcType(objItem.optInt("c_type"));
			item.setcTypeName(objItem.optString("c_type_name"));
			item.setDetail(objItem.optString("detail"));
			item.setId(objItem.optInt("id"));
			item.settType(objItem.optInt("t_type"));
			item.settTypeName(objItem.optString("t_type_name"));
			JSONArray imgArray=objItem.optJSONArray("imgs");
			List<Image> imgList=new ArrayList<Image>();
			for(int j=0;j<imgArray.length();j++){
				JSONObject objImg=imgArray.optJSONObject(j);
				Image image=new Image();
				image.setImg_path(objImg.optString("img_path"));
				image.setImgExt(objImg.optString("img_ext"));
				image.setThumb_path(objItem.optString("thumb_path"));
				imgList.add(image);
			}
			item.setImgs(imgList);
			itemList.add(item);
		}
		detail.setRecordList(itemList);
		
		return detail;
	}

}
