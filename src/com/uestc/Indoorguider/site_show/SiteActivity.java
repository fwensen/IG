package com.uestc.Indoorguider.site_show;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.R.drawable;
import com.uestc.Indoorguider.R.id;
import com.uestc.Indoorguider.R.layout;
import com.uestc.Indoorguider.R.string;
import com.uestc.Indoorguider.map.MapActivity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;

public class SiteActivity extends Activity implements OnClickListener{

	ListView  siteList ;
	LinearLayout layout1,ticketOffice,ticketOffice1,layout_site_row1,layout_site_row2,layout_site_row3;
	private ImageView buttImage;
	private TextView buttText ;
	LinearLayout[] linearLayout;
	Boolean flag_ticket = false;
	ListView listView;
	
	
	public final static int RESULT_CODE = 0;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.site_activity);
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText("站内服务");
		layout_site_row1 = (LinearLayout) findViewById(R.id.site_row1);
		layout_site_row2 = (LinearLayout) findViewById(R.id.site_row2);
		layout_site_row3 = (LinearLayout) findViewById(R.id.site_row3);
		
		int width = MapActivity.windowWidth/3;
		int imgWidth = (int) (width*0.4);
		android.widget.LinearLayout.LayoutParams imgParam= new LinearLayout.LayoutParams(imgWidth,imgWidth);
		
		for(int i = 0 ;i<3;i++)
		{
			LinearLayout LL = new LinearLayout(this);
			LL.setId(11+i);
			LL.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
			LL.setOrientation(LinearLayout.VERTICAL);
			LL.setBackgroundColor(Color.TRANSPARENT);
			LL.setGravity(Gravity.CENTER_HORIZONTAL);
			//设置image
			buttImage =new ImageView(this);
			buttImage.setId(100+i);
			buttImage.setImageResource(R.drawable.site11+i);
			buttImage.setBackgroundColor(Color.TRANSPARENT);
		    LL.addView(buttImage);
			//设置text
			buttText = new TextView(this); 
			buttText.setLayoutParams(new LinearLayout.LayoutParams(width,LayoutParams.WRAP_CONTENT));
			
			buttText.setText(R.string.image_text_butt11+i);
			buttText.setTextSize(12);
			buttText.setTextColor(Color.BLACK);
			buttText.setGravity(Gravity.CENTER_HORIZONTAL);
			buttText.setBackgroundColor(Color.TRANSPARENT);
			LL.addView(buttText);
			
			LL.setOnClickListener(this);
			layout_site_row1.addView(LL);
		}
		
		for(int i = 0 ;i<3;i++)
		{
			LinearLayout LL = new LinearLayout(this);
			LL.setId(21+i);
			LL.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
			LL.setOrientation(LinearLayout.VERTICAL);
			LL.setBackgroundColor(Color.TRANSPARENT);
			LL.setGravity(Gravity.CENTER_HORIZONTAL);
			
			//设置image
			buttImage =new ImageView(this);
			buttImage.setId(103+i);
			buttImage.setImageResource(R.drawable.site21+i);
			buttImage.setBackgroundColor(Color.TRANSPARENT);
		    LL.addView(buttImage);
			//设置text
			buttText = new TextView(this); 
			buttText.setLayoutParams(new LinearLayout.LayoutParams(width,LayoutParams.WRAP_CONTENT));
			buttText.setText(R.string.image_text_butt21+i);
			buttText.setTextSize(12);
			buttText.setTextColor(Color.BLACK);
			buttText.setGravity(Gravity.CENTER_HORIZONTAL);
			buttText.setBackgroundColor(Color.TRANSPARENT);
			
			LL.addView(buttText);
			LL.setOnClickListener(this);
			
			layout_site_row2.addView(LL);
		}
		for(int i = 0 ;i<3;i++)
		{
			LinearLayout LL = new LinearLayout(this);
			LL.setId(31+i);
			LL.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
			LL.setOrientation(LinearLayout.VERTICAL);
			LL.setBackgroundColor(Color.TRANSPARENT);
			LL.setGravity(Gravity.CENTER_HORIZONTAL);
			//设置image
			buttImage =new ImageView(this);
			buttImage.setId(106+i);
			buttImage.setImageResource(R.drawable.site31+i);
			buttImage.setBackgroundColor(Color.TRANSPARENT);
		    LL.addView(buttImage);
			//设置text
			buttText = new TextView(this); 
			buttText.setLayoutParams(new LinearLayout.LayoutParams(width,LayoutParams.WRAP_CONTENT));
			buttText.setText(R.string.image_text_butt31+i);
			buttText.setTextSize(12);
			buttText.setTextColor(Color.BLACK);
			buttText.setGravity(Gravity.CENTER_HORIZONTAL);
			buttText.setBackgroundColor(Color.TRANSPARENT);
			
			LL.addView(buttText);
			LL.setOnClickListener(this);
			
			layout_site_row3.addView(LL);
		}
		for(int i=0;i<9;i++ )
		{
			
				ImageView  img = (ImageView) findViewById(100+i);
				img.setLayoutParams(imgParam);
		}
		
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i ;
		switch(v.getId())
		{
			case 11:
				
			i = new Intent(SiteActivity.this,MapActivity.class);
			startActivity(i);
			this.finish();
				break;
			case 12:
				break;
			case 13:
				break;
			case 21:
				break;
			case 22:
				break;
			case 23:
				break;
			case 31:
				break;
			case 32:
				//餐饮购物
				i = new Intent(SiteActivity.this,SiteItemActivity.class);
				startActivity(i);
				break;
			case 33:
				//旅客服务
				i = new Intent(SiteActivity.this,SiteItemActivity.class);
				startActivity(i);
				break;
		}
		
		
	}

	
	private void calculateViewParams()
	{
		
	}

}
