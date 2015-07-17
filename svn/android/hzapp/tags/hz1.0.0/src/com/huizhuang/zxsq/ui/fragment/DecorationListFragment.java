package com.huizhuang.zxsq.ui.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.FitmentList;
import com.huizhuang.zxsq.module.parser.FitmentParser;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DecorationListItemBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 装修清单
 * 
 * @ClassName: DecorationListFragment
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-28 上午11:02:00
 */
public class DecorationListFragment extends BaseFragment {

	private LinearLayout mRootLayout;
	private ViewGroup mListEmptyView;

	private Context mContext;
	private String mUserId;

	public static DecorationListFragment newInstance() {
		return new DecorationListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_decoration_list, container, false);
		mRootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
		mListEmptyView = (ViewGroup) view.findViewById(R.id.list_empty_view);
		mListEmptyView.setVisibility(View.GONE);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mContext = getActivity();
		httpRequestQuneryDecorationList();
	}

	/**
	 * HTTP请求 - 获取装修清单数据
	 */
	private void httpRequestQuneryDecorationList() {
		RequestParams requestParams = new RequestParams();
		mUserId = getActivity().getIntent().getStringExtra("userId");
		LogUtil.i("mUserId:"+mUserId);
		if (mUserId == null) {
			mUserId = ZxsqApplication.getInstance().getUser().getId();
		}
		requestParams.add("profile_id", mUserId);
		HttpClientApi.get(HttpClientApi.REQ_USER_CENTER_TYPE_TOTAL, requestParams, new FitmentParser(), new RequestCallBack<List<FitmentList>>() {

			@Override
			public void onSuccess(List<FitmentList> list) {

				if (null != list && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						FitmentList fitmentList = list.get(i);
						DecorationListItemBar decorationListItemBar = new DecorationListItemBar(mContext);
						decorationListItemBar.setContent(fitmentList.getName(), fitmentList.getTotal());
						decorationListItemBar.setBigTextMode();
						mRootLayout.addView(decorationListItemBar);
						List<FitmentList> fitSubList = fitmentList.getSub();
						if (fitSubList != null && fitSubList.size() > 0) {
							LinearLayout llSub = new LinearLayout(mContext);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							llSub.setOrientation(LinearLayout.VERTICAL);
							params.bottomMargin = 4;
							mRootLayout.addView(llSub, params);
							for (int j = 0; j < fitSubList.size(); j++) {
								FitmentList fitSub = fitSubList.get(j);
								DecorationListItemBar fitSubbar = new DecorationListItemBar(mContext);
								fitSubbar.setContent(fitSub.getName(), fitSub.getTotal());
								if (j == fitSubList.size() - 1) {
									fitSubbar.enableViewLine(false);
								} else {
									fitSubbar.enableViewLine(true);
								}
								llSub.addView(fitSubbar);
							}
						}
					}
					mListEmptyView.setVisibility(View.GONE);
				} else {
					mListEmptyView.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onFailure(NetroidError error) {
				mListEmptyView.setVisibility(View.VISIBLE);
			}

		});
	}

}