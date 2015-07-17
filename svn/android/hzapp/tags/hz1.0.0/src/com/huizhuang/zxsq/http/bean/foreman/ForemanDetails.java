package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

import com.huizhuang.zxsq.http.bean.Logo;

public class ForemanDetails {

	//工长ID
	private String store_id;
	//
	private String full_name;
	private String short_name;
	//个人宣言
	private String manifesto;
	//从业经历
	private String experience;
	//logo图片
	private Logo logo;
	//评分
	private String rank;
	//评论数
	private int comment_count;
	//业主预约数目
	private int order_count;
	//籍贯
	private String native_place;
	//工龄
	private String work_age;
	//施工方式
	private List<String> renovation_way;
	//施工队伍人数
	private int member_amount;
	//擅长
	private String skills;
	//施工现场数目
	private int case_count;
	//代表作品
	private String master_works;
	//报价清单数
	private int price_list_amount;
	//施工现场数目
	private int construction_site_amount;
	//服务保障：1,免费量房  3,免费预算  4,专业质量检测(原装修质量监理) 5,正品保障 6,延期赔付(原网民赔付计划) ,7, 三家比价  8,担保交易(原家装支付保) 9,优惠券 10,免费设计 11,总价0增项 
	private List<String> services;
	//装修现场列表
	private List<ConstructionSite> construction_sites;
	//是否关注。1:关注 0:未关注 
	private int is_followed;
	
	private Advertisement ad;
	
	
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
	public String getManifesto() {
		return manifesto;
	}
	public void setManifesto(String manifesto) {
		this.manifesto = manifesto;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public Logo getLogo() {
		return logo;
	}
	public void setLogo(Logo logo) {
		this.logo = logo;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getOrder_count() {
		return order_count;
	}
	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}
	public String getNative_place() {
		return native_place;
	}
	public void setNative_place(String native_place) {
		this.native_place = native_place;
	}
	public String getWork_age() {
		return work_age;
	}
	public void setWork_age(String work_age) {
		this.work_age = work_age;
	}
	public int getMember_amount() {
		return member_amount;
	}
	public void setMember_amount(int member_amount) {
		this.member_amount = member_amount;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public int getCase_count() {
		return case_count;
	}
	public void setCase_count(int case_count) {
		this.case_count = case_count;
	}
	public String getMaster_works() {
		return master_works;
	}
	public void setMaster_works(String master_works) {
		this.master_works = master_works;
	}
	public int getPrice_list_amount() {
		return price_list_amount;
	}
	public void setPrice_list_amount(int price_list_amount) {
		this.price_list_amount = price_list_amount;
	}
	public int getConstruction_site_amount() {
		return construction_site_amount;
	}
	public void setConstruction_site_amount(int construction_site_amount) {
		this.construction_site_amount = construction_site_amount;
	}
	public List<String> getRenovation_way() {
		return renovation_way;
	}
	public void setRenovation_way(List<String> renovation_way) {
		this.renovation_way = renovation_way;
	}
	public List<String> getServices() {
		return services;
	}
	public void setServices(List<String> services) {
		this.services = services;
	}
	public List<ConstructionSite> getConstruction_sites() {
		return construction_sites;
	}
	public void setConstruction_sites(List<ConstructionSite> construction_sites) {
		this.construction_sites = construction_sites;
	}
	public int getIs_followed() {
		return is_followed;
	}
	public void setIs_followed(int is_followed) {
		this.is_followed = is_followed;
	}
	public Advertisement getAd() {
		return ad;
	}
	public void setAd(Advertisement ad) {
		this.ad = ad;
	}
}
