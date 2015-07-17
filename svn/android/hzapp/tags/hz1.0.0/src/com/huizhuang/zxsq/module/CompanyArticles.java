package com.huizhuang.zxsq.module;

import java.io.Serializable;

public class CompanyArticles implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String summary;//摘要
	private String img;
	private String addTime;
	private int shareCount;
	
	private int ffCount; //收藏数
	private int commentCount;
	private boolean isFavored;// 
	private String detailUrl;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTtitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public int getFfCount() {
		return ffCount;
	}

	public void setFfCount(int ffCount) {
		this.ffCount = ffCount;
	}
	
	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	public boolean getIsFavored() {
		return isFavored;
	}

	public void setIsFavored(boolean isFavored) {
		this.isFavored = isFavored;
	}
	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	

}
