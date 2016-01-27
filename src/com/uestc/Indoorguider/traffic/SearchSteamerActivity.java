package com.uestc.Indoorguider.traffic;

import com.uestc.Indoorguider.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SearchSteamerActivity extends Activity{

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_traffic_steamer);
		
	}
}
