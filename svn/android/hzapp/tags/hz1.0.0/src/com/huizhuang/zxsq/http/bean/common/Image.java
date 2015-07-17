package com.huizhuang.zxsq.http.bean.common;

import java.io.Serializable;

public class Image implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String img_path;
	private String thumb_path;
	private String imgExt;

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getThumb_path() {
		return thumb_path;
	}

	public void setThumb_path(String thumb_path) {
		this.thumb_path = thumb_path;
	}

	public String getImgExt() {
		return imgExt;
	}

	public void setImgExt(String imgExt) {
		this.imgExt = imgExt;
	}

	@Override
	public String toString() {
		return "Image [img_path=" + img_path + ", thumb_path="
				+ thumb_path + ", imgExt=" + imgExt + "]";
	}

}