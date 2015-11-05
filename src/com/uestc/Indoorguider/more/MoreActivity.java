package com.uestc.Indoorguider.more;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.R.id;
import com.uestc.Indoorguider.R.layout;
import com.uestc.Indoorguider.history.HistoryRecorder;
import com.uestc.Indoorguider.login.LoginActivity;
import com.uestc.Indoorguider.map.MapActivity;
import com.uestc.Indoorguider.util.ClientAgent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity implements OnClickListener{
	
	private LinearLayout history_lay, logout_lay,help_lay = null;
	private RelativeLayout layout_have_login = null;
	private LinearLayout layout_no_login = null;
	private ImageView image21,back_icon;
	private TextView setIP;
	public static String userName;
	public static String userImg;
	SharedPreferences mPrefrences;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_more);
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText("更多");
		mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
		
		layout_have_login = (RelativeLayout) findViewById(R.id.have_login_layout);
		layout_no_login = (LinearLayout) findViewById(R.id.no_login_layout);
		layout_no_login.setOnClickListener(this);
		help_lay = (LinearLayout) findViewById(R.id.help_lay);
		help_lay.setOnClickListener(this);
		history_lay  = (LinearLayout) findViewById(R.id.history_lay);
		history_lay.setOnClickListener(this);
		back_icon = (ImageView) findViewById(R.id.back_icon);
		back_icon.setOnClickListener(this);
		//setIP = (TextView) findViewById(R.id.setip);
		//setIP.setOnClickListener(this);
		//动态变化view组件
		android.view.ViewGroup.LayoutParams  params1 = layout_have_login.getLayoutParams();
        params1.height =(int) (MapActivity.windowWidth*0.26) ;
        layout_have_login.setLayoutParams(params1);
        layout_have_login.setOnClickListener(this);
        android.view.ViewGroup.LayoutParams  params2 = layout_no_login.getLayoutParams();
        params2.height =(int) (MapActivity.windowWidth*0.26) ;
        layout_no_login.setLayoutParams(params2);
        //用户头像
        int userImgWidth = (int) (MapActivity.windowWidth*0.2);
        ImageView img2 = (ImageView) findViewById(R.id.have_log_img);
        ImageView img = (ImageView) findViewById(R.id.no_log_img);
        img2.setLayoutParams(new RelativeLayout.LayoutParams(userImgWidth,userImgWidth));
        img.setLayoutParams(new LinearLayout.LayoutParams(userImgWidth,userImgWidth));
        //历史条
        android.view.ViewGroup.LayoutParams  params4 = history_lay.getLayoutParams();
        params4.height =(int) (MapActivity.windowWidth*0.14) ;
        history_lay.setLayoutParams(params4);
        ImageView historyimg = (ImageView) findViewById(R.id.history_img);
        android.view.ViewGroup.LayoutParams  params5 = historyimg.getLayoutParams();
        params5.width = params5.height =(int) (MapActivity.windowWidth*0.1) ;
        historyimg.setLayoutParams(params5);
		mPrefrences.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener(){

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				
					initLoginBar();
					
					
				}
			});
		

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		//不需要登录的功能
		switch(v.getId())
		{
			case R.id.help_lay:
				i = new Intent(this, APPHelpActivity.class);
				startActivity(i);
				return;
			case R.id.notice_lay:
				return;
			case R.id.no_login_layout:
				i = new Intent(this, LoginActivity.class);
				startActivity(i);
				return;
			case R.id.have_login_layout:
				i = new Intent(this, MyActivity.class);
				startActivity(i);
				return;
			case R.id.back_icon:
				i = new Intent(this, MapActivity.class);
				this.finish();
				startActivity(i);
				return;
			case R.id.setip:
				EditText ip = (EditText) findViewById(R.id.myip);
				ClientAgent.ip  = ip.getText().toString();
		}
		
		//for test**************************
		if(!mPrefrences.getBoolean("HaveLogin", false))
		{
			Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
			//跳转到登录页面
			i = new Intent(this, LoginActivity.class);
			startActivity(i);
			return;
		}
		//登录后才能使用的功能
		switch(v.getId())
		{
			case R.id.history_lay:
				i = new Intent(this, HistoryRecorder.class);
				//this.finish();
				startActivity(i);
				break;
		
		}	
	}
	@Override
	public void onResume()
	{
		super.onResume();
		initLoginBar();
	}
	
   private void  initLoginBar()
   {
	   if(mPrefrences.getBoolean("HaveLogin", false))
		{
			layout_have_login.setVisibility(View.VISIBLE);
			layout_no_login.setVisibility(View.GONE);
			TextView name = (TextView) findViewById(R.id.login_username);
			userName = mPrefrences.getString("UserName", "蒙面侠");
			name.setText(userName);
			//userImg  = 
			TextView score = (TextView) findViewById(R.id.login_score);
			score.setText(mPrefrences.getString("Score", "0"));
		}
		else
		{
			layout_have_login.setVisibility(View.GONE);
			layout_no_login.setVisibility(View.VISIBLE);
		}
   }

   
   //********************设置IP的菜单******************************
   final int SETTING_IP = 0x111;
   @Override 
   public boolean onCreateOptionsMenu(Menu menu) {
	   menu.add(0, SETTING_IP, 0, "设置IP");
	   //Toast.makeText(this, "click menu key", Toast.LENGTH_SHORT).show();
	   return super.onCreateOptionsMenu(menu);
   }
   
   @Override 
   public boolean onOptionsItemSelected(MenuItem mi) {
	   //View view = LayoutInflater.from(this).inflate(R.layout.activity_my, null);
		//setIP = (TextView) view.findViewById(R.id.setip);
		final EditText text;
		switch(mi.getItemId()) {		
		case SETTING_IP:
			Builder dialog = new AlertDialog.Builder(MoreActivity.this);
					dialog.setTitle("设置IP");
					dialog.setView(
					text =  new EditText(this){	
					} );
					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 String IP= text.getText().toString();
							 if(isIP(IP)) {
								 ClientAgent.ip  = IP;
								 Toast.makeText(MoreActivity.this, "手动设置IP成功", Toast.LENGTH_LONG).show();
							 } else {							
								 Toast.makeText(MoreActivity.this, "不是合法的IP", Toast.LENGTH_LONG).show();
							 }
						}					
					});
					dialog.setNegativeButton("取消", null);
					dialog.create().show();
			break;			
		}
		return true;
	}
   
 //判断是否是一个IP 
   public static boolean isIP(String IP){
       boolean b = false; 
       //IP = trimSpaces(IP); 
       if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){ 
           String s[] = IP.split("\\."); 
           if(Integer.parseInt(s[0])<255 && Integer.parseInt(s[1])<255 && Integer.parseInt(s[2])<255
        		   && Integer.parseInt(s[3])<255) 
                       b = true; 
       } 
       return b; 
   }
   
	 //**************手动设置IP************************* 
   }
   