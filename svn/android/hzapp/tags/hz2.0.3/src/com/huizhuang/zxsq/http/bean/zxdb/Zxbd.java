package com.huizhuang.zxsq.http.bean.zxdb;

import java.io.Serializable;

public class Zxbd implements Serializable {

	/**
	 * 装修宝典
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title;
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Zxbd [id=" + id + ", title=" + title + ", url=" + url + "]";
	}
	
}
