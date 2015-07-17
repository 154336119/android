package com.huizhuang.zxsq.http.bean.order;

import com.huizhuang.zxsq.http.bean.Logo;

public class OrderDetailChild {

	private String name;
	private int statu;
	private int store_id;
	private int allot_id;
	private int ske_contract_time;
	private int measure; //是否量房    0.不可以量房 1.可以量房
	private int contract;//是否预约  
	
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
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	public int getAllot_id() {
		return allot_id;
	}
	public void setAllot_id(int allot_id) {
		this.allot_id = allot_id;
	}
	
}
