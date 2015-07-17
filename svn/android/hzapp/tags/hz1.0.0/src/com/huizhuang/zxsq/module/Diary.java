package com.huizhuang.zxsq.module;

import java.io.Serializable;
import java.util.ArrayList;

public class Diary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;// 标题
	private String summary;// 描述
	private String content;// 内容
	private String imageUrl;
	private String datetime;// 日期

	private String weather;// 天气
	private String zxNode;// 装修节点
	private int zxNodeNum;//当前节点包含的日记数目
	private int secrecy;// 保密
	private int favored;// 是否已收藏

	private String favoritNum;// 收藏数
	private String discussNum;// 评论数
	private String shareNum;// 分享数
	private String likeNum;// 喜欢数
	private String readNum;// 阅读数

	private String province;// 省
	private String city;// 市

	private Author author;// 作者
	private ArrayList<Visitor> visitors;// 访问者
	private ArrayList<Atlas> atlass;// 图片
	private ArrayList<BillDate> bills;// 关联账单

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getZxNode() {
		return zxNode;
	}

	public void setZxNode(String zxNode) {
		this.zxNode = zxNode;
	}

	public int getZxNodeNum() {
		return zxNodeNum;
	}

	public void setZxNodeNum(int zxNodeNum) {
		this.zxNodeNum = zxNodeNum;
	}

	public int getSecrecy() {
		return secrecy;
	}

	public void setSecrecy(int secrecy) {
		this.secrecy = secrecy;
	}

	public int getFavored() {
		return favored;
	}

	public void setFavored(int favored) {
		this.favored = favored;
	}

	public String getFavoritNum() {
		return favoritNum;
	}

	public void setFavoritNum(String favoritNum) {
		this.favoritNum = favoritNum;
	}

	public String getDiscussNum() {
		return discussNum;
	}

	public void setDiscussNum(String discussNum) {
		this.discussNum = discussNum;
	}

	public String getShareNum() {
		return shareNum;
	}

	public void setShareNum(String shareNum) {
		this.shareNum = shareNum;
	}

	public String getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public ArrayList<Visitor> getVisitors() {
		return visitors;
	}

	public void setVisitors(ArrayList<Visitor> visitors) {
		this.visitors = visitors;
	}

	public ArrayList<Atlas> getAtlass() {
		return atlass;
	}

	public void setAtlass(ArrayList<Atlas> atlass) {
		this.atlass = atlass;
	}

	public ArrayList<BillDate> getBills() {
		return bills;
	}

	public void setBills(ArrayList<BillDate> bills) {
		this.bills = bills;
	}

	@Override
	public String toString() {
		return "Diary [id=" + id + ", title=" + title + ", summary=" + summary
				+ ", content=" + content + ", imageUrl=" + imageUrl
				+ ", datetime=" + datetime + ", favoritNum=" + favoritNum
				+ ", discussNum=" + discussNum + ", shareNum=" + shareNum
				+ ", likeNum=" + likeNum + ", readNum=" + readNum + ", author="
				+ author + ", visitors=" + visitors + ", atlass=" + atlass
				+ ", weather=" + weather + ", province=" + province + ", city="
				+ city + ", zxNode=" + zxNode + ", secrecy=" + secrecy
				+ ", favored=" + favored + "]";
	}
	
}