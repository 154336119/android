package com.huizhuang.zxsq.module;

import java.io.Serializable;

public class TypeSummary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 552240421457859385L;
	private double total;
	private int tType;
	private int cType;
	private String cTypeName;
	private String tTypeName;
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int gettType() {
		return tType;
	}
	public void settType(int tType) {
		this.tType = tType;
	}
	public int getcType() {
		return cType;
	}
	public void setcType(int cType) {
		this.cType = cType;
	}
	public String getcTypeName() {
		return cTypeName;
	}
	public void setcTypeName(String cTypeName) {
		this.cTypeName = cTypeName;
	}
	public String gettTypeName() {
		return tTypeName;
	}
	public void settTypeName(String tTypeName) {
		this.tTypeName = tTypeName;
	}
	@Override
	public String toString() {
		return "TypeSummary [total=" + total + ", tType=" + tType + ", cType=" + cType + ", cTypeName=" + cTypeName + ", tTypeName=" + tTypeName + "]";
	}

}