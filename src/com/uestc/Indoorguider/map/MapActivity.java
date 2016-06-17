package com.uestc.Indoorguider.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.more.MoreActivity;
import com.uestc.Indoorguider.orientation.OrientationTool;
import com.uestc.Indoorguider.site_show.SearchNearestSite;
import com.uestc.Indoorguider.site_show.SiteActivity;
import com.uestc.Indoorguider.traffic.TrafficActivity;
import com.uestc.Indoorguider.util.SiteInfo;
import com.uestc.Indoorguider.zxing_view.CaptureActivity;
import com.uestc.Indoorguider.map.search_destination.SearchDestination;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeyMissingException;
import edu.wlu.cs.levy.CG.KeySizeException;
import com.uestc.Indoorguider.map.search_destination.*;
//关于android2.3中javascript交互的问题
//http://code.google.com/p/android/issues/detail?id=12987
public class MapActivity extends APPActivity implements OnClickListener, SearchDestination.SearchViewListener{
    private MapUtils mapUtils;
    private WebViewUtils webUtils;
    
	private static  MyWebView webView = null;
	
	JSONObject path; //记录导引路径，供楼层切换
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
	
	
	public static Boolean isForeground = true;//是否位于最前面foreground process ,用于告诉服务器是否返回定位信息

	WifiManager wifiManager;
	
	
	File tfile,mypath;
	String TAG ="scale";
	public final static int REQUEST_MYLOCATION = 0;
	public final static int REQUEST_SITE_NEAR = 1;
	public final static int RESULT_MYLOCATION = 10;

	/**
	 * 上一次定位位置
	 * unit:cm
	 */
	private float[] locationOld_cm = {15200,540,1};
	/**
	 * 最新位置
	 * 单位：cm
	 */
	private float[] locationNow_cm = {20000,20000,1};
	private static int currentLayer = 1;//用户当前楼层
	
	

	private SensorManager mSensorManager;// 传感器管理对象
	private Sensor accSensor; 
	private Sensor magneticSensor;  
	private SensorEventListener sensorEventListener;
	Intent intent;
	long facility_go_time = 0;//设施点按键时间
	boolean isMove =true;
	
    /**
     *  搜索结果列表view
     */
    //private ListView lvResults;
    private SearchDestination searchView;
    //热搜框列表adapter
    private ArrayAdapter<String> hintAdapter;
     // 自动补全列表adapter
    private ArrayAdapter<String> autoCompleteAdapter;
    //搜索结果列表adapter
    private SearchAdapter resultAdapter;
    private List<DestinationSite> dbData;
    // 热搜版数据
    private List<String> hintData;

     //搜索过程中自动补全数据
    private List<String> autoCompleteData;

     //搜索结果的数据
    private List<DestinationSite> resultData;
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;
    public int getCurrentLayer()
    {
    	return currentLayer;
    }
    
    boolean canGo = false;
    boolean canBack = false;
	@Override
	protected void handleResult(JSONObject obj) 
	{
		try {
			switch(obj.getInt("typecode"))
			{
				case Constant.LOCATION_WIFI_SUCCESS://行人位置更新（wifi定位）
					int layer = obj.getInt("z");
					 if(layer != currentLayer ){
							 currentLayer = layer;
							 if(canGo)
							 {
								 webView.goForward();
								 canGo = false;
								 canBack = true;
							 }else if(canBack){
								 webView.goBack();
								 canGo = true;
								 canBack = false;
							 }
							 else{
								 switchLayer(layer);
								 canBack = true;
							 }
							 
						 }
						 if(path != null){
							 mapUtils.showRouteMultiLayer(path, srcLocation_px, pathDestLocation_px);
						 }
					 
					 mapUtils.updateLocationWithCorrect(locationNow_cm, locationOld_cm, destLocation_px, obj);
					 break;
				case Constant.ACCELERATOR:
					 isMove = obj.getBoolean("ismove");
				     break;
				case Constant.LOCATION_WIFI_ERROR:
					break;
				case Constant.ORIENTATION://行人朝向更新
					mapUtils.updateOrientation(obj, locationNow_cm);
					break;
				case Constant.GUIDE_SUCCESS://导引路线更新，返回以地图为原点
					path = obj;
					mapUtils.showRouteMultiLayer(path, srcLocation_px, pathDestLocation_px);
					webView.loadUrl("javascript:setVisibility('L7','hidden')");
					break;
				case Constant.GUIDE_ERROR:
					Toast.makeText(MapActivity.this, "路线请求不成功，请换个地点重试！", Toast.LENGTH_SHORT).show();
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
        initData();
        initView();
        initContent();
        initSensors();// 初始化传感器和位置服务
        //lvResults = (ListView) findViewById(R.id.main_lv_search_results);
       
        //开启服务
	    Intent intent = new Intent();
	    intent.setAction("com.uestc.Indoorguider.util.UtilService");	    
	    startService(intent);
        getWindowSize();
       
        
       
      
   }
    
	
	@Override
	public void onResume()
	{
		super.onResume();
		isForeground = true;
		OrientationTool.setMainHandler(handler);
		MyWebView.setMainHandler(handler);
		//注册传感器监听器
	    mSensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);  
	    mSensorManager.registerListener(sensorEventListener, magneticSensor,SensorManager.SENSOR_DELAY_NORMAL);  
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		isForeground = false;
		//注销传感器监听器
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
            //webUtils.setVisibility(siteName,visibility);
            MyWebView.getInstance().showBaseLayer();
           // MyWebView.getInstance().showMapLayer();
			break;
		case R.id.myLocation:
			//scan two-dimension code to get location
			Intent intent  = new Intent(this,ScanResultActivity.class);
			intent.putExtra("layer", 1);
			startActivity(intent);
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
			CmToPxCalculator calculator = new CmToPxCalculator();
			//选择计算策略
			if(currentLayer == Constant.LAYER_NEGATIVE1)
			{
				calculator.setStrategy(new LayerNegative1CmToPxSrategy());
				
			}else{
				calculator.setStrategy(new Layer1CmToPxSrategy());
				
			}
			srcLocation_px[0] = calculator.calculatorX(locationNow_cm[1]);
		    srcLocation_px[1] = calculator.calculatorY(locationNow_cm[0]);
		    srcLocation_px[2] = locationNow_cm[2];
		    pathDestLocation_px[0] = destLocation_px[0];
		    pathDestLocation_px[1] = destLocation_px[1];
		    pathDestLocation_px[2] = currentLayer;
		    mapUtils.requestPath(srcLocation_px,pathDestLocation_px);
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
    			webUtils.setAim("-50", "-50");
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
            	    String visibility ="visible";
            	    //显示制定类型
            	    MyWebView.typeFlag = true; 
            	    webUtils.setVisibility(sitename, visibility);
            	  
	            }  
	        }
	        else if(requestCode== REQUEST_MYLOCATION)
	        {  //扫码定位信息,使用新activity打开
	        	if(resultCode == RESULT_MYLOCATION)
	        	{
	        		Intent i  = new Intent(this,ScanResultActivity.class);
	        		Bundle bundle = data.getBundleExtra("location");  
	        		i.putExtra("location", bundle);
	        		 // webUtils.setScanResult("1000","1000");
	        		startActivity(i);        	
	        	               	     
	        	}
	        }

       }
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
    * 设置提示框显示项的个数
    *
    * @param hintSize 提示框显示个数
    */
   public static void setHintSize(int hintSize) {
   	MapActivity.hintSize = hintSize;
   }
   
   
   /**
    * 自动补全，回调(non-Javadoc)
    * @see com.uestc.Indoorguider.map.search_destination.SearchDestination.SearchViewListener#onRefreshAutoComplete(java.lang.String)
    */
   @Override
   public void onRefreshAutoComplete(String text) {
	 //更新数据
      getAutoCompleteData(text);
	
   }	

   /**
    * 查询，回调, 完成搜索时的工作(non-Javadoc)
    * @see com.uestc.Indoorguider.map.search_destination.SearchDestination.SearchViewListener#onSearch(java.lang.String)
    */
   @Override
   public void onSearch(String text) {
	   //更新result数据
       //getResultData(text);
      SearchSitesPositions instance = SearchSitesPositions.getInstance();
       //mapUtil srcLocation_px pathDestLocation_px
      Log.v("search", "text: " + text);
      SiteInfo ret = instance.search(text);
      if (ret == null) {
    	  Toast.makeText(this, "目的地点不存在", Toast.LENGTH_LONG).show();
    	  return;
      }
      pathDestLocation_px[0] = (float) ret.getX();
      pathDestLocation_px[1] = (float) ret.getY();
      pathDestLocation_px[2] = 1;
      Log.v("search", "ret: " + "x: " + pathDestLocation_px[0] + " y: " + pathDestLocation_px[1]);
      webView.loadUrl("javascript:setAim('"+pathDestLocation_px[0]+"','"+pathDestLocation_px[1]+"')");
      mapUtils.requestPath(srcLocation_px, pathDestLocation_px);
   }

   /**
    * 初始化数据
    */
   private void initData() {
       //从数据库获取数据
       getDbData();
       //初始化热搜版数据
       getHintData();
       //初始化自动补全数据
       getAutoCompleteData(null);
       //初始化搜索结果数据
       getResultData(null);
   }

   /**
    * 获取db 数据
    */
   private void getDbData() {
       int size = 10;
       dbData = new ArrayList<DestinationSite>(size);
       SearchSitesPositions instance = SearchSitesPositions.getInstance();
       List<String> sites = instance.getAllSitesNames();
       
       for (String name : sites) {
           dbData.add(new DestinationSite(R.drawable.icon, name, name));
       }
       
   }

   /**
    * 获取热搜版data 和adapter
    */
   private void getHintData() {
       hintData = new ArrayList<String>(hintSize);
       
       hintData.add("洗手间");
       hintData.add("地铁入口");
       hintData.add("长途汽车站");
       hintData.add("公交 132 东直门枢纽站-望京北路东口");
       hintAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hintData);
   }

   /**
    * 获取自动补全data 和adapter
    */
   private void getAutoCompleteData(String text) {
       if (autoCompleteData == null) {
           //初始化
           autoCompleteData = new ArrayList<String>(hintSize);
       } else {
           // 根据text 获取auto data
           autoCompleteData.clear();
           for (int i = 0, count = 0; i < dbData.size()
                   && count < hintSize; i++) {
               if (dbData.get(i).getTitle().contains(text.trim())) {
                   autoCompleteData.add(dbData.get(i).getTitle());
                   count++;
               }
           }
       }
       if (autoCompleteAdapter == null) {
           autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autoCompleteData);
       } else {
           autoCompleteAdapter.notifyDataSetChanged();
       }
   }

   /**
    * 获取搜索结果data和adapter
    */
   private void getResultData(String text) {
       if (resultData == null) {
           // 初始化
           resultData = new ArrayList<DestinationSite>();
       } else {
           resultData.clear();
           for (int i = 0; i < dbData.size(); i++) {
               if (dbData.get(i).getTitle().contains(text.trim())) {
                   resultData.add(dbData.get(i));
               }
           }
       }
       if (resultAdapter == null) {
           resultAdapter = new SearchAdapter(this, resultData, R.layout.search_site);
       } else {
           resultAdapter.notifyDataSetChanged();
       }
   }

@Override
protected void initView() {
	// TODO Auto-generated method stub
	setContentView(R.layout.main);
	webView = (MyWebView) findViewById(R.id.webview);
	webView.requestFocus();
	searchView = (SearchDestination) findViewById(R.id.main_search_layout);
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
}

@Override
protected void initContent() {
	  mapUtils = new MapUtils(this,webView);
	  webUtils = new WebViewUtils(webView);
	  searchView.setSearchViewListener(this);
      searchView.setTipsHintAdapter(hintAdapter);
      searchView.setAutoCompleteAdapter(autoCompleteAdapter);
	  configWebView();

  }




private void configWebView(){

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
    // TODO Auto-generated method stub
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
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
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
	
	switchLayer(1);
	//webView.loadUrl("file:///android_res/raw/layer1.svg");		
	
   }

   static void setLayer(int layer){
	 currentLayer = layer;
	
   }
   static int getLayer(){
     return currentLayer;
		
	}
   private void switchLayer(int layer){
	   webUtils.setLayer(layer);
	   setLayer(layer);
	   
	   
   }
}