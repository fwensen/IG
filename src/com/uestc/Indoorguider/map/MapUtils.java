package com.uestc.Indoorguider.map;
import java.util.ArrayList;

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
 * ��ͼ�����࣬����Ե�ͼ����ز���
 * 
 * */
public class MapUtils {
 
	private Context context;
    private MyWebView webView ;
    private boolean isGuided = false; //������ʼ�ı�־
	private final static int MinDistance_px = 50;
	private KDTree<Integer>  kdtree ;
	boolean isMove =true;
	double [][] sites_px;
	private boolean firstData  = true;//��һ���յ���λ����
	private int lastLayer = 1;
	
	
	
	
	
	public MapUtils(Context context, MyWebView webView){
		this.context = context;
		this.webView = webView;
	}
	
   //ʵ�����꣨cm������ͼ����(px)��λת��
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
   
 //ʵ�����꣨cm������ͼ����(px)��λת����������
   public int cmToPx_X1(float dimension_cm)
   {
	   int map = (int) (MyWebView.offsetXNegative1-dimension_cm/MyWebView.PNegative1);
	   return map;
   }
   public int cmToPx_Y2(float dimension_cm)
   {
	   int map = (int) (MyWebView.offsetYNegative1-dimension_cm/MyWebView.PNegative1);
	   return map;
   }
	
	/**
	 * ������·��
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
	
  

   /**ƫ�����*/
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
    
 
   /**��ʾ����·��*/
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
		//���һ���ڵ㣨Ŀ�ĵ����꣩
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
		//����javascript�еķ�������·��
		isGuided = true;     //guided!
        webView.loadUrl("javascript:drawPath('"+path+"')");
	   
   }
	
	 /**��ʾ����·��*/
	public void showRouteMultiLayer(JSONObject obj ,float[] srcLocation_px,float[] pathDestLocation_px) throws JSONException{
		   
			
		    JSONArray pathArray_px;
		    JSONObject currentLayerPath = obj.getJSONObject("path1");
		    int layer = currentLayerPath.getInt("z");
		    int hasMultilayer =  obj.getInt("multilayer");
		    int currentLayer = ((MapActivity)context).getCurrentLayer();
		    if(hasMultilayer == 0)
		    {
		    	if(layer != currentLayer)
		    	{
		    		return;//���Ǹò��·����ֱ������
		    	}
		    	
		    }else{
		    	if(layer != ((MapActivity)context).getCurrentLayer())
		    	{
		    		currentLayerPath= obj.getJSONObject("path2");
		    	}
		    }
		    pathArray_px = currentLayerPath.getJSONArray("path");//unit:px
		    if(pathArray_px.length()<2)
		    {return;}
		    JSONObject node = new JSONObject();
		    node = (JSONObject) pathArray_px.get(0);
			String path = "M"+ node.getInt("x")+" "+node.getInt("y")+"L" ;
			
			int i = 1;
			// for kdtree
			sites_px = new double[pathArray_px.length()][2];
			Log.v("test", "in response!");
			
			for(; i<(pathArray_px.length()-1); i++)
			{
				node = (JSONObject) pathArray_px.get(i);
				path = path + node.getInt("x")+" "+node.getInt("y")+"L";
				//init sites
				sites_px[i][0] = node.getInt("x");
				sites_px[i][1] = node.getInt("y");
				Log.v("test", "site[0]: " + sites_px[i][0]);
				Log.v("test", "site[1]: " + sites_px[i][1]);
			}
			//���һ���ڵ㣨Ŀ�ĵ����꣩
			JSONObject lastNode = (JSONObject) pathArray_px.get(i);
			path = path + lastNode.getInt("x")+" "+lastNode.getInt("y");
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
			//����javascript�еķ�������·��
			isGuided = true;     //guided!
	        webView.loadUrl("javascript:drawPath('"+path+"')");
	        webView.loadUrl("javascript:setPathAim('"+lastNode.getInt("x")+"','"+lastNode.getInt("y")+"')");
	        	
	   }
	

	
   
   /**��ʾ����·��*/
    public void showRoute1(JSONObject obj,float[] srcLocation_px,float[] pathDestLocation_px) throws JSONException{
	   
	    double [][] sites_px;
	    JSONArray pathArray_px;
	    JSONObject currentLayerPath;
	    int hasMultilayer =  obj.getInt("multilayer");
	    if(hasMultilayer == 0)
	    {
	    	currentLayerPath = obj.getJSONObject("path1");
	    	
	    }else{
	    	currentLayerPath = obj.getJSONObject("path1");
	    	int layer = currentLayerPath.getInt("z");
	    	if(layer != ((MapActivity)context).getCurrentLayer())
	    	{
	    		currentLayerPath= obj.getJSONObject("path2");
	    	}
	    }
	    pathArray_px = currentLayerPath.getJSONArray("path");//unit:px
		
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
		    	//���α���������
		    	node = (JSONObject) pathArray_px.get(index);
				path = path + " Q"+node.getInt("x")+" "+node.getInt("y");
				sites_px[index][0] = node.getInt("x");
				sites_px[index][1] = node.getInt("y");
				path = path + ","+pathDestLocation_px[0]+" "+pathDestLocation_px[1];
		    	break;
		    case 2:
		    	//���α�����
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
		//����javascript�еķ�������·��
		isGuided = true;     //guided!
        webView.loadUrl("javascript:drawPath('"+path+"')");
	   
   }
   
    
    /**
     * �������ڣ�ʹ���˳�ʼλ��λ����Ļ����
    */
     
     public void scrollWindow(int x ,int y){
    	 
    	 float scale = webView.getScale();
    	 webView.scrollTo((int)(x*scale), (int)(y*scale));
    	 
    	 
     }
    
     
//    public boolean makeLocationValid(int x_cm,int y_cm){
//    	
//    }
    public boolean isChangeLayer(int x_cm ,int y_cm){
    	int x_px = cmToPx_X(x_cm);
    	int y_px = cmToPx_Y(y_cm);
    	if ((x_px > 741.75 && x_px<1259.86) && (y_px > 1434.49 && y_px < 1660.56)){
    		return true;
    	}
    	return false;
       
     }
   /**
    * ������������
    * �յ��Ķ�λ��Ϣ��λ��cm
    * */

   public void updateLocation(float[] locationNow_cm ,float[] locationOld_cm, float[] destLocation_px, JSONObject obj) throws JSONException{
	   if(obj.getInt("x") ==0 &&obj.getInt("y") == 0)
	   {
		   return;
	   }
	    int layer = obj.getInt("z");
	    lastLayer = layer;
		CmToPxCalculator calculator = new CmToPxCalculator();
		//ѡ��������
		if(layer == Constant.LAYER_NEGATIVE1)
		{
			calculator.setStrategy(new LayerNegative1CmToPxSrategy());
			
		}else{
			calculator.setStrategy(new Layer1CmToPxSrategy());
			
		}
	    locationNow_cm[0] = obj.getInt("x"); //unit:CM
		locationNow_cm[1] = obj.getInt("y"); 
		locationNow_cm[2] = layer;
		
		if(firstData)
		{
			int x = calculator.calculatorX(locationNow_cm[1]);
			int y = calculator.calculatorY(locationNow_cm[0]);
			((MapActivity) context).setLayer(obj.getInt("z"));
			//���� �Ƕȣ�λ��x,y
			webView.loadUrl("javascript:setPointer('"+OrientationTool.angle+"','"+x+"','"+y+"')");
			locationOld_cm[0] = locationNow_cm[0];
		    locationOld_cm[1] = locationNow_cm[1];
		    locationOld_cm[2] = locationNow_cm[2];
		    //��������
		  //  webView.scrollTo(x, y);
		    firstData = false;
		}
	 
		if((Math.pow(locationOld_cm[0] - locationNow_cm[0],2) + Math.pow(locationOld_cm[1]-locationNow_cm[1],2)) > Math.pow(50,2) && isMove )//1m=20px
		{
			
			int x = calculator.calculatorX(locationOld_cm[1]);
			int y = calculator.calculatorY(locationOld_cm[0]); 
			//���� �Ƕȣ�λ��x,y
			webView.loadUrl("javascript:setPointer('"+OrientationTool.angle+"','"+x+"','"+y+"')");
		    locationOld_cm[0] = locationNow_cm[0];
		    locationOld_cm[1] = locationNow_cm[1];
		    locationOld_cm[2] = locationNow_cm[2];
		}
		
		
		//get the nearest site, and culculate the distance
//		if (isGuided) {
//			double dis = 0;
//			Log.v("test", "test in calculate");
//			float [] location = {locationNow_cm[0], locationNow_cm[1]};
//			try {
//				dis = culculateNearestDistance(location);
//				Log.v("sites", "distance: " + dis);
//				Log.v("sites", "--------------------------------");
//				Log.v("test", "test in calculate");
//				Log.v("test", "dis: " + dis);
//			} catch (KeySizeException e) {
//				e.printStackTrace();
//			} catch (KeyDuplicateException e) {
//				e.printStackTrace();
//			}
//			Log.v("test", "in dis calculate!");
//			if (dis > MinDistance_px) {
//				Log.v("distance", "request");
//				requestPath(
//						new float[]{cmToPx_X(locationNow_cm[0]),  cmToPx_Y(locationNow_cm[1]), locationNow_cm[2]},  
//						destLocation_px);
//			}
//		}
   }
   
   
   /**�������˷�λ*/
   public void updateOrientation(JSONObject obj,float location_cm[]) throws JSONException{
	   double angle = 0;//���˷�λ���ɴ�������ȡ����
	   angle = obj.getDouble("angle");
	   int layer = (int)location_cm[2];
	   CmToPxCalculator calculator =CmToPxCalculator.getCalculator(layer);
		//ѡ��������
	   webView.loadUrl("javascript:setPointer('"+angle+"','"+calculator.calculatorX(location_cm[1])+"','"+calculator.calculatorY(location_cm[0])+"')");
	  // Log.i("�Ƕ�",locationOld_cm[0]+" "+locationOld_cm[1]);
   }

	

}
