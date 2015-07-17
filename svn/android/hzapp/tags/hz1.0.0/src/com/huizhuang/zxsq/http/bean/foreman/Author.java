package com.huizhuang.zxsq.http.bean.foreman;

import com.huizhuang.zxsq.http.bean.Logo;

public class Author {

	private String user_id;
	private String screen_name;
	private Logo user_thumb;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public Logo getUser_thumb() {
		return user_thumb;
	}
	public void setUser_thumb(Logo user_thumb) {
		this.user_thumb = user_thumb;
	}
}
