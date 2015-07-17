package com.huizhuang.zxsq.http.bean.common;

import java.io.Serializable;

public class Housing implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String comarea;
    private String district;
    private String lon; // "103.84424178218",
    private String lat; // "30.723955712531",
    private int is_possession;// 是不是期房，1是 0不是
    private String province_name;
    private String city_name;
    
    public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
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

    public String getComarea() {
        return comarea;
    }

    public void setComarea(String comarea) {
        this.comarea = comarea;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public int getIs_possession() {
        return is_possession;
    }

    public void setIs_possession(int is_possession) {
        this.is_possession = is_possession;
    }

    @Override
    public String toString() {
        return "Housing [id=" + id + ", name=" + name + ", comarea=" + comarea + ", district=" + district + ", lon=" + lon + ", lat=" + lat
                + ", is_possession=" + is_possession + "]";
    }

}
