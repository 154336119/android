package com.huizhuang.zxsq.module;

import com.huizhuang.zxsq.http.bean.common.Image;

/**
 * 
 * @author jiengyh
 * 
 * @date 2014-11-25
 */
public class ContractStatus {
	/**
	 * 合同首页
	 */
	public static final int IMAGE_TYPE_INDEX = 1;
	/**
	 * 合同金额页
	 */
	public static final int IMAGE_TYPE_MONEY = 2;
	/**
	 * 合同末页
	 */
	public static final int IMAGE_TYPE_END = 3;
	/**
	 * 未上传
	 */
	public static final int STATU_NOT_UPDATE = 0;
	/**
	 * 已上传
	 */
	public static final int STATU_UPDATED = 1;
	/**
	 * 上传未通过
	 */
	public static final int STATU_NOT_PASS = 2;
	/**
	 * 上传通过
	 */
	public static final int STATU_PASSED = 3;

	private String attachId;
	private String iid;
	private int type;
	private String typeName;
	private int statu;
	private Image image;

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "ContractStatus [attachId=" + attachId + ", iid=" + iid + ", type=" + type + ", typeName=" + typeName + ", statu=" + statu + ", image=" + image
				+ "]";
	}

}
