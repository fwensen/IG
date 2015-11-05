package com.uestc.Indoorguider.site_show;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.uestc.Indoorguider.R;
import android.app.Application;
import android.util.Log;
import android.util.Xml;

public class InitApplication extends Application{

	public  static Map<String, String> sitesNameEnAndChinese = null;
	public  static ArrayList<SiteInfo> sitesApplication  = null;
	private InputStream inputStream ;  
	
	 @Override
	    public void onCreate()
	    {
	       super.onCreate();
	       sitesNameEnAndChinese = sitesAndChineseMap();   
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
	
	 
	 private Map<String, String> sitesAndChineseMap() {
		 
		 Map<String, String> sacMap = new HashMap<String, String>();
		 sacMap.put("FirstAid", "急救中心");
		 sacMap.put("GiftStore", "礼物店");
		 sacMap.put("Taxi", "出租车");
		 sacMap.put("Bus", "公交站");
		 sacMap.put("WashRoom", "洗手间");
		 sacMap.put("Ticket", "票台");
		 sacMap.put("WaitRoom", "等待室");
		 sacMap.put("Exit", "出口");
		 sacMap.put("Parking", "停车场");
		 sacMap.put("ATM", "ATM");
		 sacMap.put("KFC", "肯德基");
		 sacMap.put("Charging", "充电处");
		 sacMap.put("WesternFood", "西餐厅");
		 sacMap.put("Stair", "楼梯");
		 sacMap.put("StationExit", "站台出口");
		 sacMap.put("StationEntrance", "站台入口");
		 sacMap.put("Luggage", "行李寄存处");
		 sacMap.put("Coffee", "咖啡厅");
		 sacMap.put("Lost", "失物招领处");
		 sacMap.put("C-Restaurant", "中餐厅");
		 sacMap.put("Restaurant", "餐厅");		 
		 return sacMap;
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