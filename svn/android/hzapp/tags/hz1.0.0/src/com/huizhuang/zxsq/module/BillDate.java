package com.huizhuang.zxsq.module;

import java.io.Serializable;

/**
 * @ClassName: BillDateGroup
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lim
 * @mail limshare@gmail.com
 * @date 2014-11-4 下午4:08:20
 * 
 */
public class BillDate implements Serializable{
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	public static final int ITEM = 0;
	public static final int SECTION = 1;

	private int id;
	private String userId;
	private String ttypeId;
	private String ctypeId;
	private String ttypeName;
	private String ctypeName;
	private String amount;
	private String detail;
	private String datetime;
	private int status;

	private int pinnedType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTtypeId() {
		return ttypeId;
	}

	public void setTtypeId(String ttypeId) {
		this.ttypeId = ttypeId;
	}

	public String getCtypeId() {
		return ctypeId;
	}

	public void setCtypeId(String ctypeId) {
		this.ctypeId = ctypeId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTtypeName() {
		return ttypeName;
	}

	public void setTtypeName(String ttypeName) {
		this.ttypeName = ttypeName;
	}

	public String getCtypeName() {
		return ctypeName;
	}

	public void setCtypeName(String ctypeName) {
		this.ctypeName = ctypeName;
	}

	public int getPinnedType() {
		return pinnedType;
	}

	public void setPinnedType(int pinnedType) {
		this.pinnedType = pinnedType;
	}

}
