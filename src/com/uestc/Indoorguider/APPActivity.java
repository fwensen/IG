package com.uestc.Indoorguider;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.util.ClientAgent;


import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public abstract class APPActivity  extends Activity{
	
	
	protected abstract void handleResult(JSONObject obj);
	protected static int handlerNum = 6;
	int sWidth;
	protected Handler handler = new MyHandler();
	class  MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg)
		{
			//shduiahidh
			JSONObject obj;
			try {
				obj = new JSONObject(msg.obj.toString());
				if(obj.getInt("typecode") == Constant.NETWORK_EXCEPTION )	
				{
					Toast.makeText(APPActivity.this, obj.getInt("network_ex"), Toast.LENGTH_LONG).show();
				}
				handleResult(obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {  
	 super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metric);
	     sWidth = metric.widthPixels;     // 屏幕宽度（像素）
	    int height = metric.heightPixels;   // 屏幕高度（像素）
	    initView();
	    initContent();
	}
	
	@Override
	   protected void onPause()
	   {
		   super.onPause();
		   ClientAgent.setHandler(null);
	   }
	   @Override
	   protected void onResume()
	   {
		   super.onResume();
		   ClientAgent.setHandler(handler);
	   }
	   
	   /**
	      * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	      * 
	      * @param v
	      * @param event
	      * @return
	      */
	     private boolean isShouldHideInput(View v, MotionEvent event) {
	         if (v != null ) {
	            if(v instanceof EditText )
	            {
	            	 int[] l = { 0, 0 };
	                 v.getLocationInWindow(l);
	                 int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
	                         + v.getWidth();
	                 if (event.getX() > left && event.getX() < sWidth
	                         && event.getY() > top && event.getY() < bottom) {
	                     // 点击EditText的事件，忽略它。
	                     return false;
	                 } else {
	                     return true;
	                 }
	            }
	         }
	         // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
	         return false;
	     }
	 
	     /**
	      * 多种隐藏软件盘方法的其中一种
	      * 
	      * @param token
	      */
	     private void hideSoftInput(IBinder token) {
	         if (token != null) {
	             InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	             im.hideSoftInputFromWindow(token,
	                     InputMethodManager.HIDE_NOT_ALWAYS);
	         }
	     }
	     @Override
	     public boolean dispatchTouchEvent(MotionEvent ev) {
	         if (ev.getAction() == MotionEvent.ACTION_DOWN) {

	             // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
	             View v = getCurrentFocus(); 
	             if (isShouldHideInput(v, ev)) {
	                 hideSoftInput(v.getWindowToken());
	             }
	         }
	         return super.dispatchTouchEvent(ev);
	     }
	     
	     
	     protected abstract void initView();
	     protected abstract void initContent();
	     
	     
	     
	
	
}