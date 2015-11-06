package com.uestc.Indoorguider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.uestc.Indoorguider.orientation.OrientationTool;
import com.uestc.Indoorguider.site_show.SiteInfo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.util.Xml;



public class IndoorGuiderApplication extends Application {
	private static IndoorGuiderApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	public static IndoorGuiderManagerModel IGManager = null;
	public static IndoorGuiderHelper IGHelper = null;

	public  static Map<String, String> sitesNameEnAndChinese = null;
	public  static ArrayList<SiteInfo> sitesApplication  = null;
	private InputStream inputStream ; 
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		IGManager = new IndoorGuiderManager(this);
		IGHelper = new IndoorGuiderHelper();
		sitesNameEnAndChinese = Constant.sitesAndChineseMap();   
	    inputStream = this.getResources().openRawResource(R.raw.site);	       
	       try {
			sitesApplication = getSites(inputStream);
			//for test***************
			//for (int i = 0; i < 1000; ++i)
				//sitesApplication.add(sitesApplication.get(i % sitesApplication.size()));
	       } catch (XmlPullParserException e) {
			e.printStackTrace();
	       } catch (IOException e) {
			e.printStackTrace();
	       }    

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
		IGHelper.logout();
	}
	
	public void login(String username, String password) {
		IGHelper.login(username, password);
	}
	
	//从XML中解析出siteinfo
		private ArrayList<SiteInfo> getSites(InputStream xml) throws XmlPullParserException, IOException {
				
				ArrayList<SiteInfo> sites = null;
				SiteInfo site = null;
				XmlPullParser pullParser = Xml.newPullParser();
		        pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据        
		        int event = pullParser.getEventType();
		        while (event != XmlPullParser.END_DOCUMENT){
		        	switch(event) {
		        	
		        	case XmlPullParser.START_DOCUMENT:
		        		sites = new ArrayList<SiteInfo>();  
		        		Log.v("xml", "start document");
		        		break;
		        		
		        	case XmlPullParser.START_TAG:	
		        		if("row".equals(pullParser.getName())) {     			
		        			site = new SiteInfo();
		        		} else if ("SiteId".equals(pullParser.getName())) {
		        			int id = Integer.valueOf(pullParser.nextText());
		        			site.setID(id);
		        			
		        		} else if ("positionX".equals(pullParser.getName())) {	        			
		        			//Log.v("xml", "x: "+pullParser.nextText());
		        			double x = Double.valueOf(pullParser.nextText());
		        			//Log.e("xml", " "+x);
		        			site.setX(x);
		        			
		        		}else if ("positionY".equals(pullParser.getName())) {        			
		        			double y = Double.valueOf(pullParser.nextText());
		        			site.setY(y);
		        			
		        		} else if ("positionZ".equals(pullParser.getName())) {	        			
		        			double z = Double.valueOf(pullParser.nextText());
		        			site.setZ(z);
		        			
		        		} else if ("SiteName".equals(pullParser.getName())) {	        			
		        			String siteName = pullParser.nextText();
		        			Log.v("xml", "sitename: "+siteName);
		        			site.setSiteName(siteName);
		        		} else if ("Left".equals(pullParser.getName())) {
		        			int l = Integer.valueOf(pullParser.nextText());
		        			Log.v("xml", "topleftx: "+l);
		        			site.setLeft(l);
		        			
		        		}  else if("Top".equals(pullParser.getName())) {
		        			int t = Integer.valueOf(pullParser.nextText());
		        			site.setTop(t);
		        			
		        		}  else if("Right".equals(pullParser.getName())) {
		        			int r = Integer.valueOf(pullParser.nextText());
		        			site.setRight(r);
		        			
		        		}  else if("Buttom".equals(pullParser.getName())) {
		        			int b = Integer.valueOf(pullParser.nextText());
		        			site.setButtom(b);
		        		} 
		        		break;
		        	
		        	case XmlPullParser.END_TAG:
		        		if("row".equals(pullParser.getName())) {
		        			//Log.v("xml", "end tag");
		        			sites.add(site);
		        			site = null;	        		
		        		}
		        		break;	        		
		        	}
		        	event = pullParser.next();			
		        }
		        return sites;
			}	 
}

