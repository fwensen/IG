package com.uestc.Indoorguider.util;

import org.json.JSONArray;
import org.json.JSONObject;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SendToServerThread extends Thread {
	
    private static Handler mHandler;  
    private final static Object mSync = new Object();  
  
	public void run(){
		
		Log.i("sendThread",ClientAgent.flag+"");
		while(ClientAgent.flag)
		{
			
			Looper.prepare();  
            synchronized (mSync) {  
                mHandler = new Handler(){  
                    @Override  
                    public void handleMessage(Message msg){
                    		JSONObject obj =  (JSONObject) msg.obj;
                        	ClientAgent.sendServerMessage(obj);
                    }  
                };  
                mSync.notifyAll();  
            }  
            Looper.loop();  
			
		}
		
	}
//	public static Handler getHandler() {  
//        synchronized (mSync) {  
//            if (mHandler == null) {  
//                try {  
//                    mSync.wait();  
//                } catch (InterruptedException e) {  
//                }  
//            }  
//            return mHandler;  
//        }  
//    }  
	public static Handler getHandler() { 
		if(ClientAgent.flag){
			synchronized (mSync) {  
            if (mHandler == null) {  
                try {  
                    mSync.wait();  
                } catch (InterruptedException e) {  
                }  
            }  
            return mHandler;  
			}  
		}
		return null;
	}
    public void exit() {  
        getHandler().post(new Runnable(){  
            public void run() {  
                Looper.myLooper().quit();  
            }});  
    }  
}  
	


