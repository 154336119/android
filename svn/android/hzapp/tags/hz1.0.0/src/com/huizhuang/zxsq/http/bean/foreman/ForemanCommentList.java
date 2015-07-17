package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

public class ForemanCommentList {

	private String totalrecord;
	private int totalpage;
	private List<ForemanComment> list;
	
	
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
	public List<ForemanComment> getList() {
		return list;
	}
	public void setList(List<ForemanComment> list) {
		this.list = list;
	}
}
