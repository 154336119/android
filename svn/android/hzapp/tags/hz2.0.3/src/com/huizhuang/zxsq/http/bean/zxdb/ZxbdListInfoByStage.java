package com.huizhuang.zxsq.http.bean.zxdb;

import java.io.Serializable;
import java.util.ArrayList;


public class ZxbdListInfoByStage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String totalrecord;
	private String totalpage;
	private ArrayList<Zxbd> list;
	public String getTotalrecord() {
		return totalrecord;
	}
	public void setTotalrecord(String totalrecord) {
		this.totalrecord = totalrecord;
	}
	public String getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}
	public ArrayList<Zxbd> getList() {
		return list;
	}
	public void setList(ArrayList<Zxbd> list) {
		this.list = list;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "ZxbdListInfoByStage [totalrecord=" + totalrecord
				+ ", totalpage=" + totalpage + ", list=" + list + "]";
	}
	
}
