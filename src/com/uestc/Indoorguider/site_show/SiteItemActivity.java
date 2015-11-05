package com.uestc.Indoorguider.site_show;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.map.MapActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class SiteItemActivity extends Activity implements OnItemClickListener{

	ListView  siteList ;
	LinearLayout layout1,ticketOffice,ticketOffice1,layout_site_row1;

	LinearLayout[] linearLayout;
	Boolean flag_ticket = false;
	ListView listView;
	final int siteNum =3;
	String[] site = new String[siteNum];
	
	int[] img = {R.drawable.ticketoffice,R.drawable.aototicket,R.drawable.exit2,R.drawable.wc2,R.drawable.supermarket2,R.drawable.shop,R.drawable.food};
		
	public final static int RESULT_CODE = 0;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.site_item_activity);

		layout1 = (LinearLayout) findViewById(R.id.layout1);
		listView = (ListView) findViewById(R.id.sitelist);
//		ticketOffice1 = (LinearLayout) findViewById(R.id.ticketOfficeLayout1);
//		ticketOffice = (LinearLayout) findViewById(R.id.ticketOfficeLayout);
		listView.setOnItemClickListener(this);
		site=getResources().getStringArray(R.array.shop);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.site, 
				 new String[] {"img","text"} ,//layout中的id
				 new int[] {R.id.img,R.id.site});//匹配的内容
		listView.setAdapter(adapter);
		
    }

	private List<? extends Map<String, ?>> getData() {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>> ();
		for(int i= 0; i < siteNum; i++)
		{
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("img", img[i]);
			map.put("text", site[i]);
			list.add(map);
		}
		
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
			
				Intent i = new Intent(SiteItemActivity.this,MapActivity.class);
				//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				i.putExtra("siteName",site[position]);
				setResult(RESULT_CODE, i);  
				this.finish();
			
		
		
		
	}
	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch(v.getId()){
//			case R.id.ticketOfficeLayout:
//				if(flag_ticket == false)
//				{
//					Button bt1 = new Button(this);
//					bt1.setText("最近");
//					bt1.setTag("ticket1");
//					bt1.setOnClickListener(myListener);
//					ticketOffice1.addView(bt1);
//					
//					Button bt2 = new Button(this);
//					bt2.setTag("ticket2");
//					bt2.setText("最快");
//					ticketOffice1.addView(bt2);
//					bt2.setOnClickListener(myListener);
//					
//					Button bt3 = new Button(this);
//					bt3.setTag("ticket3");
//					bt3.setOnClickListener(myListener);
//					bt3.setText("全部");
//					ticketOffice1.addView(bt3);
//					flag_ticket = true;
//					
//				}
//				else{
//					ticketOffice1.removeAllViews();
//					flag_ticket = false;
//					
//				}
//				
//				break;
//			case R.id.WCLayout:
//		}
//			
//				
//		
//		
//	}
//	private OnClickListener myListener = new OnClickListener(){
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			String Tag = (String) v.getTag();
//			if(Tag == "ticket1")
//			{
//				
//			}
//			else if(Tag == "ticket2")
//			{
//				
//			}
//			else if(Tag == "ticket3")
//			{
//				
//			}
//			
//		}
//		
//	};
//	
		
	

}
