package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Article;
import com.huizhuang.zxsq.module.ArticleGroup;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * @ClassName: SupervisionQuestionAdapter
 * @Package com.huizhuang.supervision.ui.adapter
 * @Description: 
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-8-4  下午3:01:13
 */
public class ArticleELVAdapter extends BaseExpandableListAdapter{

	private Context mContext;
	private ViewTitleHolder mTitleholder;
	private ViewHolder holder;
	
	private ArrayList<Article> mList;
	
	private int mPaddingDimen = 0;
	
	public ArticleELVAdapter(Context context, ArrayList<Article> list){
		mContext = context;
		mList = list;
		mPaddingDimen = UiUtil.dp2px(context, 8);
	}
	
	public ArrayList<Article> getList() {
		return mList;
	}

	public void setList(ArticleGroup list) {
		mList.clear();
		mList.addAll(list);
	}

	public void addList(ArticleGroup list){
		mList.addAll(list);
	}

    
	@Override
	public int getGroupCount() {
		return mList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mList.get(groupPosition).getChildArticle().size();
	}

	@Override
	public Article getGroup(int groupPosition) {
		return mList.get(groupPosition);
	}

	@Override
	public Article getChild(int groupPosition, int childPosition) {
		return mList.get(groupPosition).getChildArticle().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
		if (view == null) {
			mTitleholder = new ViewTitleHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.recommed_article_list_group_item, parent, false);
			mTitleholder.tvDatetime = (TextView) view.findViewById(R.id.tv_datetime);
			mTitleholder.ivImage = (ImageView) view.findViewById(R.id.iv_image);
			mTitleholder.tvName = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(mTitleholder);
		}else{
			mTitleholder = (ViewTitleHolder) view.getTag();
		}
		
		Article order = mList.get(groupPosition);
		mTitleholder.tvDatetime.setText(DateUtil.friendlyTime2(DateUtil.timestampToSdate(order.getCreatetime(), "yyyy-MM-dd HH:mm:ss")));
		mTitleholder.tvName.setText(order.getTitle());
		ImageLoader.getInstance().displayImage(order.getImageUrl(), mTitleholder.ivImage);
		
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.recommed_article_list_child_item, parent, false);
			 holder.ivPic = (ImageView) view.findViewById(R.id.iv_image);
			 holder.tvName = (TextView) view.findViewById(R.id.tv_name);
             holder.rlItem = (RelativeLayout) view.findViewById(R.id.rl_item);
             
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		Article goods = mList.get(groupPosition).getChildArticle().get(childPosition);
		holder.tvName.setText(goods.getTitle());
		if (!TextUtils.isEmpty(goods.getImageUrl())) {
        	ImageLoader.getInstance().displayImage(goods.getImageUrl(), holder.ivPic);
		}else{
			holder.ivPic.setImageResource(R.drawable.global_defaultmain);
		}
		
		if (childPosition < mList.get(groupPosition).getChildArticle().size() - 1) {
			holder.rlItem.setBackgroundResource(R.drawable.global_list_corner_round_middle_selector);
		}else{
			holder.rlItem.setBackgroundResource(R.drawable.global_list_corner_round_bottom_selector);
		}
		
		holder.rlItem.setPadding(mPaddingDimen, mPaddingDimen, mPaddingDimen, mPaddingDimen);
		
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void updateView(ListView listView, int itemIndex) {
		int visiblePosition = listView.getFirstVisiblePosition();
		View v = listView.getChildAt(itemIndex - visiblePosition);
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		if (viewHolder != null) {
//			viewHolder.titleTextView.setText("我更新了");
		}
	}  
	
	static class ViewTitleHolder {
		TextView tvDatetime;
		ImageView ivImage;
		TextView tvName;
	}
	
	static class ViewHolder {
		ImageView ivPic;
		TextView tvName;
		RelativeLayout rlItem;
	}

}
