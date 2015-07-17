package com.huizhuang.zxsq.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huizhuang.hz.R;

/**
 * @ClassName: LoadingDialog
 * @Description: 等待对话框
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3  上午11:03:42
 */
public class LoadingDialog extends ProgressDialog {

	private TextView content;
	private String message;
	private Button btnRight;
	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialog);
	}

	public LoadingDialog(Context context, String message) {
		super(context, R.style.LoadingDialog);
		this.message = message;
	}

	public LoadingDialog(Context context, int theme, String message) {
		super(context, theme);
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_tips_loading);
		setCanceledOnTouchOutside(false);
		setInverseBackgroundForced(false);
		content = (TextView) findViewById(R.id.tips_msg);
		btnRight = (Button) findViewById(R.id.btn_right);
		setText(message);
	}

	public void setText(String message) {
		if (content == null) {
			this.message = message;
			return;
		}
		
		if (TextUtils.isEmpty(message)) {
			content.setVisibility(View.GONE);
		}else{
			content.setVisibility(View.VISIBLE);
			content.setText(message);
		}
	}

	public void setText(int resId) {
		setText(getContext().getResources().getString(resId));
	}
	
	public void setOnRightBtnListener(android.view.View.OnClickListener onClickListener){
		btnRight.setOnClickListener(onClickListener);
	}
}