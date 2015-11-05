package com.uestc.Indoorguider.util;

import android.content.Context;
import android.net.wifi.WifiManager;


import com.uestc.Indoorguider.IndoorGuiderManager;
import com.uestc.Indoorguider.wifi.ScanWifiThread;

public class ConnectTool {
	
	public static void startConnectThreads(Context context,WifiManager wifiManager)
	{
		//开启接收线程
	    new ClientAgent(context).start();
	    try {
			Thread.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    //发送线程
        Thread threadSend = new SendToServerThread();
        threadSend.start();
       
        try {
			Thread.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
      //scan WIFI signal 
	      Thread threadRssi = new ScanWifiThread(wifiManager);
	      threadRssi.start();
	  //登陆检测
	      if(IndoorGuiderManager.getInstance().getAlreadyLogin())
	      {
	    	//请求登陆
			IndoorGuiderManager.getInstance().login(IndoorGuiderManager.getInstance().getUsername(),
					IndoorGuiderManager.getInstance().getPassword());
	      }
	}
	
	
	public static Boolean checkConnect(Context context, WifiManager wifiManager)
	{
		if(ClientAgent.flag == false)
		{
			ConnectTool.startConnectThreads(context,wifiManager);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ClientAgent.flag == false)
		{
			return false;
		}
		else{
			return true;
		}
		
	}

}

