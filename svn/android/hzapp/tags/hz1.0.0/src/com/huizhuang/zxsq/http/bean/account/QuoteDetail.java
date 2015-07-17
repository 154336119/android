package com.huizhuang.zxsq.http.bean.account;

import java.util.List;

public class QuoteDetail {

    private String id;
    private String number;
    private String total_price;
    private String average_price;
    private List<QuoteCate> cate_list;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public String getAverage_price() {
		return average_price;
	}
	public void setAverage_price(String average_price) {
		this.average_price = average_price;
	}
	public List<QuoteCate> getCate_list() {
		return cate_list;
	}
	public void setCate_list(List<QuoteCate> cate_list) {
		this.cate_list = cate_list;
	}


}
