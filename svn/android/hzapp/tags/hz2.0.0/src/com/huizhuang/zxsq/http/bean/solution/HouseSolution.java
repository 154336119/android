package com.huizhuang.zxsq.http.bean.solution;


import com.huizhuang.zxsq.http.bean.common.Image;

public class HouseSolution {

    private int id; //
    private String name;
    private int case_count;// 
    private String lon; //
    private String lat; // 
    private Image image; //
    private int showcase_id;
	public int getShowcase_id() {
		return showcase_id;
	}
	public void setShowcase_id(int showcase_id) {
		this.showcase_id = showcase_id;
	}
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
	public int getCase_count() {
		return case_count;
	}
	public void setCase_count(int case_count) {
		this.case_count = case_count;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	

}
