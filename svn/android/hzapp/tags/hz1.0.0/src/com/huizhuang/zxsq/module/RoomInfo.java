package com.huizhuang.zxsq.module;

import java.util.List;

import com.huizhuang.zxsq.module.base.BaseBean;

/**
 * @ClassName: RoomInfo
 * @Description: 爱家档案信息
 * @author jiangyh
 * @mail jiengyh@qq.com
 * @date 2014-10-28 上午10:51:29
 */
public class RoomInfo implements BaseBean{
	
	private int  id;
	private int userId;
	private String roomType;
	private String roomStyle;
	private double roomArea;
	private String zxStore;
	private String zxDesigne;
	private String zxBudget;
	private String jfDate;//交房时间
	private String zxDate;//装修时间
	private long addTime;
	private List<RoomImage> hxImage;
	private List<RoomImage> sjImage;
	private List<RoomImage> xgImage;
	private int costRange;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getRoomStyle() {
		return roomStyle;
	}

	public void setRoomStyle(String roomStyle) {
		this.roomStyle = roomStyle;
	}

	public double getRoomArea() {
		return roomArea;
	}

	public void setRoomArea(double roomArea) {
		if(!Double.isNaN(roomArea)){
			this.roomArea = roomArea;
		}
	}

	public String getZxStore() {
		return zxStore;
	}

	public void setZxStore(String zxStore) {
		this.zxStore = zxStore;
	}

	public String getZxDesigne() {
		return zxDesigne;
	}

	public void setZxDesigne(String zxDesigne) {
		this.zxDesigne = zxDesigne;
	}

	public  class RoomImage{
		
		public RoomImage(){};
		private int id;
		private String imgPath;
		private String thumbPath;
		private String imgExt;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getImgPath() {
			return imgPath;
		}
		public void setImgPath(String imgPath) {
			this.imgPath = imgPath;
		}
		public String getThumbPath() {
			return thumbPath;
		}
		public void setThumbPath(String thumbPath) {
			this.thumbPath = thumbPath;
		}
		public String getImgExt() {
			return imgExt;
		}
		public void setImgExt(String imgExt) {
			this.imgExt = imgExt;
		}
		@Override
		public String toString() {
			return "RoomImage [id=" + id + ", imgPath=" + imgPath
					+ ", thumbPath=" + thumbPath + ", imgExt=" + imgExt + "]";
		}
		
		
		
		
	}

	

	@Override
	public String toString() {
		return "RoomInfo [id=" + id + ", userId=" + userId + ", roomType="
				+ roomType + ", roomStyle=" + roomStyle + ", roomArea="
				+ roomArea + ", zxStore=" + zxStore + ", zxDesigne="
				+ zxDesigne + ", zxBudget=" + zxBudget + ", jfDate=" + jfDate
				+ ", zxDate=" + zxDate + ", addTime=" + addTime + ", hxImage="
				+ hxImage + ", sjImage=" + sjImage + ", xgImage=" + xgImage
				+ "]";
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getZxBudget() {
		return zxBudget;
	}

	public void setZxBudget(String zxBudget) {
		this.zxBudget = zxBudget;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public List<RoomImage> getHxImage() {
		return hxImage;
	}

	public void setHxImage(List<RoomImage> hxImage) {
		this.hxImage = hxImage;
	}

	public List<RoomImage> getSjImage() {
		return sjImage;
	}

	public void setSjImage(List<RoomImage> sjImage) {
		this.sjImage = sjImage;
	}

	public List<RoomImage> getXgImage() {
		return xgImage;
	}

	public void setXgImage(List<RoomImage> xgImage) {
		this.xgImage = xgImage;
	}

	public String getJfDate() {
		return jfDate;
	}

	public void setJfDate(String jfDate) {
		this.jfDate = jfDate;
	}

	public String getZxDate() {
		return zxDate;
	}

	public void setZxDate(String zxDate) {
		this.zxDate = zxDate;
	}

	public int getCostRange() {
		return costRange;
	}

	public void setCostRange(int costRange) {
		this.costRange = costRange;
	}
	
	
	
	

}
