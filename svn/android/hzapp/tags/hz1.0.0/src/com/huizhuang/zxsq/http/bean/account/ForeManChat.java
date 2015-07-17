package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;


/** 
* @ClassName: ForeManChat
* @Description: 订单类
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-7 上午11:11:15 
*  
*/
public class ForeManChat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForeManChat(){
		
	}
	private int id;
	private int p1_id;
	private String last_msg;
	private int last_time;
	private int unread_count;
	private String username;
	private String avater;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getP1_id() {
		return p1_id;
	}
	public void setP1_id(int p1_id) {
		this.p1_id = p1_id;
	}
	public String getLast_msg() {
		return last_msg;
	}
	public void setLast_msg(String last_msg) {
		this.last_msg = last_msg;
	}
	public int getLast_time() {
		return last_time;
	}
	public void setLast_time(int last_time) {
		this.last_time = last_time;
	}
	public int getUnread_count() {
		return unread_count;
	}
	public void setUnread_count(int unread_count) {
		this.unread_count = unread_count;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvater() {
		return avater;
	}
	public void setAvater(String avater) {
		this.avater = avater;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
