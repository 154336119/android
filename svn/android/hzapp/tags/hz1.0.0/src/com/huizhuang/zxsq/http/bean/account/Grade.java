package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

/** 
 * 
 * @ClassName: Grade
 * @Description: 打分
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-11 上午10:45:47
 * 
 */
public class Grade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Foreman store;
	private CommentsWait staff;

	public Foreman getStore() {
		return store;
	}

	public void setStore(Foreman store) {
		this.store = store;
	}

	public CommentsWait getStaff() {
		return staff;
	}

	public void setStaff(CommentsWait staff) {
		this.staff = staff;
	}

	@Override
	public String toString() {
		return "Grade [store=" + store + ", staff=" + staff + "]";
	}

}
