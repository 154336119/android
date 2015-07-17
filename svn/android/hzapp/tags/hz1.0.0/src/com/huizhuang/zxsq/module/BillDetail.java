package com.huizhuang.zxsq.module;

import java.io.Serializable;
import java.util.List;

public class BillDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 731721081033479205L;

	private List<MonthSummary> monSumList;
	private List<TypeSummary> typeSumList;
	private double total;
	private String remain;

	public class MonthSummary implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1375669629740192869L;

		private double total;
		private int year;
		private int month;
		public double getTotal() {
			return total;
		}
		public void setTotal(double total) {
			this.total = total;
		}
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public int getMonth() {
			return month;
		}
		public void setMonth(int month) {
			this.month = month;
		}
		@Override
		public String toString() {
			return "MonthSummary [total=" + total + ", year=" + year + ", month=" + month + "]";
		}
	}

	public List<MonthSummary> getMonSumList() {
		return monSumList;
	}

	public void setMonSumList(List<MonthSummary> monSumList) {
		this.monSumList = monSumList;
	}

	public List<TypeSummary> getTypeSumList() {
		return typeSumList;
	}

	public void setTypeSumList(List<TypeSummary> typeSumList) {
		this.typeSumList = typeSumList;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getRemain() {
		return remain;
	}

	public void setRemain(String remain) {
		this.remain = remain;
	}

	@Override
	public String toString() {
		return "BillDetail [monSumList=" + monSumList + ", typeSumList=" + typeSumList + ", total=" + total + ", remain=" + remain + "]";
	}

}
