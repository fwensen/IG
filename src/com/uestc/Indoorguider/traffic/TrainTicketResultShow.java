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
	 * ����������UI
	 */
	public final Handler handler = new Handler() {
		@Override
		// ������Ϣ���ͳ�����ʱ���ִ��Handler���������
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			DoHandler(msg.what);
		}
	};
	
	void DoHandler(int msgWhat) {
		switch (msgWhat) {
		case 0:
			if (ticketValueList != null && ticketValueList.size() != 0) {
				// ȡ����
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
						int height = wm.getDefaultDisplay().getHeight();// ��Ļ���
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
						tv_cc.setText(Common.ObjectToString(map.get("����")));
						tv_sfsj.setText(Common.ObjectToString(map.get("����ʱ��")));
						tv_ddsj.setText(Common.ObjectToString(map.get("����ʱ��")));
						tv_qchs.setText(Common.ObjectToString(map.get("ȫ�̺�ʱ")));
						tv_swz.setText(Common.ObjectToString(map.get("������")));
						tv_tdz.setText(Common.ObjectToString(map.get("�ص���")));
						tv_ydz.setText(Common.ObjectToString(map.get("һ����")));
						tv_edz.setText(Common.ObjectToString(map.get("������")));
						tv_gjrw.setText(Common.ObjectToString(map.get("�߼�����")));
						tv_rw.setText(Common.ObjectToString(map.get("����")));
						tv_yw.setText(Common.ObjectToString(map.get("Ӳ��")));
						tv_rz.setText(Common.ObjectToString(map.get("����")));
						tv_yz.setText(Common.ObjectToString(map.get("Ӳ��")));
						tv_wz.setText(Common.ObjectToString(map.get("����")));
						tv_qt.setText(Common.ObjectToString(map.get("����")));
						// ��Ҫ����һ�´˲����������߿���ʧ
						pop.setBackgroundDrawable(new BitmapDrawable());
						// ���õ��������ߴ�����ʧ
						pop.setOutsideTouchable(true);
						// ���ô˲�����ý��㣬�����޷����
						pop.setFocusable(true);
						//pop.setAnimationStyle(R.style.AnimBottom) ;
						pop.showAtLocation(TrainTicketResultShow.this.findViewById(R.id.train_ticketshowlayout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
						
					}

				
				});
			} else {
				Toast.makeText(getApplicationContext(), "���ݻ�ȡʧ�ܣ������������!",
						Toast.LENGTH_SHORT).show();
				this.finish();
			}
			break;
		}
		
	}
	
	
	void initViews(){
		
		
		
		
		
	}	
	
}
