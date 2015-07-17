package com.huizhuang.zxsq.http.bean.foreman;

import com.huizhuang.zxsq.http.bean.Logo;

public class ConstructionSite {
	
	//装修现场ID
	private String showcase_id;
	//施工现场名
	private String site_name;
	//户型
	private String door_model;
	//工期
	private String construction_period;
	//装修方式
	private String renovation_way;
	//预算
	private int cost;
	//面积
	private String size;
	//阶段
	private String phrase;
	//图片地址
	private Logo image ;

	
	public String getShowcase_id() {
		return showcase_id;
	}
	public void setShowcase_id(String showcase_id) {
		this.showcase_id = showcase_id;
	}
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public String getDoor_model() {
		return door_model;
	}
	public void setDoor_model(String door_model) {
		this.door_model = door_model;
	}
	public String getConstruction_period() {
		return construction_period;
	}
	public void setConstruction_period(String construction_period) {
		this.construction_period = construction_period;
	}
	public String getRenovation_way() {
		return renovation_way;
	}
	public void setRenovation_way(String renovation_way) {
		this.renovation_way = renovation_way;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public Logo getImage() {
		return image;
	}
	public void setImage(Logo image) {
		this.image = image;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
}
