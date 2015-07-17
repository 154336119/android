package com.huizhuang.zxsq.http;

import java.util.List;

public class CityDistrict {

	private int id;
	private String name;
	private int parent_id;
	private int level;
	
	private List<CityDistrict> district;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<CityDistrict> getDistrict() {
		return district;
	}

	public void setDistrict(List<CityDistrict> district) {
		this.district = district;
	}
}
