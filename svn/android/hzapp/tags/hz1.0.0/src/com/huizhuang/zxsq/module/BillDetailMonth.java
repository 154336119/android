package com.huizhuang.zxsq.module;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

public class BillDetailMonth {

	private double total;

	private List<BillDetailItem> recordList;

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<BillDetailItem> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BillDetailItem> recordList) {
		this.recordList = recordList;
	}

	public class BillDetailItem {

		private int id;
		private double total;
		private String detail;
		private int tType;
		private String tTypeName;
		private int cType;
		private String cTypeName;
		private List<Image> imgs;

		@Override
		public String toString() {
			return "BillDetailItem [id=" + id + ", total=" + total
					+ ", detail=" + detail + ", tType=" + tType
					+ ", tTypeName=" + tTypeName + ", cType=" + cType
					+ ", cTypeName=" + cTypeName + ", imgs=" + imgs + "]";
		}

		public int gettType() {
			return tType;
		}

		public void settType(int tType) {
			this.tType = tType;
		}

		public String gettTypeName() {
			return tTypeName;
		}

		public void settTypeName(String tTypeName) {
			this.tTypeName = tTypeName;
		}

		public int getcType() {
			return cType;
		}

		public void setcType(int cType) {
			this.cType = cType;
		}

		public String getcTypeName() {
			return cTypeName;
		}

		public void setcTypeName(String cTypeName) {
			this.cTypeName = cTypeName;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public List<Image> getImgs() {
			return imgs;
		}

		public void setImgs(List<Image> imgs) {
			this.imgs = imgs;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}

	

	}
	
	
	

	@Override
	public String toString() {
		return "BillDetailMonth [total=" + total + ", recordList=" + recordList
				+ "]";
	}
}
