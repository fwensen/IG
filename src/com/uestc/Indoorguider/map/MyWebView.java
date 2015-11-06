package com.uestc.Indoorguider.map;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.Constant;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class MyWebView extends WebView {
	/**
	 * 地图与实际的比例 1px= 5cm
	 * */
	// 
	public static float webviewX0;
	public static float webviewY0;
	public static int P = 5;
	public static int offsetX = 282;
	public static float offsetY = (float) 1667.12;
	static float scale;
	private boolean scaleFlag1  = false;
	private boolean scaleFlag2  = false;
	float xd,xu; 
	float yd,yu;
	float OldX1,OldX2,OldY1,OldY2,NewX1,NewX2,NewY1,NewY2;
	static Handler h ;
	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	void onScaleChanged(WebView, float, float) 
//	{}
//	
//	@Override  
//	public void onScaleChanged(WebView view, float oldScale, float newScale)  
//	{  
//	    // TODO Auto-generated method stub  
//	    super.onScaleChanged(view, oldScale, newScale);  
//	}  
 @Override
    public boolean onTouchEvent(MotionEvent event) {   
           // TODO Auto-generated method stub
	        event.getAction();
		 	if(event.getAction()==MotionEvent.ACTION_DOWN)
		 	{
		 		xd = event.getX();
			    yd= event.getY();
			    Log.v("click", "in mywebview xd: " +xd +" yd: "+yd);	
			    	
		 	}
		 	else if (event.getAction()==MotionEvent.ACTION_UP)
		 	{
		 		xu = event.getX();
			    yu= event.getY();
			        if(Math.abs(xu-xd)<100 && Math.abs(yu-yd)<100)
			        {
			        	float scale = getScale();
			        	//地图被放大到一定程度，显示细节
			        	
				    	int x = getScrollX();
				        int y = getScrollY();
				    	float x0 =  ((x+xd)/scale);
				    	float y0 =  ((y+yd)/scale);
				    	webviewX0 = x0;
				    	webviewY0 = y0;
				    	Log.v("click", "in mywebview x: " +x +" y: "+y);	
				    	Log.v("click", "in mywebview x0: " +x0 +" y0: "+y0);	
				    	//Intent intent = new Intent(TAG_BROAD);
				    	//intent.putExtra("Name", "hellogv");
				    	//intent.putExtra("Blog", "http://blog.csdn.net/hellogv");
				    	//sendBroadcast(intent);//传递过去
				    	System.out.println(scale);
				    	System.out.println(y0);
				    	loadUrl("javascript:setAim('"+x0+"','"+y0+"')");
				    	//loadUrl("javascript:drawcircle('"+x0+"','"+y0+"')");
			        	//查询数据库，匹配该坐标是否有站点存在
				    	float[] destLocation = new float[3];
				    	destLocation[0] = x0;//单位：px
				    	destLocation[1] = y0;
				    	destLocation[2] = 1;
				    	//Facility facility =  new Facility("肯德基", destLocation);
				    	
				    	//如果有，通知MainActivity显示站点简介
				    	sendHandlerMsg(h, destLocation);
			        }
		 		
		 	}
		 
           super.onTouchEvent(event);
         scale = getScale();
       	//地图被放大到一定程度，显示细节
       	if(scale>0.4 && scaleFlag1 == false)
       	{
       		String[] sites = {"候车处","餐饮购物","停车场"};
       		String v1 = "visible";
       		for(String site:sites)
				{
					loadUrl("javascript:setVisibility('"+site+"','"+v1+"')");
				}
       		scaleFlag1  = true;
       	}
       	if(scale>0.8 && scaleFlag2 == false)
       	{
       		String[] sites = {"旅客服务","公交站台","出租车"};
       		String v1 = "visible";
       		for(String site:sites)
				{
					loadUrl("javascript:setVisibility('"+site+"','"+v1+"')");
				}
       		scaleFlag2  = true;
       	}
       	else if(scale<0.8 && scaleFlag2 == true){
       		String[] sites = {"旅客服务","公交站","出租车"};
       		String v1 = "hidden";
       		for(String site:sites)
				{
					loadUrl("javascript:setVisibility('"+site+"','"+v1+"')");
				}
       		scaleFlag2  = false;
       	}
       	else if(scale<0.4 && scaleFlag1 == true){
       		String[] sites = {"候车处","餐饮购物","停车场"};
       		String v1 = "hidden";
       		for(String site:sites)
				{
					loadUrl("javascript:setVisibility('"+site+"','"+v1+"')");
				}
       		scaleFlag1  = false;
       	}
           
           return super.onTouchEvent(event);

    }   
 
 
    //向MainActivity发送站点简介
		private static void sendHandlerMsg(Handler h, float[] destLocation)
		{
			JSONObject obj= new JSONObject();
			try {
				obj.put("typecode", Constant.FACILITY_INFOR);
				obj.put("x",destLocation[0]);
				obj.put("y",destLocation[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = h.obtainMessage();
	    	msg.obj = obj;
			h.sendMessage(msg);
			
		}
		
		
		
		public static void setMainHandler(Handler handler)
		{
			h = handler;
		}
		
		
		
		 //设施类
		static class Facility
		{
			String name;//名称
			float[] location = new float[3];//坐标
			int type;//类别
			String infor;//简介
			
			Facility(String name ,int type, float[] location ,String infor)
			{
				this.name = name;
				this.type = type;
				this.location[0] = location[0];
				this.location[1] = location[1];
				this.location[2] = location[2];
				this.infor = infor;
			}
		    Facility(String name , float[] location)
			{
				this.name = name;
				this.location[0] = location[0];
				this.location[1] = location[1];
				this.location[2] = location[2];
			}
			public String getName()
			{
				return name;
			}
			public float[] getLocation()
			{
				return location;
			}
		}
	
		
	
	
}
