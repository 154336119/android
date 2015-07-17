package com.huizhuang.zxsq.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.huizhuang.hz.R;

/**
 * 通用自定义样式AlertDialog
 * 
 * @ClassName: CommonAlertDialog.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-11 上午9:38:40
 */
public class CommonAlertDialog {

	private AlertDialog mAlertDialog;
	private TextView mTxtViewTitle;
	private TextView mTxtViewMessage;
	private Button mBtnPositive;
	private Button mBtnNegative;

	public CommonAlertDialog(Context context) {
		mAlertDialog = new AlertDialog.Builder(context).create();
		mAlertDialog.show();
		Window window = mAlertDialog.getWindow();
		window.setContentView(R.layout.dialog_custom);
		mTxtViewTitle = (TextView) window.findViewById(R.id.txt_dialog_title);
		mTxtViewMessage = (TextView) window.findViewById(R.id.txt_dialog_message);
		mBtnPositive = (Button) window.findViewById(R.id.btn_dialog_positive);
		mBtnNegative = (Button) window.findViewById(R.id.btn_dialog_negative);
	}

	public void setButtonTextColor(int resId){
		mBtnPositive.setTextColor(resId);
		mBtnNegative.setTextColor(resId);
	}
	public void setTitle(int resId) {
		mTxtViewTitle.setText(resId);
		mTxtViewTitle.setVisibility(View.VISIBLE);
	}

	public void setMessage(int resId) {
		mTxtViewMessage.setText(resId);
	}

    public void setMessage(String txt){
        mTxtViewMessage.setText(txt);
    }

	/**
	 * 设置按钮（确定）
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(int resId, final View.OnClickListener onClickListener) {
		mBtnPositive.setText(resId);
		mBtnPositive.setOnClickListener(onClickListener);
	}

	/**
	 * 设置按钮（否定）
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(int resId, final View.OnClickListener onClickListener) {
		mBtnNegative.setText(resId);
		mBtnNegative.setOnClickListener(onClickListener);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		if (null != mAlertDialog) {
			mAlertDialog.dismiss();
		}
	}

	/**
	 * 显示对话框
	 */
	public void show() {
		if (null != mAlertDialog && !mAlertDialog.isShowing()) {
			mAlertDialog.show();
		}
	}

}
