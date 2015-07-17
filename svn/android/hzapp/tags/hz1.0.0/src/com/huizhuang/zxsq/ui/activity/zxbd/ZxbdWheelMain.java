package com.huizhuang.zxsq.ui.activity.zxbd;

import android.content.Context;
import android.view.View;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.widget.wheel.AbstractWheel;
import com.huizhuang.zxsq.widget.wheel.OnWheelChangedListener;
import com.huizhuang.zxsq.widget.wheel.OnWheelScrollListener;
import com.huizhuang.zxsq.widget.wheel.WheelVerticalView;
import com.huizhuang.zxsq.widget.wheel.adapter.ArrayWheelAdapter;


public class ZxbdWheelMain {

	private Context mContext;
	
	private View view;
	private WheelVerticalView wvParent;
	private WheelVerticalView wvChild;
	 // Scrolling flag
    private boolean scrolling = false;
    
	private String[] mMomentOneLevel = { "装修前", "正式开工", "入住新家" };
	private String[][] mMomentTwoLevel = {
			{ "收房验房", "量房设计", "预算", "选定装修公司", "建材采购", "买家具" },
			{ "开工", "水电改造", "泥瓦工程", "土木工程", "油漆工程", "竣工" }, { "软装", "入住" } };
   
	public ZxbdWheelMain(Context context,View view) {
		this.mContext = context;
		this.view = view;
		setView(view);
	}
	
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	public void init() {
		wvParent = (WheelVerticalView) view.findViewById(R.id.one);
		ArrayWheelAdapter<String> oneAdapter = new ArrayWheelAdapter<String>(mContext,mMomentOneLevel);
		oneAdapter.setItemResource(R.layout.zxbd_wheel_item);
		oneAdapter.setItemTextResource(R.id.name);
		wvParent.setViewAdapter(oneAdapter);// 设置"年"的显示数据
		wvParent.setCyclic(false);// 可循环滚动
		wvParent.setCurrentItem(0);// 初始化时显示的数据
		wvParent.setVisibleItems(3);
		// 月
		ArrayWheelAdapter<String> twoAdapter = new ArrayWheelAdapter<String>(mContext,mMomentTwoLevel[0]);
		twoAdapter.setItemResource(R.layout.zxbd_wheel_item);
		twoAdapter.setItemTextResource(R.id.name);
		wvChild = (WheelVerticalView) view.findViewById(R.id.two);
		wvChild.setViewAdapter(twoAdapter);
		wvChild.setCyclic(false);
		wvChild.setCurrentItem(0);
		wvChild.setVisibleItems(4);
		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				 if (!scrolling) {
					 updateCities(wvChild, newValue);
				 }
			}

		};
		
		OnWheelScrollListener scrollingListener = new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                scrolling = true;
            }
            public void onScrollingFinished(AbstractWheel wheel) {
                scrolling = false;
                updateCities(wvChild, wvParent.getCurrentItem());
            }
        };
		
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
			}
		};
		wvParent.addChangingListener(wheelListener_year);
		wvParent.addScrollingListener(scrollingListener);
		wvChild.addChangingListener(wheelListener_month);

	}
	
	/**
     * Updates the city spinnerwheel
     */
    private void updateCities(AbstractWheel city, int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(mContext, mMomentTwoLevel[index]);
        adapter.setItemResource(R.layout.zxbd_wheel_item);
		adapter.setItemTextResource(R.id.name);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    public String getMomentOne() {
    	return mMomentOneLevel[wvParent.getCurrentItem()];
    }
    
	public String getMomentTwo() {
		return mMomentTwoLevel[wvParent.getCurrentItem()][wvChild.getCurrentItem()];
	}
}
