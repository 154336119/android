package com.huizhuang.zxsq.widget.calendar;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeekdayAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<String> mList;

//	public static int textColor = Color.LTGRAY;
	public static int textColor = Color.parseColor("#666666");
	
	public WeekdayAdapter(Context context, ArrayList<String> str) {
		this.mList = str;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		TextView textView = new TextView(mContext);
		
		// Set content
		String item = mList.get(position);
		textView.setText(item);
		if (item.length() <= 3) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		} else {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
		}

		textView.setTextColor(textColor);
		textView.setGravity(Gravity.CENTER);
		return textView;
	}


	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
