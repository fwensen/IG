package com.uestc.Indoorguider.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;

import org.json.JSONObject;

import com.uestc.Indoorguider.Constant;


public class DataGetUtilSimple
{
	private static DataInputStream din;
	private static DataOutputStream dout;
	private static Socket s;
	public static String readinfo;
	public static byte[] data;
	public static JSONObject objIn;
	
	//在服务员端，当快速点击多个菜品子类时，会有多个线程同时调用ConnectSevert方法，而din,dout都是静态的，因此
	//会产生EOFException，因此将ConnectSevert()方法声明为同步方法。
	public synchronized static void ConnectSevert(JSONObject obj) throws Exception
	{
		
				try
				{
					s=new Socket();
					s.connect(new InetSocketAddress(Constant.ip,Constant.port),5000);
					//s = new Socket(Constant.ip, Constant.port);
					if(!s.isConnected())
					{
						if(s!=null)
						{
							s.close();
						}
						return;
					}
					
					din=new DataInputStream(s.getInputStream());
					dout=new DataOutputStream(s.getOutputStream());
					//编码
					//info=MyConverter.escape(info);
					//dout.writeInt(obj.length());
					System.out.println(obj.toString());
					dout.write(obj.toString().getBytes());
					dout.flush();
					System.out.println("send!");
					
					//int len = din.read();
					BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
					String getinfo = br.readLine();
					//String getinfo =  
					System.out.println("rec!");
					objIn = new JSONObject(getinfo);
					System.out.println(objIn);

					
//					if(Integer.parseInt(len) !=(objIn.length()))
//					{
//						dout.writeUTF("err");//数据有误请求重传
//					}
					
				}
				finally
				{
					try{if(din!=null) {din.close();} }catch(Exception e){ e.printStackTrace();}
					try{if(dout!=null) {dout.close();} }catch(Exception e){ e.printStackTrace();}
					try{if(s!=null)
					{s.close();} }
					catch(Exception e){e.printStackTrace();}
				}
			
    }
}