package com.huizhuang.zxsq.http.bean.order;


public class OrderDetailChild {

    private String name;//工长或商家的名称
    private int statu;//订单分配状态
    private String store_id;//工长/商家id
    private String allot_id;//子订单id
    private int ske_contract_time;
    private int measure; // 是否量房 0.不可以量房 1.可以量房
    private int contract;// 是否预约
    private String mobile;//电话
    private String avater;//头像
    private String score;//分数
    private int orders;//接单数
    private String city;//城市
    private int is_auth;//是否认证
    private int is_pj;//有无评价 1：有   0：无

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }

    public int getContract() {
        return contract;
    }

    public void setContract(int contract) {
        this.contract = contract;
    }

    public int getSke_contract_time() {
        return ske_contract_time;
    }

    public void setSke_contract_time(int ske_contract_time) {
        this.ske_contract_time = ske_contract_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getAllot_id() {
        return allot_id;
    }

    public void setAllot_id(String allot_id) {
        this.allot_id = allot_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }


    public int getIs_pj() {
        return is_pj;
    }

    public void setIs_pj(int is_pj) {
        this.is_pj = is_pj;
    }

    @Override
    public String toString() {
        return "OrderDetailChild [name=" + name + ", statu=" + statu + ", store_id=" + store_id
                + ", allot_id=" + allot_id + ", ske_contract_time=" + ske_contract_time
                + ", measure=" + measure + ", contract=" + contract + ", mobile=" + mobile
                + ", avater=" + avater + ", score=" + score + ", orders=" + orders + ", city="
                + city + ", is_auth=" + is_auth + ", is_pj=" + is_pj + "]";
    }


}
