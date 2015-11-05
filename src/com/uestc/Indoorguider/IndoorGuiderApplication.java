package com.uestc.Indoorguider;

import java.util.Map;

import com.uestc.Indoorguider.orientation.OrientationTool;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;



public class IndoorGuiderApplication extends Application {
	private static IndoorGuiderApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	public static IndoorGuiderManagerModel IGManager = null;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		IGManager = new IndoorGuiderManager(this);
		//开启服务
	    Intent intent = new Intent();
	    intent.setAction("com.uestc.Indoorguider.util.UtilService");
	    startService(intent);
	}

	public static IndoorGuiderApplication getInstance() {
		return instance;
	}

	
	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return IGManager.getUsername();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return IGManager.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
		IGManager.setUsername(username);
	}

	
	public void setPassword(String password) {
		IGManager.setPassword(password);
	}

	
	public boolean getAlreadyLogin(){
		return IGManager.getAlreadyLogin();
	}
	
	
	public boolean saveAlreadyLogin(boolean alreadyLogin ){
		return IGManager.saveAlreadyLogin(alreadyLogin);
	}
	
	
	/**
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 先调用sdk logout，在清理app中自己的数据
		IGManager.logout();
	}
	
}

