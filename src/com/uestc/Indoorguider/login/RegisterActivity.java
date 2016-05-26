package com.uestc.Indoorguider.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.IndoorGuiderApplication;
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
    private Button registerBut;
    private String userid;
    private String userpw;
    protected void handleResult(JSONObject obj)
	 {
		
		  try {
			switch(obj.getInt("typecode"))
			  {
			    //ע��ɹ�
			    case Constant.REGISTER_SUCCESS:
			    	 String hint1 = "��ϲ��ע��ɹ���";
				     Toast.makeText(RegisterActivity.this, hint1, Toast.LENGTH_LONG).show();
			    	 Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
					 startActivity(intent);
					 RegisterActivity.this.finish();
					 //overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);//�л�Ч��
			    break;
			    //�û����ѱ�ע��
			    case Constant.REGISTER_ERROR_REUES:
			    	//��ȡ��Ϣ�е�����
			    	String hint ="��Ǹ�����û����ѱ�ע��!";
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
		title.setText("�û�ע��");
		userName = (EditText) findViewById(R.id.r_username);
		userPw = (EditText) findViewById(R.id.r_pw);
		userPwConfirm = (EditText) findViewById(R.id.r_confirm_ps);
		registerBut = (Button)findViewById(R.id.register);
		registerBut.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//��ȡ������Ϣ
				 userid=userName.getText().toString();
				 userpw=userPw.getText().toString();
				 String userpwConfirm = userPwConfirm.getText().toString();
				//�жϵ�¼�����Ȩ��
				 if(userid.equals(""))
				  {
					 Toast.makeText(RegisterActivity.this, "�������û���!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				   if(userpw.equals(""))
				  {
					  Toast.makeText(RegisterActivity.this, "����������!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				  if(userpwConfirm.equals(""))
				  {
					  Toast.makeText(RegisterActivity.this, "���ٴ���������!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				  if(!userpwConfirm.equals(userpw))
				  {
					  
					  Toast.makeText(RegisterActivity.this, "�����������ò�һ��!", Toast.LENGTH_SHORT).show(); 
				      return;
				  }
				  
				 //ע��
				  IndoorGuiderApplication.getInstance().register(userid,userpw);
			}
			
		});
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
