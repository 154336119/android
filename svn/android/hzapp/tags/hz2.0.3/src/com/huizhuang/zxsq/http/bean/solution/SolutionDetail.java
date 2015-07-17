package com.huizhuang.zxsq.http.bean.solution;

import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.http.bean.foreman.TimeSlot;

/**
 * 施工现场详情头部
 * 
 * @author th
 * 
 */
public class SolutionDetail {

    private String id;
    private String name;
    private Float budget;
    private TimeSlot data;
    private String renovation_way;
    private String area;
    private String room_style;
    private int zx_node;
    private String housing_name;
    private int order_count;
    private Image image;
    private String door_model;
    private String store_id;
    private String store_name;
    private String lat;
    private String lon;
    private int distance;
    
    public SolutionDetail() {
		super();
	}

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

    public Float getBudget() {
        return budget;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

    public TimeSlot getData() {
        return data;
    }

    public void setData(TimeSlot data) {
        this.data = data;
    }

    public String getRenovation_way() {
        return renovation_way;
    }

    public void setRenovation_way(String renovation_way) {
        this.renovation_way = renovation_way;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRoom_style() {
        return room_style;
    }

    public void setRoom_style(String room_style) {
        this.room_style = room_style;
    }

    public int getZx_node() {
        return zx_node;
    }

    public void setZx_node(int zx_node) {
        this.zx_node = zx_node;
    }

    public String getHousing_name() {
        return housing_name;
    }

    public void setHousing_name(String housing_name) {
        this.housing_name = housing_name;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    public String getDoor_model() {
        return door_model;
    }

    public void setDoor_model(String door_model) {
        this.door_model = door_model;
    }
    
    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public SolutionDetail(String id, String name, Float budget, TimeSlot data,
			String renovation_way, String area, String room_style, int zx_node,
			String housing_name, int order_count, Image image,
			String door_model, String store_id, String store_name, String lat,
			String lon, int distance) {
		super();
		this.id = id;
		this.name = name;
		this.budget = budget;
		this.data = data;
		this.renovation_way = renovation_way;
		this.area = area;
		this.room_style = room_style;
		this.zx_node = zx_node;
		this.housing_name = housing_name;
		this.order_count = order_count;
		this.image = image;
		this.door_model = door_model;
		this.store_id = store_id;
		this.store_name = store_name;
		this.lat = lat;
		this.lon = lon;
		this.distance = distance;
	}


}
