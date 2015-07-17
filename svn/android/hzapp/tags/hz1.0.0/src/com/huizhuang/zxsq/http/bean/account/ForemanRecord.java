package com.huizhuang.zxsq.http.bean.account;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

/**
 * @ClassName: ForemanRecord
 * @Description: 工长记录
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-10 下午4:48:22
 * 
 */
public class ForemanRecord {

	private String zx_node;
	private String id;
	private String desc;
	private String add_time;
	private List<Image> image;

	public String getZx_node() {
		return zx_node;
	}

	public void setZx_node(String zx_node) {
		this.zx_node = zx_node;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}


	public List<Image> getImage() {
		return image;
	}

	public void setImage(List<Image> image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "ForemanRecord [zx_node=" + zx_node + ", id=" + id + ", desc="
				+ desc + ", add_time=" + add_time + ", image=" + image + "]";
	}


}
