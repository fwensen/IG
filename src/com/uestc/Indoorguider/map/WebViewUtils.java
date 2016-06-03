package com.uestc.Indoorguider.map;

import android.content.Context;

public class WebViewUtils {
	MyWebView webView;
	WebViewUtils(MyWebView webView){
		this.webView = webView;
		
	}
    
	public void setLayer(int layer)
	{
		webView.loadUrl("file:///android_res/raw/layer"+layer+".svg");	
		
	}
	public void setScanResult(String x, String y)
	{
	    webView.loadUrl("javascript:setScanResult('"+x+"','"+y+"')");
		
	}
	
	public void setVisibility(String siteName, String visibility)
	{
	    webView.loadUrl("javascript:setVisibility('"+siteName+"','"+visibility+"')");	
		
	}
	
	public void setAim(String x, String y)
	{	
	    webView.loadUrl("javascript:setAim('"+x+"','"+y+"')");
	}
	
	

}
