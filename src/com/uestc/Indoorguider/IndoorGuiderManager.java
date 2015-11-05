package com.uestc.Indoorguider;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
/**
 * 管理类，单例模式，必须先实例化再使用*/
public class IndoorGuiderManager extends IndoorGuiderManagerModel {
	 private static final String PREF_USERNAME = "username";
	 private static final String PREF_PWD = "pwd";
	 private static final String PREF_ALREADY_LOGIN = "already_login";
	 private Context context = null;
	 private static IndoorGuiderManager me ;
	 public IndoorGuiderManager(Context context){
		 this.context = context;
		 me = this;
	 }
	 
	 public static IndoorGuiderManager getInstance()
	 {
		 return me;
	 }
	 
	 
	 @Override
	public boolean setUsername(String username) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_USERNAME, username).commit();
	
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_USERNAME, "unkonwname");
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_PWD, "unknowpsw");
	}
	@Override
	public boolean setPassword(String password) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_PWD, password).commit();
	}
	@Override
	public boolean saveAlreadyLogin(boolean alreadyLogin) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.edit().putBoolean(PREF_ALREADY_LOGIN, alreadyLogin).commit();
	}

	@Override
	public boolean getAlreadyLogin() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_ALREADY_LOGIN, false);
	}
	
	
    public void logout()
	{
		WifiManager wifiManager = (WifiManager) IndoorGuiderApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
		if(ConnectTool.checkConnect(IndoorGuiderApplication.getInstance(),wifiManager))
		{
			JSONObject obj = new JSONObject();
			try {
				obj.put("typecode", Constant.LOGOUT_REQUEST);
				obj.put("username",IndoorGuiderApplication.getInstance().getUserName());
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
	
	 public void login(String username, String password)
	   {
		   WifiManager wifiManager = (WifiManager) IndoorGuiderApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
		   if(ConnectTool.checkConnect(IndoorGuiderApplication.getInstance(),wifiManager))
		   {
			   JSONObject obj = new JSONObject();
				
				try {
					obj.put("typecode",Constant.LOGIN_REQUEST_NAME);
				    obj.put("username", username);
					obj.put("password",password);
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
	   

}
