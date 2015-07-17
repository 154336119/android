package com.huizhuang.zxsq.module;

import com.huizhuang.zxsq.module.base.Group;

public class ProblemGroup extends Group<Problem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private int score;
	private String remark;
	private String id;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "ProblemGroup [title=" + title + ", score=" + score
				+ ", remark=" + remark + ", id=" + id + "]";
	}


}
