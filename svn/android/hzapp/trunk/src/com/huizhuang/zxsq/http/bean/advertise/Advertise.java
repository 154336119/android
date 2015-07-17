package com.huizhuang.zxsq.http.bean.advertise;

import java.io.Serializable;

public class Advertise implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ad_name;//活动名称
	private String img;//该活动对应的图片
	private String url;//活动的链接
	private String State;//0未启用，1为启用
	private String start_time;//活动开始的时间
	private String end_time;//活动结束的时间
	public String getAd_name() {
		return ad_name;
	}
	public void setAd_name(String ad_name) {
		this.ad_name = ad_name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	

}
