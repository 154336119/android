package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

import com.huizhuang.zxsq.http.bean.Logo;

public class Foreman {

	// 工长ID 商家id
	private String store_id;

	private String full_name;

	private String short_name;
	// 口碑评分
	private String rank;
	// 评论数量
	private String comment_count;
	// 预约人数
	private String order_count;
	// 成交量
	private String done_count;
	// 施工现场数量
	private String case_count;
	// 报价清单数量
	private String price_list_amount;
	// 距离
	private String distance;
	//
	private List<String> services;
	// logo图片
	private Logo logo;

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

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getOrder_count() {
		return order_count;
	}

	public void setOrder_count(String order_count) {
		this.order_count = order_count;
	}

	public String getDone_count() {
		return done_count;
	}

	public void setDone_count(String done_count) {
		this.done_count = done_count;
	}
	
	public String getCase_count() {
		return case_count;
	}

	public void setCase_count(String case_count) {
		this.case_count = case_count;
	}

	public String getPrice_list_amount() {
		return price_list_amount;
	}

	public void setPrice_list_amount(String price_list_amount) {
		this.price_list_amount = price_list_amount;
	}
	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public Logo getLogo() {
		return logo;
	}

	public void setLogo(Logo logo) {
		this.logo = logo;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
}
