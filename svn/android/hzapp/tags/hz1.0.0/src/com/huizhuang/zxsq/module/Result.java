package com.huizhuang.zxsq.module;

import java.io.Serializable;

import com.huizhuang.zxsq.module.base.BaseBean;

public class Result implements BaseBean, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String code;
	public String msg;
	public String data;
	@Override
	public String toString() {
		return "Result [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
	
	
}
