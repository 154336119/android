package com.huizhuang.zxsq.http.bean.order;

import java.io.Serializable;
/**
 * 延期变更单
 * @author jean
 *
 */
public class DeylayChangeOrder implements Serializable {

	private static final long serialVersionUID = 310160851878955808L;

	private String days;
	private String completed_date;
	private String cause;
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getCompleted_date() {
		return completed_date;
	}
	public void setCompleted_date(String completed_date) {
		this.completed_date = completed_date;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	

}
