package com.huizhuang.zxsq.module;

import java.io.Serializable;


public class Visitor implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String sex; //性别 1:男 2：女
	private String avatar;
	
	private String province;
	private String city;
	
	private boolean isFollowed;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}

	@Override
	public String toString() {
		return "Visitor [id=" + id + ", name=" + name + ", sex=" + sex
				+ ", avatar=" + avatar + ", province=" + province + ", city="
				+ city + ", isFollowed=" + isFollowed + "]";
	}
	
	
	
}
