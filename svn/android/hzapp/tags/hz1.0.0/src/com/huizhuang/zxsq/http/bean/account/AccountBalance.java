package com.huizhuang.zxsq.http.bean.account;

import java.util.List;
/**
 * 账户余额
 * @author admin
 *
 */
public class AccountBalance {
	private int total_amount;
	private int pay_amount;
	private int totalrecord;
	private int totalpage;
	private List<AccountFlow> list;
	public int getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(int total_amount) {
		this.total_amount = total_amount;
	}
	public int getPay_amount() {
		return pay_amount;
	}
	public void setPay_amount(int pay_amount) {
		this.pay_amount = pay_amount;
	}
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
	public List<AccountFlow> getList() {
		return list;
	}
	public void setList(List<AccountFlow> list) {
		this.list = list;
	}
	
	
}
