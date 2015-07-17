package com.huizhuang.zxsq.http.bean.order;

import java.io.Serializable;

import com.huizhuang.zxsq.http.bean.common.Image;

public class OrderSignForeman implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String allot_id;//子订单id
	private String sub_order_no;//子订单号
	private int statu;//订单分配状态:-4 无法量房 -3 竞标失败, -2 已拒绝, -1 已取消, 1 待确认, 2 待量房, 3 待出图, 4 待签合同, 5 已签合同, 6 装修中, 7 已完
	private String short_name;//店铺/工长简称
	private String full_name;//店铺/工长全称
	private int is_book;//0-工长未发起签约，1-工长发起签约，2-工长已确认签约 
	private int area_id;//城市id
	private String city_name;//城市所在省份+城市名
	private int store_id;//工长id;
	private int is_auth;//是否认证
	private int logo_img_id;//头像id;
	private float score;//分数
	private int gongzhang_orders;//工长订单数
	private Image image;
	private String mobile;//电话
	public String getAllot_id() {
		return allot_id;
	}
	public void setAllot_id(String allot_id) {
		this.allot_id = allot_id;
	}
	public String getSub_order_no() {
		return sub_order_no;
	}
	public void setSub_order_no(String sub_order_no) {
		this.sub_order_no = sub_order_no;
	}
	public int getStatu() {
		return statu;
	}
	public void setStatu(int status) {
		this.statu = status;
	}
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public int getIs_book() {
		return is_book;
	}
	public void setIs_book(int is_book) {
		this.is_book = is_book;
	}
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	public int getIs_auth() {
		return is_auth;
	}
	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}
	public int getLogo_img_id() {
		return logo_img_id;
	}
	public void setLogo_img_id(int logo_img_id) {
		this.logo_img_id = logo_img_id;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public int getGongzhang_orders() {
		return gongzhang_orders;
	}
	public void setGongzhang_orders(int gongzhang_orders) {
		this.gongzhang_orders = gongzhang_orders;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Override
	public String toString() {
		return "OrderSignForeman [allot_id=" + allot_id + ", sub_order_no="
				+ sub_order_no + ", statu=" + statu + ", short_name="
				+ short_name + ", full_name=" + full_name + ", is_book="
				+ is_book + ", area_id=" + area_id + ", city_name=" + city_name
				+ ", store_id=" + store_id + ", is_auth=" + is_auth
				+ ", logo_img_id=" + logo_img_id + ", score=" + score
				+ ", gongzhang_orders=" + gongzhang_orders + ", image=" + image
				+ "]";
	}
	
}
