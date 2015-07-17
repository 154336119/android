package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

import com.huizhuang.zxsq.http.bean.common.Image;

public class Comments implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String comment_id;
	private String date;
	private Float a_source;
	private String operator_name;
	private Image operator_photo;
	private String content;

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Float getA_source() {
		return a_source;
	}

	public void setA_source(Float a_source) {
		this.a_source = a_source;
	}

	public String getOperator_name() {
		return operator_name;
	}

	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}

	public Image getOperator_photo() {
		return operator_photo;
	}

	public void setOperator_photo(Image operator_photo) {
		this.operator_photo = operator_photo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Comments [comment_id=" + comment_id + ", date=" + date
				+ ", a_source=" + a_source + ", operator_name=" + operator_name
				+ ", operator_photo=" + operator_photo + ", content=" + content
				+ "]";
	}

}
