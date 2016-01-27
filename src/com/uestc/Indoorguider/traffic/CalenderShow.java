package com.uestc.Indoorguider.traffic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.uestc.Indoorguider.R;

public class CalenderShow extends Activity{

	CalendarView cv;
	private int year;
	private int month;
	private int dayOfMonth;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {  
	 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calendar_main);
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText("出行日期");
		
		cv = (CalendarView) findViewById(R.id.calendarView);
		cv.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
			
				Toast.makeText(CalenderShow.this, "您选择的是: "+year+" : " + (month+1) + " : " + dayOfMonth, Toast.LENGTH_LONG).show();
				// TODO Auto-generated method stub
				year = year;
				month = month + 1;
				dayOfMonth = dayOfMonth;
				
				Intent intent = getIntent();
				intent.putExtra("year", year);
				intent.putExtra("month", month);
				intent.putExtra("day", dayOfMonth);
				CalenderShow.this.setResult(0, intent);
				CalenderShow.this.finish();
				//Intent i = new Intent(CalenderShow.this, SearchTrain.class);
				//startActivity(i);
				//finish();
			}
		});
	
	}
	
}
