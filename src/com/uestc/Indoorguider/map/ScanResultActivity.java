package com.uestc.Indoorguider.map;

import org.json.JSONObject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uestc.Indoorguider.APPActivity;
import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.zxing_view.CaptureActivity;

public class ScanResultActivity extends APPActivity{
	private WebViewUtils webUtils;
	MyWebView webView;
	int currentLayer;
    public static final String SCAN_RESULT_ACTION = "com.uestc.Indoorguider.map.scan_result_action";
    public static final String SCAN_RESULT_CATRGORY_CLOSE = "com.uestc.Indoorguider.map.scan_result_category_close";
    public static final String SCAN_RESULT_CATRGORY_SHOW_LOCATION = "com.uestc.Indoorguider.map.scan_result_category_show_location";
	
	class scanResultBroadCastreceiver extends  BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getCategories().contains(SCAN_RESULT_CATRGORY_SHOW_LOCATION)){
				Log.i("resultActivity", "onreceive!");
				Bundle bd =intent.getBundleExtra("location");
				String[] addr = bd.getStringArray("addr");
				String layer = addr[4];
				if(layer.equals("1"))
				{
					layer = "2";
					
				}else{
					layer = "1";
				}
				
				if(layer.equals(currentLayer+"")){
					 String x = addr[2];
				     String y = addr[3];
				     webUtils.setScanResult(x, y);
				}else{
					ScanResultActivity.this.finish();
				}
			}else{
				ScanResultActivity.this.finish();
				
				
			}
			
			
		}
		
	}
	
	

	@Override
	protected void handleResult(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		Log.i("resultActivity", "initView!");
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_scan_result);
	    webView = (MyWebView) findViewById(R.id.webview); 
	    configWebView();
	    Intent  i =  getIntent();
	    currentLayer = i.getIntExtra("layer", 1);
	    webView.loadUrl("file:///android_res/raw/layer"+ currentLayer +".svg");
	    Log.i("resultActivity", "initView!");
	    
		
	}

	@Override
	protected void initContent() {
		// TODO Auto-generated method stub
		((TextView)findViewById(R.id.title_text)).setText("扫码定位结果");
		((ImageButton)findViewById(R.id.back_icon)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScanResultActivity.this.finish();
			}
			
		});
		webUtils = new WebViewUtils(webView);
		scanResultBroadCastreceiver receiver = new scanResultBroadCastreceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SCAN_RESULT_ACTION);
		filter.addCategory(SCAN_RESULT_CATRGORY_SHOW_LOCATION);
		filter.addCategory(SCAN_RESULT_CATRGORY_CLOSE);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		this.registerReceiver(receiver,filter);
		if(currentLayer == 1)
		{
			Intent intent2  = new Intent(this,ScanResultActivity.class);
			intent2.putExtra("layer", 2);
			startActivity(intent2);
			
		}else{
			Intent i = new Intent(this,CaptureActivity.class);
			startActivity(i);
			
		}
		

	}
	
	protected void onDestroy(){
		super.onDestroy();
		Log.i("resultActivity", "ondestroyed!");
		
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
		
	   }

}
