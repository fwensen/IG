package com.uestc.Indoorguider.traffic;

import java.util.Calendar;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uestc.Indoorguider.R;

public class SearchTrainActivity extends Activity implements OnClickListener{

	
	AutoCompleteTextView startPlaceEdit;
	AutoCompleteTextView destPlaceEdit;
	TextView dateText;
	Calendar calendar;
	Button searchBtn;
	LinearLayout selectdateBtn;
	//DialogTool dialogTool;
	Button changeBtn;
	 //onCreate()
    public void onCreate(Bundle savedInstanceState)
	{
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
    		dateText.setText(year+"-"+month+"-"+dayOfMonth);
    	}
    }
    
    private void initViews() {
    	
    	startPlaceEdit = (AutoCompleteTextView) findViewById(R.id.train_startplace_edit);
		destPlaceEdit = (AutoCompleteTextView)  findViewById(R.id.train_destplace_edit);
		dateText = (TextView) findViewById(R.id.train_datetextview);
		searchBtn = (Button) findViewById(R.id.train_search_btn);
		selectdateBtn = (LinearLayout)  findViewById(R.id.train_selectdatebtn);
		
		changeBtn = (Button) findViewById(R.id.train_changebtn);

		selectdateBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					selectdateBtn.setBackgroundColor(Color
							.parseColor("#1C86EE"));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					selectdateBtn.setBackgroundColor(Color.TRANSPARENT);

				}

				return false;
			}
		});
		selectdateBtn.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		// 添加事件监听
		searchBtn.setOnClickListener(this);
		changeBtn.setOnClickListener(this);
		selectdateBtn.setOnClickListener(this);
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
		if (v.equals(selectdateBtn)) {
			
			Intent intent = new Intent(SearchTrainActivity.this, CalenderShow.class);
			startActivityForResult(intent, 0);			
			
		} else if (v.equals(searchBtn)) {
			
			String startPlace = startPlaceEdit.getText().toString();
			String endPlace = destPlaceEdit.getText().toString();
			String startDate = dateText.getText().toString();
			
			Intent intent = new Intent(SearchTrainActivity.this, TrainTicketResultShow.class);
			Bundle data	 = new Bundle();
			data.putString("type", "Train");
			data.putString("startPlace", startPlace);
			data.putString("endPlace", endPlace);
			data.putString("startDate", startDate);
			intent.putExtras(data);
			startActivity(intent);
			
		} else if (v.equals(changeBtn) ){
			
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
			
		}
	}
	
}
