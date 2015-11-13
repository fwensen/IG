package com.uestc.Indoorguider.history;

import java.util.List;
import com.uestc.Indoorguider.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class HistoryPathShow extends Activity implements
					SeekBar.OnSeekBarChangeListener{
	
	SeekBar mSeekBar;
	private TextView description;
	private static  WebView webView = null;
	private static final String TAG = "pathshow"; 
	private ImageButton backButton;
	private String timePoint ;
	TextView tvTitle;   //标题
	//TextView prop_show; //显示进度
	@SuppressLint("NewApi") @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history_path_show);
        Log.v(TAG, "before find view by id");     
        
        initData();     //初始化
       //设置支持JavaScript脚本
		WebSettings webSettings = webView.getSettings();  
		Log.v(TAG, "find view by id");
		webSettings.setJavaScriptEnabled(true);
		//webSettings.setUseWideViewPort(false);  
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		//设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//内容成单列显示出来
		webSettings.setDisplayZoomControls(false);    //缩小控件隐藏
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
			
		Log.v(TAG, "before set webview client");
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
			});
			
			
			//设置WebChromeClient
		webView.setWebChromeClient(new WebChromeClient(){
				//处理javascript中的alert
				public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
					//构建一个Builder来显示网页中的对话框
					Builder builder = new Builder(HistoryPathShow.this);
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
					Builder builder = new Builder(HistoryPathShow.this);
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
					HistoryPathShow.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
					super.onProgressChanged(view, newProgress);
				}

				//设置应用程序的标题title
			public void onReceivedTitle(WebView view, String title) {
					HistoryPathShow.this.setTitle(title);
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
						
			Log.v(TAG, "load path over");
			
	}
	
	//初始化
	public void initData() {
		
		tvTitle = (TextView) findViewById(R.id.title_text);
		//prop_show = (TextView) findViewById(R.id.history_seekbar_description);
		backButton = (ImageButton) findViewById(R.id.back_icon);
		//返回键
		backButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent clickIntent = new Intent(HistoryPathShow.this, HistoryActivity.class);
				finish();
				startActivity(clickIntent);					
			}
		});
       webView = (WebView) findViewById(R.id.history_webview);
       mSeekBar = (SeekBar)this.findViewById(R.id.history_seekbar);
       mSeekBar.setOnSeekBarChangeListener(this);
       description=(TextView)findViewById(R.id.history_seekbar_description);
		
		Intent intent = getIntent();
		//获取该intent所携带的数据
		Bundle data = intent.getExtras();
		HistoryItem item = (HistoryItem)data.getSerializable("historyitem");
		tvTitle.setText(item.getStartTime()+"-"+item.getEndTime());
		pthInSites = item.getPath();
		mPath = getPathFromSites(pthInSites);
		pathLen = pthInSites.size();
		webView.loadUrl("file:///android_res/raw/spot1.svg");
		webView.loadUrl("javascript:drawPath('"+mPath+"')");    //画不成功
		webView.loadUrl("javascript:drawcircle('"+pthInSites.get(0).getX()+"','"+pthInSites.get(0).getY()+"')");
	}
	
	//
	String mPath ;
	private String getPathFromSites(List<Site> sites) {
		String path = "M";
		if(sites == null) {
			Log.v(TAG,"path = null");
			path += "0 0";
			return path;
		}
		for(Site site:sites) {
			path += site.getX()+" "+site.getY() +"L";
		}
		path = path.substring(0, path.length()-1);
		Log.v(TAG, "path2:" + path);
		return path;
	}
		
 	int startPos = 0; 
 	int endPos = 0;
	int pathLen = 0;
	List<Site> pthInSites;    //路径点
	//简单粗暴
	private String trimPath(String path) {
		String currentPath = path;
		if(path.length()<20)
			return path;
		while(!currentPath.endsWith("L")) {	
			int len = currentPath.length();
			currentPath = currentPath.substring(0, len-1);
		}
		currentPath = currentPath.substring(0, currentPath.length()-1);
		return currentPath;
	}
	
 	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		//webView.loadUrl("");
 		//一共101个点
		double ratio = (double)(progress)/101.0;
		int points =  (int) (pathLen*ratio);
		endPos = points*4;
		String currentPath = trimPath(mPath.substring(0, endPos));	
		webView.loadUrl("javascript:drawPath('"+currentPath+"')");
		timePoint = pthInSites.get(points).getTime();
		description.setText(pthInSites.get(points).getTime());
		//description.setText(pthInSites.get(points).getTime());
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		description.setText("拖动查看路径");
		description.setTextSize(17);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		description.setText(timePoint);
	}
}