package com.huizhuang.zxsq.http.bean.foreman;

import com.huizhuang.zxsq.http.bean.common.Image;

public class Foreman {

    // 工长ID 商家id
    private String store_id;

    private String full_name;

    private String short_name;
    // 口碑评分
    private String rank;
    // 评论数量
    private String comment_count;
    // 预约人数
    private String order_count;
    // 距离
    private String distance;
    // 均价
    private String avg_price;
    // logo图片
    private Image logo;
    // 从业年限
    private int employed_age;
    // 是否认证
    private int is_auth;
    private String num;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getEmployed_age() {
        return employed_age;
    }

    public void setEmployed_age(int employed_age) {
        this.employed_age = employed_age;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }
    
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Foreman [store_id=" + store_id + ", full_name=" + full_name + ", short_name="
                + short_name + ", rank=" + rank + ", comment_count=" + comment_count
                + ", order_count=" + order_count + ", distance=" + distance + ", avg_price="
                + avg_price + ", logo=" + logo + ", employed_age=" + employed_age + ", is_auth="
                + is_auth + ", num=" + num + "]";
    }

}
