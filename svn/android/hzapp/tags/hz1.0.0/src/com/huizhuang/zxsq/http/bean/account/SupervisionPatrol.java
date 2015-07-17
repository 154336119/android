package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

/**
 * @ClassName: SupervisionPatrol
 * @Description: 监理师巡查
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-10 下午2:27:19
 * 
 */
public class SupervisionPatrol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String time;
	private String remark;
	private int has_read;
	private String name;
	private String avater;
	private String node;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getHas_read() {
		return has_read;
	}

	public void setHas_read(int has_read) {
		this.has_read = has_read;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvater() {
		return avater;
	}

	public void setAvater(String avater) {
		this.avater = avater;
	}

	
	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "SupervisionPatrol [id=" + id + ", time=" + time + ", remark="
				+ remark + ", has_read=" + has_read + ", name=" + name
				+ ", avater=" + avater + ", node=" + node + "]";
	}



}
