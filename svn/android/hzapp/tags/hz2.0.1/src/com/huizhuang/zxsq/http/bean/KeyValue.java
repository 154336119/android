package com.huizhuang.zxsq.http.bean;

import java.io.Serializable;

public class KeyValue implements Serializable {

	private static final long serialVersionUID = 310160851878955808L;

	private String id;
	private String name;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "KeyValue [id=" + id + ", name=" + name + ", remark=" + remark
				+ "]";
	}

}
