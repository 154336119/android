package com.huizhuang.zxsq.http.bean;

import java.io.Serializable;
import java.util.List;

public class ProviceCityArea implements Serializable{

	/**
	 * 省市区
	 */
	private static final long serialVersionUID = 1L;
	private int area_id;
	private String area_name;
	private int area_level;
	private int parent_id;
	private String areacode;
	
	private List<ProviceCityArea> citys;

	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public int getArea_level() {
		return area_level;
	}

	public void setArea_level(int area_level) {
		this.area_level = area_level;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public List<ProviceCityArea> getCitys() {
		return citys;
	}

	public void setCitys(List<ProviceCityArea> citys) {
		this.citys = citys;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	
	
}
