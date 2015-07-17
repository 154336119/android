package com.huizhuang.zxsq.http.bean.foreman;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.huizhuang.zxsq.http.bean.common.Image;

public class ConstructionSite {

    // 装修现场ID
    private String id;
    // 施工现场名
    private String name;
    // 户型
    private String room_number;
    // 装修方式
    private String renovation_way;
    // 预算
    private String cost;
    // 面积
    private String room_area;
    // 阶段
    private String zx_node;
    // 图片地址
    private Image logo;
    private String start_date;
    private String end_date;
    private String user_name;
    private String housing_name;
    private int views;
    private String room_style;

    public String getTimeSlot() {
        if (start_date == null || end_date == null) {
            return "0";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = new Date();
        Date end = new Date();
        try {
            start = format.parse(start_date);
            end = format.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = end.getTime() - start.getTime();
        time = time / (1000 * 60 * 60 * 24);
        if (time > 0) {

            return time + "";
        } else {
            return "0";
        }
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

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getRenovation_way() {
        return renovation_way;
    }

    public void setRenovation_way(String renovation_way) {
        this.renovation_way = renovation_way;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public String getZx_node() {
        return zx_node;
    }

    public void setZx_node(String zx_node) {
        this.zx_node = zx_node;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHousing_name() {
        return housing_name;
    }

    public void setHousing_name(String housing_name) {
        this.housing_name = housing_name;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getRoom_style() {
        return room_style;
    }

    public void setRoom_style(String room_style) {
        this.room_style = room_style;
    }

    @Override
    public String toString() {
        return "ConstructionSite [id=" + id + ", name=" + name + ", room_number=" + room_number
                + ", renovation_way=" + renovation_way + ", cost=" + cost + ", room_area="
                + room_area + ", zx_node=" + zx_node + ", logo=" + logo + ", start_date="
                + start_date + ", end_date=" + end_date + "]";
    }

}
