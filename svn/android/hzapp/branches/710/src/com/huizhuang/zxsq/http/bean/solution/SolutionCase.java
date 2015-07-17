package com.huizhuang.zxsq.http.bean.solution;


import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.bean.common.HouseType;
import com.huizhuang.zxsq.http.bean.common.Housing;
import com.huizhuang.zxsq.http.bean.common.Image;

public class SolutionCase {

    private int id; //
    private Housing housing;
    private KeyValue room_style;// 风格
    private HouseType house_type; //
    private String budget; // 预算
    private int renovation_way; // 1半包，2全包，
    private String start_date; //
    private String end_date; //
    private Image image;// 图片
    private KeyValue zx_node; // 工地进度

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Housing getHousing() {
        return housing;
    }

    public void setHousing(Housing housing) {
        this.housing = housing;
    }

    public KeyValue getRoom_style() {
        return room_style;
    }

    public void setRoom_style(KeyValue room_style) {
        this.room_style = room_style;
    }

    public HouseType getHouse_type() {
        return house_type;
    }

    public void setHouse_type(HouseType house_type) {
        this.house_type = house_type;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public int getRenovation_way() {
        return renovation_way;
    }

    public void setRenovation_way(int renovation_way) {
        this.renovation_way = renovation_way;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public KeyValue getZx_node() {
        return zx_node;
    }

    public void setZx_node(KeyValue zx_node) {
        this.zx_node = zx_node;
    }

    @Override
    public String toString() {
        return "SolutionCase [id=" + id + ", housing=" + housing + ", room_style=" + room_style + ", house_type=" + house_type + ", budget=" + budget
                + ", renovation_way=" + renovation_way + ", start_date=" + start_date + ", end_date=" + end_date + ", image=" + image + ", zx_node="
                + zx_node + "]";
    }

}
