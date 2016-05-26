package com.uestc.Indoorguider.map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.orientation.OrientationTool;
import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

/**
 * 地图工具类，负责对地图的相关操作
 * 
 * */
public class MapUtils {
 
	private Context context;
    private MyWebView webView ;
    private boolean isGuided = false; //导引开始的标志
	private final static int MinDistance_px = 1000;
	private KDTree<Integer>  kdtree ;
	boolean isMove =true;
	double [][] sites_px;
	private boolean firstData  = true;//第一次收到定位数据
	
	public MapUtils(Context context, MyWebView webView){
		this.context = context;
		this.webView = webView;
	}
	
   //实际坐标（cm）到地图坐标(px)单位转换
   public int cmToPx_X(float dimension_cm)
   {
	   int map = (int) (MyWebView.offsetX-dimension_cm/MyWebView.P);
	   return map;
   }
   public int cmToPx_Y(float dimension_cm)
   {
	   int map = (int) (MyWebView.offsetY-dimension_cm/MyWebView.P);
	   return map;
   }
	
	/**
	 * 请求导引路径
	 * */
	public void requestPath( float[] srcLocation,float[] destLocation)
	{
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(ConnectTool.checkConnect(context,wifiManager))
		{
			JSONObject obj = new JSONObject();
			try {
				obj.put("typecode", Constant.GUIDE_REQUEST);
				JSONObject src = new JSONObject();
				src.put("x", srcLocation[0]);
				src.put("y", srcLocation[1]);
				src.put("z", srcLocation[2]);
				obj.put("sour", src);
				JSONObject dest = new JSONObject();
				dest.put("x", destLocation[0]);
				dest.put("y", destLocation[1]);
				dest.put("z", destLocation[2]);
				obj.put("dest", dest);
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
	
  

   /**偏离计算*/
	public double culculateNearestDistance(float[] location_cm) 
			throws KeySizeException, KeyDuplicateException {
		
		Log.v("test", "into calculate");
		Log.v("test", "k[0] and k[1]: " + location_cm[0] + "  " +location_cm[1]);
		int m = kdtree.nearest(new double[]{location_cm[0], location_cm[1]});	
		return Math.sqrt( Math.pow(cmToPx_X(location_cm[0]) - sites_px[m][0],  2.0)  + 
									   Math.pow(cmToPx_Y(location_cm[1]) - sites_px[m][1],  2.0));		

//		return Math.sqrt( Math.pow(location_cm[0] - sites_px[m][0],  2)  + 
//									   Math.pow(location_cm[1] - sites_px[m][1],  2));		

   }
    
 
   /**显示导引路线*/
	public void showRoute(JSONObject obj ,float[] srcLocation_px,float[] pathDestLocation_px) throws JSONException{
	    double [][] sites_px;
		JSONArray pathArray_px = obj.getJSONArray("path");//unit:px
		String path = "M"+ srcLocation_px[0] +" "+srcLocation_px[1]+"L" ;
		JSONObject node = new  JSONObject();
		int i = 0;
		// for kdtree
		sites_px = new double[pathArray_px.length()][2];
		Log.v("test", "in response!");
		for(; i<pathArray_px.length(); i++)
		{
			node = (JSONObject) pathArray_px.get(i);
			path = path + node.getInt("x")+" "+node.getInt("y")+"L";
			//init sites
			sites_px[i][0] = node.getInt("x");
			sites_px[i][1] = node.getInt("y");
			Log.v("test", "site[0]: " + sites_px[i][0]);
			Log.v("test", "site[1]: " + sites_px[i][1]);
		}
		//最后一个节点（目的地坐标）
		path = path + pathDestLocation_px[0]+" "+pathDestLocation_px[1];
		--i;
		node = (JSONObject) pathArray_px.get(i);
		//init last site
		sites_px[i][0] = node.getInt("x");
		sites_px[i][1] = node.getInt("y");
		Log.v("test", "site[0]: " + sites_px[i][0]);
		Log.v("test", "site[1]: " + sites_px[i][1]);
		// build the kdtree
		kdtree = new KDTree<Integer>(2);
		for (int j = 0; j < sites_px.length; ++j)
			try {
				kdtree.insert(sites_px[j], j);
			} catch (KeySizeException e) {
				e.printStackTrace();
			} catch (KeyDuplicateException e) {
				e.printStackTrace();
			}
		//调用javascript中的方法画出路线
		isGuided = true;     //guided!
        webView.loadUrl("javascript:drawPath('"+path+"')");
	   
   }
   
   /**显示导引路线*/
  /* private void showRoute1(JSONObject obj) throws JSONException{
	   
	    double [][] sites_px;
	    main_bar.setVisibility(View.VISIBLE);
	    facility_infor.setVisibility(View.GONE);
		JSONArray pathArray_px = obj.getJSONArray("path");//unit:px
		String path = "M"+ srcLocation_px[0] +" "+srcLocation_px[1] ;
		JSONObject node = new  JSONObject();
		int i = 0;
		// for kdtree
		sites_px = new double[pathArray_px.length()][2];
		Log.v("test", "in response!");
		int group3 =  pathArray_px.length()/3;
		int k = pathArray_px.length()%3;
		int j = 0;
		for(; j < group3; j++)
		{
			node = (JSONObject) pathArray_px.get(j+0);
			path = path + " C"+node.getInt("x")+" "+node.getInt("y");
			sites_px[j][0] = node.getInt("x");
			sites_px[j][1] = node.getInt("y");
			node = (JSONObject) pathArray_px.get(j+1);
			path = path + ","+node.getInt("x")+" "+node.getInt("y");
			sites_px[j+1][0] = node.getInt("x");
			sites_px[j+1][1] = node.getInt("y");
			node = (JSONObject) pathArray_px.get(j+2);
			path = path + ","+node.getInt("x")+" "+node.getInt("y");
			sites_px[j+2][0] = node.getInt("x");
		    sites_px[j+2][1] = node.getInt("y");
		}
		int index = 3*j;
		switch(k)
		{
		    case 1:
		    	//二次贝塞尔相连
		    	node = (JSONObject) pathArray_px.get(index);
				path = path + " Q"+node.getInt("x")+" "+node.getInt("y");
				sites_px[index][0] = node.getInt("x");
				sites_px[index][1] = node.getInt("y");
				path = path + ","+pathDestLocation_px[0]+" "+pathDestLocation_px[1];
		    	break;
		    case 2:
		    	//三次贝塞尔
		    	node = (JSONObject) pathArray_px.get(index);
				path = path + " C"+node.getInt("x")+" "+node.getInt("y");
				sites_px[index][0] = node.getInt("x");
				sites_px[index][1] = node.getInt("y");
				node = (JSONObject) pathArray_px.get(index+1);
				path = path + ","+node.getInt("x")+" "+node.getInt("y");
				sites_px[index+1][0] = node.getInt("x");
				sites_px[index+1][1] = node.getInt("y");
				path = path + ","+pathDestLocation_px[0]+" "+pathDestLocation_px[1];
		    	break;
		}
	
		
//		--i;
//		node = (JSONObject) pathArray_px.get(i);
//		//init last site
//		sites_px[i][0] = node.getInt("x");
//		sites_px[i][1] = node.getInt("y");
//		Log.v("test", "site[0]: " + sites_px[i][0]);
//		Log.v("test", "site[1]: " + sites_px[i][1]);
		// build the kdtree
		kdtree = new KDTree<Integer>(2);
		for (int q = 0; q < sites_px.length; ++q)
			try {
				kdtree.insert(sites_px[q], q);
			} catch (KeySizeException e) {
				e.printStackTrace();
			} catch (KeyDuplicateException e) {
				e.printStackTrace();
			}
		//调用javascript中的方法画出路线
		isGuided = true;     //guided!
        webView.loadUrl("javascript:drawPath('"+path+"')");
	   
   }
*/
   
   /**
    * 更新行人坐标
    * 收到的定位信息单位：cm
    * */

   public void updateLocation(float[] locationNow_cm ,float[] locationOld_cm, float[] destLocation_px, JSONObject obj) throws JSONException{
	   if(obj.getInt("x") ==0 &&obj.getInt("y") == 0)
	   {
		   return;
	   }
	    locationNow_cm[0] = obj.getInt("x"); //unit:CM
		locationNow_cm[1] = obj.getInt("y"); 
		locationNow_cm[2] = obj.getInt("z");
		
		if(firstData)
		{
			//放入 角度，位置x,y
			webView.loadUrl("javascript:setPointer('"+OrientationTool.angle+"','"+cmToPx_X(locationOld_cm[0])+"','"+cmToPx_Y(locationOld_cm[1])+"')");
			locationOld_cm[0] = locationNow_cm[0];
		    locationOld_cm[1] = locationNow_cm[1];
		    locationOld_cm[2] = locationNow_cm[2];
		    firstData = false;
		}
	 
		if((Math.pow(locationOld_cm[0] - locationNow_cm[0],2) + Math.pow(locationOld_cm[1]-locationNow_cm[1],2)) > Math.pow(50,2) && isMove )//1m=20px
		{
			//放入 角度，位置x,y
			webView.loadUrl("javascript:setPointer('"+OrientationTool.angle+"','"+cmToPx_X(locationOld_cm[0])+"','"+cmToPx_Y(locationOld_cm[1])+"')");
		    locationOld_cm[0] = locationNow_cm[0];
		    locationOld_cm[1] = locationNow_cm[1];
		    locationOld_cm[2] = locationNow_cm[2];
		}
		
		
		//get the nearest site, and culculate the distance
		if (isGuided) {
			double dis = 0;
			Log.v("test", "test in calculate");
			float [] location = {locationNow_cm[0], locationNow_cm[1]};
			try {
				dis = culculateNearestDistance(location);
				Log.v("sites", "distance: " + dis);
				Log.v("sites", "--------------------------------");
				Log.v("test", "test in calculate");
				Log.v("test", "dis: " + dis);
			} catch (KeySizeException e) {
				e.printStackTrace();
			} catch (KeyDuplicateException e) {
				e.printStackTrace();
			}
			Log.v("test", "in dis calculate!");
			if (dis > MinDistance_px) {
				Log.v("distance", "request");
				requestPath(
						new float[]{cmToPx_X(locationNow_cm[0]),  cmToPx_Y(locationNow_cm[1]), locationNow_cm[2]},  
						destLocation_px);
			}
		}
   }
   
   
   /**更新行人方位*/
   public void updateOrientation(JSONObject obj,float location_cm[]) throws JSONException{
	   double angle = 0;//行人方位，由传感器获取更新
	   angle = obj.getDouble("angle");
	   webView.loadUrl("javascript:setPointer('"+angle+"','"+cmToPx_X(location_cm[0])+"','"+cmToPx_Y(location_cm[1])+"')");
	  // Log.i("角度",locationOld_cm[0]+" "+locationOld_cm[1]);
   }

	

}
