package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

public class ConstructionCase {


	private String id;
	//案例数目
	private String count;
	//阶段
	private String zx_code;
    //描述
	private String desc;
	//创建时间
	private String add_time;
	
	private List<Image> images;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getZx_code() {
		return zx_code;
	}

	public void setZx_code(String zx_code) {
		this.zx_code = zx_code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
}
