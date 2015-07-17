package com.huizhuang.zxsq.module;

import java.io.Serializable;

public class Discuss implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 
	private String username;// 
	private int userId;// 
	private String userThumb;// 
	private String content;// 
	private String datetime;// 
	private int upNum;//
	private int downNum;//
	private float score;// 

	public int getUserId() {
		return userId;
	}
 
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUpNum() {
		return upNum;
	}
 
	public void setUpNum(int upNum) {
		this.upNum = upNum;
	}
	
	public String getUserThumb() {
		return userThumb;
	}
 
	public void setUserThumb(String userThumb) {
		this.userThumb = userThumb;
	}

	public int getDownNum() {
		return downNum;
	}
 
	public void SetDownNum(int downNum) {
		this.downNum = downNum;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the datetime
	 */
	public String getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime
	 *            the datetime to set
	 */
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	/**
	 * @return the score
	 */
	public float getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}

}
