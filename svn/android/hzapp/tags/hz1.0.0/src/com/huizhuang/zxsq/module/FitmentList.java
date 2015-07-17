package com.huizhuang.zxsq.module;

import java.util.List;

public class FitmentList {
	
	private int id;
	private String name;
	private double total;
	private List<FitmentList> sub;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public List<FitmentList> getSub() {
		return sub;
	}
	public void setSub(List<FitmentList> sub) {
		this.sub = sub;
	}
	@Override
	public String toString() {
		return "FitmentList [id=" + id + ", name=" + name + ", total=" + total
				+ ", sub=" + sub + "]";
	}
	
	
	
}
