package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

import com.huizhuang.zxsq.http.bean.order.Stage;


/**
 * @ClassName: Order
 * @Description: 订单类
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-7 上午11:11:15
 * 
 */
public class Order implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int order_id;
    private String order_no;
    private int add_time;
    private int statu;
    private String housing_address;
    private String housing_name;
    private String measuring_time;
    private String user_remark;
    private int order_type;
    private int area_id;
    private int sub_statu;
    private int is_book;
    private int can_pj;
    private String stage_time;
    private Stage stage;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public String getHousing_address() {
        return housing_address;
    }

    public void setHousing_address(String housing_address) {
        this.housing_address = housing_address;
    }

    public String getHousing_name() {
        return housing_name;
    }

    public void setHousing_name(String housing_name) {
        this.housing_name = housing_name;
    }

    public String getMeasuring_time() {
        return measuring_time;
    }

    public void setMeasuring_time(String measuring_time) {
        this.measuring_time = measuring_time;
    }

    public String getUser_remark() {
        return user_remark;
    }

    public void setUser_remark(String user_remark) {
        this.user_remark = user_remark;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getSub_statu() {
        return sub_statu;
    }

    public void setSub_statu(int sub_statu) {
        this.sub_statu = sub_statu;
    }

    public int getIs_book() {
        return is_book;
    }

    public void setIs_book(int is_book) {
        this.is_book = is_book;
    }

    public int getCan_pj() {
        return can_pj;
    }

    public void setCan_pj(int can_pj) {
        this.can_pj = can_pj;
    }

    public String getStage_time() {
        return stage_time;
    }

    public void setStage_time(String stage_time) {
        this.stage_time = stage_time;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "Order [order_id=" + order_id + ", order_no=" + order_no + ", add_time=" + add_time
                + ", statu=" + statu + ", housing_address=" + housing_address + ", housing_name="
                + housing_name + ", measuring_time=" + measuring_time + ", user_remark="
                + user_remark + ", order_type=" + order_type + ", area_id=" + area_id
                + ", sub_statu=" + sub_statu + ", is_book=" + is_book + ", can_pj=" + can_pj
                + ", stage_time=" + stage_time + ", stage=" + stage + "]";
    }


}
