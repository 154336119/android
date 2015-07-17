package com.huizhuang.zxsq.module;

import java.util.HashMap;

import com.huizhuang.zxsq.module.base.Group;

public class DiscussGroup extends Group<Discuss> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HashMap<String, Float> scores;
	
	private String rankScore;

	/**
	 * @return the scores
	 */
	public HashMap<String, Float> getScores() {
		return scores;
	}

	/**
	 * @param scores the scores to set
	 */
	public void setScores(HashMap<String, Float> scores) {
		this.scores = scores;
	}

	/**
	 * @return the rankScore
	 */
	public String getRankScore() {
		return rankScore;
	}

	/**
	 * @param rankScore the rankScore to set
	 */
	public void setRankScore(String rankScore) {
		this.rankScore = rankScore;
	}
	
	
}
