package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

public class ConstructionSiteDetails {
	
	//ID
	private String id;
	//小区名
	private String housing_name;
	//标题
	private String name;
	//预算
	private int cost;
	//装修方式
	private String renovation_way;
	//户型
	private String door_model;
	//面积
	private String size;
	//风格
	private String room_style;
	//阶段ID
	private int phrase;
    private int order_count;
	//图片地址
	private Image image ;
	//施工方案
	private List<ConstructionCase> subcases;
	
	private TimeSlot data;
	private String store_id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHousing_name() {
		return housing_name;
	}
	public void setHousing_name(String housing_name) {
		this.housing_name = housing_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getRenovation_way() {
		return renovation_way;
	}
	public void setRenovation_way(String renovation_way) {
		this.renovation_way = renovation_way;
	}
	public String getDoor_model() {
		return door_model;
	}
	public void setDoor_model(String door_model) {
		this.door_model = door_model;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getRoom_style() {
		return room_style;
	}
	public void setRoom_style(String room_style) {
		this.room_style = room_style;
	}
	public int getPhrase() {
		return phrase;
	}
	public void setPhrase(int phrase) {
		this.phrase = phrase;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public List<ConstructionCase> getSubcases() {
		return subcases;
	}
	public void setSubcases(List<ConstructionCase> subcases) {
		this.subcases = subcases;
	}
	public TimeSlot getData() {
		return data;
	}
	public void setData(TimeSlot data) {
		this.data = data;
	}

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }
    
    public String getStore_id() {
        return store_id;
    }
    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
    @Override
    public String toString() {
        return "ConstructionSiteDetails{" +
                "id='" + id + '\'' +
                ", housing_name='" + housing_name + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", renovation_way='" + renovation_way + '\'' +
                ", door_model='" + door_model + '\'' +
                ", size='" + size + '\'' +
                ", room_style='" + room_style + '\'' +
                ", phrase=" + phrase +
                ", order_count=" + order_count +
                ", image=" + image +
                ", subcases=" + subcases +
                ", data=" + data +
                '}';
    }
}
