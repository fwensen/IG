package com.uestc.Indoorguider.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.R;
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
import com.uestc.Indoorguider.map.search_destination.SearchDestination;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//Washington and Lee University: kdtree, http://home.wlu.edu/~levys/software/kd/docs/
import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeyMissingException;
import edu.wlu.cs.levy.CG.KeySizeException;
import com.uestc.Indoorguider.map.search_destination.*;
//����android2.3��javascript����������
//http://code.google.com/p/android/issues/detail?id=12987
public class MapActivity extends APPActivity implements OnClickListener, SearchDestination.SearchViewListener{
    private boolean firstData  = true;//��һ���յ�����
	private final static int MinDistance_px = 1000;
	private static  MyWebView webView = null;
	private LinearLayout myLocation = null;
	private LinearLayout near = null;
	private LinearLayout ticket = null;
	private LinearLayout more = null;
	private LinearLayout main_bar,facility_infor,facility_go = null;
    private TextView facility_name ;//��ʩ����
	//private TextView recordText ;//���Լ�¼
	
	public static int windowHeight ,windowWidth;
    private float[] srcLocation_px = new float[3] ;
	private float[] destLocation_px = new float[3]; //��¼Ŀ����ʩλ�ã����ڵ�������unit:px
	private float[] pathDestLocation_px = new float[3]; //��¼·�������Ŀ�ĵ�λ�ã����ڵ�������unit:px
	
	
	public static Boolean isForeground = true;//�Ƿ�λ����ǰ��foreground process

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
	 * ��һ�ζ�λλ��
	 * unit:cm
	 */
	private float[] locationOld_cm = {854,7541,1};
	/**
	 * ����λ��
	 * ��λ��cm
	 */
	private float[] locationNow_cm = {20000,20000,1};
	
	double angle = 0;//���˷�λ���ɴ�������ȡ����

	private SensorManager mSensorManager;// �������������
	private Sensor accSensor; 
	private Sensor stepSensor; 
	private Sensor magneticSensor;  
	private SensorEventListener sensorEventListener;
	Intent intent;
	long facility_go_time = 0;//��ʩ�㰴��ʱ��
	boolean isMove =true;
	
    // ��������б�view
    private ListView lvResults;
    private SearchDestination searchView;
    //���ѿ��б�adapter
    private ArrayAdapter<String> hintAdapter;
     // �Զ���ȫ�б�adapter
    private ArrayAdapter<String> autoCompleteAdapter;
    //��������б�adapter
    private SearchAdapter resultAdapter;
    private List<DestinationSite> dbData;
    // ���Ѱ�����
    private List<String> hintData;

     //�����������Զ���ȫ����
    private List<String> autoCompleteData;

     //�������������
    private List<DestinationSite> resultData;
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * ��ʾ����ʾ��ĸ���
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

	@Override
	protected void handleResult(JSONObject obj) 
	{
		try {
			switch(obj.getInt("typecode"))
			{
				case Constant.LOCATION_WIFI_SUCCESS://����λ�ø��£�wifi��λ��
					 updateLocation(obj);
					 break;
				case Constant.ACCELERATOR:
					 isMove = obj.getBoolean("ismove");
				     break;
				case Constant.LOCATION_WIFI_ERROR:
					break;
				case Constant.ORIENTATION://���˳������
					updateOrientation(obj);
					break;
				case Constant.GUIDE_SUCCESS://����·�߸��£������Ե�ͼΪԭ��
					showRoute(obj);
					break;
				case Constant.GUIDE_ERROR:
					Toast.makeText(MapActivity.this, "·�����󲻳ɹ����뻻���ص�λ���ԣ�", Toast.LENGTH_SHORT).show();
					break;
				case Constant.FACILITY_INFOR://վ��ƥ��ɹ����������ơ����꣬��ʾ
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
        initSensors();// ��ʼ����������λ�÷���
        
        searchView = (SearchDestination) findViewById(R.id.main_search_layout);
        searchView.setSearchViewListener(this);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
        //��������
        //���ü���
        
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
        //��ȡλ��XML�ļ�****************
        /*
        Log.v("xml", "0: "+sites.get(0).getX());
        SearchTree tree = new SearchTree(sites);
		SiteInfo mGoal = new SiteInfo(1, 1574, 233, 0, "nn");
		SiteInfo target = tree.searchNearestNeighbor(mGoal, tree.mTree);
		Log.v("xml", "result x: "+target.getX()+" y: "+target.getY());
        */
        //��ȡ���*************************
		//����֧��JavaScript�ű�
		WebSettings webSettings = webView.getSettings();  
		webSettings.setJavaScriptEnabled(true);
		//webSettings.setUseWideViewPort(false);  
		//���ÿ��Է����ļ�
		webSettings.setAllowFileAccess(true);
		//����֧������
		webSettings.setBuiltInZoomControls(true);	
	    webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//���ݳɵ�����ʾ����	
		// ��������Ļ�Ĵ�С
		webSettings.setUseWideViewPort(true); 
        webSettings.setLoadWithOverviewMode(true);  
		
		webSettings.setDatabaseEnabled(true);  
		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(dir);	
		//ʹ��localStorage������
		webSettings.setDomStorageEnabled(true);		
		webSettings.setGeolocationEnabled(true);
		//webSettings.setGeolocationDatabasePath(dir);
		webSettings.setJavaScriptEnabled(true);
			 
		//����WebViewClient
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
		
		
		//����WebChromeClient
		webView.setWebChromeClient(new WebChromeClient(){
			//����javascript�е�alert
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				//����һ��Builder����ʾ��ҳ�еĶԻ���
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
			//����javascript�е�confirm
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
			//������ҳ���صĽ�����
			public void onProgressChanged(WebView view, int newProgress) {
				MapActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}

			//����Ӧ�ó���ı���title
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
		// ����Ĭ�Ϻ��˰�ť�����ã��滻��WebView��Ĳ鿴��ʷҳ��  
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
		webView.loadUrl("file:///android_res/raw/spot.svg");		
    
   }
    
	
	@Override
	public void onResume()
	{
		super.onResume();
		isForeground = true;
		OrientationTool.setMainHandler(handler);
		MyWebView.setMainHandler(handler);
		//ע�ᴫ����������******************
	    mSensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);  
	    mSensorManager.registerListener(sensorEventListener, magneticSensor,SensorManager.SENSOR_DELAY_NORMAL);  
	    
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		isForeground = false;
		//ע��������������******************
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
			//����վ����ʾ
			RelativeLayout siteLayout =(RelativeLayout)findViewById(R.id.siteInf);
            siteLayout.setVisibility(View.GONE);
            MyWebView.typeFlag = false;
            //�ָ�������ͼͼ��
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
			//�������
			long dur  = System.currentTimeMillis()-facility_go_time;
			if((System.currentTimeMillis()-facility_go_time) < 3000)
			{
				Toast.makeText(this, "����Ŭ��Ϊ������·�ߣ����Եȣ�", Toast.LENGTH_SHORT).show();
				break;
			}
			
			facility_go_time = System.currentTimeMillis();
			//��ȡ�յ�λ�ã�����·��
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
	
	//���ؼ�������Ŀ�ĵص�ѡͼ��
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
                Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", 
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
	                //�����������͵�վ��
	                MyWebView.getInstance().hiddenAll();
            	    String visibility =" visible";
            	    //��ʾ�ƶ�����
            	    MyWebView.typeFlag = true; 
            		webView.loadUrl("javascript:setVisibility('"+sitename+"','"+visibility+"')");
            	  
	            }  
	        }
	        else if(requestCode== REQUEST_MYLOCATION)
	        {  //ɨ�붨λ��Ϣ
	        	if(resultCode == RESULT_MYLOCATION)
	        	{
	        		Bundle bundle = data.getBundleExtra("location");  
	        		String[] addr = new String[5];
	        		addr = bundle.getStringArray("addr");
	                String x = addr[2];
	                String y = addr[3];
	                String z = addr[4];
	                //������ǰλ��
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
	
   //ʵ�����굽��ͼ����
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
	    // ʹ��api11 �¼� api�ķ���
			Point size = new Point();
			display.getSize(size);
		    windowHeight = size.y;
			windowWidth = size.x;
	    }
	    else 
	    {
	        // �Ͱ汾�����Դ�����
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
		   if (siteName.equals("�򳵴�") || siteName.equals("��������") 
				   || siteName.equals("ͣ����") || siteName.equals("�ÿͷ���") 
				   || siteName.equals("���⳵") || siteName.equals("��������")
				   || siteName.equals("����Ĵ洦") //|| siteName.equals("����վ")
				   ||siteName.equals("�ȴ���") || siteName.equals("�в���")
				   || siteName.equals("�����") || siteName.equals("ATM")
				   || siteName.equals("ʧ�����촦") || siteName.equals("��紦")
				   || siteName.equals("¥��") || siteName.equals("����") )
			   return false;
	   } else if (scale > 0.4 && scale <= 0.8) {
		   if (siteName.equals("�ÿͷ���")  || siteName.equals("���⳵")  
				   || siteName.equals("��������") || siteName.equals("����Ĵ洦"))
			   return false;
	   } 
	   return true;
   }
   */
   /**ƫ�����*/
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
   
   /**��ʾ����·��*/
   private void showRoute(JSONObject obj) throws JSONException{
	   
   
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
    * ��ʾ��ʩ����*/
   private void showfacilityName(JSONObject obj) throws JSONException{
	    main_bar.setVisibility(View.GONE);
		facility_infor.setVisibility(View.VISIBLE);
		destLocation_px[0] = obj.getInt("x");
		destLocation_px[1] = obj.getInt("y");
		destLocation_px[2] = 1;
		//facility_name.setText(text);
  }
   /**
    * ������������
    * �յ��Ķ�λ��Ϣ��λ��cm
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
			//���� �Ƕȣ�λ��x,y
			webView.loadUrl("javascript:setPointer('"+OrientationTool.angle+"','"+cmToPx_X(locationOld_cm[0])+"','"+cmToPx_Y(locationOld_cm[1])+"')");
		    locationOld_cm[0] = locationNow_cm[0];
		    locationOld_cm[1] = locationNow_cm[1];
		    locationOld_cm[2] = locationNow_cm[2];
		    firstData = false;
		}
	 
		if((Math.pow(locationOld_cm[0] - locationNow_cm[0],2) + Math.pow(locationOld_cm[1]-locationNow_cm[1],2)) > Math.pow(50,2) && isMove )//1m=20px
		{
			//webView.loadUrl("javascript:drawcircle('"+x+"','"+y+"')");
			//���� �Ƕȣ�λ��x,y
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
   /**�������˷�λ*/
   private void updateOrientation(JSONObject obj) throws JSONException{
	 
	   angle = obj.getDouble("angle");
	   webView.loadUrl("javascript:setPointer('"+angle+"','"+cmToPx_X(locationOld_cm[0])+"','"+cmToPx_Y(locationOld_cm[1])+"')");
	  // Log.i("�Ƕ�",locationOld_cm[0]+" "+locationOld_cm[1]);
   }

   
   
   /**
    * ������ʾ����ʾ��ĸ���
    *
    * @param hintSize ��ʾ����ʾ����
    */
   public static void setHintSize(int hintSize) {
   	MapActivity.hintSize = hintSize;
   }
   
   
   //�Զ���ȫ���ص�
   @Override
   public void onRefreshAutoComplete(String text) {
	 //��������
      getAutoCompleteData(text);
	
   }	

   //��ѯ���ص�
   @Override
   public void onSearch(String text) {
	   //����result����
       getResultData(text);
       lvResults.setVisibility(View.VISIBLE);
       //��һ�λ�ȡ��� ��δ����������
       if (lvResults.getAdapter() == null) {
           //��ȡ�������� ����������
           lvResults.setAdapter(resultAdapter);
       } else {
           //������������
           resultAdapter.notifyDataSetChanged();
       }
       Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();

	
   }

   /**
    * ��ʼ������
    */
   private void initData() {
       //�����ݿ��ȡ����
       getDbData();
       //��ʼ�����Ѱ�����
       getHintData();
       //��ʼ���Զ���ȫ����
       getAutoCompleteData(null);
       //��ʼ�������������
       getResultData(null);
   }

   /**
    * ��ȡdb ����
    */
   private void getDbData() {
       int size = 100;
       dbData = new ArrayList<>(size);
       for (int i = 0; i < size; i++) {
           dbData.add(new DestinationSite(R.drawable.icon, "android�����ر�����" + (i + 1), "Android�Զ���view�����Զ�������view"));
       }
   }

   /**
    * ��ȡ���Ѱ�data ��adapter
    */
   private void getHintData() {
       hintData = new ArrayList<>(hintSize);
       for (int i = 1; i <= hintSize; i++) {
           hintData.add("���Ѱ�" + i + "��Android�Զ���View");
       }
       hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
   }

   /**
    * ��ȡ�Զ���ȫdata ��adapter
    */
   private void getAutoCompleteData(String text) {
       if (autoCompleteData == null) {
           //��ʼ��
           autoCompleteData = new ArrayList<>(hintSize);
       } else {
           // ����text ��ȡauto data
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
           autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
       } else {
           autoCompleteAdapter.notifyDataSetChanged();
       }
   }

   /**
    * ��ȡ�������data��adapter
    */
   private void getResultData(String text) {
       if (resultData == null) {
           // ��ʼ��
           resultData = new ArrayList<>();
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

}