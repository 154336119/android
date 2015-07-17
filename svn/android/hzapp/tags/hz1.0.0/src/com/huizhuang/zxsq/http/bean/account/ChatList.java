package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatList implements Serializable{
	
	private List<ForeManChat> foreman;
	
	private List<SystemChat> system;

	public List<ForeManChat> getForeman() {
		return foreman;
	}

	public void setForeman(List<ForeManChat> foreman) {
		this.foreman = foreman;
	}

	public List<SystemChat> getSystem() {
		return system;
	}

	public void setSystem(List<SystemChat> system) {
		this.system = system;
	}

	
}
