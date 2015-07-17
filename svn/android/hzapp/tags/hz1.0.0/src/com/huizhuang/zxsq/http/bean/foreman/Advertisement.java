package com.huizhuang.zxsq.http.bean.foreman;

import com.huizhuang.zxsq.http.bean.Logo;

public class Advertisement {

	private String title;
	private String tips;
	private Logo images;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public Logo getImages() {
		return images;
	}
	public void setImages(Logo images) {
		this.images = images;
	}
}
