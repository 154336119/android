package com.huizhuang.zxsq.http.bean;

import com.huizhuang.zxsq.utils.LogUtil;

import java.io.Serializable;
import java.util.List;

public class Constant implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private List<JourneyType> journeyTypeList;
	private List<KeyValue> roomTypes;
	private List<KeyValue> roomSpaces;
	private List<KeyValue> roomParts;
	private List<KeyValue> roomStyles;
	private List<KeyValue> ZxNodes;
	private List<KeyValue> jlNodes;
	private List<KeyValue> decorationTypes;
	private List<KeyValue> renovationWays;
	private List<KeyValue> storeOrderOptions;
	private List<KeyValue> costRanges;
	private List<RankType> appDiaryv2;
	private List<RankType> appStore;
	private List<RankType> jlStaff;
	private List<RankType> jlStore;
	private List<RankType> jlGongzhang;
	private List<KeyValue> diaryOrderOptions;

	public List<JourneyType> getJourneyTypeList() {
		return journeyTypeList;
	}

	public void setJourneyTypeList(List<JourneyType> journeyTypeList) {
		this.journeyTypeList = journeyTypeList;
	}

	public List<KeyValue> getRoomTypes() {
		return roomTypes;
	}

	public String getRoomTypeIdByRoomName(String id) {
		for (int i = 0; i < roomTypes.size(); i++) {
			if (roomTypes.get(i).getId().equals(id)) {
				return roomTypes.get(i).getName();
			}
		}
		return null;
	}

	public String getRoomStyleIdByRoomName(String id) {
		for (int i = 0; i < roomStyles.size(); i++) {
			if (roomStyles.get(i).getId().equals(id)) {
				return roomStyles.get(i).getName();
			}
		}
		return null;
	}

	public String getDecorationBudgetIdByRoomName(String id) {
		for (int i = 0; i < costRanges.size(); i++) {
			if (costRanges.get(i).getId().equals(id)) {
				return costRanges.get(i).getName();
			}
		}
		return null;
	}

	public void setRoomTypes(List<KeyValue> roomTypes) {
		this.roomTypes = roomTypes;
	}

	public List<KeyValue> getRoomSpaces() {
		return roomSpaces;
	}

	public void setRoomSpaces(List<KeyValue> roomSpaces) {
		this.roomSpaces = roomSpaces;
	}

	public List<KeyValue> getRoomParts() {
		return roomParts;
	}

	public void setRoomParts(List<KeyValue> roomParts) {
		this.roomParts = roomParts;
	}

	public List<KeyValue> getRoomStyles() {
		return roomStyles;
	}

	public void setRoomStyles(List<KeyValue> roomStyles) {
		this.roomStyles = roomStyles;
	}

	public List<KeyValue> getZxNodes() {
		LogUtil.i("ZxNodes:" + ZxNodes);
		return ZxNodes;
	}

	public void setZxNodes(List<KeyValue> zxNodes) {
		ZxNodes = zxNodes;
	}

	public List<KeyValue> getDecorationTypes() {
		return decorationTypes;
	}

	public KeyValue getKeyValueByKeyName(List<KeyValue> list, String value) {
		KeyValue keyValue = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(value)) {
				keyValue = list.get(i);
			}
		}
		return keyValue;
	}

	public void setDecorationTypes(List<KeyValue> decorationTypes) {
		this.decorationTypes = decorationTypes;
	}

	public List<KeyValue> getRenovationWays() {
		return renovationWays;
	}

	public void setRenovationWays(List<KeyValue> renovationWays) {
		this.renovationWays = renovationWays;
	}

	public List<KeyValue> getStoreOrderOptions() {
		return storeOrderOptions;
	}

	public void setStoreOrderOptions(List<KeyValue> storeOrderOptions) {
		this.storeOrderOptions = storeOrderOptions;
	}

	public List<KeyValue> getCostRanges() {
		return costRanges;
	}

	public void setCostRanges(List<KeyValue> costRanges) {
		this.costRanges = costRanges;
	}

	/**
	 * 
	 * @author jiengyh
	 * 
	 */
	public class JourneyType implements Serializable {

		public JourneyType() {
		};

		private int id;
		private int pid;
		private int cid;
		private String name;
		private String img;
		private List<JourneyType> stub;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public int getCid() {
			return cid;
		}

		public void setCid(int cid) {
			this.cid = cid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<JourneyType> getStub() {
			return stub;
		}

		public void setStub(List<JourneyType> stub) {
			this.stub = stub;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		@Override
		public String toString() {
			return "JourneyType [id=" + id + ", pid=" + pid + ", cid=" + cid
					+ ", name=" + name + ", img=" + img + ", stub=" + stub
					+ "]";
		}

	}

	public class RankType implements Serializable {
		private int id;
		private String name;
		private int maxScore;

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

		public int getMaxScore() {
			return maxScore;
		}

		public void setMaxScore(int maxScore) {
			this.maxScore = maxScore;
		}

		@Override
		public String toString() {
			return "RankType [id=" + id + ", name=" + name + ", maxScore="
					+ maxScore + "]";
		}

	}

	public List<RankType> getAppStore() {
		return appStore;
	}

	public void setAppStore(List<RankType> appStore) {
		this.appStore = appStore;
	}

	public List<RankType> getJlStaff() {
		return jlStaff;
	}

	public void setJlStaff(List<RankType> jlStaff) {
		this.jlStaff = jlStaff;
	}

	public List<RankType> getJlStore() {
		return jlStore;
	}

	public void setJlStore(List<RankType> jlStore) {
		this.jlStore = jlStore;
	}

	public List<RankType> getAppDiaryv2() {
		return appDiaryv2;
	}

	public void setAppDiaryv2(List<RankType> appDiaryv2) {
		this.appDiaryv2 = appDiaryv2;
	}

	public List<KeyValue> getJlNodes() {
		return jlNodes;
	}

	public void setJlNodes(List<KeyValue> jlNodes) {
		this.jlNodes = jlNodes;
	}

	public List<RankType> getJlGongzhang() {
		return jlGongzhang;
	}

	public void setJlGongzhang(List<RankType> jlGongzhang) {
		this.jlGongzhang = jlGongzhang;
	}

	@Override
	public String toString() {
		return "Constant [journeyTypeList=" + journeyTypeList + ", roomTypes="
				+ roomTypes + ", roomSpaces=" + roomSpaces + ", roomParts="
				+ roomParts + ", roomStyles=" + roomStyles + ", ZxNodes="
				+ ZxNodes + ", jlNodes=" + jlNodes + ", decorationTypes="
				+ decorationTypes + ", renovationWays=" + renovationWays
				+ ", storeOrderOptions=" + storeOrderOptions + ", costRanges="
				+ costRanges + ", appDiaryv2=" + appDiaryv2 + ", appStore="
				+ appStore + ", jlStaff=" + jlStaff + ", jlStore=" + jlStore
				+ ", jlGongzhang=" + jlGongzhang + ", diaryOrderOptions="
				+ diaryOrderOptions + "]";
	}

	public List<KeyValue> getDiaryOrderOptions() {
		return diaryOrderOptions;
	}

	public void setDiaryOrderOptions(List<KeyValue> diaryOrderOptions) {
		this.diaryOrderOptions = diaryOrderOptions;
	}

}
