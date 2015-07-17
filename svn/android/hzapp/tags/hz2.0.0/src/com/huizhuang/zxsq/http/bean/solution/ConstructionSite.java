package com.huizhuang.zxsq.http.bean.solution;

import java.io.Serializable;
import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

/**
 * 施工现场
 * 
 * @author th
 * 
 */
public class ConstructionSite implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int zx_node;
    private int cost;
    private int renovation_way;
    private int room_area;
    private String user_name;
    private String desc;
    private int count;
    private List<Image> image;
    private String add_time;

    public int getZx_node() {
        return zx_node;
    }

    public void setZx_node(int zx_node) {
        this.zx_node = zx_node;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getRenovation_way() {
        return renovation_way;
    }

    public void setRenovation_way(int renovation_way) {
        this.renovation_way = renovation_way;
    }

    public int getRoom_area() {
        return room_area;
    }

    public void setRoom_area(int room_area) {
        this.room_area = room_area;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    
    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    @Override
    public String toString() {
        return "ConstructionSite [zx_node=" + zx_node + ", cost=" + cost + ", renovation_way="
                + renovation_way + ", room_area=" + room_area + ", user_name=" + user_name
                + ", desc=" + desc + ", count=" + count + ", image=" + image + ", add_time="
                + add_time + "]";
    }

}
