package com.uestc.Indoorguider.traffic;

import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.traffic.utils.Common;
import com.uestc.Indoorguider.traffic.utils.TicketShowListAdapter;

public class TrainTicketResultShow extends Activity{

	ListView listView;
	List<Map<String, Object>> ticketValueList;
	SearchTicket search;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {  
	 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE) ;
		setContentView(R.layout.train_ticket_show);
		listView = (ListView) findViewById(R.id.train_ticket_listView);
		
		final String type = "Train";
		final String startPlace = null;
		final String endPlace = null;
		final String startDate = null;
		search = SearchTicket.getInstance();
		
		new Thread() {
			@Override
			public void run() {
				try {
					
					ticketValueList = search.getResult(type, startPlace, endPlace, startDate);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				handler.sendEmptyMessage(0);
			}
		}.start();
		
		
		
	}
	/**
	 * 处理网络后的UI
	 */
	public final Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			DoHandler(msg.what);
		}
	};
	
	void DoHandler(int msgWhat) {
		switch (msgWhat) {
		case 0:
			if (ticketValueList != null && ticketValueList.size() != 0) {
				// 取得了
				TicketShowListAdapter adapter = new TicketShowListAdapter(
						getApplicationContext(), ticketValueList);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Map<String, Object> map = ticketValueList.get(arg2);
						LayoutInflater inflater = LayoutInflater
								.from(TrainTicketResultShow.this);
						View view = inflater.inflate(
								R.layout.train_ticket_detail, null);
						WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
						int height = wm.getDefaultDisplay().getHeight();// 屏幕宽度
						final PopupWindow pop = new PopupWindow(view,
								LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
								true);
						TextView tv_cc = (TextView) view.findViewById(R.id.cc);
						TextView tv_sfsj = (TextView) view
								.findViewById(R.id.sfsj);
						TextView tv_ddsj = (TextView) view
								.findViewById(R.id.ddsj);
						TextView tv_qchs = (TextView) view
								.findViewById(R.id.qchs);
						TextView tv_swz = (TextView) view
								.findViewById(R.id.swz);
						TextView tv_tdz = (TextView) view
								.findViewById(R.id.tdz);
						TextView tv_ydz = (TextView) view
								.findViewById(R.id.ydz);
						TextView tv_edz = (TextView) view
								.findViewById(R.id.edz);
						TextView tv_gjrw = (TextView) view
								.findViewById(R.id.gjrw);
						TextView tv_rw = (TextView) view.findViewById(R.id.rw);
						TextView tv_yw = (TextView) view.findViewById(R.id.yw);
						TextView tv_rz = (TextView) view.findViewById(R.id.rz);
						TextView tv_yz = (TextView) view.findViewById(R.id.yz);
						TextView tv_wz = (TextView) view.findViewById(R.id.wz);
						TextView tv_qt = (TextView) view.findViewById(R.id.qt);
						tv_cc.setText(Common.ObjectToString(map.get("车次")));
						tv_sfsj.setText(Common.ObjectToString(map.get("出发时间")));
						tv_ddsj.setText(Common.ObjectToString(map.get("到达时间")));
						tv_qchs.setText(Common.ObjectToString(map.get("全程耗时")));
						tv_swz.setText(Common.ObjectToString(map.get("商务座")));
						tv_tdz.setText(Common.ObjectToString(map.get("特等座")));
						tv_ydz.setText(Common.ObjectToString(map.get("一等座")));
						tv_edz.setText(Common.ObjectToString(map.get("二等座")));
						tv_gjrw.setText(Common.ObjectToString(map.get("高级卧铺")));
						tv_rw.setText(Common.ObjectToString(map.get("软卧")));
						tv_yw.setText(Common.ObjectToString(map.get("硬卧")));
						tv_rz.setText(Common.ObjectToString(map.get("软座")));
						tv_yz.setText(Common.ObjectToString(map.get("硬座")));
						tv_wz.setText(Common.ObjectToString(map.get("无座")));
						tv_qt.setText(Common.ObjectToString(map.get("其他")));
						// 需要设置一下此参数，点击外边可消失
						pop.setBackgroundDrawable(new BitmapDrawable());
						// 设置点击窗口外边窗口消失
						pop.setOutsideTouchable(true);
						// 设置此参数获得焦点，否则无法点击
						pop.setFocusable(true);
						//pop.setAnimationStyle(R.style.AnimBottom) ;
						pop.showAtLocation(TrainTicketResultShow.this.findViewById(R.id.train_ticketshowlayout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
						
					}

				
				});
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查检索条件!",
						Toast.LENGTH_SHORT).show();
				this.finish();
			}
			break;
		}
		
	}
	
	
	void initViews(){
		
		
		
		
		
	}	
	
}
