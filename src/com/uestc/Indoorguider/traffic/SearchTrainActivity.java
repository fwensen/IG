package com.uestc.Indoorguider.traffic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uestc.Indoorguider.R;

public class SearchTrainActivity extends Activity implements OnClickListener{

	
	AutoCompleteTextView startPlaceEdit;
	AutoCompleteTextView destPlaceEdit;
	//TextView dateText;
	Calendar calendar;
	Button searchBtn;
	//LinearLayout selectdateBtn;
	//Button changeBtn;
	private EditText tiketDay,tiketMonth,tiketYear;
	 //onCreate()
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_traffic_train);
		initViews();
	}
    
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	
    	if (requestCode == 0 && resultCode == 0) {
    		Bundle data = intent.getExtras();
    		int year = data.getInt("year");
    		int month = data.getInt("month");
    		int dayOfMonth = data.getInt("day");
    		
    		//dateText.setText(year+"-"+month+"-"+dayOfMonth);
    	}
    }
    
    private void initViews() {
    	
    	startPlaceEdit = (AutoCompleteTextView) findViewById(R.id.train_startplace_edit);
		destPlaceEdit = (AutoCompleteTextView)  findViewById(R.id.train_destplace_edit);
		
		tiketDay = (EditText) findViewById(R.id.ticket_train_day);
		tiketMonth = (EditText) findViewById(R.id.ticket_train_month);
		tiketYear = (EditText) findViewById(R.id.ticket_train_year);
		

	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	    String date = df.format(new Date());// new Date()为获取当前系统时间
	    String[] date1 = date.split("-");
			
	   	tiketYear.setText(date1[0]);
		tiketMonth.setText(date1[1]);
		tiketDay.setText(date1[2]);
		//dateText = (TextView) findViewById(R.id.train_datetextview);
		searchBtn = (Button) findViewById(R.id.train_search_btn);
		//selectdateBtn = (LinearLayout)  findViewById(R.id.train_selectdatebtn);
		//changeBtn = (Button) findViewById(R.id.train_changebtn);

		// 添加事件监听
		searchBtn.setOnClickListener(this);
		//changeBtn.setOnClickListener(this);
		
		/*
		Common.changeBtnBackground(searchBtn, new int[] { R.drawable.searchbtn,
				R.drawable.searchbtn_pressed });
		Common.changeBtnBackground(changeBtn, new int[] { R.drawable.searchbtn,
				R.drawable.searchbtn_pressed });
    	*/
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(searchBtn)) {
			
			String startPlace = startPlaceEdit.getText().toString();
			String endPlace = destPlaceEdit.getText().toString();
			
			String day = tiketDay.getText().toString();
			String month = tiketMonth.getText().toString();
			String year = tiketYear.getText().toString();
			 
			if("".equals(year)||year==null)
			{
				Toast.makeText(getApplicationContext(), "请输入年份!", Toast.LENGTH_SHORT).show();
				return;
			}
			if("".equals(month)||month==null){
				Toast.makeText(getApplicationContext(), "请输入月份!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(month.length()<2) {
				month = "0"+month;
				tiketMonth.setText(month);
			}
			
			if("".equals(day)||day==null) {
				Toast.makeText(getApplicationContext(), "请输入几号!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(day.length()<2) {
				day = "0"+day;
				tiketDay.setText(day);
			}
			Intent intent = new Intent(SearchTrainActivity.this, TrainTicketResultShow.class);
			Bundle data	 = new Bundle();
			data.putString("type", "Train");
			data.putString("startPlace", startPlace);
			data.putString("endPlace", endPlace);
			data.putString("day", day);
			data.putString("month", month);
			data.putString("year", year);
			intent.putExtras(data);
			startActivity(intent);
			
		} /*else if (v.equals(changeBtn) ){
			
			if (TextUtils.isEmpty(destPlaceEdit.getText())) {

				Toast.makeText(SearchTrainActivity.this, "目的地地址为空", Toast.LENGTH_SHORT)
						.show();
			} else if (TextUtils.isEmpty(startPlaceEdit.getText())) {
				Toast.makeText(SearchTrainActivity.this, "出发地地址为空", Toast.LENGTH_SHORT)
						.show();
			}
			// 交换出发地和目的地
			CharSequence[] charSequences = new CharSequence[] {
					destPlaceEdit.getText(), startPlaceEdit.getText() };
			startPlaceEdit.setText("");
			destPlaceEdit.setText("");
			startPlaceEdit.setText(charSequences[0]);
			destPlaceEdit.setText(charSequences[1]);
			
		}*/
	}
	
}
