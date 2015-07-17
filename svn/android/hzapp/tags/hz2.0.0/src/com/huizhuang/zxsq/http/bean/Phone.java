package com.huizhuang.zxsq.http.bean;

public class Phone {
	String verify;//验证码
	String send_phone;//发送验证码的号码

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getSend_phone() {
		return send_phone;
	}

	public void setSend_phone(String send_phone) {
		this.send_phone = send_phone;
	}
	
}
