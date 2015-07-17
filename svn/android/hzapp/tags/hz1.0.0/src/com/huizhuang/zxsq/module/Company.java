package com.huizhuang.zxsq.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.huizhuang.zxsq.module.base.BaseBean;

public class Company implements BaseBean, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3876829634732451188L;
	private int id;// id
	private String fullName;// 公司全称
	private String shortName;// 公司简称
	private String px;// 商家X坐标
	private String py;// 商家Y坐标
	private String logoImage;
	private String address;// 公司地址
	private String phone;// 公司电话
	private String thumbSketch;// 封面
	private float score;// 星星评分
	private String rank;//综合评分
	private String contractPrice; // 合同均价
	private ArrayList<Service> services = new ArrayList<Service>(); // 服务列表
	private boolean isFavored;// 是否已收藏
	private Atlas coverAtlas;// 效果图
	private String orderNum;// 预约人数

	private String description;// 描述
	private boolean isSupportZJB;// 是否支持家装宝
	private String lastestDiscuss;// 最后一次讨论
	private String discussNum;
	private String tag;// 活动标签
	private Atlas recommendAtlas;
	private ArrayList<Atlas> atlasList;
	private String supervisionDiaryCount; // 监理日记数目
	private String renovatioDiaryCount; // 装修日记数目
	private String intro;// 公司简介
	private ArrayList<RankDetail> rankDetailList;
	
	private String diaryCount;// 日记
	private String commentCount;//口碑	
	private Discuss latestComment;
	private Discuss hotComment; //热门评论
	
	private Atlas atlas;
	public String getdiaryCount() {
		return diaryCount;
	}

	public void setdiaryCount(String diaryCount) {
		this.diaryCount = diaryCount;
	}
	
	public Discuss getHotComment() {
		return hotComment;
	}

	public void setHotComment(Discuss hotComment) {
		this.hotComment = hotComment;
	}
	
	public Discuss getLatestComment() {
		return latestComment;
	}

	public void setLatestComment(Discuss latestComment) {
		this.latestComment = latestComment;
	}

	public String getThumbSketch() {
		return thumbSketch;
	}

	public void setrRank(String rank) {
		this.rank = rank;
	}

	public String getRank() {
		return rank;
	}

	public void setThumbSketch(String thumbSketch) {
		this.thumbSketch = thumbSketch;
	}
	
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getSupervisionDiaryCount() {
		return supervisionDiaryCount;
	}

	public void setSupervisionDiaryCount(String supervisionDiaryCount) {
		this.supervisionDiaryCount = supervisionDiaryCount;
	}

	public String getRenovatioDiaryCount() {
		return renovatioDiaryCount;
	}

	public void setRenovatioDiaryCount(String renovatioDiaryCount) {
		this.renovatioDiaryCount = renovatioDiaryCount;
	}

	public ArrayList<Atlas> getAtlasList() {
		return atlasList;
	}

	public void setAtlasList(ArrayList<Atlas> atlasList) {
		this.atlasList = atlasList;
	}

	public ArrayList<RankDetail> getRankDetailList() {
		return rankDetailList;
	}

	public void setRankDetailList(ArrayList<RankDetail> rankDetailList) {
		this.rankDetailList = rankDetailList;
	}

	private HashMap<String, String> scores;

	public HashMap<String, String> getScores() {
		return scores;
	}

	public void setScores(HashMap<String, String> scores) {
		this.scores = scores;
	}

	public String getLastestDiscuss() {
		return lastestDiscuss;
	}

	public void setLastestDiscuss(String lastestDiscuss) {
		this.lastestDiscuss = lastestDiscuss;
	}

	public boolean isSupportZJB() {
		return isSupportZJB;
	}

	public void setSupportZJB(boolean isSupportZJB) {
		this.isSupportZJB = isSupportZJB;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDiscussNum() {
		return discussNum;
	}

	public void setDiscussNum(String discussNum) {
		this.discussNum = discussNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getContractPrice() {
		return contractPrice;
	}

	public void setContractPrice(String contractPrice) {
		this.contractPrice = contractPrice;
	}

	public ArrayList<Service> getServices() {
		return services;
	}

	public void setServices(ArrayList<Service> services) {
		this.services = services;
	}
	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public Atlas getCoverAtlas() {
		return coverAtlas;
	}

	public void setCoverAtlas(Atlas atlas) {
		this.coverAtlas = atlas;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderCount) {
		this.orderNum = orderCount;
	}

	public boolean getIsFavored() {
		return isFavored;
	}

	public void setIsFavored(boolean isFavored) {
		this.isFavored = isFavored;
	}
	public Atlas getRecommendAtlas() {
		return recommendAtlas;
	}

	public void setRecommendAtlas(Atlas recommendAtlas) {
		this.recommendAtlas = recommendAtlas;
	}

	public boolean hasZjbTag() {
		for (int i = 0; i < services.size(); i++) {
			if (services.get(i).getId() == 8)
				return true;
		}
		return false;
	}

	public boolean hasCouponTag() {
		for (int i = 0; i < services.size(); i++) {
			if (services.get(i).getId() == 9)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", fullName=" + fullName + ", shortName=" + shortName + ", px=" + px + ", py=" + py + ", logoImage=" + logoImage
				+ ", address=" + address + ", phone=" + phone + ", thumbSketch=" + thumbSketch + ", score=" + score + ", contractPrice=" + contractPrice
				+ ", services=" + services + ", isFavored=" + isFavored + ", coverAtlas=" + coverAtlas + ", orderNum=" + orderNum + ", description="
				+ description + ", isSupportZJB=" + isSupportZJB + ", lastestDiscuss=" + lastestDiscuss + ", discussNum=" + discussNum + ", tag=" + tag
				+ ", recommendAtlas=" + recommendAtlas + ", atlasList=" + atlasList + ", supervisionDiaryCount=" + supervisionDiaryCount
				+ ", renovatioDiaryCount=" + renovatioDiaryCount + ", intro=" + intro + ", rankDetailList=" + rankDetailList + ", latestComment="
				+ latestComment + ", scores=" + scores + "]";
	}

	public Atlas getAtlas() {
		return atlas;
	}

	public void setAtlas(Atlas atlas) {
		this.atlas = atlas;
	}





}
