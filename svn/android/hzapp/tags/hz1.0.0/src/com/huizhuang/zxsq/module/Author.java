package com.huizhuang.zxsq.module;

import java.io.Serializable;

public class Author implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String gender;
	private String roomStyle;// 风格
	private String roomType;// 户型
	private String avatar;
	
	private String province;
	private String city;
	private String followed;// 是否已关注
	
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRoomStyle() {
		return roomStyle;
	}

	public void setRoomStyle(String roomStyle) {
		this.roomStyle = roomStyle;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public String getFollowed() {
		return followed;
	}

	public void setFollowed(String followed) {
		this.followed = followed;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + name + ", gender=" + gender
				+ ", roomStyle=" + roomStyle + ", roomType=" + roomType
				+ ", avatar=" + avatar + ", province=" + province + ", city="
				+ city + ", followed=" + followed + "]";
	}
	
	

}
