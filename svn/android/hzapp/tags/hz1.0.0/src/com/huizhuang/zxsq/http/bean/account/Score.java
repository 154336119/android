package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

/**
 * @ClassName: Score
 * @Description: 已评分分数类
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-12 上午11:32:19
 * 
 */
public class Score implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int score;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Score [id=" + id + ", score=" + score + "]";
	}

}
