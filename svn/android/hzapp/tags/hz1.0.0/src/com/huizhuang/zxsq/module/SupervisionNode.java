package com.huizhuang.zxsq.module;

import java.io.Serializable;

import com.huizhuang.zxsq.module.base.BaseBean;

/**
 * @ClassName: SupervisionNode
 * @Package com.huizhuang.supervision.module
 * @Description: 监理节点实体类
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-7-29 下午4:16:38
 */
public class SupervisionNode implements BaseBean, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1077591461162109477L;

	private int id;// 
	private String name;// 节点名称
	private int nameId;
	private int status;// 节点监理状态
	private int score;
	private String datetime;
	private String remark;
	private String sId;//节点ID
	
	private String node_id;
	private int statu;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getNameId() {
		return nameId;
	}

	public void setNameId(int nameId) {
		this.nameId = nameId;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	@Override
	public String toString() {
		return "SupervisionNode [id=" + id + ", name=" + name + ", nameId="
				+ nameId + ", status=" + status + ", score=" + score
				+ ", datetime=" + datetime + ", remark=" + remark + ", sId="
				+ sId + ", node_id=" + node_id + ", statu=" + statu + "]";
	}

	
}
