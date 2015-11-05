package com.uestc.Indoorguider.ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

public class TicketRequestActivity extends Activity implements OnItemClickListener{

	/**
	 * @param args
	 * 票务信息展示页面
	 */
	private TabHost  tabHost ;
	private ListView listView1,listView2,listView3;//1-train,2-plane,3-bus
	JSONObject obj;
	Bundle b =new Bundle();
	SimpleAdapter adapter1;
	/*public Handler ticket_handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
		    obj = (JSONObject) msg.obj;
		    if(msg.what == Constant.INQUIRE_TICKET_SUCCESS)
		    {
			//1-train
			//适配器                   context,布局文件（系统内置），数据源
		    adapter1= new SimpleAdapter(TicketRequestActivity.this,getData(1,obj),
		             R.layout.singlebus,
		             new String[]{"car_num",
		                          "from","start_time",
		                          "dur",
		                          "to","end_time",
		                          "business","class1","class2"},
		             new int[]{R.id.s_num,
		                       R.id.s_from,R.id.s_start_time,
		                       R.id.s_dur,
		                       R.id.s_to,R.id.s_end_time,
		                       R.id.s_business,R.id.s_class1,R.id.s_class2}
		             );
			listView1.setAdapter(adapter1);
			listView1.setOnItemClickListener(TicketRequestActivity.this);
			
			//2-plane
			SimpleAdapter adapter2= new SimpleAdapter(TicketRequestActivity.this,getData(2,obj),
		            R.layout.singlebus,
		            new String[]{"car_num",
		                         "from","start_time",
		                         "dur",
		                         "to","end_time",
		                         "business","class1","class2"},
		            new int[]{R.id.s_num,
		                      R.id.s_from,R.id.s_start_time,
		                      R.id.s_dur,
		                      R.id.s_to,R.id.s_end_time,
		                      R.id.s_business,R.id.s_class1,R.id.s_class2}
		            );
			listView2.setAdapter(adapter2);
					
			//3-bus
			SimpleAdapter adapter3= new SimpleAdapter(TicketRequestActivity.this,getData(3,obj),
					R.layout.singlebus,
					new String[]{"car_num",
					             "from","start_time",
					             "dur",
					             "to","end_time",
					             "business","class1","class2"},
				   new int[]{R.id.s_num,
					         R.id.s_from,R.id.s_start_time,
					         R.id.s_dur,
					         R.id.s_to,R.id.s_end_time,
					         R.id.s_business,R.id.s_class1,R.id.s_class2}
					        );
			listView3.setAdapter(adapter3);
		  }else if(msg.what == Constant.ERROR_INFORMATION)
  		{
  			String hint = msg.getData().getString("err_inf");
  		    Toast.makeText(TicketRequestActivity.this, hint, Toast.LENGTH_LONG).show();
  		}
		}
	};
	
	
	*/
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_request);
		
		
		
		
	}
	
	private List<Map<String,Object>> getData(int i, JSONObject obj) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>> ();
		
		switch(i){
		case 1:
			try {
				JSONArray train = obj.getJSONArray("train");
				for(int k = 0;k<train.length();k++)
					{   Map<String,Object> map= new HashMap<String,Object>();
						map.put("car_num",train.getJSONObject(k).getString("car_num"));
						map.put("from",train.getJSONObject(k).getString("from"));
						map.put("to",train.getJSONObject(k).getString("to"));
						map.put("start_time",train.getJSONObject(k).getString("go"));
						map.put("end_time",train.getJSONObject(k).getString("end"));
						map.put("dur",train.getJSONObject(k).getString("dur"));
						map.put("class1",train.getJSONObject(k).getInt("class1"));
						map.put("class2",train.getJSONObject(k).getInt("class2"));
						map.put("business",train.getJSONObject(k).getInt("class3"));
						list.add(map);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
					
			break;
		case 2:
			break;
			
		case 3:
			break;
			
		}

		return list ;
	}
	

	public void onItemClick(AdapterView<?> parent, View view,
			int p, long id) {
		if( parent == listView1 )
		{
			try {
				JSONObject train_p = obj.getJSONArray("train").getJSONObject(p);
				b.putString("car_num", train_p.getString("car_num"));
				b.putString("car_exit",train_p.getString("car_exit"));
				b.putString("go",train_p.getString("go") );
				b.putString("end", train_p.getString("end"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if( parent == listView2 )
		{
			
		}
		else if( parent == listView3 )
		{
			
		}
		
	}
	
	
}
