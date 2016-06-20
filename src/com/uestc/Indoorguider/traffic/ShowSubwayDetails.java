package com.uestc.Indoorguider.traffic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.uestc.Indoorguider.R;

public class ShowSubwayDetails extends Activity{

	WebView mWebView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.subway_show_detail_webview);
		initViews();
	}
	
	private void initViews() {
		Intent intent = getIntent();
		//获取该intent所携带的数据
		Bundle data = intent.getExtras();
		int id = (int)data.get("line_no");
		Log.v("Subway", "id: " + id);
		mWebView = (WebView) findViewById(R.id.wv_subway_detail);
		mWebView.loadUrl("file:///android_res/raw/subway_line_"+ id +".html");
		mWebView.setWebViewClient(new WebViewClient(){
	        
			@Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            view.loadUrl(url);
	            return true;
	       }
	    });
		WebSettings settings = mWebView.getSettings(); 
		//settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		//settings.setUseWideViewPort(true); 
		settings.setSupportZoom(true); // 支持缩放  
        //settings.setLoadWithOverviewMode(true); 
	}
}
