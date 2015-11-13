package com.uestc.Indoorguider.util;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import java.net.SocketTimeoutException;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.R.string;
import com.uestc.Indoorguider.map.MapActivity;
public class  ClientAgent extends Thread
{
	static Socket sc;
	static DataInputStream din;//输入流
	static DataOutputStream dout;//输出流
	BufferedReader br ;
	public static boolean flag ;//是否结束客服端线程
	protected static Handler handlerNow;//当前activity的handler
	public static int exType = 0 ;
	private  JSONObject objIn;
	static String getinfo;
	public static String ip ;
	
	public ClientAgent(Context context)
	{
	//	flag = true;
//		this.mainHandler = MainActivity.msgHandler;
////		this.loginHandler = LoginActivity.msgHandler;
////		this.registerHandler = RegisterActivity.msgHandler;
//		this.captureHandler = CaptureActivity.msgHandler;
		
	}
	
	public static void setHandler(Handler handler)
	{
		   handlerNow = handler;
	}

	public void run()
	{
		try {
			sc = new Socket();
			sc.connect(new InetSocketAddress(Constant.ip,Constant.port),5000);
			//sc.connect(new InetSocketAddress(ip,Constant.port),5000);
			if(!sc.isConnected())
			{
				if(sc!=null)
				{
					sc.close();
				}
				flag = false;
				return;
			}
			flag = true;
			br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			dout =new DataOutputStream( sc.getOutputStream() );
		} catch (SocketTimeoutException e) 
		{ 
			e.printStackTrace();
			//开启错误对话框*****************************
	    	try{if(din!=null) {din.close();} }catch(Exception e1){ e1.printStackTrace();}
			try{if(dout!=null) {dout.close();} }catch(Exception e1){ e1.printStackTrace();}
			try{if(sc!=null) {sc.close();} }catch(Exception e1){e1.printStackTrace();}
			flag = false;
			noticeNetworkException(1);
			return;
		}
		catch(ConnectException e)
		{
			exType = 0 ;
		    e.printStackTrace();
	    	try{if(din!=null) {din.close();} }catch(Exception e1){ e1.printStackTrace();}
			try{if(dout!=null) {dout.close();} }catch(Exception e1){ e1.printStackTrace();}
			try{if(sc!=null) {sc.close();} }catch(Exception e1){e1.printStackTrace();}
			flag = false;
			noticeNetworkException(1);
			return;
		}
		catch (Exception e) 
		{ 
			e.printStackTrace();
	    	try{if(din!=null) {din.close();} }catch(Exception e1){ e1.printStackTrace();}
			try{if(dout!=null) {dout.close();} }catch(Exception e1){ e1.printStackTrace();}
			try{if(sc!=null) {sc.close();} }catch(Exception e1){e1.printStackTrace();}
			flag = false;
			noticeNetworkException(0);
			return;
		}
		
		while(flag)
		{
			
			try
			{
				getinfo = br.readLine();
				while(getinfo != null)
				{
					System.out.println("rec!");
					System.out.println(getinfo);
					objIn = new JSONObject(getinfo);
					Log.i("recv from server", "<---" + objIn);
					if(handlerNow != null)
					{
						
						sendHandlerMsg(handlerNow);
					}
					
					 getinfo = br.readLine();
				}
			}
			catch(ConnectException e)
			{
			    e.printStackTrace();
		    	try{if(din!=null) {din.close();} }catch(Exception e1){ e1.printStackTrace();}
				try{if(dout!=null) {dout.close();} }catch(Exception e1){ e1.printStackTrace();}
				try{if(sc!=null) {sc.close();} }catch(Exception e1){e1.printStackTrace();}
				flag = false;
				noticeNetworkException(1);
				return;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				try{if(din!=null) {din.close();} }catch(Exception e1){ e1.printStackTrace();}
				try{if(dout!=null) {dout.close();} }catch(Exception e1){ e1.printStackTrace();}
				try{if(sc!=null) {sc.close();} }catch(Exception e1){e1.printStackTrace();}
				flag = false;
				noticeNetworkException(0);
			}
		}
		
		
	}
	
	//向服务器发送消息
		public  static void  sendServerMessage(JSONObject obj)
		{   
				try 
				{ 
				    // int length =  obj.toString().length();
				    //dout.writeBytes(length+obj.toString());
					Log.i("send to server", "--->" + obj.getInt("typecode"));  
					dout.writeUTF(obj.toString());
					dout.flush();
					System.out.println("send!");
					Log.i("send to server", "--->" + obj);  
				} 
				catch (SocketException e) 
				{			
					e.printStackTrace();
					try{if(din!=null) {din.close();} }catch(Exception e1){ e1.printStackTrace();}
					try{if(dout!=null) {dout.close();} }catch(Exception e1){ e1.printStackTrace();}
					try{if(sc!=null) {sc.close();} }catch(Exception e1){e1.printStackTrace();}
					//提示用户网络问题*******************************
					noticeNetworkException(1);
					flag = false;
				}
				catch (Exception e) 
				{			
					e.printStackTrace();
					try{if(din!=null) {din.close();} }catch(Exception e1){ e1.printStackTrace();}
					try{if(dout!=null) {dout.close();} }catch(Exception e1){ e1.printStackTrace();}
					try{if(sc!=null) {sc.close();} }catch(Exception e1){e1.printStackTrace();}
					//提示用户网络问题*******************************
					noticeNetworkException(0);
					flag = false;
				}
				
		}
	
	//向activity发服务器返回消息
	public static void sendHandlerMsg(Handler h)
	{
		Message msg = h.obtainMessage();
    	msg.obj = getinfo;
		h.sendMessage(msg);
		getinfo = null;
	}
	
	private static void noticeNetworkException(int index)
	{
		if(handlerNow!= null)
	    {
			//网络错误信息
			JSONObject obj =  new JSONObject();
			try {
				obj.put("typecode", Constant.NETWORK_EXCEPTION);
				obj.put("network_ex", string.network_ex1+index);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			getinfo =obj.toString();
	    	sendHandlerMsg(handlerNow);
	    }
			
	}
	
	
}
