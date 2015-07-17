package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.UserGroup;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/** 
* @ClassName: DiarySelectFriendListViewAdapter 
* @Description: 提醒好友 
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-11-3 下午6:41:43 
*  
*/
public class DiarySelectFriendListViewAdapter extends MyBaseAdapter<User>{

	private ViewHolder holder;
	private DisplayImageOptions mOptions;
	private SparseBooleanArray mCheckedMap;

	private Handler mHandler;
	
	private ArrayList<User> mSelectList;
	
	public DiarySelectFriendListViewAdapter(Context context, Handler handler) {
		super(context);
		mOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.global_defaultmain)
				.showImageForEmptyUri(R.drawable.global_defaultmain)
				.imageScaleType(ImageScaleType.NONE)// 设置图片缩放
				.bitmapConfig(Bitmap.Config.RGB_565)
				.cacheInMemory(true).cacheOnDisk(true).build();
		mCheckedMap = new SparseBooleanArray();
		mHandler = handler;
	}

	public void setList(UserGroup list) {
		getList().clear();
		getList().addAll(list);
		initChecked();
	}
	
	public SparseBooleanArray getSelectChecked(){
		return mCheckedMap;
	}
	
	
	public ArrayList<User> getSelectList() {
		return mSelectList;
	}

	public void setSelectList(ArrayList<User> selectList) {
		this.mSelectList = selectList;
	}

	private void initChecked(){
		mCheckedMap.clear();
		int length = getList().size();
		for (int i = 0; i < length; i++) {
			if (mSelectList != null) {
				boolean isSelect = false;
				for (int j = 0; j < mSelectList.size(); j++) {
					if (mSelectList.get(j).getId().equals(getList().get(i).getId())) {
						isSelect = true;
						break;
					}
				}
				mCheckedMap.put(i, isSelect);
			}else{
				mCheckedMap.put(i, false);
			}
		}
	}
	
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null) {
			holder = new ViewHolder();
			view = getLayoutInflater().inflate(R.layout.diary_edit_select_friend_item, arg2, false);
			holder.ivPhoto = (com.huizhuang.zxsq.widget.CircleImageView) view.findViewById(R.id.cv_icon);
			holder.tvSex = (TextView) view.findViewById(R.id.tv_sex);
			holder.tvCity = (TextView) view.findViewById(R.id.tv_city);
			holder.tvName = (TextView) view.findViewById(R.id.tv_name);
			holder.ckCheck = (CheckBox) view.findViewById(R.id.cb_select);
			holder.ckCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					int position = (Integer) arg0.getTag();
					if(arg1){
						mCheckedMap.put(position, true);
					} else{
						mCheckedMap.put(position, false);
					}
					mHandler.sendEmptyMessage(0);
				}
			});
			
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		holder.ckCheck.setTag(position);
		
		User user = getList().get(position);
		holder.tvName.setText(user.getName());
		holder.tvSex.setText(user.getSex());
		holder.tvCity.setText(user.getCity());
		if (mCheckedMap.get(position)) {
			holder.ckCheck.setChecked(true);
		}else{
			holder.ckCheck.setChecked(false);
		}
		ImageLoader.getInstance().displayImage(getList().get(position).getAvatar(), holder.ivPhoto, mOptions);
		return view;
	}

	class ViewHolder{
		com.huizhuang.zxsq.widget.CircleImageView ivPhoto;
		CheckBox ckCheck;
		TextView tvSex;
		TextView tvCity;
		TextView tvName;
	}
}
