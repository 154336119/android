package com.huizhuang.zxsq.http.bean.foreman;

import com.huizhuang.zxsq.http.bean.common.Image;

public class Advertisement {

	private String title;
	private String tips;
	private Image images;
	
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
	public Image getImages() {
		return images;
	}
	public void setImages(Image images) {
		this.images = images;
	}
}
