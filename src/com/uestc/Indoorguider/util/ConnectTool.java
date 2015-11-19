package com.uestc.Indoorguider.util;

import android.content.Context;
import android.net.wifi.WifiManager;


import com.uestc.Indoorguider.IndoorGuiderApplication;
import com.uestc.Indoorguider.IndoorGuiderHelper;
import com.uestc.Indoorguider.IndoorGuiderManager;
import com.uestc.Indoorguider.wifi.ScanWifiThread;

public class ConnectTool {
    private static  long count;
	public static void startConnectThreads(Context context,WifiManager wifiManager)
	{
		//���������߳�
	    new ClientAgent(context).start();
	    try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    //�����߳�
        Thread threadSend = new SendToServerThread();
        threadSend.start();
       
        try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
      //scan WIFI signal 
	      Thread threadRssi = new ScanWifiThread(wifiManager);
	      threadRssi.start();
	  //��½״̬���
	      if(IndoorGuiderApplication.getInstance().getAlreadyLogin() && (System.currentTimeMillis()-count>5000))
	      {
	    	  count = System.currentTimeMillis();
	    	//�����½
	    	  IndoorGuiderApplication.getInstance().login(IndoorGuiderApplication.getInstance().getUserName(),
	    			  IndoorGuiderApplication.getInstance().getPassword());
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

