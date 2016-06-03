package com.uestc.Indoorguider.map;

import org.json.JSONObject;


import android.content.Intent;
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
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			webUtils.setScanResult(msg.arg1+"",msg.arg2+"");
		
			
		}
		
	} ;
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
			String[] addr = new String[5];
			addr =intent.getBundleExtra("location").getStringArray("addr");
	        String x = addr[2];
	        String y = addr[3];
	        String z = addr[4];
	        
	        if(z.equals("1"))
	        {
	        	z = "2";
	        	
	        }else{
	        	z ="1";
	        }
	        if(z.equals("1"))
	        {
//	        	webUtils.setLayer(Integer.parseInt(z));
//	        	Message msg = handler.obtainMessage();
//	        	msg.arg1 = Integer.parseInt(x);
//	        	msg.arg2 = Integer.parseInt(y);
//	        	handler.sendMessage(msg);
	        	webView.goBack();
	        }
	        webUtils.setScanResult(x, y);
	       
	        Log.i("resultActivity", "onnewIntent!");
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
	   // webView.loadUrl("file:///android_res/raw/layer"+1+".svg");
	    
		
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
		currentLayer = getIntent().getIntExtra("layer", 1);
		
		
		if(currentLayer == 1)
		{
			webUtils.setLayer(1);
			Intent i = new Intent(this,ScanResultActivity.class);
			i.putExtra("layer", 2);
			startActivity(i);
		}else{
			webUtils.setLayer(2);
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
