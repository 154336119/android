package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

/**
 * @ClassName: CommentHistory
 * @Description: 已点评列表类
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-12 上午11:41:00
 * 
 */
public class CommentHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CommentsWait store;
	private CommentsWait staff;

	public CommentsWait getStore() {
		return store;
	}

	public void setStore(CommentsWait store) {
		this.store = store;
	}

	public CommentsWait getStaff() {
		return staff;
	}

	public void setStaff(CommentsWait staff) {
		this.staff = staff;
	}

	@Override
	public String toString() {
		return "CommentHistory [store=" + store + ", staff=" + staff + "]";
	}

}
