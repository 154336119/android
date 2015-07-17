package com.huizhuang.zxsq.ui.fragment.account;

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
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.UserFavorSketch;
import com.huizhuang.zxsq.module.parser.UserFavorSketchParser;
import com.huizhuang.zxsq.ui.activity.AtlasBrowseActivity;
import com.huizhuang.zxsq.ui.adapter.FavoritePicturesListAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.XListView;
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
public class FavoritePicturesFragment extends BaseListFragment {

    private static final int DEFAULT_START_PAGE = AppConfig.DEFAULT_START_PAGE;

    private XListView mXListView;
    private FavoritePicturesListAdapter mFavoritePicturesListAdapter;

    private int mCurLoadPage = DEFAULT_START_PAGE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreate Bundle = " + savedInstanceState);
        View view = inflater.inflate(R.layout.listview_refresh_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        httpRequestQueryFavoritePicturesData(mCurLoadPage);
    }

    private void initViews(){
        LogUtil.d("initView");

        mXListView = (XListView) getListView();
        mXListView.setPullRefreshEnable(true);// 显示刷新
        mXListView.setPullLoadEnable(false);// 显示加载更多
        mXListView.setAutoRefreshEnable(false);// 开始自动加载
        mXListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
        mXListView.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                LogUtil.d("mXListView onRefresh");
                mCurLoadPage = DEFAULT_START_PAGE;
                httpRequestQueryFavoritePicturesData(mCurLoadPage);
            }

            @Override
            public void onLoadMore() {
                LogUtil.d("mXListView onLoadMore");
                httpRequestQueryFavoritePicturesData(++mCurLoadPage);
            }

        });
        mFavoritePicturesListAdapter = new FavoritePicturesListAdapter(getActivity());
        mXListView.setAdapter(mFavoritePicturesListAdapter);
        mXListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("mXListView onItemClick position = " + position);
                UserFavorSketch.ImageInfo imageInfo = (UserFavorSketch.ImageInfo) parent.getAdapter().getItem(position);
                Atlas atlas = new Atlas();
                atlas.setAlbumId((String.valueOf(imageInfo.getAlbum_id())));
                atlas.setId(String.valueOf(imageInfo.getId()));
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.PARAM_ATLAS, atlas);
                ActivityUtil.next(getActivity(), AtlasBrowseActivity.class, bundle, -1);
            }

        });
    }

    /**
     * HTTP请求 - 获取收藏图片数据
     */
    private void httpRequestQueryFavoritePicturesData(int page) {
        LogUtil.d("httpRequestQueryFavoritePicturesData page = " + page);
        RequestParams params = new RequestParams();
        params.add("profile_id", ZxsqApplication.getInstance().getUser().getId());
        params.add("page", String.valueOf(page));
        HttpClientApi.get(HttpClientApi.USER_FAVORLIST_SKETCH, params, new UserFavorSketchParser(), new RequestCallBack<UserFavorSketch>() {

            @Override
            public void onSuccess(UserFavorSketch userFavorSketch) {
                LogUtil.d("httpRequestQueryFavoritePicturesData onSuccess UserFavorSketch = " + userFavorSketch);
                if (null != userFavorSketch && null != userFavorSketch.getImageInfoList() && userFavorSketch.getImageInfoList().size() > 0) {
                    int totalPage = userFavorSketch.getTotalpage();
                    List<UserFavorSketch.ImageInfo> imageInfoList = userFavorSketch.getImageInfoList();

                    if (mCurLoadPage == DEFAULT_START_PAGE) {
                        mFavoritePicturesListAdapter.setList(imageInfoList);
                    } else if (mCurLoadPage <= totalPage) {
                        mFavoritePicturesListAdapter.addList(imageInfoList);
                    }

                    // 如果是最后一页，则禁用自动加载更多
                    if (mCurLoadPage >= totalPage) {
                        mXListView.setPullLoadEnable(false);
                    }else{
                        mXListView.setPullLoadEnable(true);
                    }
                }
            }

            @Override
            public void onFailure(NetroidError netroidError) {
                LogUtil.d("httpRequestQueryFavoritePicturesData onFailure NetroidError = " + netroidError);
                String strError = netroidError.getMessage();
                if (mCurLoadPage > DEFAULT_START_PAGE) {
                    showToastMsg(strError);
                    mCurLoadPage--;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LogUtil.d("httpRequestQueryFavoritePicturesData onFinish");
                if (mCurLoadPage == DEFAULT_START_PAGE){
                    mXListView.onRefreshComplete();
                }else{
                    mXListView.onLoadMoreComplete();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("httpRequestQueryFavoritePicturesData onStart");
            }

        });
    }
}
