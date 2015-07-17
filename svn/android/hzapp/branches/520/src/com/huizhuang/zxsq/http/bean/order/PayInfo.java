package com.huizhuang.zxsq.http.bean.order;

public class PayInfo {

	private String amount_receivable;
	private String id;
	private String housing_name;
	private String housing_address;
	private String contract_no;
	private String contract_price;
	private String amount;
	private String actual_amount;
	private String zj_price;
	private String receive;
	private String rate;
	private String order_prepay;
	private String record_time;
	private String yanshou_time;
	private String time;
	private String order_no;
	public String getAmount_receivable() {
		return amount_receivable;
	}
	public void setAmount_receivable(String amount_receivable) {
		this.amount_receivable = amount_receivable;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHousing_name() {
		return housing_name;
	}
	public void setHousing_name(String housing_name) {
		this.housing_name = housing_name;
	}
	public String getHousing_address() {
		return housing_address;
	}
	public void setHousing_address(String housing_address) {
		this.housing_address = housing_address;
	}
	public String getContract_no() {
		return contract_no;
	}
	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}
	public String getContract_price() {
		return contract_price;
	}
	public void setContract_price(String contract_price) {
		this.contract_price = contract_price;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getActual_amount() {
		return actual_amount;
	}
	public void setActual_amount(String actual_amount) {
		this.actual_amount = actual_amount;
	}
	public String getZj_price() {
		return zj_price;
	}
	public void setZj_price(String zj_price) {
		this.zj_price = zj_price;
	}
	public String getReceive() {
		return receive;
	}
	public void setReceive(String receive) {
		this.receive = receive;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getOrder_prepay() {
		return order_prepay;
	}
	public void setOrder_prepay(String order_prepay) {
		this.order_prepay = order_prepay;
	}
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
	public String getYanshou_time() {
		return yanshou_time;
	}
	public void setYanshou_time(String yanshou_time) {
		this.yanshou_time = yanshou_time;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
    public String getOrder_no() {
        return order_no;
    }
    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
    @Override
    public String toString() {
        return "PayInfo [amount_receivable=" + amount_receivable + ", id=" + id + ", housing_name="
                + housing_name + ", housing_address=" + housing_address + ", contract_no="
                + contract_no + ", contract_price=" + contract_price + ", amount=" + amount
                + ", actual_amount=" + actual_amount + ", zj_price=" + zj_price + ", receive="
                + receive + ", rate=" + rate + ", order_prepay=" + order_prepay + ", record_time="
                + record_time + ", yanshou_time=" + yanshou_time + ", time=" + time + ", order_no="
                + order_no + "]";
    }
	

}
