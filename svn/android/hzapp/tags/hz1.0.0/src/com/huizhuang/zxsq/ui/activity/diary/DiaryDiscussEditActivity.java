package com.huizhuang.zxsq.ui.activity.diary;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 日记评论编辑
 * 
 * @ClassName: DiaryDiscussEditActivity
 * @Description:
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:08:47
 */
public class DiaryDiscussEditActivity extends BaseActivity {

	private Diary mDiary;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDiary =(Diary) getIntent().getExtras().getSerializable(AppConstants.PARAM_DIARY);
		setContentView(R.layout.diary_dicuss_edit_activity);
		initActionBar();
	}
	
	private void initActionBar(){
		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.diary_discuss_edit);
		commonActionBar.setLeftBtn(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commonActionBar.setRightTxtBtn(R.string.diary_discuss_edit_submit, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	private void submit() {
		EditText etContent = (EditText) findViewById(R.id.et_content);
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			showToastMsg("请填写评论内容");
			return;
		}
		hideSoftInput();
		showWaitDialog("发表中，请稍后");
		RequestParams params = new RequestParams();
		params.put("link_id", mDiary.getId());
		params.put("comment_key", "app_diary_v2");
		params.put("content", content);
		HttpClientApi.post(HttpClientApi.REQ_ADD_COMMENT, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				ActivityUtil.backWithResult(THIS, Activity.RESULT_OK, null);
			}

			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
			}

			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}
		});
	}

}
