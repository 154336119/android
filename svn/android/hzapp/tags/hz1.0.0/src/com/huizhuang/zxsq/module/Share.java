package com.huizhuang.zxsq.module;

import java.io.Serializable;

import cn.sharesdk.framework.PlatformActionListener;


/** 
* @ClassName: Share 
* @Description: 分享类
* @author th 
* @mail 342592622@qq.com   
* @date 2014-11-20 下午2:20:05 
*  
*/
public class Share implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int imgResouceId;//分享平台图标本地资源ID
	private String name;//分享平台图标名称
	private String platformName;
	private String text;
	private String imageUrl;//分享的图片网络地址
	private String imagePath;//分享的图片本地地址
	private String title;//不能超过30个字符
	private String titleUrl;
	private String site;
	private String siteUrl;
	private String comment;
	private String url;
	private boolean isCallBack;//是否需要回调分享结果  有界面的分享才有起作用
	private String venueName;
	private String venueDescription;
	private String address;
	private PlatformActionListener platformActionListener;//如果快捷分享需要回调就传入该参数  有界面的分享无用
	
	public Share(){
		text = "惠装装修神器";
		title = "#装修神器#";
		titleUrl = "http://app.huizhuang.com/?file=index&op=download";
		site = "惠装装修神器";
		siteUrl = "http://app.huizhuang.com/?file=index&op=download";
		comment = "惠装装修神器";
		url = "http://app.huizhuang.com/?file=index&op=download";
		isCallBack = false;
	}

	public int getImgResouceId() {
		return imgResouceId;
	}
	public void setImgResouceId(int imgResouceId) {
		this.imgResouceId = imgResouceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleUrl() {
		return titleUrl;
	}
	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getSiteUrl() {
		return siteUrl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isCallBack() {
		return isCallBack;
	}

	public void setCallBack(boolean isCallBack) {
		this.isCallBack = isCallBack;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getVenueDescription() {
		return venueDescription;
	}

	public void setVenueDescription(String venueDescription) {
		this.venueDescription = venueDescription;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public PlatformActionListener getPlatformActionListener() {
		return platformActionListener;
	}

	public void setPlatformActionListener(
			PlatformActionListener platformActionListener) {
		this.platformActionListener = platformActionListener;
	}
	
	
}
