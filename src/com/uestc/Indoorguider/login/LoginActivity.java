package com.uestc.Indoorguider.login;
import org.json.JSONException;
import org.json.JSONObject;
import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.IndoorGuiderApplication;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.more.MoreActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/* 登陆界面，输入用户名和密码，*/
public class LoginActivity extends APPActivity 
{
	 public  SharedPreferences sp;
	 //显示错误对话框
	 public Dialog errorDialog;
	 public String errorMsg;
	 //记录登录人的id和密码
	 String username;
	 String userpw;
	 String did;//记录从SharedPreferences获取的记录的内容
	 Bundle b;
	 //需要使用的控件
	 EditText loginName;
	 EditText loginPw;
	 TextView registertText,title;
	 Button LoginBut;
	 boolean progressShow ;
	 ProgressDialog pd;
	 protected void handleResult(JSONObject obj)
	 {
		  try {
			  pd.dismiss();
			  switch(obj.getInt("typecode"))
			  {
			    //登录成功
			    case Constant.LOGIN_SUCCESS:
				     IndoorGuiderApplication.getInstance().setUserName(username);
				     IndoorGuiderApplication.getInstance().setPassword(userpw);
				     pd.dismiss();
				     IndoorGuiderApplication.getInstance().saveAlreadyLogin(true);
			    	 Intent intent=new Intent(LoginActivity.this,MoreActivity.class);
			    	 intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  
					 startActivity(intent);
					 LoginActivity.this.finish();
					 //overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);//切换效果
			    break;
			    
			    //密码错误
			    case Constant.LOGIN_ERROR_PS:
			    	//获取消息中的数据
			    	 pd.dismiss();
			    	 String hint ="密码错误！";
				     Toast.makeText(LoginActivity.this, hint, Toast.LENGTH_LONG).show();
			    break;
			    
			    //用户名未注册
			    case Constant.LOGIN_ERROR_NO:
			    	//获取消息中的数据
			    	 pd.dismiss();
			    	 String hint1 = "用户名不存在！";
				     Toast.makeText(LoginActivity.this, hint1, Toast.LENGTH_LONG).show();
			    break;
			    
			  }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	     

//	 	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
//	    	{
//	 		super.onActivityResult(requestCode, resultCode, data);
//	 		if(requestCode==2 && resultCode==2)
//	 		{
//	 			 Intent intent=new Intent(LoginActivity.this,SelectVegeActivity.class);
//	 			 startActivity(intent);
//	 			 LoginActivity.this.finish();
//	 		}
//	 	 }
//	     
	   @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
//		   this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	       this.getWindow().getDecorView().setSystemUiVisibility(4);
		   super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_login);
	       TextView title = (TextView) findViewById(R.id.title_text);
		   title.setText("用户登录");
		   sp = PreferenceManager.getDefaultSharedPreferences(this);
	     //  this.setFinishOnTouchOutside(false);
	        //获取控件
	        LoginBut=(Button)findViewById(R.id.ok);
	        loginName=(EditText)this.findViewById(R.id.loginid);
		    loginPw=(EditText) this.findViewById(R.id.loginpw);
			registertText = (TextView)findViewById(R.id.register);
			registertText.setOnClickListener(
					new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
					startActivity(i);
				}
				
			});
			
			//“登陆”按钮的监听
			LoginBut.setOnClickListener
	        (
	         new OnClickListener()
	           {
				 public void onClick(View v) 
				   {
					 //获取输入信息
						 username=loginName.getText().toString();
						 userpw=loginPw.getText().toString();
					     
						//判断登录密码和权限
						 if(username.equals(""))
						  {
							 Toast.makeText(LoginActivity.this, "请输入用户名!", Toast.LENGTH_SHORT).show(); 
						      return;
						  }
						   if(userpw.equals(""))
						  {
							  Toast.makeText(LoginActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show(); 
						      return;
						  }
						   
						 //登陆
						  progressShow = true;
						  pd = new ProgressDialog(LoginActivity.this);
						  pd.setCanceledOnTouchOutside(false);
						  pd.setOnCancelListener(new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								progressShow = false;
							}
						  });
						  pd.setMessage(getString(R.string.is_landing));
						  pd.show();
						  IndoorGuiderApplication.getInstance().login(username,userpw);
						 
				    }	
		       }        	
	         );
	      
	    
	  }
	   
	
	   
	   public void onClick(View v){
	    	this.finish();
	    }


	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void initContent() {
		// TODO Auto-generated method stub
		
	}
	  
}
