package com.uestc.Indoorguider.more;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.IndoorGuiderApplication;
import com.uestc.Indoorguider.IndoorGuiderManager;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.R.id;
import com.uestc.Indoorguider.R.layout;
import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyActivity extends APPActivity implements OnTouchListener {
	SharedPreferences mPrefrences;
	SharedPreferences.Editor editor ;
	private ImageView back_icon;
	@Override
	protected void handleResult(JSONObject obj) {
		// TODO Auto-generated method stub
		
			try {
				switch(obj.getInt("typecode"))
				  {
				    //退出成功
				    case Constant.LOGOUT_SUCCESS:
				    	editor.putBoolean("HaveLogin",false);
				    	Intent i = new Intent(this,MoreActivity.class);
				    	startActivity(i);
				    	this.finish();
				    	break;
					     
				  }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void onCreate(Bundle savedInstanceState)
	{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText("个人信息");
		back_icon = (ImageView) findViewById(R.id.back_icon);
		back_icon.setOnTouchListener(this);
		mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = mPrefrences.edit();
	    mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
	    EditText sex = (EditText) findViewById(R.id.my_sex);
	    sex.setText(mPrefrences.getString("Sex", ""));
		sex.setOnTouchListener(this);
		
		EditText age = (EditText) findViewById(R.id.my_age);
		age.setText(mPrefrences.getString("Age", ""));
	    age.setOnTouchListener(this);
	    
	    EditText email = (EditText) findViewById(R.id.my_email);
		email.setText(mPrefrences.getString("Email", ""));
		email.setOnTouchListener(this);
		
		EditText userName = (EditText) findViewById(R.id.my_username);
		userName.setOnTouchListener(this);
		userName.setText(mPrefrences.getString("UserName", ""));
		
		LinearLayout password = (LinearLayout) findViewById(R.id.my_password);
		password.setOnTouchListener(this);
		LinearLayout logout_lay = (LinearLayout) findViewById(R.id.logout_lay);
		logout_lay.setOnTouchListener(this);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.my_username:
				break;
			case R.id.logout_lay:
				IndoorGuiderApplication.getInstance().logout();
				break;
			case R.id.my_sex:
				break;
			case R.id.my_age:
				break;
			case R.id.my_email:
				break;
			case R.id.my_password:
				
				break;
            case R.id.back_icon:
            	Intent i = new Intent(this, MoreActivity.class);
				this.finish();
				startActivity(i);
				break;
			
		}
		return false;
	}
	
	
	
}
