package com.huizhuang.zxsq.http.bean.order;

import java.util.List;

import com.huizhuang.zxsq.http.bean.account.Order;

public class OrderDetail {

	private Order parent;
	private List<OrderDetailChild> child;
	private int is_concel;
	private int is_grab;
	
	public Order getParent() {
		return parent;
	}
	public void setParent(Order parent) {
		this.parent = parent;
	}
	public List<OrderDetailChild> getChild() {
		return child;
	}
	public void setChild(List<OrderDetailChild> child) {
		this.child = child;
	}
	public int getIs_concel() {
		return is_concel;
	}
	public void setIs_concel(int is_concel) {
		this.is_concel = is_concel;
	}
	public int getIs_grab() {
		return is_grab;
	}
	public void setIs_grab(int is_grab) {
		this.is_grab = is_grab;
	}

	
}
