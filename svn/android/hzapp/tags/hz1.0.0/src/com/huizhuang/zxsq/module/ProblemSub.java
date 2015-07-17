package com.huizhuang.zxsq.module;

import java.io.Serializable;
import java.util.ArrayList;

public class ProblemSub implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private int status;
	private ArrayList<ProblemPic> leftPicList;
	private ArrayList<ProblemPic> rightPicList;

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

	public ArrayList<ProblemPic> getLeftPicList() {
		return leftPicList;
	}

	public void setLeftPicList(ArrayList<ProblemPic> leftPicList) {
		this.leftPicList = leftPicList;
	}

	public ArrayList<ProblemPic> getRightPicList() {
		return rightPicList;
	}

	public void setRightPicList(ArrayList<ProblemPic> rightPicList) {
		this.rightPicList = rightPicList;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ProblemSub [id=" + id + ", name=" + name + ", status=" + status + ", leftPicList=" + leftPicList + ", rightPicList=" + rightPicList + "]";
	}

}
