package com.uestc.Indoorguider.traffic;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.uestc.Indoorguider.R;
/**
 * 机场线时间表
 * @author vincent
 *
 */
public class SubwayAirLineShowActivity extends Activity {

	/**
	 * webview
	 */
	WebView mWebView;
	WebSettings settings;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.subway_air_details_webview);
		initViews();
	}
	 
	@SuppressWarnings({ "deprecation", "deprecation" })
	void initViews() {
		mWebView = (WebView) findViewById(R.id.wv_subway_air_detail);
		mWebView.setWebViewClient(new WebViewClient(){
		        
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
		      view.loadUrl(url);
		      return true;
		  }
		});
		
		settings = mWebView.getSettings(); 
		settings.setSupportZoom(true); // 支持缩放  
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);  
		//settings.setUseWideViewPort(true);// 这个很关键
		//settings.setLoadWithOverviewMode(true);
		mWebView.loadUrl("file:///android_res/raw/subway_air_lines.html");
	 }
}