package com.huizhuang.zxsq.http.bean.home;

import java.util.List;

public class HomePageData {

	private List<Advertise> ad_list;
	private List<EssenceDiary> diary;

	private List<GoodForemanCase> case_list;
	private List<Advertise> nav_ads;

	public List<EssenceDiary> getDiary() {
		return diary;
	}

	public void setDiary(List<EssenceDiary> diary) {
		this.diary = diary;
	}

	public List<GoodForemanCase> getCase_list() {
		return case_list;
	}

	public void setCase_list(List<GoodForemanCase> case_list) {
		this.case_list = case_list;
	}

	public List<Advertise> getAd_list() {
		return ad_list;
	}

	public void setAd_list(List<Advertise> ad_list) {
		this.ad_list = ad_list;
	}

	public List<Advertise> getNav_ads() {
		return nav_ads;
	}

	public void setNav_ads(List<Advertise> nav_ads) {
		this.nav_ads = nav_ads;
	}
	
	
}
