package com.uestc.Indoorguider.wifi;

import com.uestc.Indoorguider.util.ClientAgent;

import android.net.wifi.WifiManager;

public class ScanWifiThread extends Thread {
	static  Boolean wifiFlag ;
	WifiManager wifi;
	public ScanWifiThread(WifiManager wifi)
	{
		
		this.wifi =  wifi;
	}
	public void run(){
		while (ClientAgent.flag) {
			//System.out.println("wifiScanThread flag  "+ClientAgent.flag);
			
			try {

				Thread.sleep(50);//0.1s
				
			} catch (Exception e) {
                
			}
			wifi.startScan();
		}

		
	}
	

}
