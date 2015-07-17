package com.huizhuang.zxsq.http.bean.solution;

import java.io.Serializable;

import com.huizhuang.zxsq.http.bean.common.Image;

/**
 * 施工现场列表
 * 
 * @author th
 * 
 */
public class Solution implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;//id
    private String housing_name;//小区名
    private String zx_node;//阶段ID
    private String cost;//预算
    private int renovation_way;//半包、清包、全包
    private String room_area;//面积
    private String user_name;//业主
    private String door_model;//户型
    private Image image;//图片
    private String store_name;//工长名
    private int views;//人气
    private String distance;//距离
    private String room_style;//风格
    
    public Solution() {
		super();
	}

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

    public String getZx_node() {
        return zx_node;
    }

    public void setZx_node(String zx_node) {
        this.zx_node = zx_node;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getRenovation_way() {
        return renovation_way;
    }

    public void setRenovation_way(int renovation_way) {
        this.renovation_way = renovation_way;
    }

    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDoor_model() {
        return door_model;
    }

    public void setDoor_model(String door_model) {
        this.door_model = door_model;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getRoom_style() {
		return room_style;
	}

	public void setRoom_style(String room_style) {
		this.room_style = room_style;
	}

	@Override
	public String toString() {
		return "Solution [id=" + id + ", housing_name=" + housing_name
				+ ", zx_node=" + zx_node + ", cost=" + cost
				+ ", renovation_way=" + renovation_way + ", room_area="
				+ room_area + ", user_name=" + user_name + ", door_model="
				+ door_model + ", image=" + image + ", store_name="
				+ store_name + ", views=" + views + ", distance=" + distance
				+ ", room_style=" + room_style + "]";
	}

}
