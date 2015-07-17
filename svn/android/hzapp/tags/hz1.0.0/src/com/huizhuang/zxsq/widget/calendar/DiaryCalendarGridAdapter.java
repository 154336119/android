package com.huizhuang.zxsq.widget.calendar;

import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;


public class DiaryCalendarGridAdapter extends CaldroidGridAdapter{

	private HashMap<DateTime, String> diaryDates;
	
	protected DateTime mSelectDateTime;
	
	public DiaryCalendarGridAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData,
			HashMap<DateTime, String> diaryDates) {
		super(context, month, year, caldroidData, extraData);
		this.diaryDates = diaryDates;
		mSelectDateTime = new DateTime(year, month, 1, 0, 0, 0, 0);
		
	}
	
	/**
	 * @return the diaryDates
	 */
	public HashMap<DateTime, String> getDiaryDates() {
		return diaryDates;
	}

	/**
	 * @param diaryDates the diaryDates to set
	 */
	public void setDiaryDates(HashMap<DateTime, String> diaryDates) {
		this.diaryDates = diaryDates;
	}

	/**
	 * @return the selectDateTime
	 */
	public DateTime getSelectDateTime() {
		return mSelectDateTime;
	}

	/**
	 * @param selectDateTime the selectDateTime to set
	 */
	public void setSelectDateTime(DateTime selectDateTime) {
		this.mSelectDateTime = selectDateTime;
	}

	public void oneWeeksInCalendar(){
		ArrayList<DateTime> dates = new ArrayList<DateTime>();
		
		int length = datetimeList.size();
		int j = 0;
		for (int i = 0; i < length; i++) {
			if (datetimeList.get(i).equals(mSelectDateTime)) {
				j = i;
				break;
			}
		}
		int num = j/7;
		for (int i = num * 7; i < num * 7 + 7; i++) {
			dates.add(datetimeList.get(i));
		}
		
		datetimeList.clear();
		datetimeList.addAll(dates);
	}
	
	public void sixWeeksInCalendar(){
		this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year,
				startDayOfWeek, sixWeeksInCalendar);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView cellView = (TextView) convertView;

		// For reuse
		if (convertView == null) {
			cellView = (TextView) inflater.inflate(R.layout.calendar_date_cell, null);
		}
		customizeTextView(position, cellView);
		cellView.setTextColor(Color.BLACK);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		
		return cellView;
	}
	
	
}
