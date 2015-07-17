package com.huizhuang.zxsq.module;

import java.io.Serializable;
import java.util.ArrayList;

public class Problem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private ArrayList<ProblemSub> problemSubs;
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
	public ArrayList<ProblemSub> getProblemSubs() {
		return problemSubs;
	}
	public void setProblemSubs(ArrayList<ProblemSub> problemSubs) {
		this.problemSubs = problemSubs;
	}
	
	
}
