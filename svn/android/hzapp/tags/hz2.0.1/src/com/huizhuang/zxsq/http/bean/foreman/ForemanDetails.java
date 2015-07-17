package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

public class ForemanDetails {

    private String store_id;
    private int is_auth;
    private String full_name;
    private String rank;
    private String short_name;
    private int comment_count;
    private String company_address;
    private int work_age;
    private int order_count;
    private String place_name;
    private String avg_price;
    private String experience;
    private String distance;
    private Image logo;
    private List<ConstructionSite> showcase;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public int getWork_age() {
        return work_age;
    }

    public void setWork_age(int work_age) {
        this.work_age = work_age;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public List<ConstructionSite> getShowcase() {
        return showcase;
    }

    public void setShowcase(List<ConstructionSite> showcase) {
        this.showcase = showcase;
    }

    @Override
    public String toString() {
        return "ForemanDetails [store_id=" + store_id + ", is_auth=" + is_auth + ", full_name="
                + full_name + ", rank=" + rank + ", short_name=" + short_name + ", comment_count="
                + comment_count + ", company_address=" + company_address + ", work_age=" + work_age
                + ", order_count=" + order_count + ", place_name=" + place_name + ", avg_price="
                + avg_price + ", experience=" + experience + ", distance=" + distance + ", logo="
                + logo + ", showcase=" + showcase + "]";
    }


}
