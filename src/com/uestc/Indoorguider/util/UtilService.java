package com.uestc.Indoorguider.util;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uestc.Indoorguider.network.NetworkStateBroadcastReceiver;
import com.uestc.Indoorguider.wifi.WifiStateReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class UtilService  extends Service{
	BroadcastReceiver wifiReceiver,networkReceiver;
	WifiManager wifiManager ;
	private MyBinder  binder = new MyBinder();
	String Tag  = "BindService";
	public class MyBinder extends Binder
	{
		public void sendMsg(JSONObject obj)
		{
			//发送服务器消息
			//发给发送线程发
			//ClientAgent.sendServerMessage(obj);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(Tag,"UtilService onCreate !");
		//get RSSI
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiStateReceiver(wifiManager);
		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		//network change
		networkReceiver  = new NetworkStateBroadcastReceiver(wifiManager);
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)); 
		
	}
	
	@Override
	public boolean onUnbind(Intent intent )
	{
		return true;
	}
	
	@Override
	public void onDestroy()
	{
		ClientAgent.flag = false;
		unregisterReceiver(wifiReceiver);
		unregisterReceiver(networkReceiver);
		super.onDestroy();
		Log.i(Tag,"UtilService onDestroy !");
		
		
	}
	
	

}
