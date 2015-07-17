package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;


/** 
* @ClassName: Order 
* @Description: 订单类
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-7 上午11:11:15 
*  
*/
public class Order implements Serializable {

	/**
	 * 所有订单
	 */
	public static final int ORDER_TYPE_ALL = 0;
	/**
	 * 装修公司订单
	 */
	public static final int ORDER_TYPE_COMPANY = 19;
	/**
	 * 工长订单
	 */
	public static final int ORDER_TYPE_FOREMAN = 20;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int order_id;
	private String order_no;
	private String add_time;
	private int statu;
	private String housing_address;
	private String full_name;
	private String avater;
	private int order_type;
	public int getOrder_type() {
		return order_type;
	}

	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}

	public int getContract_statu() {
		return contract_statu;
	}

	public void setContract_statu(int contract_statu) {
		this.contract_statu = contract_statu;
	}

	private int contract_statu;
	
	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public String getHousing_address() {
		return housing_address;
	}

	public void setHousing_address(String housing_address) {
		this.housing_address = housing_address;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getAvater() {
		return avater;
	}

	public void setAvater(String avater) {
		this.avater = avater;
	}


}
