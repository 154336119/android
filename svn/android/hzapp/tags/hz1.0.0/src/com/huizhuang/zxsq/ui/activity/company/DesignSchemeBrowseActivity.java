package com.huizhuang.zxsq.ui.activity.company;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.AtlasGroup;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.module.parser.AtlasBrowseGroupParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderCheckPhoneActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.adapter.AtlasBrowseViewPagerAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LocalRestrictClicksUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.HackyViewPager;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * Lock/Unlock button is added to the ActionBar.
 * Use it to temporarily disable ViewPager navigation in order to correctly interact with ImageView by gestures.
 * Lock/Unlock state of ViewPager is saved and restored on configuration changes.
 *
 * Julia Zudikova
 */

/**
 * @author lim
 * @ClassName: AtlasBrowseActivity
 * @Package com.huizhuang.zxsq.ui.activity
 * @Description: 图集浏览
 * @mail lgmshare@gmail.com
 * @date 2014-9-9 下午2:53:04
 */
@SuppressWarnings("deprecation")
public class DesignSchemeBrowseActivity extends BaseActivity implements OnClickListener {

    private final int REQ_LOGIN_CODE = 661;

    private ViewPager mViewPager;
    private TextView mTvPageTotal;// 总数
    private TextView mTvPageIndex;
    private ImageView mIvFavrorited;// 是否收藏
    private ImageView mIvLike;// 是否点赞
    private ImageView mIvShare;// 是否点赞

    private Atlas mAtlas;
    private ArrayList<Atlas> mAtlasList;
    private AtlasBrowseViewPagerAdapter mAdapter;

    private String mShareNum, mFavorNum;
    private boolean isShare = false, isFavorte = false;
    private int mCurrentIndex = 0;

    private String mLikeTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atlas_browse_viewpager);
        getIntentExtra();
        initParamsAndData();
        initUI();
        queryAtlasData();
    }

    private void getIntentExtra() {
        mAtlas = (Atlas) getIntent().getExtras().getSerializable(AppConstants.PARAM_ATLAS);
    }

    private void initParamsAndData() {
        mAtlasList = new ArrayList<Atlas>();
        if (null != ZxsqApplication.getInstance().getUser()) {
            mLikeTag = ZxsqApplication.getInstance().getUser().getId() + "_atlas";
        }
    }

    private void initUI() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_like).setOnClickListener(this);
        findViewById(R.id.btn_collect).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);

        mTvPageTotal = (TextView) findViewById(R.id.tv_total);
        mTvPageIndex = (TextView) findViewById(R.id.tv_index);
        mIvFavrorited = (ImageView) findViewById(R.id.btn_collect);
        mIvLike = (ImageView) findViewById(R.id.btn_like);
        mIvShare = (ImageView) findViewById(R.id.btn_share);

        mTvPageTotal.setText("0");
        mTvPageIndex.setText("0");

        mAdapter = new AtlasBrowseViewPagerAdapter(this, mAtlasList);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                updateUI(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 获取图集效果图列表
     */
    private void queryAtlasData() {
        if (mAtlas == null || mAtlas.getAlbumId() == null)
            return;
        showWaitDialog("");
        RequestParams params = new RequestParams();
        if (ZxsqApplication.getInstance().isLogged()) {
            params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
        }
        params.put("album_id", mAtlas.getAlbumId());
        HttpClientApi.get(HttpClientApi.REQ_ATLAS_LIST, params, new AtlasBrowseGroupParser(), new RequestCallBack<AtlasGroup>() {

            @Override
            public void onSuccess(AtlasGroup result) {
                if (result.size() > 0) {
                    mAtlasList.clear();
                    mAtlasList.addAll(result);
                    mAdapter.notifyDataSetChanged();

                    int currentIndex = 0;
                    for (int i = 0; i < mAtlasList.size(); i++) {
                        Atlas atlas = mAtlasList.get(i);
                        if (atlas.getId().equalsIgnoreCase(mAtlas.getId())) {
                            currentIndex = i;
                            break;
                        }
                    }

                    mViewPager.setCurrentItem(currentIndex);
                    mTvPageIndex.setText(String.valueOf(currentIndex + 1));
                    mTvPageTotal.setText(String.valueOf(result.size()));
                    updateLike(currentIndex);
                    updateFavorite(currentIndex);
                    mCurrentIndex = currentIndex;
                }
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

    private void favorite(final int position) {
        if (!ZxsqApplication.getInstance().isLogged()) {
            ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
            return;
        }
        if (!isShare) {
            mShareNum = mAtlasList.get(mCurrentIndex).getShareNum();
        }
        showWaitDialog("收藏中...");
        RequestParams params = new RequestParams();
        params.put("cnt_id", mAtlasList.get(position).getId());
        params.put("cnt_type", HttpClientApi.CntType.app_sketch_v2);
        String url = null;
        if (mAtlasList.get(position).isFavorited()) {
            url = HttpClientApi.REQ_COMMON_FAVORITE_DEL;
        } else {
            url = HttpClientApi.REQ_COMMON_FAVORITE;
        }
        HttpClientApi.post(url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(String response) {
                Atlas photo = mAtlasList.get(position);
                if (photo.isFavorited()) {
                    mFavorNum = String.valueOf((Integer.valueOf(mAtlasList.get(position).getFavoriteNum()) - 1));
                    photo.setFavorited(false);
                    showToastMsg("取消成功");
                } else {
                    try {
                        JSONObject json = new JSONObject(response);
                        mFavorNum = json.getJSONObject("data").getString("favor_num");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    photo.setFavorited(true);
                    showToastMsg("收藏成功");
                }
                if (position == mCurrentIndex) {
                    isFavorte = true;
                }
                // 发送收藏状态改变广播
                BroadCastManager.sendBroadCast(DesignSchemeBrowseActivity.this, BroadCastManager.ACTION_PICTURE_FAVORITE_STATUS_CHANGED);
                updateFavorite(position);
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

    private void like(final int position) {
//		if (!AppContext.getInstance().isLogged()) {
//			ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
//			return;
//		}
        showWaitDialog("");
        RequestParams params = new RequestParams();
        params.put("cnt_id", mAtlasList.get(position).getId());
        params.put("cnt_type", HttpClientApi.CntType.app_sketch_v2);
        HttpClientApi.post(HttpClientApi.REQ_COMMON_LIKE, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(String response) {
                Atlas photo = mAtlasList.get(position);
                photo.setLiked(true);
                showToastMsg("点赞成功");
                LocalRestrictClicksUtil.getInstance().setUserClickStateDiseable(mLikeTag, photo.getId());
                updateLike(position);
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

    private void updateUI(int position) {
        mTvPageIndex.setText(String.valueOf(position + 1));
        updateLike(position);
        updateFavorite(position);
    }

    /**
     * 点赞
     */
    private void updateLike(int position) {
        Atlas photo = mAtlasList.get(position);
        if (photo.isLiked()) {
            mIvLike.setImageResource(R.drawable.ic_like_selected);
        } else {
            if (LocalRestrictClicksUtil.getInstance().isUserCanClick(mLikeTag, mAtlasList.get(position).getId())) {
                mIvLike.setImageResource(R.drawable.ic_like);
            } else {
                mIvLike.setImageResource(R.drawable.ic_like_selected);
            }
        }
    }

    /**
     * 收藏
     */
    private void updateFavorite(int position) {
        Atlas photo = mAtlasList.get(position);
        if (photo.isFavorited()) {
            mIvFavrorited.setImageResource(R.drawable.ic_collected);
        } else {
            mIvFavrorited.setImageResource(R.drawable.ic_collect);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (isShare || isFavorte) {
                    Intent intent = getIntent();
                    intent.putExtra("shareNum", mShareNum);
                    intent.putExtra("favorNum", mFavorNum);
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            case R.id.btn_collect:
                if (mAtlasList.size() > 0) {
                    int index = mViewPager.getCurrentItem();
                    favorite(index);
                }
                break;
            case R.id.btn_like:
                if (mAtlasList.size() > 0) {
                    int index = mViewPager.getCurrentItem();
                    if (LocalRestrictClicksUtil.getInstance().isUserCanClick(mLikeTag, mAtlasList.get(index).getId())) {
                        like(index);
                    } else {
                        // 是否已收藏
                        if (!mAtlasList.get(index).isFavorited()) {
                            showToastMsg("已经点赞,喜欢就收藏吧！");
                        }
                    }
                }
                break;
            case R.id.btn_share:
                Share share = new Share();
                share.setCallBack(true);
                int index = mViewPager.getCurrentItem();
//                String url = AppConfig.URL_SHARE_ATLAS + "album_id=" + mAtlas.getAlbumId() + "&sketch_id=" + mAtlasList.get(index).getId();
//                share.setUrl(url);
//                share.setSite(url);
//                share.setSiteUrl(url);

                String text = String.format(getString(R.string.txt_share_xgt), mAtlasList.get(index).getName());
                share.setText(text);
                Util.showShare(false, DesignSchemeBrowseActivity.this, share);
                break;
            case R.id.btn_order:
                AnalyticsUtil.onEvent(THIS, UmengEvent.ID_ATALS_SUBMIT_INFO);
                ActivityUtil.next(THIS, OrderCheckPhoneActivity.class);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 分享效果图
     *
     * @param position         效果图ID
     * @param share_type 分享类型
     */
    private void httpRequestToShare(final int position, int share_type) {
        showWaitDialog("分享中...");
        if (!isFavorte) {
            mFavorNum = mAtlasList.get(mCurrentIndex).getFavoriteNum();
        }
        RequestParams params = new RequestParams();
        params.put("cnt_id", mAtlasList.get(position).getId());
        params.put("cnt_type", share_type);
        params.put("share_type", "app_sketch_v2");
        HttpClientApi.post(HttpClientApi.REQ_COMMON_SHARE, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(String response) {
                LogUtil.e("onSuccess()", "onSuccess():" + response);
                showToastMsg("分享成功");
                try {
                    JSONObject json = new JSONObject(response);
                    mShareNum = json.getJSONObject("data").getString("share_num");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (position == mCurrentIndex) {
                    isShare = true;
                }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShare || isFavorte) {
                Intent intent = getIntent();
                intent.putExtra("shareNum", mShareNum);
                intent.putExtra("favorNum", mFavorNum);
                setResult(RESULT_OK, intent);
            }
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_LOGIN_CODE) {
                if (mLikeTag == null) {
                    mLikeTag = ZxsqApplication.getInstance().getUser().getId() + "_atlas";
                }
                queryAtlasData();
            } else if (requestCode == AppConstants.TO_SHARE_CODE) {
                int shareType = data.getIntExtra("shareType", 0);
                if (mAtlasList.size() > 0) {
                    int index = mViewPager.getCurrentItem();
                    httpRequestToShare(index, shareType);
                }
            }
        }
    }
}
