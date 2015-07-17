package com.huizhuang.zxsq.ui.activity.account;

import android.content.Context;
import android.view.View;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.widget.wheel.AbstractWheel;
import com.huizhuang.zxsq.widget.wheel.OnWheelChangedListener;
import com.huizhuang.zxsq.widget.wheel.OnWheelScrollListener;
import com.huizhuang.zxsq.widget.wheel.WheelVerticalView;
import com.huizhuang.zxsq.widget.wheel.adapter.ArrayWheelAdapter;


public class CityWheelMain {

	private Context mContext;
	
	private View view;
	private WheelVerticalView wvParent;
	private WheelVerticalView wvChild;
	 // Scrolling flag
    private boolean scrolling = false;
    
    private String[] one = { "辽宁", "黑龙江", "吉林","内蒙古","新疆","青海","西藏","甘肃","宁夏","陕西","山西","河北","山东","江苏","安徽","河南","河北","四川","云南","贵州","湖南","江西","浙江","福建","广东","广西","海南","台湾","北京","天津","上海","重庆","香港","澳门"};
    private String[][] two = {{ "沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "铁岭", "朝阳", "盘锦", "葫芦岛"  }
                             ,{ "哈尔滨", "齐齐哈尔", "鸡西", "鹤岗", "双鸭山", "大庆", "伊春", "佳木斯", "牡丹江", "七台河", "黑河" }
                             ,{ "长春", "吉林" ,"四平", "辽源", "通化", "白山", "松原", "白城", "延吉"}
                             ,{ "呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯", "呼伦贝尔", "巴彦淖尔", "乌兰察布", "乌兰浩特", "二连浩特"}
                             ,{ "乌鲁木齐", "克拉玛依", "吐鲁番", "昌吉", "博乐", "库尔勒", "阿克苏", "阿图什", "喀什", "和田", "伊宁","塔城", "阿勒泰"}
                             ,{ "西宁"} ,{ "拉萨"}
                             ,{ "兰州", "嘉峪关", "金昌", "白银", "天水", "武威", "张掖", "平凉", "酒泉", "庆阳", "定西","陇南", "临夏", "合作"}
                             ,{ "银川", "石嘴山", "吴忠", "固原", "中卫"}
                             ,{ "西安", "铜川", "咸阳", "渭南", "延安", "汉中", "榆林", "安康", "商洛","宝鸡"}
                             ,{ "太原", "大同", "阳泉", "长治", "晋城", "朔州", "晋中", "运城", "忻州", "临汾", "吕梁"}
                             ,{ "石家庄", "唐山", "秦皇岛", "邯郸", "邢台", "保定", "承德", "沧州", "廊坊", "衡水"}
                             ,{ "济南", "青岛", "淄博", "枣庄", "东营", "烟台", "潍坊", "济宁", "泰安", "威海", "日照","莱芜", "临沂", "德州", "聊城","滨州", "菏泽"}
                             ,{ "南京", "无锡", "徐州", "常州", "苏州", "南通", "连云港", "淮安", "盐城", "扬州", "镇江","泰州", "宿迁"}
                             ,{ "合肥", "芜湖", "蚌埠", "淮南", "马鞍山", "淮北", "铜陵", "安庆", "黄山", "滁州", "阜阳","宿州", "巢湖", "六安", "毫州","池州", "宣城"}
                             ,{ "郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河","三门峡", "南阳", "商丘", "信阳","周口", "驻马店"}
                             ,{ "武汉", "黄石", "十堰", "宜昌", "襄樊", "鄂州", "荆门", "荆州", "黄冈", "咸宁", "随州","恩施", "孝感"}
                             ,{ "成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳", "广元", "遂宁", "内江", "乐山", "南充","眉山", "宜宾", "广安", "达州", "雅安","巴中", "资阳"}
                             ,{ "昆明", "曲靖", "玉溪", "保山", "博乐", "昭通", "丽江", "思茅", "临沧"}
                             ,{ "贵阳", "六盘水", "遵义", "安顺", "铜仁", "兴义", "毕节", "凯里", "都匀"}
                             ,{ "长沙", "株洲", "湘潭", "衡阳", "邵阳", "岳阳", "常德", "张家界", "益阳", "郴州", "永州","怀化", "娄底", "吉首"}
                             ,{ "南昌", "萍乡", "九江", "新余", "鹰潭", "赣州", "吉安", "宜春", "抚州", "上饶"}
                             ,{ "杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州", "丽水"}
                             ,{ "福州", "厦门", "莆田", "三明", "泉州", "漳州", "南平", "龙岩", "宁德"}
                             ,{ "广州", "韶关", "深圳", "珠海", "汕头", "佛山", "江门", "湛江", "茂名", "肇庆", "惠州","梅州", "河源", "阳江", "清远", "清远", "中山", "潮州", "揭阳","云浮"}
                             ,{ "南宁", "柳州", "桂林", "梧州", "北海", "防城港", "钦州", "贵港", "百色", "贺州", "河池","来宾","崇左"}
                             ,{ "海口", "三亚"}
                             ,{ "台北", "高雄", "台中"}
                             ,{ ""} ,{ ""},{ ""} ,{ ""},{ ""} ,{ ""}
                             
                             
                             
                             
                             
   };
    
	public CityWheelMain(Context context,View view) {
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
		ArrayWheelAdapter<String> oneAdapter = new ArrayWheelAdapter<String>(mContext,one);
		oneAdapter.setItemResource(R.layout.zxbd_wheel_item);
		oneAdapter.setItemTextResource(R.id.name);
		wvParent.setViewAdapter(oneAdapter);// 设置"年"的显示数据
		wvParent.setCyclic(false);// 可循环滚动
		wvParent.setCurrentItem(0);// 初始化时显示的数据
		wvParent.setVisibleItems(3);
		// 月
		ArrayWheelAdapter<String> twoAdapter = new ArrayWheelAdapter<String>(mContext,two[0]);
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
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(mContext, two[index]);
        adapter.setItemResource(R.layout.zxbd_wheel_item);
		adapter.setItemTextResource(R.id.name);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    public String getPro() {
    	return one[wvParent.getCurrentItem()];
    }
    
	public String getCity() {
		return two[wvParent.getCurrentItem()][wvChild.getCurrentItem()];
	}
	
	public void setProList(String[] one){
		this.one = one;
	}
	
	public void setCityList(String[][] two){
		this.two = two;
	}
}
