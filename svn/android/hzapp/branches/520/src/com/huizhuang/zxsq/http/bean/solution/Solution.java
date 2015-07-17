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
    private String id;
    private String housing_name;
    private String zx_node;
    private float cost;
    private int renovation_way;
    private float room_area;
    private String user_name;
    private String door_model;
    private Image image;
    private String store_name;

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

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getRenovation_way() {
        return renovation_way;
    }

    public void setRenovation_way(int renovation_way) {
        this.renovation_way = renovation_way;
    }

    public float getRoom_area() {
        return room_area;
    }

    public void setRoom_area(float room_area) {
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

    @Override
    public String toString() {
        return "Solution [id=" + id + ", housing_name=" + housing_name + ", zx_node=" + zx_node
                + ", cost=" + cost + ", renovation_way=" + renovation_way + ", room_area="
                + room_area + ", user_name=" + user_name + ", door_model=" + door_model
                + ", image=" + image + ", store_name=" + store_name + "]";
    }

    
}
