package com.uestc.Indoorguider.traffic;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.uestc.Indoorguider.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

public class SearchPlaneActivity extends Activity{

	/**
	 * 日、月、年
	 */
	private EditText tiketDay,tiketMonth,tiketYear;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_traffic_plane);
		initViews();
	}
	
	private void initViews() {
		tiketDay = (EditText) findViewById(R.id.ticket_plane_day);
		tiketMonth = (EditText) findViewById(R.id.ticket_plane_month);
		tiketYear = (EditText) findViewById(R.id.ticket_plane_year);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	    String date = df.format(new Date());// new Date()为获取当前系统时间
	    String[] date1 = date.split("-");
			
	   	tiketYear.setText(date1[0]);
		tiketMonth.setText(date1[1]);
		tiketDay.setText(date1[2]);
	}
	
}
