package com.uestc.Indoorguider;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
/**
 * 管理类，单例模式，必须先实例化再使用*/
public class IndoorGuiderManager extends IndoorGuiderManagerModel {
	 private static final String PREF_USERNAME = "username";
	 private static final String PREF_PWD = "pwd";
	 private static final String PREF_ALREADY_LOGIN = "already_login";
	 private static final String  PREF_SCORE = "user_score";
	 private Context context = null;
	 private static IndoorGuiderManager me ;
	 public IndoorGuiderManager(Context context){
		 this.context = context;
		 me = this;
	 }
	 
	 public static IndoorGuiderManager getInstance()
	 {
		 return me;
	 }
	 
	 
	 @Override
	public boolean setUsername(String username) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_USERNAME, username).commit();
	
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_USERNAME, "unkonwname");
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_PWD, "unknowpsw");
	}
	@Override
	public boolean setPassword(String password) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_PWD, password).commit();
	}
	
	 @Override
	public boolean setScore(int score) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putInt(PREF_SCORE, score).commit();
	
	}
	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(PREF_SCORE, 0);
	}
	@Override
	public boolean saveAlreadyLogin(boolean alreadyLogin) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.edit().putBoolean(PREF_ALREADY_LOGIN, alreadyLogin).commit();
	}

	@Override
	public boolean getAlreadyLogin() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_ALREADY_LOGIN, false);
	}
	
	
    
	
	
	   

}
