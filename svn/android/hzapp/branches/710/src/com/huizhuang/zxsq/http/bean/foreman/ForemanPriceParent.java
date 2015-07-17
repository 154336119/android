package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

public class ForemanPriceParent {

	private String category_name;

	private List<ForemanPriceChildren> children;

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public List<ForemanPriceChildren> getChildren() {
		return children;
	}

	public void setChildren(List<ForemanPriceChildren> children) {
		this.children = children;
	}
}
