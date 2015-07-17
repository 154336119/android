package com.huizhuang.zxsq.http.bean;

import java.io.Serializable;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.utils.PrefersUtil;

import cn.sharesdk.framework.PlatformActionListener;

/**
 * @ClassName: Share
 * @Description: 分享类
 * @author th
 * @mail 342592622@qq.com
 * @date 2014-11-20 下午2:20:05
 * 
 */
public class Share implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int imgResouceId;// 分享平台图标本地资源ID
	private String name;// 分享平台图标名称
	private String platformName;
	private String text;
	private String imageUrl;// 分享的图片网络地址
	private String imagePath;// 分享的图片本地地址
	private String title;// 不能超过30个字符
	private String titleUrl;
	private String site;
	private String siteUrl;
	private String comment;
	private String url;
	private boolean isCallBack;// 是否需要回调分享结果 有界面的分享才有起作用
	private String venueName;
	private String venueDescription;
	private String address;
	private PlatformActionListener platformActionListener;// 如果快捷分享需要回调就传入该参数,有界面的分享无用

	public Share() {
		//text是分享文本，所有平台都需要这个字段
		//title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		title = "【惠装】工薪阶层装修首选，比装修公司省40%";//默认按照微信平台来设置text和title
		if(PreferenceConfig.getShareByForeman()){
			text = "我在#惠装APP#上找到了一个非常靠谱的工长"+PreferenceConfig.getShareByForemanName()+
					"，人品好服务棒！装修的人都在用这个软件，比传统装修节省40%的费用！强烈推荐你试试~";
			//PreferenceConfig.setShareByForeman(false);
		}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_COMMON){
			text = "下载惠装APP，找惠装工长做装修省钱40%，在线看工地和验收报告，不能更洋气~";
		}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_START_WORKING){
			text = "耶~我家新房开工装修啦！想看我家啥样？猛戳下载惠装APP，立马可看装修进度照";
		}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_WATER){
			text = "hoho~惠装工长刚给我发了水电验收报告，想看装修进度照？猛戳下载惠装APP吧";
		}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_WOOD){
			text = "hoho~惠装工长刚给我发了泥木验收报告，想看装修进度照？猛戳下载惠装APP吧";
		}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_PAINT){
			text = "hoho~惠装工长刚给我发了油漆验收报告，想看装修进度照？猛戳下载惠装APP吧";
		}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_COMPLETE){
			text = "yoho~我家新房装修终于竣工啦！想看我家装修毕业照？猛戳下载惠装APP吧！";
		}
		
		//titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		titleUrl = AppConfig.SHARE_URL;
		//site是分享此内容的网站名称，仅在QQ空间使用
		site = "惠装装修神器";
		//siteUrl是分享此内容的网站地址，仅在QQ空间使用
		siteUrl = AppConfig.SHARE_URL;
		comment = "惠装装修神器";
		//url仅在微信（包括好友和朋友圈）中使用
		url = AppConfig.SHARE_URL;
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
