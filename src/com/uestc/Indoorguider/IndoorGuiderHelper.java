package com.uestc.Indoorguider;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;

public class IndoorGuiderHelper extends IndoorGuiderHelperModel{
	static IndoorGuiderHelper me;
	public IndoorGuiderHelper(){
		
		 me = this;
	 }
	 
	 public static IndoorGuiderHelper getInstance()
	 {
		 return me;
	 }
	 
	@Override
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
	
	
    @Override
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
