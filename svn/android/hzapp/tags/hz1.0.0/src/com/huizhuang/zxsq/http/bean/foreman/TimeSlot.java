package com.huizhuang.zxsq.http.bean.foreman;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSlot {

	private String start_date;
	private String end_date;

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getTimeSlot() {
		if (start_date == null || end_date == null) {
			return "0";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = new Date();
		Date end = new Date();
		try {
			start = format.parse(start_date);
			end = format.parse(end_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = end.getTime() - start.getTime();
		time = time / (1000 * 60 * 60 * 24);
		if (time > 0) {

			return time + "";
		} else {
			return "0";
		}
	}

}
