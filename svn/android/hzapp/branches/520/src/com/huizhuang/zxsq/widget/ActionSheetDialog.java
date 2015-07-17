package com.huizhuang.zxsq.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huizhuang.hz.R;

public class ActionSheetDialog {
	
	private Context context;
	
	private Dialog mDialog;
	
	private TextView mTvTtile;
	private TextView mTvCancel;
	private LinearLayout mLLContent;
	private ScrollView mScrollView;
	
	private boolean mShowTitle = false;
	private List<SheetItem> mSheetItemList;
	private DisplayMetrics mMetrics = new DisplayMetrics();

	public ActionSheetDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		display.getMetrics(mMetrics);
	}

	public ActionSheetDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(R.layout.view_actionsheet, null);

		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(mMetrics.widthPixels);

		// 获取自定义Dialog布局中的控件
		mScrollView = (ScrollView) view.findViewById(R.id.sLayout_content);
		mLLContent = (LinearLayout) view.findViewById(R.id.lLayout_content);
		mTvTtile = (TextView) view.findViewById(R.id.txt_title);
		mTvCancel = (TextView) view.findViewById(R.id.txt_cancel);
		mTvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		// 定义Dialog布局和参数
		mDialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		mDialog.setContentView(view);
		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}

	public ActionSheetDialog setTitle(String title) {
		mShowTitle = true;
		mTvTtile.setVisibility(View.VISIBLE);
		mTvTtile.setText(title);
		return this;
	}

	public ActionSheetDialog setCancelable(boolean cancel) {
		mDialog.setCancelable(cancel);
		return this;
	}

	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	/**
	 * 
	 * @param strItem
	 *            条目名称
	 * @param color
	 *            条目字体颜色，设置null则默认蓝色
	 * @param listener
	 * @return
	 */
	public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color,
			OnSheetItemClickListener listener) {
		if (mSheetItemList == null) {
			mSheetItemList = new ArrayList<SheetItem>();
		}
		mSheetItemList.add(new SheetItem(strItem, color, listener));
		return this;
	}
	
//	public ActionSheetDialog addStringArrayItem(String[] items, SheetItemColor color,
//			OnSheetItemClickListener listener){
//	}

	/** 设置条目布局 */
	private void setSheetItems() {
		if (mSheetItemList == null || mSheetItemList.size() <= 0) {
			return;
		}

		int size = mSheetItemList.size();

		// TODO 高度控制，非最佳解决办法
		// 添加条目过多的时候控制高度
		if (size >= 7) {
			LinearLayout.LayoutParams params = (LayoutParams) mScrollView
					.getLayoutParams();
			params.height = mMetrics.heightPixels / 2;
			mScrollView.setLayoutParams(params);
		}

		// 循环添加条目
		for (int i = 1; i <= size; i++) {
			final int index = i;
			SheetItem sheetItem = mSheetItemList.get(i - 1);
			String strItem = sheetItem.name;
			SheetItemColor color = sheetItem.color;
			final OnSheetItemClickListener listener = sheetItem.itemClickListener;

			TextView textView = new TextView(context);
			textView.setTag(i);
			textView.setText(strItem);
			textView.setTextSize(18);
			textView.setGravity(Gravity.CENTER);

			// 背景图片
			if (size == 1) {
				if (mShowTitle) {
					textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
				} else {
					textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
				}
			} else {
				if (mShowTitle) {
					if (i >= 1 && i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				} else {
					if (i == 1) {
						textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
					} else if (i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				}
			}

			// 字体颜色
			if (color == null) {
				textView.setTextColor(Color.parseColor(SheetItemColor.Blue
						.getName()));
			} else {
				textView.setTextColor(Color.parseColor(color.getName()));
			}

			// 高度
			float scale = context.getResources().getDisplayMetrics().density;
			int height = (int) (45 * scale + 0.5f);
			textView.setLayoutParams(new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, height));

			// 点击事件
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(index);
					mDialog.dismiss();
				}
			});

			mLLContent.addView(textView);
		}
	}

	public void show() {
		setSheetItems();
		mDialog.show();
	}

	public interface OnSheetItemClickListener {
		void onClick(int which);
	}

	public class SheetItem {
		String name;
		OnSheetItemClickListener itemClickListener;
		SheetItemColor color;

		public SheetItem(String name, SheetItemColor color,
				OnSheetItemClickListener itemClickListener) {
			this.name = name;
			this.color = color;
			this.itemClickListener = itemClickListener;
		}
	}

	public enum SheetItemColor {
		Blue("#037BFF"), Red("#FD4A2E");

		private String name;

		private SheetItemColor(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
