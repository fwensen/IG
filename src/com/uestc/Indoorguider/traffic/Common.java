package com.uestc.Indoorguider.traffic;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/*
 *<p>
 *工具类
 *</p>
 */

public class Common {

	public static String IS_FIRST_IN = "";// 是否是第一次使用

	public static String ObjectToString(Object obj) {
		if (obj == null) {
			return "";
		}
		return String.valueOf(obj);
	}

	/**
	 * 格式化时间
	 * 
	 * @param object
	 * @return
	 */
	public static String formatDateToString(Date date) {

		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		result = sdf.format(date);

		return result;
	}

	/**
	 * 录入配置
	 * 
	 * @param context
	 * @param key
	 * @param val
	 */
	public static void writeConfig(Context context, String key, String val) {
		SharedPreferences share = context.getSharedPreferences("perference",
				Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString(key, val);
		editor.commit();
	}

	/**
	 * 读取配置
	 * 
	 * @param context
	 * @param key
	 * @param defval
	 * @return
	 */
	public static String readConfig(Context context, String key, String defval) {
		String str = "";
		SharedPreferences share = context.getSharedPreferences("perference",
				Context.MODE_PRIVATE);
		str = share.getString(key, defval);
		return str;
	}

	/**
	 * @param context
	 * @return 联网标志
	 */
	public static boolean onlineFlag(Context context) {

		boolean bol = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager == null ? null
				: connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable()) {

			bol = true;
			return bol;
		}

		return bol;
	}

//	/**
//	 * Toast提示框
//	 * 
//	 * @param context
//	 * @param str
//	 */
//	public static void createToast(Context context, String str) {
//
//		Toast toast = new Toast(context);
//		LinearLayout layout = new LinearLayout(context);
//		layout.setOrientation(LinearLayout.HORIZONTAL);
//
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		ImageView imageview = new ImageView(context);
//		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
//				30, 30);
//		LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		textviewParams.leftMargin = 5;
//		imageview.setLayoutParams(imageParams);
//		imageview.setBackgroundDrawable(context.getResources().getDrawable(
//				R.drawable.net_warn_icon));
//		TextView textView = new TextView(context);
//		textView.setText(str);
//		textView.setTextSize(20);
//		textView.setTextColor(Color.parseColor("#CC0000"));
//		textView.setSingleLine(true);
//		textView.setLayoutParams(textviewParams);
//		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
//		layout.addView(imageview, params);
//		layout.addView(textView, params);
//		toast.setView(layout);
//
//		toast.show();
//	}
	
	/**
	 * 修改按钮背景色
	 */
	public static void changeBtnBackground(Button btn ,int[] drawable) {

		
		class OnTouch implements OnTouchListener {

			private int drawable[] = null;

			public OnTouch(int[] drawable) {

				this.drawable = drawable;

			}

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					v.setBackgroundResource(drawable[1]);

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					v.setBackgroundResource(drawable[0]);
				}

				return false;
			}

		}

		btn.setOnTouchListener(new OnTouch(drawable));
		
	}

}
