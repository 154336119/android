package com.huizhuang.zxsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

public class TipsAlertDialog {
	
	private Context mContext;
	private Dialog mDialog;
	private LinearLayout mLinearLayout;
	private TextView mTvTitle;
	private TextView mTvMessage;
	private Button mBtnNeg;
	private Button mBtnPos;
	private ImageView mImgLine;
	
	private Display display;
	
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;

	public TipsAlertDialog(Context context) {
		this.mContext = context;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public TipsAlertDialog Builder() {
		// Dialog
		View view = LayoutInflater.from(mContext).inflate(R.layout.view_alertdialog, null);
		// Dialog
		mLinearLayout = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		mTvTitle = (TextView) view.findViewById(R.id.txt_title);
		mTvTitle.setVisibility(View.GONE);
		mTvMessage = (TextView) view.findViewById(R.id.txt_msg);
		mTvMessage.setVisibility(View.GONE);
		mBtnNeg = (Button) view.findViewById(R.id.btn_neg);
		mBtnNeg.setVisibility(View.GONE);
		mBtnPos = (Button) view.findViewById(R.id.btn_pos);
		mBtnPos.setVisibility(View.GONE);
		mImgLine = (ImageView) view.findViewById(R.id.img_line);
		mImgLine.setVisibility(View.GONE);

		mDialog = new Dialog(mContext, R.style.AlertDialogStyle);
		mDialog.setContentView(view);

		mLinearLayout.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85),
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		return this;
	}

	public TipsAlertDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			mTvTitle.setText("");
		} else {
			mTvTitle.setText(title);
		}
		return this;
	}

	public TipsAlertDialog setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			mTvMessage.setText("");
		} else {
			mTvMessage.setText(msg);
		}
		return this;
	}

	public TipsAlertDialog setCancelable(boolean cancel) {
		mDialog.setCancelable(cancel);
		return this;
	}

	public TipsAlertDialog setPositiveButton(String text,
			final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			mBtnPos.setText("确定");
		} else {
			mBtnPos.setText(text);
		}
		mBtnPos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				mDialog.dismiss();
			}
		});
		return this;
	}

	public TipsAlertDialog setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			mBtnNeg.setText("取消");
		} else {
			mBtnNeg.setText(text);
		}
		mBtnNeg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				mDialog.dismiss();
			}
		});
		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			mTvTitle.setText("确定框");
			mTvTitle.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			mTvTitle.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			mTvMessage.setVisibility(View.VISIBLE);
		}

		if (!showPosBtn && !showNegBtn) {
			mBtnPos.setText("确定");
			mBtnPos.setVisibility(View.VISIBLE);
			mBtnPos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			mBtnPos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
				}
			});
		}

		if (showPosBtn && showNegBtn) {
			mBtnPos.setVisibility(View.VISIBLE);
			mBtnPos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			mBtnNeg.setVisibility(View.VISIBLE);
			mBtnNeg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			mImgLine.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			mBtnPos.setVisibility(View.VISIBLE);
			mBtnPos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			mBtnNeg.setVisibility(View.VISIBLE);
			mBtnNeg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		mDialog.show();
	}
}
