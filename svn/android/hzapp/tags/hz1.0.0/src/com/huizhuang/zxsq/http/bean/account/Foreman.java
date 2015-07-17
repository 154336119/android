package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;

import com.huizhuang.zxsq.http.bean.common.Image;


/** 
* @ClassName: Foreman 
* @Description:工长信息类
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-11 上午10:45:34 
*  
*/
public class Foreman implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private Image image;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Foreman [id=" + id + ", name=" + name + ", image=" + image
				+ "]";
	}

}
