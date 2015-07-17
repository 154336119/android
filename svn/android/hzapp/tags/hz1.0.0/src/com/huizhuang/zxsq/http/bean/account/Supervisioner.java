package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;
import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;


/** 
* @ClassName: Supervisioner 
* @Description: 理师类 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-9 下午5:20:39 
*  
*/
public class Supervisioner implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private int service_num;
	private Float score;
	private Image photo;
	private int work_year;
	private int comments_num;
	private List<Comments> comments_list;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getService_num() {
		return service_num;
	}

	public void setService_num(int service_num) {
		this.service_num = service_num;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Image getPhoto() {
		return photo;
	}

	public void setPhoto(Image photo) {
		this.photo = photo;
	}

	
	public int getWork_year() {
		return work_year;
	}

	public void setWork_year(int work_year) {
		this.work_year = work_year;
	}

	
	public int getComments_num() {
		return comments_num;
	}

	public void setComments_num(int comments_num) {
		this.comments_num = comments_num;
	}

	public List<Comments> getComments_list() {
		return comments_list;
	}

	public void setComments_list(List<Comments> comments_list) {
		this.comments_list = comments_list;
	}

	@Override
	public String toString() {
		return "Supervisioner [id=" + id + ", name=" + name + ", service_num="
				+ service_num + ", score=" + score + ", photo=" + photo
				+ ", work_year=" + work_year + ", comments_num=" + comments_num
				+ ", comments_list=" + comments_list + "]";
	}

}
