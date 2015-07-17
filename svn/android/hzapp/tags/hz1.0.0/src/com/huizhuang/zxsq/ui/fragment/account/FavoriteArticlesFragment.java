package com.huizhuang.zxsq.ui.fragment.account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.CompanyArticles;
import com.huizhuang.zxsq.module.MyArticles;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.MyArticlesParser;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.company.CompanyArticleDetailsActicity;
import com.huizhuang.zxsq.ui.activity.company.CompanyArticlesActicity;
import com.huizhuang.zxsq.ui.adapter.FavoriteArticlesListAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenu;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenuCreator;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenuItem;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenuListView;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

import java.util.List;

/**
 * Created with Android Studio.
 * User : Lim
 * Email: lgmshare@gmail.com
 * Date : 2015/2/6
 * Time : 15:29
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteArticlesFragment extends BaseFragment {

    private static final int DEFAULT_START_PAGE = AppConfig.DEFAULT_START_PAGE;

    /**
     * 所有控件（页面中从上到下排列）
     */
    private DataLoadingLayout mDataLoadingLayout;
    private SwipeMenuListView mSwipeMenuListView;
    private FavoriteArticlesListAdapter mFavoriteArticlesListAdapter;

    private int mCurLoadPage = DEFAULT_START_PAGE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreate Bundle = " + savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_favorite_articles, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        httpRequestQueryFavoriteArticlesData(mCurLoadPage);
    }

    private void initViews(View view){
        LogUtil.d("initView");
        mDataLoadingLayout = (DataLoadingLayout) view.findViewById(R.id.favorite_articles_data_loading_layout);

        mSwipeMenuListView = (SwipeMenuListView) view.findViewById(R.id.my_articles_list);
        mSwipeMenuListView.setPullRefreshEnable(true);// 显示刷新
        mSwipeMenuListView.setPullLoadEnable(true);// 显示加载更多
        mSwipeMenuListView.setAutoRefreshEnable(false);// 开始自动加载
        mSwipeMenuListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
        mSwipeMenuListView.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                LogUtil.d("mXListView onRefresh");
                mCurLoadPage = DEFAULT_START_PAGE;
                httpRequestQueryFavoriteArticlesData(mCurLoadPage);
            }

            @Override
            public void onLoadMore() {
                LogUtil.d("mXListView onLoadMore");
                httpRequestQueryFavoriteArticlesData(++mCurLoadPage);
            }

        });

        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu swipeMenu) {
                LogUtil.d("SwipeMenuCreator create SwipeMenu = " + swipeMenu);
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
                openItem.setBackground(R.color.color_f22c48);
                openItem.setWidth(UiUtil.dp2px(getActivity(), 137));
                openItem.setTitle(R.string.txt_cancel_favorite);
                openItem.setTitleSize(20);
                openItem.setTitleColor(Color.WHITE);
                swipeMenu.addMenuItem(openItem);
            }

        };
        mSwipeMenuListView.setMenuCreator(swipeMenuCreator);
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                LogUtil.d("mSwipeMenuListView onMenuItemClick position = " + position);
                httpRequestCancelFavoriteArticle(position);
                return false;
            }

        });

        mFavoriteArticlesListAdapter = new FavoriteArticlesListAdapter(getActivity());
        mSwipeMenuListView.setAdapter(mFavoriteArticlesListAdapter);
        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("mSwipeMenuListView onItemClick position = " + position);

                MyArticles.ArticleDigest articleDigest = (MyArticles.ArticleDigest) parent.getAdapter().getItem(position);
                LogUtil.d("articleDigest = " + articleDigest);

                CompanyArticles companyArticles = new CompanyArticles();
                companyArticles.setAddTime(String.valueOf(articleDigest.getAddTime()));
                companyArticles.setCommentCount(articleDigest.getCommentCount());
                companyArticles.setDetailUrl(articleDigest.getDetailUrl());
                companyArticles.setFfCount(articleDigest.getFfCount());
                companyArticles.setId(String.valueOf(articleDigest.getCntId()));
                companyArticles.setImg(articleDigest.getThumb());

                companyArticles.setIsFavored(true);
                companyArticles.setShareCount(articleDigest.getShareCount());
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.PARAM_DIARY, companyArticles);
                if (CompanyArticleDetailsActicity.CNT_TYPE_RENOVATION.equals(articleDigest.getCntType())) {//后台埋的坑  这个收藏类型没有真的返回回来 直接使用的
                    bundle.putInt(CompanyArticlesActicity.COMPANY_ARTICLES, 1);
                } else if (CompanyArticleDetailsActicity.CNT_TYPE_SUPERVISION.equals(articleDigest.getCntType())) {
                    bundle.putInt(CompanyArticlesActicity.COMPANY_ARTICLES, 0);
                }
                ActivityUtil.next(getActivity(), CompanyArticleDetailsActicity.class, bundle, -1);
            }
        });
    }

    /**
     * HTTP请求 - 获取收藏文章数据
     */
    private void httpRequestQueryFavoriteArticlesData(int page) {
        LogUtil.d("httpRequestQueryFavoriteArticlesData page = " + page);
        RequestParams params = new RequestParams();
        params.add("profile_id", ZxsqApplication.getInstance().getUser().getId());
        params.add("page", String.valueOf(page));
        HttpClientApi.get(HttpClientApi.USER_FAVORLIST, params, new MyArticlesParser(), new RequestCallBack<MyArticles>() {

            @Override
            public void onSuccess(MyArticles myArticles) {
                LogUtil.i("httpRequestQueryFavoriteArticlesData onSuccess MyArticles = " + myArticles);
                if (null != myArticles && null != myArticles.getAcArticleDigestList() && myArticles.getAcArticleDigestList().size() > 0) {
                    int totalPage = myArticles.getTotalpage();
                    List<MyArticles.ArticleDigest> articleDigestList = myArticles.getAcArticleDigestList();
                    if (mCurLoadPage == DEFAULT_START_PAGE) {
                        mDataLoadingLayout.showDataLoadSuccess();
                        mFavoriteArticlesListAdapter.setList(articleDigestList);
                    } else if (mCurLoadPage <= totalPage) {
                        mFavoriteArticlesListAdapter.addList(articleDigestList);
                    }

                    // 如果是最后一页，则禁用自动加载更多
                    if (mCurLoadPage >= totalPage) {
                        mSwipeMenuListView.setPullLoadEnable(false);
                    }

                } else {
                    if (mCurLoadPage == DEFAULT_START_PAGE) {
                        mDataLoadingLayout.showDataEmptyView();
                    } else {
                        mSwipeMenuListView.setPullLoadEnable(false);
                    }
                }

            }

            @Override
            public void onFailure(NetroidError netroidError) {
                LogUtil.d("httpRequestQueryFavoriteArticlesData onFailure NetroidError = " + netroidError);
                String strError = netroidError.getMessage();
                if (mCurLoadPage == DEFAULT_START_PAGE) {
                    mDataLoadingLayout.showDataLoadFailed(strError);
                } else {
                    showToastMsg(strError);
                    mCurLoadPage--;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LogUtil.d("httpRequestQueryFavoriteArticlesData onFinish");
                mSwipeMenuListView.onLoadMoreComplete();
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("httpRequestQueryFavoriteArticlesData onStart");
                if (DEFAULT_START_PAGE == mCurLoadPage) {
                    mDataLoadingLayout.showDataLoading();
                }
            }

        });
    }

    /**
     * HTTP请求 - 取消收藏
     *
     * @param position
     */
    private void httpRequestCancelFavoriteArticle(final int position) {
        LogUtil.d("httpRequestCancelFavoriteArticle position = " + position);

        MyArticles.ArticleDigest articleDigest = mFavoriteArticlesListAdapter.getItem(position);
        RequestParams params = new RequestParams();
        params.add("cnt_type", articleDigest.getCntType());
        params.add("cnt_id", String.valueOf(articleDigest.getCntId()));
        HttpClientApi.post(HttpClientApi.REQ_COMMON_FAVORITE_DEL, params, new ResultParser(), new RequestCallBack<Result>() {

            @Override
            public void onSuccess(Result result) {
                LogUtil.d("httpRequestCancelFavoriteArticle onSuccess Result = " + result);
                mFavoriteArticlesListAdapter.removeArticleDigest(position);
            }

            @Override
            public void onFailure(NetroidError netroidError) {
                LogUtil.d("httpRequestCancelFavoriteArticle onFailure NetroidError = " + netroidError);
                showToastMsg(netroidError.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LogUtil.d("httpRequestCancelFavoriteArticle onFinish");
                hideWaitDialog();
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("httpRequestCancelFavoriteArticle onStart");
                showWaitDialog(getString(R.string.txt_on_waiting));
            }

        });
    }

}
