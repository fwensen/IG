package com.uestc.Indoorguider.map;

import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.R.id;
import com.uestc.Indoorguider.R.layout;
import com.uestc.Indoorguider.more.MoreActivity;
import com.uestc.Indoorguider.orientation.OrientationTool;
import com.uestc.Indoorguider.site_show.SearchNearestSite;
import com.uestc.Indoorguider.site_show.SiteActivity;
import com.uestc.Indoorguider.site_show.SiteInfo;
import com.uestc.Indoorguider.ticket.TicketRequestActivity;
import com.uestc.Indoorguider.traffic.TrafficActivity;
import com.uestc.Indoorguider.util.ClientAgent;
import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;
import com.uestc.Indoorguider.wifi.WifiStateReceiver;
import com.uestc.Indoorguider.zxing_view.CaptureActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//Washington and Lee University: kdtree, http://home.wlu.edu/~levys/software/kd/docs/
import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeyMissingException;
import edu.wlu.cs.levy.CG.KeySizeException;
//关于android2.3中javascript交互的问题
//http://code.google.com/p/android/issues/detail?id=12987
public class MapActivity extends APPActivity implements OnClickListener{
    private boolean firstData  = true;//第一次收到数据
	private final static int MinDistance_px = 1000;
	private static  MyWebView webView = null;
	private LinearLayout myLocation = null;
	private LinearLayout near = null;
	private LinearLayout ticket = null;
	private LinearLayout more = null;
	private LinearLayout main_bar,facility_infor,facility_go = null;
    private TextView facility_name ;//设施名称
	//private TextView recordText ;//测试记录
	
	public static int windowHeight ,windowWidth;
    private float[] srcLocation_px = new float[3] ;
	private float[] destLocation_px = new float[3]; //记录目标设施位置，用于导引请求。unit:px
	private float[] pathDestLocation_px = new float[3]; //记录路径请求的目的地位置，用于导引请求。unit:px
	
	
	public static Boolean isForeground = true;//是否位于最前面foreground process

	private boolean isGuided;
	WifiManager wifiManager;
	private boolean flag = false;
	
	// double array
	double [][] sites_px;
	KDTree<Integer> kdtree;
	
	File tfile,mypath;
	String TAG ="scale";
	public final static int REQUEST_MYLOCATION = 0;
	public final static int REQUEST_SITE_NEAR = 1;
	public final static int RESULT_MYLOCATION = 10;

	/**
	 * 上一次定位位置
	 * unit:cm
	 */
	private float[] locationOld_cm = {854,7541,1};
	/**
	 * 最新位置
	 * 单位：cm
	 */
	private float[] locationNow_cm = {20000,20000,1};
	
	double angle = 0;//行人方位，由传感器获取更新

	private SensorManager mSensorManager;// 传感器管理对象
	private Sensor accSensor; 
	private Sensor stepSensor; 
	private Sensor magneticSensor;  
	private SensorEventListener sensorEventListener;
	Intent intent;
	long facility_go_time = 0;//设施点按键时间
	boolean isMove =true;
	@Override
	protected void handleResult(JSONObject obj) 
	{
		try {
			switch(obj.getInt("typecode"))
			{
				case Constant.LOCATION_WIFI_SUCCESS://行人位置更新（wifi定位）
					 updateLocation(obj);
					 break;
				case Constant.ACCELERATOR:
					 isMove = obj.getBoolean("ismove");
				     break;
				case Constant.LOCATION_WIFI_ERROR:
					break;
				case Constant.ORIENTATION://行人朝向更新
					updateOrientation(obj);
					break;
				case Constant.GUIDE_SUCCESS://导引路线更新，返回以地图为原点
					showRoute(obj);
					break;
				case Constant.GUIDE_ERROR:
					Toast.makeText(MapActivity.this, "路线请求不成功，请换个地点位重试！", Toast.LENGTH_SHORT).show();
					break;
				case Constant.FACILITY_INFOR://站点匹配成功，返回名称、坐标，显示
					showfacilityName(obj);
					break;
				default:
					break;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initSensors();// 初始化传感器和位置服务
        //开启服务
        //  test
	    Intent intent = new Intent();
	    intent.setAction("com.uestc.Indoorguider.util.UtilService");
	    startService(intent);
        getWindowSize();
        isGuided = false;
        webView = (MyWebView) findViewById(R.id.webview);
        main_bar = (LinearLayout) findViewById(R.id.main_bar);
        facility_infor = (LinearLayout) findViewById(R.id.facility_infor);
        facility_name = (TextView)findViewById(R.id.facility_name);
        facility_go = (LinearLayout) findViewById(R.id.facility_go);
        facility_go.setOnClickListener(this);
        myLocation = (LinearLayout) findViewById(R.id.myLocation);
        myLocation.setOnClickListener(this);
        near = (LinearLayout) findViewById(R.id.near);
        near.setOnClickListener(this); 
        more = (LinearLayout) findViewById(R.id.more);
        more.setOnClickListener(this);
        ticket =  (LinearLayout) findViewById(R.id.ticket);
        ticket.setOnClickListener(this) ;
        ImageView siteCancle = (ImageView) findViewById(R.id.site_cancel);
        siteCancle.setOnClickListener(this);
        //recordText = (TextView)findViewById(R.id.recordText);
        //获取位置XML文件****************
        /*
        Log.v("xml", "0: "+sites.get(0).getX());
        SearchTree tree = new SearchTree(sites);
		SiteInfo mGoal = new SiteInfo(1, 1574, 233, 0, "nn");
		SiteInfo target = tree.searchNearestNeighbor(mGoal, tree.mTree);
		Log.v("xml", "result x: "+target.getX()+" y: "+target.getY());
        */
        //获取完成*************************
		//设置支持JavaScript脚本
		WebSettings webSettings = webView.getSettings();  
		webSettings.setJavaScriptEnabled(true);
		//webSettings.setUseWideViewPort(false);  
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		//设置支持缩放
		webSettings.setBuiltInZoomControls(true);	
	    webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//内容成单列显示出来	
		// 缩放至屏幕的大小
		webSettings.setUseWideViewPort(true); 
        webSettings.setLoadWithOverviewMode(true);  
		
		webSettings.setDatabaseEnabled(true);  
		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(dir);	
		//使用localStorage则必须打开
		webSettings.setDomStorageEnabled(true);		
		webSettings.setGeolocationEnabled(true);
		//webSettings.setGeolocationDatabasePath(dir);
		webSettings.setJavaScriptEnabled(true);
			 
		//设置WebViewClient
		webView.setWebViewClient(new WebViewClient(){   
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {   
		        view.loadUrl(url);   
		        return true;   
		    }  
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			@Override
		    public void onScaleChanged(WebView view, float oldScale, float newScale)
			{
				float s = newScale;
				float s2 = s;
			}
		});
		
		
		//设置WebChromeClient
		webView.setWebChromeClient(new WebChromeClient(){
			//处理javascript中的alert
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				//构建一个Builder来显示网页中的对话框
				Builder builder = new Builder(MapActivity.this);
				builder.setTitle("Alert");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result.confirm();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};
			//处理javascript中的confirm
			public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
				Builder builder = new Builder(MapActivity.this);
				builder.setTitle("confirm");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result.confirm();
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result.cancel();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};
			
			@Override
			//设置网页加载的进度条
			public void onProgressChanged(WebView view, int newProgress) {
				MapActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}

			//设置应用程序的标题title
			public void onReceivedTitle(WebView view, String title) {
				MapActivity.this.setTitle(title);
				super.onReceivedTitle(view, title);
			}

			public void onExceededDatabaseQuota(String url,
					String databaseIdentifier, long currentQuota,
					long estimatedSize, long totalUsedQuota,
					WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(estimatedSize * 2);
			}
			
			public void onGeolocationPermissionsShowPrompt(String origin,
					GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
			
			public void onReachedMaxAppCacheSize(long spaceNeeded,
					long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(spaceNeeded * 2);
			}
		});
		// 覆盖默认后退按钮的作用，替换成WebView里的查看历史页面  
		webView.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if ((keyCode == KeyEvent.KEYCODE_BACK)
							&& webView.canGoBack()) {
						webView.goBack();
						return true;
					}
				}
				return false;
			}
		});		
		webView.loadUrl("file:///android_res/raw/newmap.svg");		
    
   }
    
	
	@Override
	public void onResume()
	{
		super.onResume();
		isForeground = true;
		OrientationTool.setMainHandler(handler);
		MyWebView.setMainHandler(handler);
		//注册传感器监听器******************
	    mSensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);  
	    mSensorManager.registerListener(sensorEventListener, magneticSensor,SensorManager.SENSOR_DELAY_NORMAL);  
	    
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		isForeground = false;
		//注销传感器监听器******************
		mSensorManager.unregisterListener(sensorEventListener);  
		OrientationTool.setMainHandler(null);
	}
	
   @Override
   public void onDestroy()
   {   
	   stopService(intent);
	   super.onDestroy();	   
   }
   
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		Intent i;
		switch(v.getId()){
		case R.id.site_cancel:
			//隐藏站点提示
			RelativeLayout siteLayout =(RelativeLayout)findViewById(R.id.siteInf);
            siteLayout.setVisibility(View.GONE);
            MyWebView.typeFlag = false;
            //恢复正常地图图层
            TextView siteText = (TextView) findViewById(R.id.siteText);
            String siteName = siteText.getText().toString();
            String visibility = "hidden";
            webView.loadUrl("javascript:setVisibility('"+siteName+"','"+visibility+"')");	
            MyWebView.getInstance().showBaseLayer();
            MyWebView.getInstance().showMapLayer();
			break;
		case R.id.myLocation:
			//scan two-dimension code to get location
			i = new Intent(MapActivity.this,CaptureActivity.class);
            startActivityForResult(i, REQUEST_MYLOCATION);
            return;
		case R.id.near:
			i = new Intent(MapActivity.this,SiteActivity.class);
            startActivityForResult(i, REQUEST_SITE_NEAR);
            return;
		case R.id.ticket:
			i = new Intent(MapActivity.this,TrafficActivity.class);
			startActivity(i);
            return;
	
		case R.id.more:
			i = new Intent(MapActivity.this,MoreActivity.class);
            startActivity(i);
			return;	
		case R.id.facility_go:
			//操作间隔
			long dur  = System.currentTimeMillis()-facility_go_time;
			if((System.currentTimeMillis()-facility_go_time) < 3000)
			{
				Toast.makeText(this, "正在努力为您安排路线，请稍等！", Toast.LENGTH_SHORT).show();
				break;
			}
			
			facility_go_time = System.currentTimeMillis();
			//获取终点位置，请求路径
			srcLocation_px[0] = cmToPx_X(locationNow_cm[0]);
		    srcLocation_px[1] = cmToPx_Y(locationNow_cm[1]);
		    srcLocation_px[2] = 1;
		    pathDestLocation_px[0] = destLocation_px[0];
		    pathDestLocation_px[1] = destLocation_px[1];
		    pathDestLocation_px[2] = destLocation_px[2];
		    requestPath(srcLocation_px,pathDestLocation_px);
			return;			
		}
		
	}
	
	//返回键，隐藏目的地点选图标
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
            //do something...
        	if(main_bar.getVisibility() == View.GONE)
        	{
        		main_bar.setVisibility(View.VISIBLE);
    			facility_infor.setVisibility(View.GONE);
    			webView.loadUrl("javascript:setAim('"+-50+"','"+-50+"')");
    			 return true;
        	}
            
         }
        // when click the back key twice 
        if(keyCode == KeyEvent.KEYCODE_BACK && 
        		event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", 
                		Toast.LENGTH_SHORT).show();                                
                exitTime = System.currentTimeMillis();   
            } else {
            	//stopService(intent);
                finish();
                System.exit(0);
            }
            return true;   
        }
        // **************
         return super.onKeyDown(keyCode, event);
     }
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
		if(data!= null)
       {
	        if (requestCode== REQUEST_SITE_NEAR)  
	        {  
	            if (resultCode==SiteActivity.RESULT_CODE)  
	            {  
	                Bundle bundle=data.getExtras();  
	                String sitename = bundle.getString("sitename");  
	                TextView siteText = (TextView) findViewById(R.id.siteText);
	                siteText.setText(sitename);
	                RelativeLayout siteLayout =(RelativeLayout)findViewById(R.id.siteInf);
	                siteLayout.setVisibility(View.VISIBLE);
	                //隐藏其他类型的站点
	                MyWebView.getInstance().hiddenAll();
            	    String visibility =" visible";
            	    //显示制定类型
            	    MyWebView.typeFlag = true; 
            		webView.loadUrl("javascript:setVisibility('"+sitename+"','"+visibility+"')");
            	  
	            }  
	        }
	        else if(requestCode== REQUEST_MYLOCATION)
	        {  //扫码定位信息
	        	if(resultCode == RESULT_MYLOCATION)
	        	{
	        		Bundle bundle = data.getBundleExtra("location");  
	        		String[] addr = new String[5];
	        		addr = bundle.getStringArray("addr");
	                String x = addr[2];
	                String y = addr[3];
	                String z = addr[4];
	                //画出当前位置
	                webView.loadUrl("javascript:setScanResult('"+x+"','"+y+"')");	        		
	        	}
	        }

       }
    }  
	
	protected void requestPath(float[] srcLocation,float[] destLocation)
	{
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if(ConnectTool.checkConnect(this,wifiManager))
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
	
   //实际坐标到地图坐标
   public int cmToPx_X(float dimension_cm)
   {
	   int map = (int) (dimension_cm/MyWebView.P+MyWebView.offsetX);
	   return map;
   }
   public int cmToPx_Y(float dimension_cm)
   {
	   int map = (int) (dimension_cm/MyWebView.P+MyWebView.offsetY);
	   return map;
   }

   @TargetApi(13)
   public void getWindowSize()
   {
   	Display display = getWindowManager().getDefaultDisplay();
	    if (Build.VERSION.SDK_INT >= 13 ) {
	    // 使用api11 新加 api的方法
			Point size = new Point();
			display.getSize(size);
		    windowHeight = size.y;
			windowWidth = size.x;
	    }
	    else 
	    {
	        // 低版本的折衷处理方法
			windowHeight = display.getHeight();
			windowWidth = display.getWidth();
	    }
   
   }
   private void initSensors() {
		// sensor manager
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);	
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);  
        sensorEventListener = OrientationTool.sensorEventListener;    
	}
	
   //when click the map
   SearchNearestSite search = SearchNearestSite.getInstance();
   @Override
   public boolean onTouchEvent(MotionEvent event) {
	   	
	   facility_name.setText("");
	   SiteInfo goal = new SiteInfo(0, webView.webviewX0, webView.webviewY0, 0, "");	  
	   float scale = webView.scale;
	   String siteName = null;
	   try {
		   siteName = search.findClickedNearestSite(goal);
	   } catch (KeySizeException e) {
		   e.printStackTrace();
	   } catch (KeyDuplicateException e) {
		   e.printStackTrace();
	   } catch (KeyMissingException e) {
		   e.printStackTrace();
	   }

	   if (siteName != null) {
		  // Log.v("dis", ""+siteName);
		   //if (isScaleEnough(scale, siteName))
			   facility_name.setText(siteName);
	   }
	   else {
		   facility_name.setText("");
	   }
    	return super.onTouchEvent(event);
   }
   /*
   private boolean isScaleEnough(float scale, String siteName) {
	   
	   if (scale <= 0.4) {
		   if (siteName.equals("候车处") || siteName.equals("餐饮购物") 
				   || siteName.equals("停车场") || siteName.equals("旅客服务") 
				   || siteName.equals("出租车") || siteName.equals("急救中心")
				   || siteName.equals("行李寄存处") //|| siteName.equals("公交站")
				   ||siteName.equals("等待室") || siteName.equals("中餐厅")
				   || siteName.equals("礼物店") || siteName.equals("ATM")
				   || siteName.equals("失物招领处") || siteName.equals("充电处")
				   || siteName.equals("楼梯") || siteName.equals("餐厅") )
			   return false;
	   } else if (scale > 0.4 && scale <= 0.8) {
		   if (siteName.equals("旅客服务")  || siteName.equals("出租车")  
				   || siteName.equals("急救中心") || siteName.equals("行李寄存处"))
			   return false;
	   } 
	   return true;
   }
   */
   /**偏离计算*/
   double culculateNearestDistance(float[] location_cm) 
			throws KeySizeException, KeyDuplicateException {
		
		Log.v("test", "into calculate");
		Log.v("test", "k[0] and k[1]: " + location_cm[0] + "  " +location_cm[1]);
		int m = kdtree.nearest(new double[]{location_cm[0], location_cm[1]});
		Log.v("test", "find ok");

		
		printSites(location_cm);
		Log.v("sites", "return "+"x: " + sites_px[m][0] + " y: " + sites_px[m][1]);
		Log.v("sites", "return m: " + m);
		return Math.sqrt( Math.pow(cmToPx_X(location_cm[0]) - sites_px[m][0],  2.0)  + 
									   Math.pow(cmToPx_Y(location_cm[1]) - sites_px[m][1],  2.0));		

//		return Math.sqrt( Math.pow(location_cm[0] - sites_px[m][0],  2)  + 
//									   Math.pow(location_cm[1] - sites_px[m][1],  2));		

   }
    
   //test
   void printSites(float[] current) {
	   
	   int i;
	   Log.v("sites","current: " + "x: "+cmToPx_X(current[0]) + " y:" + cmToPx_Y(current[1]));
	   for (i = 0; i < sites_px.length; i++) {
		   Log.v("sites", "all sites "+i+"--x: " + sites_px[i][0] + " y: " + sites_px[i][1]);
	   }
   }
   
   /**显示导引路线*/
   private void showRoute(JSONObject obj) throws JSONException{
	   
   
	    main_bar.setVisibility(View.VISIBLE);
	    facility_infor.setVisibility(View.GONE);
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
   /**
    * 显示设施名称*/
   private void showfacilityName(JSONObject obj) throws JSONException{
	    main_bar.setVisibility(View.GONE);
		facility_infor.setVisibility(View.VISIBLE);
		destLocation_px[0] = obj.getInt("x");
		destLocation_px[1] = obj.getInt("y");
		destLocation_px[2] = 1;
		//facility_name.setText(text);
  }
   /**
    * 更新行人坐标
    * 收到的定位信息单位：cm
    * */
   private void updateLocation(JSONObject obj) throws JSONException{
	   if(obj.getInt("x") ==0 &&obj.getInt("y") == 0)
	   {
		   return;
	   }
	    locationNow_cm[0] = obj.getInt("x"); //unit:CM
		locationNow_cm[1] = obj.getInt("y"); 
		locationNow_cm[2] = obj.getInt("z");
		//recordText.setText(locationNow_cm[0]+" "+locationNow_cm[1]);
		Log.v("test", "x: " + locationNow_cm[0] );
		Log.v("test", "y: " + locationNow_cm[1]);
		Log.v("test", "destination x: "+ destLocation_px[0]);
		Log.v("test", "destination y: "+ destLocation_px[1]);
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
			//webView.loadUrl("javascript:drawcircle('"+x+"','"+y+"')");
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
				requestPath(new float[]
					{cmToPx_X(locationNow_cm[0]),  cmToPx_Y(locationNow_cm[1]), locationNow_cm[2]},  destLocation_px);
			}
		}
   }
   /**更新行人方位*/
   private void updateOrientation(JSONObject obj) throws JSONException{
	 
	   angle = obj.getDouble("angle");
	   webView.loadUrl("javascript:setPointer('"+angle+"','"+cmToPx_X(locationOld_cm[0])+"','"+cmToPx_Y(locationOld_cm[1])+"')");
	  // Log.i("角度",locationOld_cm[0]+" "+locationOld_cm[1]);
   }
  
}