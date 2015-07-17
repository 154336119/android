package com.huizhuang.zxsq.http.bean.home;

import java.util.List;

import com.huizhuang.zxsq.http.bean.Logo;

public class GoodForemanCase {

	//
	private String id;
	//工长id
	private String store_id;
	//工长名称
	private String full_name;
	//小区名
	private String housing_name;
	private List<Logo> images;
	//评分 数
	private String comment_count;
	//评分 
	private String comment_avg;

	private Logo user_logo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getHousing_name() {
		return housing_name;
	}

	public void setHousing_name(String housing_name) {
		this.housing_name = housing_name;
	}

	public List<Logo> getImages() {
		return images;
	}

	public void setImages(List<Logo> images) {
		this.images = images;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getComment_avg() {
		return comment_avg;
	}

	public void setComment_avg(String comment_avg) {
		this.comment_avg = comment_avg;
	}

	public Logo getUser_logo() {
		return user_logo;
	}

	public void setUser_logo(Logo user_logo) {
		this.user_logo = user_logo;
	}

}
