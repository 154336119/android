package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.huizhuang.hz.R;

/**
 * 通用顶部搜索条
 * 
 * @ClassName: CommonTopSearchBar.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-27 下午3:27:30
 */
public class CommonTopSearchBar extends RelativeLayout {

	private ViewGroup mContainerView;
	private ClearEditText mClearEditText;
	private Button mBtnCancel;

	public CommonTopSearchBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public CommonTopSearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public CommonTopSearchBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.search_condition_input_bar, this);
		mContainerView = (ViewGroup) rootLayout.findViewById(R.id.rl_container);
		mBtnCancel = (Button) rootLayout.findViewById(R.id.btn_cancel);
		mBtnCancel.setVisibility(View.VISIBLE);
		mClearEditText = (ClearEditText) rootLayout.findViewById(R.id.edt_keywork);
		mClearEditText.setEnableFocusChange(false);
		mClearEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 隐藏软键盘
					InputMethodManager inputMethodManager = ((InputMethodManager) mClearEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
					if (null != inputMethodManager) {
						inputMethodManager.hideSoftInputFromWindow(mClearEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
					if (null != mOnKeyboradListener) {
						mOnKeyboradListener.onSearchKeyClick();
					}
					return true;
				}
				return false;
			}

		});
	}

	/**
	 * 设置取消按钮点击事件
	 * 
	 * @param onClickListener
	 */
	public void setBtnCancelOnClickListener(View.OnClickListener onClickListener) {
		mBtnCancel.setOnClickListener(onClickListener);
	}

	/**
	 * 获得输入框中的文字
	 * 
	 * @return
	 */
	public String getInputKeyWord() {
		return mClearEditText.getText().toString().trim();
	}

	/**
	 * 设置软键盘事件监听
	 * 
	 * @param onKeyboradListener
	 */
	public void setOnKeyboradListener(OnKeyboradListener onKeyboradListener) {
		mOnKeyboradListener = onKeyboradListener;
	}

	public void setInputKeyWordEmpty() {
		mClearEditText.setText("");
	}

	private OnKeyboradListener mOnKeyboradListener;

	public interface OnKeyboradListener {
		void onSearchKeyClick();
	}

	public void setCommonTopSearchBarVisibility(int visibility) {
		mContainerView.setVisibility(visibility);
	}
}
