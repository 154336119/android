package com.huizhuang.zxsq.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.AtlasGroup;
import com.huizhuang.zxsq.module.parser.AtlasGroupParser;
import com.huizhuang.zxsq.ui.activity.AtlasBrowseActivity;
import com.huizhuang.zxsq.ui.adapter.AtlasListViewAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseListFragment;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.component.PictureListFilterBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @author lim
 * @ClassName: AtlasFragment
 * @Description: 效果图
 * @mail lgmshare@gmail.com
 * @date 2014-9-5 下午3:54:37
 */
public class AtlasFragment extends BaseListFragment implements OnClickListener, IXListViewListener {

    private static final int REQ_DETAIL_CODE = 10;

    private DataLoadingLayout mDataLoadingLayout;

    private PictureListFilterBar mPictureFilterBar;

    private XListView mXListView;
    private AtlasListViewAdapter mAdapter;
    private LinearLayout mBtnBar;

    private ImageView mBtnGoTop;// 返回顶部
    private ImageView mBtnSearch;// 搜索

    private KeyValue mStyle;
    private KeyValue mSpace;
    private KeyValue mPart;
    private KeyValue mType;
    private int mCurrentPageIndex = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atlas, container, false);
        initActionBar(view);
        initVews(view);
        return view;
    }

    /**
     * 初始化ActionBar
     *
     * @param view
     */
    private void initActionBar(View view) {
        CommonActionBar commonActionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
        commonActionBar.setActionBarTitle(R.string.txt_bottom_tab_design_sketch);
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initVews(View view) {
        mDataLoadingLayout = (DataLoadingLayout) view.findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                httpRequestQueryAtlasList(XListRefreshType.ON_PULL_REFRESH);
            }
        });

        // 筛选条件
        mPictureFilterBar = (PictureListFilterBar) view.findViewById(R.id.top_filter_bar);
        mPictureFilterBar.setOnDropdownBarItemClickListener(new PictureListFilterBar.OnDropdownBarItemClickListener() {
            @Override
            public void onItemClick(PictureListFilterBar.TopType topType, KeyValue filterKeyValue) {
                if (PictureListFilterBar.TopType.FILTER_STYLE == topType) {
                    mStyle = filterKeyValue;
                } else if (PictureListFilterBar.TopType.FILTER_SPACE == topType) {
                    mSpace = filterKeyValue;
                } else if (PictureListFilterBar.TopType.FILTER_PARTS == topType) {
                    mPart = filterKeyValue;
                } else if (PictureListFilterBar.TopType.FILTER_TYPE == topType) {
                    mType = filterKeyValue;
                }
                onRefresh();
            }
        });

        // 搜索及返回顶部条
        mBtnBar = (LinearLayout) view.findViewById(R.id.ll_btn_bar);
        mBtnSearch = (ImageView) view.findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(this);
        mBtnGoTop = (ImageView) view.findViewById(R.id.btn_go_top);
        mBtnGoTop.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new AtlasListViewAdapter(getActivity());

        mXListView = (XListView) getListView();
        mXListView.setPullRefreshEnable(true);// 显示刷新
        mXListView.setPullLoadEnable(false);// 显示加载更多
        mXListView.setAutoRefreshEnable(true);// 开始自动加载
        mXListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
        mXListView.setXListViewListener(this);
        // 滚动不加载
        PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
        mXListView.setOnScrollListener(listener);
        mXListView.setAdapter(mAdapter);
        mXListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                    if (mBtnBar.getVisibility() != View.VISIBLE) {
                        mBtnBar.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mBtnBar.getVisibility() != View.INVISIBLE && totalItemCount > 3) {
                        mBtnBar.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
    }

    private void httpRequestQueryAtlasList(final XListRefreshType xListRefreshType) {
        RequestParams params = new RequestParams();
        if (mStyle != null) {
            params.put("room_style", mStyle.getId());
        }
        if (mSpace != null) {
            params.put("room_space", mSpace.getId());
        }
        if (mPart != null) {
            params.put("room_part", mPart.getId());
        }
        if (mType != null) {
            params.put("room_type", mType.getId());
        }
        params.put("page", mCurrentPageIndex);
        HttpClientApi.post(HttpClientApi.REQ_ATLAS_HOMEPAGE_LIST, params, new AtlasGroupParser(), new RequestCallBack<AtlasGroup>() {

            @Override
            public void onStart() {
                super.onStart();
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && mAdapter.getCount() == 0) {
                    mDataLoadingLayout.showDataLoading();
                }
            }

            @Override
            public void onSuccess(AtlasGroup group) {
                mDataLoadingLayout.showDataLoadSuccess();
                // 加载更多还是刷新
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    mAdapter.setList(group);
                    if (0 == group.size()) {
                        mDataLoadingLayout.showDataLoadEmpty("未找到匹配的数据，请更改筛选条件");
                    }
                    mXListView.setSelection(0);
                } else {
                    mAdapter.addList(group);
                }
                // 是否可以加载更多
                if (group.getTotalSize() == mAdapter.getCount()) {
                    mXListView.setPullLoadEnable(false);
                } else {
                    mXListView.setPullLoadEnable(true);
                }
                // 是否显示底部搜索框
                if (0 == group.size()) {
                    showSearchBar();
                } else {
                    hideSearchBar();
                }
            }

            @Override
            public void onFailure(NetroidError error) {
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    if (0 == mAdapter.getCount()) {
                        mDataLoadingLayout.showDataLoadFailed(error.getMessage());
                    } else {
                        showToastMsg(error.getMessage());
                    }
                } else {
                    mCurrentPageIndex--;
                    showToastMsg(error.getMessage());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    mXListView.onRefreshComplete();
                } else {
                    mXListView.onLoadMoreComplete();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                break;
            case R.id.btn_go_top:
                mXListView.smoothScrollToPosition(0);
                // setSelection是瞬间滚动到顶部
                // mXListView.requestFocusFromTouch();
                // mXListView.setSelection(0);
                mBtnBar.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position >= mXListView.getHeaderViewsCount()) {
            Atlas atlas = mAdapter.getList().get(position - mXListView.getHeaderViewsCount());
            Bundle bd = new Bundle();
            bd.putSerializable(AppConstants.PARAM_ATLAS, atlas);
            Intent intent = new Intent(getActivity(), AtlasBrowseActivity.class);
            intent.putExtras(bd);
            startActivityForResult(intent, REQ_DETAIL_CODE);
        }
    }

    private void showSearchBar() {
        mBtnBar.setVisibility(View.VISIBLE);
        mBtnSearch.setVisibility(View.INVISIBLE);
        mBtnGoTop.setVisibility(View.INVISIBLE);
    }

    private void hideSearchBar() {
        mBtnBar.setVisibility(View.INVISIBLE);
        mBtnSearch.setVisibility(View.INVISIBLE);
        mBtnGoTop.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新list
     */
    private void updateListView() {
        mXListView.onRefresh();
    }

    private void clearCondition() {
        mStyle = null;
        mSpace = null;
        mPart = null;
        mType = null;
    }

    @Override
    public void onRefresh() {
        mCurrentPageIndex = 1;
        httpRequestQueryAtlasList(XListRefreshType.ON_PULL_REFRESH);
    }

    @Override
    public void onLoadMore() {
        mCurrentPageIndex++;
        httpRequestQueryAtlasList(XListRefreshType.ON_LOAD_MORE);
    }


}
