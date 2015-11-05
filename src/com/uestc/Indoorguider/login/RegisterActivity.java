package com.uestc.Indoorguider.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends APPActivity {

	/**
	 * @param args
	 */
    private EditText userName, userPw,userPwConfirm;
    private ImageButton backBut ;
    private Button registerBut;
    private String userid;
    private String userpw;
    protected void handleResult(JSONObject obj)
	 {
		
		  try {
			switch(obj.getInt("typecode"))
			  {
			    //注册成功
			    case Constant.REGISTER_SUCCESS:
			    	 String hint1 = "恭喜，注册成功！";
				     Toast.makeText(RegisterActivity.this, hint1, Toast.LENGTH_LONG).show();
			    	 Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
					 startActivity(intent);
					 RegisterActivity.this.finish();
					 //overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);//切换效果
			    break;
			    //用户名已被注册
			    case Constant.REGISTER_ERROR_REUES:
			    	//获取消息中的数据
			    	String hint ="抱歉，该用户名已被注册!";
				    Toast.makeText(RegisterActivity.this, hint, Toast.LENGTH_LONG).show();
			    break;
			  }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText("用户注册");
		userName = (EditText) findViewById(R.id.r_username);
		userPw = (EditText) findViewById(R.id.r_pw);
		userPwConfirm = (EditText) findViewById(R.id.r_confirm_ps);
		backBut = (ImageButton) findViewById(R.id.back_icon);
		
		registerBut = (Button)findViewById(R.id.register);
		registerBut.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//获取输入信息
				 userid=userName.getText().toString();
				 userpw=userPw.getText().toString();
				 String userpwConfirm = userPwConfirm.getText().toString();
				//判断登录密码和权限
				 if(userid.equals(""))
				  {
					 Toast.makeText(RegisterActivity.this, "请输入用户名!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				   if(userpw.equals(""))
				  {
					  Toast.makeText(RegisterActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				  if(userpwConfirm.equals(""))
				  {
					  Toast.makeText(RegisterActivity.this, "请再次输入密码!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				  if(!userpwConfirm.equals(userpw))
				  {
					  
					  Toast.makeText(RegisterActivity.this, "两次密码设置不一致!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				  
				 //注册
				   register();
			}
			
		});
	}
	 
	public void register()
	{
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if(ConnectTool.checkConnect(this, wifiManager)){
			JSONObject obj = new JSONObject();
			try {
				obj.put("typecode", Constant.REGISTER_REQUEST_NAME);
			    obj.put("username", userid);
				obj.put("password",userpw);
				Handler handler = SendToServerThread.getHandler();
				if(handler!= null)
				{
					Message msg = handler.obtainMessage();
					msg.obj = obj;
					handler.sendMessage(msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	 		
			
		}
	 }
	
    public void onClick(View v){
    	this.finish();
    }
	
}
