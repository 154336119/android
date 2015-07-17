package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;
import java.util.List;

/** 
* @ClassName: SupervisionerGroup 
* @Description: 监理师列表
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-9 下午5:20:48 
*  
*/
public class SupervisionerGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalrecord;// 结果的总条数
	private int totalpage;// 结果的总页数
	private List<Supervisioner> list;

	public int getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(int totalrecord) {
		this.totalrecord = totalrecord;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public List<Supervisioner> getList() {
		return list;
	}

	public void setList(List<Supervisioner> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "SupervisionerGroup [totalrecord=" + totalrecord
				+ ", totalpage=" + totalpage + ", list=" + list + "]";
	}

}
