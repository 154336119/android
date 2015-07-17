package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

import com.huizhuang.zxsq.http.bean.common.Image;

/**
 * @ClassName: Gallery
 * @Description: 标准牌子图集类
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-7 上午11:11:03
 * 
 */
public class Gallery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Image pic;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getPic() {
		return pic;
	}

	public void setPic(Image pic) {
		this.pic = pic;
	}

	@Override
	public String toString() {
		return "Gallery [name=" + name + ", pic=" + pic + "]";
	}

}
