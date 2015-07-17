package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

public class ForemanList {

	private String totalrecord;
	private int totalpage;
	private List<Foreman> list;
	
	
	public String getTotalrecord() {
		return totalrecord;
	}
	public void setTotalrecord(String totalrecord) {
		this.totalrecord = totalrecord;
	}
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	public List<Foreman> getList() {
		return list;
	}
	public void setList(List<Foreman> list) {
		this.list = list;
	}
}
