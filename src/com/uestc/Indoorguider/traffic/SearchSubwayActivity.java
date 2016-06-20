package com.uestc.Indoorguider.traffic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.traffic.utils.SubwayAdapter;

public class SearchSubwayActivity extends Activity{
	
	ListView mListView; 
	List<SubwayLine> subwayLines;
	SubwayAdapter mAdaper;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_subway_show);
		initViews();
	}
	 
	 
	 
	private void initViews() {
		mListView = (ListView) findViewById(R.id.subway_list);
		subwayLines = new ArrayList<SubwayLine>();
		prepareData();
		mAdaper = new SubwayAdapter(this, subwayLines, R.layout.subway_line_item);
		mListView.setAdapter(mAdaper);
		
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Bundle data = new Bundle();
				data.putInt("line_no", position);
				Intent intent = new Intent(SearchSubwayActivity.this, ShowSubwayDetails.class);
				intent.putExtras(data);
				//启动intent对应的Activity
				startActivity(intent);
			}
			
		});
	}
	
	/**
	 * 准备地铁线路数据
	 */
	private void prepareData() {
		
		 subwayLines.add(new SubwayLine(1, "1号线首末车时刻表", "往四惠东方向", "往苹果园方向"));
		 subwayLines.add(new SubwayLine(2, "2号线首末车时刻表", "外环(西直门-复兴门-东直门-西直门)", "内环(积水潭-东直门-复兴门-积水潭)"));
		 subwayLines.add(new SubwayLine(3, "4号线/大兴线首末车时刻表", "上行", "下行"));
		 subwayLines.add(new SubwayLine(4, "5号线首末车时刻表", "开往天通苑北方向", "开往宋家庄方向"));
		 subwayLines.add(new SubwayLine(5, "6号线首末车时刻表", "开往海淀五路居方向", "开往潞城方向"));
		 subwayLines.add(new SubwayLine(6, "7号线首末车时刻表", "开往北京西站方向", "开往焦化厂站方向"));
		 subwayLines.add(new SubwayLine(7, "8号线首末车时刻表", "开往南锣鼓巷方向", "开往朱辛庄方向"));
		 subwayLines.add(new SubwayLine(8, "9号线首末车时刻表", "开往郭公庄方向", "开往国家图书馆方向"));
		 subwayLines.add(new SubwayLine(9, "10号线首末车时刻表", "下行(内环)巴沟-国贸-宋家庄-车道沟方向", "上行(外环)车道沟-宋家庄-国贸-巴沟方向"));
		 subwayLines.add(new SubwayLine(10, "13号线首末车时刻表", "往西直门 往东直门 往西直门", "往东直门 往霍营站 往回龙观站"));
		 subwayLines.add(new SubwayLine(11, "14号线(西段)首末车时刻表", "开往西局方向", "开往张郭庄方向"));
		 subwayLines.add(new SubwayLine(12, "14号线东段(含中段)首末车时刻表", "开往善各庄方向", "开往北京南站方向"));
		 subwayLines.add(new SubwayLine(13, "15号线首末车时刻表", "开往清华东路西口方向", "开往俸伯方向"));
		 subwayLines.add(new SubwayLine(14, "八通线首末车时刻表", "四惠→土桥", "土桥→四惠"));
		 subwayLines.add(new SubwayLine(15, "昌平线首末车时刻表", "往西二旗方向 往昌平西山口方向", "往西二旗方向 往昌平西山口方向"));
		 subwayLines.add(new SubwayLine(16, "亦庄线首末车时刻表", "往次渠方向", "往宋家庄方向"));
		 subwayLines.add(new SubwayLine(17, "4号线/大兴线首末车时刻表", "上行", "下行"));
		 subwayLines.add(new SubwayLine(18, "房山线首末车时刻表", "往苏庄方向", "往郭公庄方向"));
		 subwayLines.add(new SubwayLine(19, "机场线首末车时刻表", "上行→往市区", "下行→往机场"));
	}
}
